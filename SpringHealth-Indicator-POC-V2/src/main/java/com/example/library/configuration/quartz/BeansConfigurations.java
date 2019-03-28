package com.example.library.configuration.quartz;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.library.healthcheck.HealthCheckStatusController;
import com.example.library.jobs.ScheduleJobs;
import com.example.library.jobs.SchedulerFireJob;

@Configuration
public class BeansConfigurations {
	
	@Bean
	public ApplicationContextProvider applicationContextProvider(){
		return new ApplicationContextProvider();
	}
	
	@Bean
	public HealthCheckStatusController jmsHealthCheckStatusController(){
		return new HealthCheckStatusController();
	}
	
	@Bean
	public ScheduleJobs getAccessSpmFileUpdatesJob(SchedulerFireJob jobConfiguration) {
		return new ScheduleJobs(jobConfiguration);
	}

	
}
