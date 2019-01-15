package com.example.library.jms;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class JMSListenerManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSListenerManager.class);
	
	@Autowired
	private ApplicationContext context;
	
	private String JMS_LISTENER_CONTAINER;
	
	private Supplier <DefaultMessageListenerContainer> getListenerContainer =
			() -> (DefaultMessageListenerContainer) context.getBean(JMS_LISTENER_CONTAINER);

	public void stopListener(String container) {
		setJmsValue(container);
		if(getListenerContainer.get().isRunning()) {
			LOGGER.info("Shutting down listener....");
			getListenerContainer.get().stop();
			getListenerContainer.get().initialize();
		}
	}
	
	public void starListener() {
		if(!getListenerContainer.get().isRunning()) {
			LOGGER.info("Starting up listener....");
			getListenerContainer.get().start();
			
		}
	}
	
	private void setJmsValue(String value) {
		if(JMS_LISTENER_CONTAINER == null || JMS_LISTENER_CONTAINER.isEmpty()) {
			JMS_LISTENER_CONTAINER=value;
		}
	}
	
}
