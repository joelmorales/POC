package com.example.dataproviders;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.crosscutting.aspect.exeptions.DataProviderExceptionAnnotation;
import com.example.dataproviders.database.domain.StatusTable;
import com.example.dataproviders.database.persistance.StatusDAO;

@Component
public class DataBaseProvider {

	@Autowired
	@Qualifier("jdbcServiceTemplate")
	private JdbcTemplate jdbcTemplate;

	//@Autowired
	private StatusDAO statusDAO;

	public DataBaseProvider(StatusDAO statusDAO) {
		this.statusDAO=statusDAO;
	}
	
	@Transactional
	@DataProviderExceptionAnnotation
	public int databasePing() {
		String sql = "select 1";
		int count = jdbcTemplate.queryForObject(sql, Integer.class);
		return count;
	}

	@Transactional
	@DataProviderExceptionAnnotation
	public int saveJDBC(String message) {
		String sql="insert into status(ID,MESSAGE) values(?,?)";
		int t=jdbcTemplate.update(sql, new Object[] { getID(),message});
		return t;
	}
	
	@Transactional
	@DataProviderExceptionAnnotation
	public String save(String message) {
		return statusDAO.save(new StatusTable(getID(),message)).getId();
	}

	private String getID() {
		return UUID.randomUUID().toString();
	}
	
	
}
