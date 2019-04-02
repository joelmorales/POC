package com.example.library.jms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class JMSListenerManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSListenerManager.class);

	@Autowired
	private ApplicationContext context;

	@Value("${health.check.jms.listener.container.names}")
	private String JMS_LISTENER_CONTAINER;

	private Supplier<DefaultMessageListenerContainer> getListenerContainer = () -> (DefaultMessageListenerContainer) context
			.getBean(JMS_LISTENER_CONTAINER);

	private Supplier<List<DefaultMessageListenerContainer>> getListenerContainers = () -> getJMSListenerContainers();

	private List<DefaultMessageListenerContainer> getJMSListenerContainers() {
		List<DefaultMessageListenerContainer> jmslistenerContainers = new ArrayList<>();
		try {
			String[] listeners = JMS_LISTENER_CONTAINER.split(",");
			
			for (String jmsListenerContainer : listeners) {
				jmslistenerContainers.add((DefaultMessageListenerContainer) context.getBean(jmsListenerContainer));
			}

		} catch (Exception ex) {
			LOGGER.info("SERVICE LOG: " + ex.getMessage());
		}
		return jmslistenerContainers;
	}

	public void stopListener() {
		
		for(DefaultMessageListenerContainer getListener: getListenerContainers.get()) {
			if(getListener.isRunning()) {
				LOGGER.info("Shutting down listener...."+getListener.getDestinationName());
				getListener.stop();
				getListener.initialize();
				
			}
		}
		
		/*if (getListenerContainer.get().isRunning()) {
			LOGGER.info("Shutting down listener....");
			getListenerContainer.get().stop();
			getListenerContainer.get().initialize();
		}*/
	}

	public void starListener() {
		for(DefaultMessageListenerContainer getListener: getListenerContainers.get()) {
			if(!getListener.isRunning()) {
				LOGGER.info("Starting up listener...."+getListener.getDestinationName());
				///getListener.initialize();
				getListener.start();
				
			}
		}
		
		/*if (!getListenerContainer.get().isRunning()) {
			LOGGER.info("Starting up listener....");
			getListenerContainer.get().start();

		}*/
	}

}
