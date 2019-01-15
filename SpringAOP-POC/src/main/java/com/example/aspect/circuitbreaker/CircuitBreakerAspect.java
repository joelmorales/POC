package com.example.aspect.circuitbreaker;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.osds.location.CircuitBreakerFallBack;

@Component
@Scope("prototype")
@Aspect("perthis(com.example.aspect.circuitbreaker.CircuitBreakerAspect.circuitBreakerMethods())")
public class CircuitBreakerAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(CircuitBreakerAspect.class);

	@Pointcut("execution(@com.example.aspect.circuitbreaker.CircuitBreaker * *(..))")
	public void circuitBreakerMethods() {
	}

	private AtomicInteger failureCounter = new AtomicInteger();
	private AtomicReference<LocalTime> lastFailureTime = new AtomicReference<LocalTime>();
	private static int failureThreshold = 10;
	private static int resetTimeout = 30;// seconds
	private Throwable throwable;

	@Around("com.example.aspect.circuitbreaker.CircuitBreakerAspect.circuitBreakerMethods()")
	public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
		CircuitBreakerFallBack fallback = (CircuitBreakerFallBack) joinPoint.getTarget();
		try {

			LOGGER.info("Retry logic , counter=" + failureCounter.get());
			if (failureCounter.get() == 0) {
				// LOGGER.info("Retry logic ,is close");
				return joinPoint.proceed();
			}
			//LOGGER.info("Retry logic , original time=" + lastFailureTime.get().toSecondOfDay());
			// LOGGER.info("Retry logic , time now=" + LocalTime.now().toSecondOfDay());
			LOGGER.info("Retry logic , failure on seconds =" + getFailureTimeOnSeconds(lastFailureTime.get()));
			if (failureCounter.incrementAndGet() == failureThreshold
					|| getFailureTimeOnSeconds(lastFailureTime.get()) > resetTimeout) {
				
				LOGGER.info("Retry logic ,close the circuit");
				Object result = joinPoint.proceed();
				failureCounter.set(0);
				return result;
			}
			LOGGER.info("Retry logic ,half open");
		} catch (Throwable throwable) {
			LOGGER.info("Retry logic ,open the circuit");
			this.throwable = throwable;
			failureCounter.set(1);
			lastFailureTime.set(LocalTime.now());
		}
		// throw this.throwable;
		return fallback.getFallBack();
	}

	private int getFailureTimeOnSeconds(LocalTime failuretime) {
		return (LocalTime.now().toSecondOfDay() - lastFailureTime.get().toSecondOfDay());
	}

}
