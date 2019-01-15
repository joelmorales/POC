package com.example.aspect.parameters;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.aspect.logs.AspectForLogs;

@Component
@Aspect
public class ParametersAnnotation {

private static final Logger LOGGER = LoggerFactory.getLogger(AspectForLogs.class);
	
	@Pointcut("@annotation(AspectForParameters)")
	public void myAnnotation() {
		
	}
	
	@Before("myAnnotation()")
	public void entering(JoinPoint jointPoint) {
		String methodName = jointPoint.getSignature().getName();
		int parameterCnt=0;
		String parameterName;
		
		Object[] signatureArgs = jointPoint.getArgs();
		CodeSignature codeSignature = (CodeSignature) jointPoint.getSignature();
		LOGGER.info("Parameters of "+methodName+" are :\n");
		
		for (Object signatureArg: signatureArgs) {
			parameterName = codeSignature.getParameterNames()[parameterCnt];
			LOGGER.info("Parameter Name: "+parameterName+" , Value: "+signatureArg);
			parameterCnt+=1;
		}
		
	}
	
}
