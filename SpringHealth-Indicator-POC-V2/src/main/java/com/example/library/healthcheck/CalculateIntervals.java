package com.example.library.healthcheck;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CalculateIntervals  {

	@Value("${health.check.interval.increment}")
	private int increment ;
	@Value("${health.check.interval.factor}")
	private double factor ;
	@Value("${health.check.interval.start}")
	private int intervalInSeconds ;

	public CalculateIntervals() {
		
	}
	
	public int getNewInterval(int intervalInSeconds) {		
		this.intervalInSeconds = (int) (intervalInSeconds * increment * factor);
		//4+1.5 X Slope
		
		if(this.intervalInSeconds <= 1){
			this.intervalInSeconds=2;
		}
		return this.intervalInSeconds;
	}

	
	public int getIntervalInSeconds() {
		return intervalInSeconds;
	}

	


}
