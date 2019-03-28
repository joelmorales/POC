package com.example.library.crosscutting.quartz;

import com.example.library.jobs.SchedulerFireJob;

public abstract class JobStatusType {

	public abstract void sendJob(SchedulerFireJob jobConfiguration, JobRule jobRule);

}
