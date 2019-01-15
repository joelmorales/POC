package com.example.crosscutting;

public class ApplicationException  extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public ApplicationException(final String message) {
		super(message);
	}
	
}
