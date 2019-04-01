package com.example.library.healthcheck;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ServiceHealthIndicator implements HealthIndicator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHealthIndicator.class);
	
	@Autowired
	private ScheduleResourceValidator scheduleResource;
	
	@Autowired
	private List<ResourceHealthCheck> ResourceHealthCheckList;

	@Value("${health.check.interval.start}")
	private int intervalStart;
	
	private HashMap<String, String> serviceMap = new HashMap<String, String>();

	private Boolean healthStatus;

	private Boolean scheduleRunning=false;

	
	public void setResourceHealthCheckList(ResourceHealthCheck resourceHealthCheck) {
		ResourceHealthCheckList.add(resourceHealthCheck);
	}

	@Override
	public Health health() {
		healthStatus = true;
		LOGGER.info("\n");
		LOGGER.info("Health System Check");
		getResourcesHealthStatus();
		return buildHealthDetails();
	}

	public void startSheduler() {
		LOGGER.info("Schedule Running:"+scheduleRunning);
		if (!scheduleRunning) {
			scheduleRunning = true;
			scheduleResource.execute(intervalStart);
			scheduleRunning = false;
			LOGGER.info("Schedule Ending:");
		}
	}

	
	
	private void getResourcesHealthStatus() {
		for (ResourceHealthCheck resource : ResourceHealthCheckList) {
			//Remove for TBS
			serviceMap.put(resource.getResourceName(), buildStringStatus(resource));
			//Apply logic for get the status and return false
		}
		//return healthy here
	}

	//Remove for TBS
	private String buildStringStatus(ResourceHealthCheck resource) {
		if (resource.isHealthly()) {
			return " is UP";
		}
		healthStatus = false;
		return " is Down";
	}

	//Remove for TBS
	@SuppressWarnings("rawtypes")
	private Health buildHealthDetails() {
		Builder builder = getHealthStatus();
		Iterator<Entry<String, String>> it = serviceMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			LOGGER.info(pair.getKey() + ""+ pair.getValue());
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
