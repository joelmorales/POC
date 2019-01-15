package com.example.library.healthcheck;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.example.dataproviders.DataBaseProvider;

@Component
public class ServiceHealthIndicator implements HealthIndicator {

	@Autowired
	private DataBaseProvider dataprovider;

	private HashMap<String, String> serviceMap = new HashMap<String, String>();

	private Boolean healthStatus;

	@Override
	public Health health() {
		healthStatus = true;
		System.out.println("Health System Check");

		serviceMap.put("db", getDatabaseHealth());

		return buildHealthDetails();
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

	//Another Aspect or Something to manage Exception
	private String getDatabaseHealth() {
		healthStatus = false;
		String message = "DataBase is Down";
		try {
			if (dataprovider.databasePing() > 0) {
				healthStatus = true;
				return message = "DataBase is UP";
			}
		} catch (Exception ex) {
			//System.out.println(ex.getMessage());
		}
		return message;
	}

}
