package com.example.aspect.exceptions;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.crosscutting.BusinessException;
import com.example.crosscutting.DataProviderException;

@Component
@Aspect
public class AspectForExceptions {

	private static final Logger LOGGER = LoggerFactory.getLogger(AspectForExceptions.class);

	@Pointcut("@annotation(ExceptionAnnotation)")
	public void myAnnotation() {

	}

	// Use just if we going to return a new Exception and we have the place to
	// manage
	// @AfterThrowing(pointcut = "execution(* com.example..*(..))" , throwing="ex" )
	public void logRunTimeException(JoinPoint jointPoint, RuntimeException ex) throws Throwable {
		LOGGER.info("SERVICE_LOGGER-" + "", ex.getMessage().toString());
		LOGGER.info("ITOPS_LOGGER-" + "", ex.getMessage().toString());
	}

	@Around("myAnnotation()")
	// @Around("execution(* com.example..*(..))")
	public Object doLogging(ProceedingJoinPoint joingPoint) throws Throwable {
		String methodName = joingPoint.getSignature().getName();
		LOGGER.info("Enter to method-" + methodName);
		try {
			return joingPoint.proceed();
		} catch (BusinessException ex) {
			LOGGER.info("SERVICE_LOGGER-" + methodName + " , ", ex.toString());
			LOGGER.info("ITOPS_LOGGER-" + methodName + " , ", ex.getMessage());
			throw ex;
		} catch (DataProviderException ex) {
			LOGGER.info("SERVICE_LOGGER-" + methodName + " , ", ex.toString());
			throw ex;
		}

	}

}
