package com.example.manager;

import org.quartz.SchedulerException;

import com.example.crosscutting.JobRule;
import com.example.crosscutting.JobStatusType;
import com.example.jobs.SchedulerFireJob;

public class ScheduleHealthCheckStatus  extends JobStatusType{

	@Override
	public
	void sendJob(SchedulerFireJob jobConfiguration, JobRule jobRule) {
		try {			
			jobConfiguration.fireJob(HealthCheckStatusController.class, jobRule);
		} catch (SchedulerException | InterruptedException e) {
			throw new RuntimeException("");
		}
	}

}
