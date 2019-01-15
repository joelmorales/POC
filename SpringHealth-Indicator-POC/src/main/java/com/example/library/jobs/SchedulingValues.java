package com.example.library.jobs;

import java.io.Serializable;

import com.example.library.crosscutting.quartz.JobRule;
import com.example.library.crosscutting.quartz.JobStatusType;

public class SchedulingValues implements JobRule, Serializable {

	private static final long serialVersionUID = 1L;

	private int increment = 1;
	private double factor = 1.5;
	private int intervalInSeconds = 5;

	public SchedulingValues(int intervalInSeconds) {		
		this.intervalInSeconds = (int) (intervalInSeconds * increment * factor);
		if(this.intervalInSeconds <= 1){
			this.intervalInSeconds=2;
		}
	}

	@Override
	public int getIntervalInSeconds() {
		return intervalInSeconds;
	}

	@Override
	public JobStatusType getType() {
		return new ScheduleHealthCheckStatus();
	}


}
