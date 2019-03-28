package com.example.dataproviders;

import org.springframework.stereotype.Component;

import com.example.library.healthcheck.ResourceHealthCheck;

@Component
public class MountDataProvider implements ResourceHealthCheck{

	@Override
	public String getResourceName() {
		return "Service Mount on Unix";
	}

	@Override
	public boolean isHealthly() {
		return true;
	}

}
