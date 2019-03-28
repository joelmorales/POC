package com.example.library.jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.example.library.crosscutting.quartz.JobRule;

public class SchedulerFireJob {

	private SchedulerFactoryBean schedulerFactory;

	public SchedulerFireJob(SchedulerFactoryBean schedulerFactory) {
		this.schedulerFactory = schedulerFactory;
	}

	public <T extends Job> void fireJob(Class<T> jobClass, Object objectJob)
			throws SchedulerException, InterruptedException {

		JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
		JobDetail jobDetail = getJobDetail(jobBuilder, objectJob);
		Trigger trigger = getTrigger(objectJob);
		schedulerFactory.getObject().scheduleJob(jobDetail, trigger);
	}

	public List<String> getJobGroupNames() {
		try {
			return schedulerFactory.getScheduler().getJobGroupNames();
		} catch (Exception ex) {
			return new ArrayList<String>();
		}
	}

	private Trigger getTrigger(Object objectJob) {
		JobRule jobrule = (JobRule) objectJob;
		Date triggerStartTime = addSecondsToLocalDate(jobrule.getIntervalInSeconds());

		System.out.println("New Interval is: " + jobrule.getIntervalInSeconds());

		Trigger trigger = TriggerBuilder.newTrigger().startAt(triggerStartTime)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2))
				.withDescription("MyTrigger").build();

		return trigger;
	}

	private Date addSecondsToLocalDate(int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	private JobDetail getJobDetail(JobBuilder jobBuilder, Object objectJob) {
		JobDataMap data = new JobDataMap();
		data.put("objectJob", objectJob);
		JobDetail jobDetail = jobBuilder.usingJobData(data).build();
		return jobDetail;
	}

}
