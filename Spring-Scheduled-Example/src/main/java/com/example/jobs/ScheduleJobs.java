package com.example.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.crosscutting.JobRule;
import com.example.crosscutting.JobStatusType;


public class ScheduleJobs {

	@Autowired
	SchedulerFireJob jobConfiguration;

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
