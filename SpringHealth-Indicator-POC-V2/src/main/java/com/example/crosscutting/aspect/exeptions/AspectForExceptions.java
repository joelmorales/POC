package com.example.crosscutting.aspect.exeptions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Component;

import com.example.crosscutting.DataProviderException;

@Component
@Aspect
public class AspectForExceptions {

	private static final Logger LOGGER = LoggerFactory.getLogger(AspectForExceptions.class);

	@Pointcut("@annotation(DataProviderExceptionAnnotation)")
	public void myAnnotation() {

	}

	@Around("myAnnotation()")
	public Object doLogging(ProceedingJoinPoint joingPoint) throws Throwable {
		String methodName = joingPoint.getSignature().getName();
		try {
			return joingPoint.proceed();
		} catch (CannotGetJdbcConnectionException ex) {
			LOGGER.info("SERVICE_LOGGER method name:" + methodName + " ;" + ex.getMessage());
			throw new DataProviderException("DataBase is down", ex);
		} catch (RuntimeException ex) {
			LOGGER.info("SERVICE_LOGGER method name:" + methodName + " ;" + ex.getMessage());
			LOGGER.info("ITOPS_LOGGER method name:" + methodName + " ;" + ex.getMessage());
			throw new DataProviderException("DataBase is down", ex);
		} catch (Exception ex) {
			LOGGER.info("SERVICE_LOGGER-" + methodName + " , ", ex.getMessage());
			throw new DataProviderException("DataBase is down", ex);
		}
	}

	
}
