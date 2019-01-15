package com.example.crosscutting;

public interface JobRule {

	public JobStatusType getType();
	//public String getName();
	public int getIntervalInSeconds();
}
