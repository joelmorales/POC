package com.example.configuration;

import javax.jms.MessageListener;
import javax.jms.QueueConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.example.entrypoints.ServiceJmsLoaderListener;

@Configuration
public class JmsLoaderConfiguration {

	private static final Integer INDIVIDUAL_ACNOWLEDGE = 4;
	private final String BROKER_URL = "tcp://localhost:61616"; 
	private final String BROKER_USERNAME = "admin"; 
	private final String BROKER_PASSWORD = "admin";
	private static final String JMS_LISTENER_CONTAINER = "jmsLoaderContainer";
	private static String DESTINATION_QUEUE_NAME = "mx.loader.queue";
	
	@Bean
	public QueueConnectionFactory loaderConnectionFactory(){
	    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
	    connectionFactory.setBrokerURL(BROKER_URL);
	    connectionFactory.setPassword(BROKER_USERNAME);
	    connectionFactory.setUserName(BROKER_PASSWORD);
	    return connectionFactory;
	}
	
	@Bean
	public JmsTemplate jmsLoaderTemplate(){
	    JmsTemplate template = new JmsTemplate();
	    template.setConnectionFactory(loaderConnectionFactory());
	    template.setSessionAcknowledgeMode(INDIVIDUAL_ACNOWLEDGE);
	    return template;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsLoaderListenerContainerFactory() {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(loaderConnectionFactory());
	    factory.setConcurrency("2-2");
	    factory.setSessionAcknowledgeMode(INDIVIDUAL_ACNOWLEDGE);
	   
	    return factory;
	}
	
	@Bean(name = JMS_LISTENER_CONTAINER)
	public DefaultMessageListenerContainer defaultLoaderMessageListenerContainer() {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(loaderConnectionFactory());
		container.setBeanName(JMS_LISTENER_CONTAINER);
		container.setSessionAcknowledgeMode(INDIVIDUAL_ACNOWLEDGE);
		container.setDestinationName(DESTINATION_QUEUE_NAME);
		container.setMessageListener(messageLoaderListener());
		return container;
	}
	
	@Bean
	public MessageListener messageLoaderListener() {
		return new ServiceJmsLoaderListener();
	}
	
}
