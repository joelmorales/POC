package com.example.library.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import com.example.library.crosscutting.HealthCheckExceptionValidator;
import com.example.library.healthcheck.ServiceHealthIndicator;
import com.example.library.jms.JMSListenerManager;
import com.example.library.jms.JmsHealthCheckConfiguration;

@Component
@Aspect
public class JmsHealthCheckAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(JmsHealthCheckAspect.class);

	@Autowired
	private ServiceHealthIndicator healthIndicator;
	
	@Autowired
	private JMSListenerManager jmsListenermanager;
	
	@Pointcut("@annotation(JmsHealthCheck)")
	public void myAnnotation() {

	}

	@Around("myAnnotation()")
	public void doLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			joinPoint.proceed();
		} catch (Exception ex) {
			if (ex instanceof HealthCheckExceptionValidator) {
				getHealthCheckStatus(joinPoint);
			} else {
				LOGGER.info("Error on " + ex.getMessage());
			}
		}
	}

	private void getHealthCheckStatus(ProceedingJoinPoint joinPoint) {
		Health health=healthIndicator.health();
		if(health.getStatus().equals(Status.DOWN)) {
			healthIndicator.startSheduler();
			getDownJmsListener(joinPoint);
		}
	}
	
	private void getDownJmsListener(ProceedingJoinPoint joinPoint) {
		JmsHealthCheckConfiguration jmsHealthCheck = (JmsHealthCheckConfiguration) joinPoint.getTarget();
		//LOGGER.info("Jms Container Value:"+jmsHealthCheck.getJmsListenerContainer());
		jmsListenermanager.stopListener(jmsHealthCheck.getJmsListenerContainer());
	}
	
	
	
}
