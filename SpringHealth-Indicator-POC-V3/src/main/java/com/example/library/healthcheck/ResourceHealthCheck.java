package com.example.library.healthcheck;

public interface ResourceHealthCheck {

	public String getResourceName();
	public boolean isHealthly();
	
}
