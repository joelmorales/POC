package com.example.library.healthcheck;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class ServiceHealthIndicator implements HealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHealthIndicator.class);
	
	@Autowired
	private ScheduleResourceValidator scheduleResource;
	
	@Autowired
	private List<ResourceHealthCheck> ResourceHealthCheckList;

	private HashMap<String, String> serviceMap = new HashMap<String, String>();

	private Boolean healthStatus;

	private Boolean scheduleRunning=false;

	
	public void setResourceHealthCheckList(ResourceHealthCheck resourceHealthCheck) {
		ResourceHealthCheckList.add(resourceHealthCheck);
	}

	@Override
	public Health health() {
		healthStatus = true;
		System.out.println("Health System Check");
		getResourcesHealthStatus();
		return buildHealthDetails();
	}

	public void startSheduler() {
		LOGGER.info("Schedule Running:"+scheduleRunning);
		if (!scheduleRunning) {
			scheduleRunning = true;
			scheduleResource.execute(2);
			scheduleRunning = false;
			LOGGER.info("Schedule Ending:");
		}
		
	}

	/*public void stopSchedule() {
		scheduleRunning = false;
	}*/

	public Boolean getScheduleStatus() {
		return scheduleRunning;
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
		Stream<HashMap<String, String>> st = Stream.of(serviceMap);
		// st.

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

	private Builder getHealthStatus() {
		if (healthStatus)
			return Health.up();
		return Health.down();
	}

	

}
