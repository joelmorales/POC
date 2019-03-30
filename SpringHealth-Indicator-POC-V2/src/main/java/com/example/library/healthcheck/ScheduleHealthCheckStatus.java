package com.example.library.healthcheck;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import com.example.library.jms.JMSListenerManager;

//@Component
public class ScheduleHealthCheckStatus extends TimerTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleHealthCheckStatus.class);
	
	//@Autowired
	private ServiceHealthIndicator healthIndicator;
	
	//@Autowired
	private JMSListenerManager jmsListenermanager;
	
	private String name;
	private Timer timer;
	private int intervalInSeconds;

	public ScheduleHealthCheckStatus(ServiceHealthIndicator healthIndicator, JMSListenerManager jmsListenermanager) {
		this.healthIndicator = healthIndicator;
		this.jmsListenermanager = jmsListenermanager;
	}
	
	public void setSchedulingvalues(String name, Timer timer,int intervalInSeconds) {
		this.timer = timer;
		this.name = name;
		this.intervalInSeconds = intervalInSeconds;
	}

	@Override
	public void run() {
		
		LOGGER.info("New Controller is starting at: " + LocalTime.now());
		checkHealthStatus();
		//timer.cancel();
		
	}

	private void checkHealthStatus() {
		try {
			//Timer t = new Timer();
			if (getStatus().equals(Status.DOWN)) {
				//LOGGER.info("Get Schedule Status:"+healthIndicator.getScheduleStatus());
				
				//scheduleJobs.schedule(new SchedulingValues(jobRule.getIntervalInSeconds()));
				intervalInSeconds = new SchedulingValues(intervalInSeconds).getIntervalInSeconds();
				System.out.println("Interval is:"+intervalInSeconds);
				//ScheduleHealthCheckStatus schedule = new ScheduleHealthCheckStatus(name, timer, intervalInSeconds);
				
				ScheduleHealthCheckStatus schedule = new  ScheduleHealthCheckStatus(healthIndicator,jmsListenermanager);
				schedule.setSchedulingvalues(name, timer, intervalInSeconds);
				
				//t.scheduleAtFixedRate(schedule, intervalInSeconds * 1000, 10);
				timer.schedule(schedule, intervalInSeconds * 1000);
			} else {
				//healthIndicator.stopSchedule();
				startJmsListener();
				//t.wait();
			}
		} catch (Exception e) {
			LOGGER.info(
					"Process File controller Job has an Exception when try to schedule " + "the job: " + e.toString());
		}
	}
	
	private void startJmsListener() {
		jmsListenermanager.starListener();
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
	
}
