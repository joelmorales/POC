package com.example.crosscutting;

public class BusinessException extends ApplicationException {

	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
		super(message);
	}

}
