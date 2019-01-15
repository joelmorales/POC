package com.example.library.crosscutting.quartz;

public interface JobRule {

	public JobStatusType getType();
	
	public int getIntervalInSeconds();
}
