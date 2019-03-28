package com.example.library.jobs;

import org.quartz.SchedulerException;

import com.example.library.crosscutting.quartz.JobRule;
import com.example.library.crosscutting.quartz.JobStatusType;
import com.example.library.healthcheck.HealthCheckStatusController;

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
