package com.example.library.aspect;

import java.lang.annotation.Annotation;

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

import com.example.library.crosscutting.HealthCheckException;
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
			
			if( getAnnotation(ex) instanceof HealthCheckException) {
				getHealthCheckStatus();
			}
			/*if (ex instanceof HealthCheckExceptionValidator) {
				getHealthCheckStatus(joinPoint);
			} */
			else {
				LOGGER.info("Exception "+ex.getClass().getCanonicalName() +" - " + ex.getMessage());
			}
		}
	}

	private Annotation getAnnotation(Exception ex) {
		Class<? extends Exception> aclass = ex.getClass();
		Annotation annotation = aclass.getAnnotation(HealthCheckException.class);
		return annotation;
	}
	
	private void getHealthCheckStatus() {
		Health health = healthIndicator.health();
		if (health.getStatus().equals(Status.DOWN)) {
			getDownJmsListener();
			healthIndicator.startSheduler();
		}
	}

	private void getDownJmsListener() {
		jmsListenermanager.stopListener();
	}

}
