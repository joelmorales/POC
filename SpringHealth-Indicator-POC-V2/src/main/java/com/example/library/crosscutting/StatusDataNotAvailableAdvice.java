package com.example.library.crosscutting;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class StatusDataNotAvailableAdvice {

	@ResponseBody
	@ExceptionHandler(StatusDataNotAvailableException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	String employeeNotFoundHandler(StatusDataNotAvailableException ex) {
		return ex.getMessage();
	}
	
}
