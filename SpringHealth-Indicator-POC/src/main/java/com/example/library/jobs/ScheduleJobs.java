package com.example.library.jobs;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.library.crosscutting.quartz.JobRule;
import com.example.library.crosscutting.quartz.JobStatusType;


public class ScheduleJobs {

	@Autowired
	private SchedulerFireJob jobConfiguration;

	public ScheduleJobs(SchedulerFireJob jobConfiguration) {
		this.jobConfiguration = jobConfiguration;
	}

	private JobStatusType getJobType(JobRule jobRule) {
		return jobRule.getType();
	}

	public void schedule(JobRule jobRule) {
		getJobType(jobRule).sendJob(jobConfiguration, jobRule);
	}

}
