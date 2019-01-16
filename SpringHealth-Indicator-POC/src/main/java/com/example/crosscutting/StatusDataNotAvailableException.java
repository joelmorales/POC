package com.example.crosscutting;

public class StatusDataNotAvailableException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	
	public StatusDataNotAvailableException() {
		super("Service internal resource(s) are not available");
		
	}

}
