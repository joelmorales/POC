package com.example.library.healthcheck;

public class CalculateIntervals  {

	private int increment = 1;
	private double factor = 1.5;
	private int intervalInSeconds = 5;

	public CalculateIntervals(int intervalInSeconds) {		
		this.intervalInSeconds = (int) (intervalInSeconds * increment * factor);
		//4+1.5 X Slope
		
		if(this.intervalInSeconds <= 1){
			this.intervalInSeconds=2;
		}
	}

	
	public int getIntervalInSeconds() {
		return intervalInSeconds;
	}

	


}
