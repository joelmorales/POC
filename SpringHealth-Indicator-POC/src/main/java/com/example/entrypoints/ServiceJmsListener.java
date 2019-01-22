package com.example.entrypoints;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.configuration.JmsConfiguration;
import com.example.dataproviders.DataBaseProvider;
import com.example.library.aspect.JmsHealthCheck;
import com.example.library.jms.JmsHealthCheckConfiguration;

@Component
public class ServiceJmsListener implements JmsHealthCheckConfiguration, MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceJmsListener.class);

	@Autowired
	private DataBaseProvider databaseProvider;

	@Override

	public void onMessage(Message message) {
		try {
			((ServiceJmsListener) AopContext.currentProxy()).handleMessage(message);
		} catch (JMSException e) {
			LOGGER.debug(e.toString());
		}
	}

	@JmsHealthCheck
	public void handleMessage(Message message) throws JMSException {
		TextMessage txtMsg = (TextMessage) message;
		System.out.println("received: " + txtMsg.getText());

		//String id = databaseProvider.save(txtMsg.getText());
		int id = databaseProvider.saveJDBC(txtMsg.getText());
		
		System.out.println("message id: " + id);
		message.acknowledge();
	}

	@Override
	public String getJmsListenerContainer() {
		return JmsConfiguration.JMS_LISTENER_CONTAINER;
	}
	
}
