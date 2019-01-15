package com.example.SpringAOPPOCclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

	@RequestMapping("/location")
	@ResponseBody
	public Location location(@RequestParam(value = "id", defaultValue = "World") String id) {
		return getLocation(id);
	}

	private Location getLocation(String id) {
		return new Location("308.373838,3739.8938383", "2702 Love Field Dr, Dallas, TX 75235");
	}

}
