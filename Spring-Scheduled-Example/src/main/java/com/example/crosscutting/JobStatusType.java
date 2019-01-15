package com.example.crosscutting;

import com.example.jobs.SchedulerFireJob;

public abstract class JobStatusType {

	public abstract void sendJob(SchedulerFireJob jobConfiguration, JobRule jobRule);

}
