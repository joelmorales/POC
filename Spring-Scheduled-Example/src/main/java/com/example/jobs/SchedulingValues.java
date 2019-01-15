package com.example.jobs;

import java.io.Serializable;

import com.example.crosscutting.JobRule;
import com.example.crosscutting.JobStatusType;
import com.example.manager.ScheduleHealthCheckStatus;

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
