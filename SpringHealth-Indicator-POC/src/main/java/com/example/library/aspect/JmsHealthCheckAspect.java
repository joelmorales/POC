package com.example.library.aspect;

import java.util.ArrayList;
import java.util.List;

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
import com.example.library.jobs.SchedulerFireJob;

@Component
@Aspect
public class JmsHealthCheckAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(JmsHealthCheckAspect.class);
	private List<String> JobGroupNames = new ArrayList<String>();

	@Autowired
	private SchedulerFireJob jobConfiguration;

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
			JobGroupNames = jobConfiguration.getJobGroupNames();
			LOGGER.info("Schedule Group Names in ASpect Methods:" + JobGroupNames);

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
		Health health = healthIndicator.health();
		if (health.getStatus().equals(Status.DOWN)) {
			LOGGER.info("Schedule Group Names in ASpect Methods:" + jobConfiguration.getJobGroupNames());
			// if (JobGroupNames.size() == 0) {
			healthIndicator.startSheduler();
			// }
			getDownJmsListener(joinPoint);
		}
	}

	private void getDownJmsListener(ProceedingJoinPoint joinPoint) {
		JmsHealthCheckConfiguration jmsHealthCheck = (JmsHealthCheckConfiguration) joinPoint.getTarget();
		//LOGGER.info("Jms Container Value:" + jmsHealthCheck.getJmsListenerContainer());
		jmsListenermanager.stopListener(jmsHealthCheck.getJmsListenerContainer());
	}

}
