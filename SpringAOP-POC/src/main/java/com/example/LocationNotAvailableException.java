package com.example;

public class LocationNotAvailableException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public LocationNotAvailableException() {
		super();
	}

	public LocationNotAvailableException(String id) {
		super("Location is not available:"+id);
		
	}

}
