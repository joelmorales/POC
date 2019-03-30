package com.example.entrypoints;

import java.util.List;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dataproviders.DataBaseProvider;
import com.example.dataproviders.database.domain.StatusTable;
import com.example.library.aspect.RestHealthCheck;

@RestController
public class ServiceRestController {

	@Autowired
	private DataBaseProvider dataBaseProvider;


	@RequestMapping("/status/list")
	@ResponseBody
	public List<StatusTable> getList() {
		return ((ServiceRestController) AopContext.currentProxy()).getStatusList();
	}

	@RestHealthCheck
	protected List<StatusTable> getStatusList() {
		return dataBaseProvider.getStatusList();
	}

	/*@RequestMapping("/status/jobs")
	@ResponseBody
	public List<String> getScheduleJobs() throws Exception {
		return jobConfiguration.getJobGroupNames();
	}*/

}
