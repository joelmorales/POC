package com.example.crosscutting;

import com.example.library.crosscutting.HealthCheckExceptionValidator;

public class DataProviderException extends ApplicationException implements HealthCheckExceptionValidator {

	public DataProviderException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataProviderException(String message, Throwable cause) {
		super(message, cause);
	}
}
