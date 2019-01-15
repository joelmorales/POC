package com.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.jobs.ScheduleJobs;
import com.example.jobs.SchedulerFireJob;
import com.example.manager.HealthCheckStatusController;

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
