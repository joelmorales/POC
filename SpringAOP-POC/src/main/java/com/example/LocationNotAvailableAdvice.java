package com.example;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class LocationNotAvailableAdvice {

	@ResponseBody
	@ExceptionHandler(LocationNotAvailableException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String employeeNotFoundHandler(LocationNotAvailableException ex) {
		return ex.getMessage();
	}
	
}
