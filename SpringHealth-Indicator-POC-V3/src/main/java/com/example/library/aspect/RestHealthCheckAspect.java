package com.example.library.aspect;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.library.crosscutting.StatusDataNotAvailableException;
import com.example.library.healthcheck.ServiceHealthIndicator;

@Component
@Scope("prototype")
@Aspect("perthis(com.example.library.aspect.RestHealthCheckAspect.restHealthCheckMethods())")
public class RestHealthCheckAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestHealthCheckAspect.class);

	@Autowired
	private ServiceHealthIndicator healthIndicator;

	private AtomicInteger failureCounter = new AtomicInteger();
	private AtomicReference<LocalTime> lastFailureTime = new AtomicReference<LocalTime>();
	// private static int failureThreshold = 10;
	private static int resetTimeout = 30;// seconds
	//private Exception throwable;

	@Pointcut("execution(@com.example.library.aspect.RestHealthCheck * *(..))")
	public void restHealthCheckMethods() {
	}

	@Around("com.example.library.aspect.RestHealthCheckAspect.restHealthCheckMethods()")
	public Object retry(ProceedingJoinPoint joinPoint) throws Exception {
		try {

			LOGGER.info("Retry logic , counter=" + failureCounter.get());
			if (failureCounter.get() == 0) {
				LOGGER.info("Retry logic ,is close");
				return joinPoint.proceed();
			}
			Health health = healthIndicator.health();
			if (failureCounter.get() == 1 && health.getStatus().equals(Status.DOWN)) {
				healthIndicator.startSheduler();
			}
			LOGGER.info("Retry logic , failure on seconds =" + getFailureTimeOnSeconds(lastFailureTime.get()));
			if (health.getStatus().equals(Status.UP) || getFailureTimeOnSeconds(lastFailureTime.get()) > resetTimeout) {

				LOGGER.info("Retry logic ,close the circuit");
				Object result = joinPoint.proceed();
				failureCounter.set(0);
				return result;
			}
			LOGGER.info("Retry logic ,half open");
		} catch (Throwable throwable) {
			LOGGER.info("Retry logic ,open the circuit");
			//throwable = new StatusDataNotAvailableException();
			failureCounter.set(1);
			lastFailureTime.set(LocalTime.now());
		}
		throw new StatusDataNotAvailableException();
	}

	private int getFailureTimeOnSeconds(LocalTime failuretime) {
		return (LocalTime.now().toSecondOfDay() - lastFailureTime.get().toSecondOfDay());
	}

	
}
