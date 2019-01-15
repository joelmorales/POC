package com.example.configuration;

import javax.jms.MessageListener;
import javax.jms.QueueConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.example.entrypoints.HCJmsListener;

@Configuration
public class JmsConfiguration {

	private static final Integer INDIVIDUAL_ACNOWLEDGE = 4;
	private final String BROKER_URL = "tcp://localhost:61616"; 
	private final String BROKER_USERNAME = "admin"; 
	private final String BROKER_PASSWORD = "admin";
	public static final String JMS_LISTENER_CONTAINER = "jmsContainer";
	public static String DESTINATION_QUEUE_NAME = "mx.ames.queue";
	
	@Bean
	public QueueConnectionFactory connectionFactory(){
	    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
	    connectionFactory.setBrokerURL(BROKER_URL);
	    connectionFactory.setPassword(BROKER_USERNAME);
	    connectionFactory.setUserName(BROKER_PASSWORD);
	    return connectionFactory;
	}
	
	@Bean
	public JmsTemplate jmsTemplate(){
	    JmsTemplate template = new JmsTemplate();
	    template.setConnectionFactory(connectionFactory());
	    template.setSessionAcknowledgeMode(INDIVIDUAL_ACNOWLEDGE);
	    return template;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(connectionFactory());
	    factory.setConcurrency("2-2");
	    factory.setSessionAcknowledgeMode(INDIVIDUAL_ACNOWLEDGE);
	   
	    return factory;
	}
	
	@Bean(name = JMS_LISTENER_CONTAINER)
	public DefaultMessageListenerContainer defaultMessageListenerContainer() {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setBeanName(JMS_LISTENER_CONTAINER);
		container.setSessionAcknowledgeMode(INDIVIDUAL_ACNOWLEDGE);
		container.setDestinationName(DESTINATION_QUEUE_NAME);
		container.setMessageListener(messageListener());
		return container;
	}
	
	@Bean
	public MessageListener messageListener() {
		return new HCJmsListener();
	}
	
}
