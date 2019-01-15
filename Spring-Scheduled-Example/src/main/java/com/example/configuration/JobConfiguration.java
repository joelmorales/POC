package com.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.example.jobs.SchedulerFireJob;

@Configuration
public class JobConfiguration {

	@Autowired
	@Qualifier("integrationLayerDataSource")
	DriverManagerDataSource dataSource;


	@Bean
	public AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory(){
		return new AutowiringSpringBeanJobFactory();
	}
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean()
	{
		SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
		quartzScheduler.setJobFactory(autowiringSpringBeanJobFactory());
		quartzScheduler.setDataSource(dataSource);
		return quartzScheduler;
	}
	
	@Bean
	public SchedulerFireJob schedulerFireJob(){
		return new SchedulerFireJob(schedulerFactoryBean());
	}
	
}
