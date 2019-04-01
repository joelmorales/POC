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


@Component
@Aspect
public class JmsHealthCheckAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(JmsHealthCheckAspect.class);
	
	@Autowired
	private ServiceHealthIndicator healthIndicator;

	@Autowired
	private JMSListenerManager jmsListenermanager;

	@Pointcut("@annotation(JmsHealthCheck)")
	public void jmsHealthCheckAspectMthods() {

	}

	@Around("jmsHealthCheckAspectMthods()")
	public void doJmsHealthCK(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			joinPoint.proceed();
		} catch (Exception ex) {
			if (ex instanceof HealthCheckExceptionValidator) {
				getHealthCheckStatus(joinPoint);
			} else {
				LOGGER.info("Exception "+ex.getClass().getCanonicalName() +" - " + ex.getMessage());
			}
		}
	}

	private void getHealthCheckStatus(ProceedingJoinPoint joinPoint) {
		Health health = healthIndicator.health();
		if (health.getStatus().equals(Status.DOWN)) {
			getDownJmsListener(joinPoint);
			healthIndicator.startSheduler();
		}
	}

	private void getDownJmsListener(ProceedingJoinPoint joinPoint) {
		jmsListenermanager.stopListener();
	}

}