package com.example.library.configuration.quartz;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
//import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.library.jobs.SchedulerFireJob;

@Configuration
public class JobConfiguration {

	@Autowired
	@Qualifier("integrationLayerDataSource")
	private DataSource integrationLayerDataSource;

	// @Autowired
	// @Qualifier("transactionManager")
	private PlatformTransactionManager transactionManager;

	@Bean
	public AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory() {
		return new AutowiringSpringBeanJobFactory();
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
		quartzScheduler.setJobFactory(autowiringSpringBeanJobFactory());
		quartzScheduler.setDataSource(integrationLayerDataSource);
		quartzScheduler.setQuartzProperties(quartzProperties());
		// quartzScheduler.setTransactionManager(transactionManager);
		return quartzScheduler;
	}

	// @Bean
	public SchedulerFactoryBeanCustomizer scheduleFactoryBeanCustomizer() {
		return bean -> {
			bean.setDataSource(integrationLayerDataSource);
			bean.setTransactionManager(transactionManager);
		};
	}

	@Bean
	public SchedulerFireJob schedulerFireJob() {
		return new SchedulerFireJob(schedulerFactoryBean());
	}

	@Bean
	public Properties quartzProperties() {
		Properties quartzProperties = new Properties();
		quartzProperties.setProperty(QuartzPropertyKeys.INSTANCE_NAME, "scheduler");
		quartzProperties.setProperty(QuartzPropertyKeys.INSTANCE_ID, "integrationID");
		quartzProperties.setProperty(QuartzPropertyKeys.ISCLUSTERED, "false");
		quartzProperties.setProperty(QuartzPropertyKeys.CLUSTER_CHECK_IN_INTERVAL, "2000");
		quartzProperties.setProperty(QuartzPropertyKeys.TABLE_PREFIX, "QRTZ_");
		quartzProperties.setProperty(QuartzPropertyKeys.DRIVER_DELEGATE_CLASS, "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
		quartzProperties.setProperty(QuartzPropertyKeys.JOB_STORE_CLASS, "org.quartz.impl.jdbcjobstore.JobStoreTX");
		quartzProperties.setProperty(QuartzPropertyKeys.JOB_STORE_THRESHOLD, "60000");
		quartzProperties.setProperty(QuartzPropertyKeys.SCHEDULER_UPDATE_CHECK, "true");
		quartzProperties.setProperty(QuartzPropertyKeys.THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
		quartzProperties.setProperty(QuartzPropertyKeys.THREAD_COUNT, "3");

		return quartzProperties;
	}

}
