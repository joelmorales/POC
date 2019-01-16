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

@Component
public class ServiceHealthIndicator implements HealthIndicator {

	//@Autowired
	//private DataBaseProvider dataprovider;

	@Autowired
	private List<ResourceHealthCheck> ResourceHealthCheckList;

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

	private Builder getHealthStatus() {
		if (healthStatus)
			return Health.up();
		return Health.down();
	}

	// Another Aspect or Something to manage Exception
	/*private String getDatabaseHealth() {
		healthStatus = false;
		String message = "DataBase is Down";
		try {
			if (dataprovider.databasePing() > 0) {
				healthStatus = true;
				return message = "DataBase is UP";
			}
		} catch (Exception ex) {
			// System.out.println(ex.getMessage());
		}
		return message;
	}*/

}
