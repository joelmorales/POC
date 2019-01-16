package com.example.library.healthcheck;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.example.library.jobs.ScheduleJobs;
import com.example.library.jobs.SchedulerFireJob;
import com.example.library.jobs.SchedulingValues;

@Component
public class ServiceHealthIndicator implements HealthIndicator {

	@Autowired
	private List<ResourceHealthCheck> ResourceHealthCheckList;

	@Autowired
	private ScheduleJobs scheduleJobs;
	
	@Autowired
	private SchedulerFireJob jobConfiguration;
	
	private HashMap<String, String> serviceMap = new HashMap<String, String>();

	private Boolean healthStatus;
	
	public void setResourceHealthCheckList(ResourceHealthCheck resourceHealthCheck) {
		ResourceHealthCheckList.add(resourceHealthCheck);
	}

	
	
	@Override
	public Health health() {
		healthStatus = true;
		System.out.println("Health System Check");
		// serviceMap.put("db", getDatabaseHealth());
		getResourcesHealthStatus();
		return buildHealthDetails();
	}

	public void startSheduler() {
		if(jobConfiguration.getJobGroupNames().size()==0) {
			System.out.println("Schedule Group Names:"+ jobConfiguration.getJobGroupNames());
			scheduleJobs.schedule(new SchedulingValues(2));
		}
	}
	
	
	private void getResourcesHealthStatus() {
		for (ResourceHealthCheck resource : ResourceHealthCheckList) {
			serviceMap.put(resource.getResourceName(), buildStringStatus(resource));
		}
	}

	private String buildStringStatus(ResourceHealthCheck resource) {
		if (resource.isHealthly()) {
			return " is UP";
		}
		healthStatus = false;
		return " is Down";
	}

	private Health buildHealthDetails() {
		Builder builder = getHealthStatus();
		Iterator<Entry<String, String>> it = serviceMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			it.remove();
			builder.withDetail(pair.getKey().toString(), pair.getValue());
		}
		return builder.build();
	}

	public Builder getHealthStatus() {
		if (healthStatus)
			return Health.up();
		return Health.down();
	}

}
