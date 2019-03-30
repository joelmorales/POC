package com.example.library.healthcheck;

import java.time.LocalTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import com.example.library.jms.JMSListenerManager;

@Component
public class ScheduleResourceValidator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleHealthCheckStatus.class);
	
	@Autowired
	private ServiceHealthIndicator healthIndicator;
	
	@Autowired
	private JMSListenerManager jmsListenermanager;
	
	private int intervalInSeconds;
	
	
	public void execute(int intervalInSeconds) {
		this.intervalInSeconds = intervalInSeconds;
		try {
			LOGGER.info("New Controller is starting at: " + LocalTime.now());
			run();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void run() throws InterruptedException, ExecutionException {
		
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		
		Callable<Status> task = () -> {
			return getStatus();
		};
		
		System.out.println("Interval is:"+intervalInSeconds);
		
		ScheduledFuture<Status> schedule = ses.schedule(task, intervalInSeconds, TimeUnit.SECONDS);
		intervalInSeconds = new SchedulingValues(intervalInSeconds).getIntervalInSeconds();
		
		if(schedule.get().equals(Status.DOWN)){
			ses.shutdown();
			execute(intervalInSeconds);
		}else {
			ses.shutdown();
			//healthIndicator.stopSchedule();
			startJmsListener();
		}
	}
	
	private Status getStatus() {
		try {
			Health health = healthIndicator.health();
			if (health.getStatus().equals(Status.DOWN)) {
				return Status.DOWN;
			}
			return Status.UP;
		} catch (Exception ex) {
			return Status.DOWN;
		}

	}
	
	private void startJmsListener() {
		jmsListenermanager.starListener();
	}
	
	
}
