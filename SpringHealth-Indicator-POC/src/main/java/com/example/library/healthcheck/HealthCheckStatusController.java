package com.example.library.healthcheck;

import java.time.LocalTime;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import com.example.library.crosscutting.quartz.JobRule;
import com.example.library.jms.JMSListenerManager;
import com.example.library.jobs.ScheduleJobs;
import com.example.library.jobs.SchedulingValues;


public class HealthCheckStatusController implements Job {

	@Autowired
	private ScheduleJobs scheduleJobs;

	@Autowired
	private ServiceHealthIndicator healthIndicator;

	@Autowired
	private JMSListenerManager jmsListenermanager;

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckStatusController.class);

	public HealthCheckStatusController() {

	}

	@Override
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		try {
			LOGGER.info("Controller is starting at: " + LocalTime.now());
			JobRule jobRule = (JobRule) jobContext.getJobDetail().getJobDataMap().getWrappedMap().get("objectJob");
			checkHealthStatus(jobRule);
			
		} catch (Exception ex) {
			LOGGER.info("Process File controller Job has an Exception: " + ex.toString());
		}
	}

	private void checkHealthStatus(JobRule jobRule) {
		try {
			if (getStatus().equals(Status.DOWN)) {
				//LOGGER.info("Schedule new Job ");
				scheduleJobs.schedule(new SchedulingValues(jobRule.getIntervalInSeconds()));
			} else {
				startJmsListener();
			}

		} catch (Exception e) {
			LOGGER.info("Process File controller Job has an Exception when try to schedule "
					+ "the job: " + e.toString());
		}
	}

	private Status getStatus() {
		try {
			Health health = healthIndicator.health();
			if (health.getStatus().equals(Status.DOWN)) {
				return Status.DOWN;
			}
			return Status.UP;
		}
		catch(Exception ex) {
			return Status.DOWN;
		}
		
	}
	
	private void startJmsListener() {
		// LOGGER.info("Jms Container Value:"+jmsHealthCheck.getJmsListenerContainer());
		jmsListenermanager.starListener();
	}

}
