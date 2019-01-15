package com.example.manager;

import java.time.LocalTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.crosscutting.JobRule;
import com.example.jobs.ScheduleJobs;
import com.example.jobs.SchedulingValues;

public class HealthCheckStatusController implements Job {
	
	@Autowired
	ScheduleJobs scheduleJobs;

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckStatusController.class);

	public HealthCheckStatusController() {

	}

	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		try {
			LOGGER.info("Controller is starting at: " + LocalTime.now());
			JobRule jobRule = (JobRule) jobContext.getJobDetail().getJobDataMap().getWrappedMap().get("objectJob");
			getStatus(jobRule);
			//LOGGER.info("Process File Manager Controller is ending at: " + Instant.now());
		} catch (Exception ex) {
			LOGGER.info("Process File controller Job has an Exception: " + ex.toString());
		}
	}

	private void getStatus(JobRule jobRule) {
		try {
			scheduleJobs.schedule(new SchedulingValues(jobRule.getIntervalInSeconds()));			
		} catch (Exception e) {
			LOGGER.info("Process File controller Job has an Exception when try to schedule "
					+ "the job to process Proposals: " + e.toString());
		}
	}

	
}
