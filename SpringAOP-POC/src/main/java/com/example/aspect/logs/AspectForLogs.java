package com.example.aspect.logs;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AspectForLogs {

	private static final Logger LOGGER = LoggerFactory.getLogger(AspectForLogs.class);
	
	@Pointcut("@annotation(LogsAnnotation)")
	public void myAnnotation() {
		
	}
	
	@Before("myAnnotation()")
	public void entering(JoinPoint jointPoint) {
		String methodName = jointPoint.getSignature().getDeclaringTypeName()+"."+jointPoint.getSignature().getName();
		//String methodName = jointPoint.getStaticPart().toString();
		LOGGER.info("Entering to : "+methodName);
	}
	
	
	
}
