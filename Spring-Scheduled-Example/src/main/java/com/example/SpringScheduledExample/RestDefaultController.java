package com.example.SpringScheduledExample;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobs.SchedulingValues;
import com.example.jobs.ScheduleJobs;

@RestController
public class RestDefaultController {

	@Autowired
	private ScheduleJobs scheduleJobs;
	
	@RequestMapping("/start")
	@ResponseBody
	public String start(){
		scheduleJobs.schedule(new SchedulingValues(2));
		return LocalTime.now().toString();
	}
	
}
