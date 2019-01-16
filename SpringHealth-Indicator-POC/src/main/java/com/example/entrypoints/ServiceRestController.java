package com.example.entrypoints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.crosscutting.StatusDataNotAvailableException;
import com.example.dataproviders.DataBaseProvider;
import com.example.dataproviders.database.domain.StatusTable;

@RestController
public class ServiceRestController {

	@Autowired
	private DataBaseProvider dataBaseProvider;

	@RequestMapping("/status/list")
	@ResponseBody
	public List<StatusTable> getStatusList() {
		try {
			return dataBaseProvider.getStatusList();
		} catch (Exception ex) {
			throw new StatusDataNotAvailableException();
		}
	}

	
}
