package com.example.library.configuration.quartz;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
//import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.library.jobs.SchedulerFireJob;

@Configuration
public class JobConfiguration {

	@Autowired
	@Qualifier("integrationLayerDataSource")
	private DataSource integrationLayerDataSource;

	//@Autowired
	//@Qualifier("transactionManager")
	//private PlatformTransactionManager transactionManager;

	@Bean
	public AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory(){
		return new AutowiringSpringBeanJobFactory();
	}
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean()
	{
		SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
		quartzScheduler.setJobFactory(autowiringSpringBeanJobFactory());
		quartzScheduler.setDataSource(integrationLayerDataSource);
		//quartzScheduler.setTransactionManager(transactionManager);
		return quartzScheduler;
	}
	
	/*@Bean
	public SchedulerFactoryBeanCustomizer scheduleFactoryBeanCustomizer() {
		return bean ->{
			bean.setDataSource(integrationLayerDataSource);
			bean.setTransactionManager(transactionManager);
		};
	}*/
	
	@Bean
	public SchedulerFireJob schedulerFireJob(){
		return new SchedulerFireJob(schedulerFactoryBean());
	}
	
}
