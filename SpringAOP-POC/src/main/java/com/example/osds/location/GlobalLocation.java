package com.example.osds.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.aspect.circuitbreaker.CircuitBreaker;

@Service
public class GlobalLocation implements CircuitBreakerFallBack {

	@Autowired
	RestTemplate restTemplate ;
	
	@CircuitBreaker
	public Location getLocation(String id) {
		final String uri="http://localhost:8091/location" ;
		Location location = restTemplate.getForObject(uri, Location.class);
		return location;
	}
	
	@Override
	public Object getFallBack() {
		return new Location("1233.35353,1233.454545","4000  Love Field Dr, Dallas, TX 75235");
	}
	
	@Bean
	public RestTemplate restemplate() {
		return new RestTemplate();
	}

	
	
}
