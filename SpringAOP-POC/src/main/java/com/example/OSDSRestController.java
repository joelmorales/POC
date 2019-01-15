package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.aspect.exceptions.AspectForExceptions;
import com.example.aspect.exceptions.ExceptionAnnotation;
import com.example.aspect.logs.LogsAnnotation;
import com.example.aspect.parameters.AspectForParameters;
import com.example.osds.location.GlobalLocation;
import com.example.osds.location.Location;
import com.example.osds.query.ACCOProcessRefactor;

@RestController
public class OSDSRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OSDSRestController.class);
	
	@Autowired
    private ACCOProcessRefactor accoprocess ;
	
	@Autowired
	private GlobalLocation globalLocation;
	
	
	@RequestMapping("/logs")
	@ResponseBody
	@LogsAnnotation
	@AspectForParameters
	public String logs(@RequestParam(value = "steps", defaultValue = "World") String steps) {
		return steps;
	}

	@RequestMapping("/exceptions")
	@ResponseBody
	@ExceptionAnnotation
	public String exceptions(@RequestParam(value = "cases", defaultValue = "World") String cases) {
		try {
			String response = accoprocess.queryACCRMaintenanceMessages(cases);
			return response;
		} catch (Exception ex) {
			LOGGER.info("Writing default message to client"+ex.toString());
		}
		return "DEFAULT";
	}

	@RequestMapping("/retry")
	@ResponseBody
	public String retry(@RequestParam(value = "id", defaultValue = "World") String id) {
		try {
			return globalLocation.getLocation(id).toString();
		} catch (Exception ex) {
			LOGGER.info("Writing default message to client"+ex.toString());
			throw new LocationNotAvailableException(id);
		}
		
	}
	
}
