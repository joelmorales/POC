package com.example.dataproviders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.crosscutting.DataProviderException;
import com.example.crosscutting.aspect.exeptions.DataProviderExceptionAnnotation;
import com.example.dataproviders.database.domain.StatusTable;
import com.example.dataproviders.database.persistance.StatusDAO;
import com.example.library.healthcheck.ResourceHealthCheck;

@Component
public class DataBaseProvider implements ResourceHealthCheck {

	@Autowired
	@Qualifier("jdbcServiceTemplate")
	private JdbcTemplate jdbcTemplate;

	// @Autowired
	private StatusDAO statusDAO;

	public DataBaseProvider(StatusDAO statusDAO) {
		this.statusDAO = statusDAO;
	}

	@Transactional
	// @DataProviderExceptionAnnotation
	public int databasePing() {
		try {
			String sql = "select 1";
			int count = jdbcTemplate.queryForObject(sql, Integer.class);
			return count;
		} catch (Exception ex) {
			throw new DataProviderException("DataBase is down", ex);
		}
	}

	@Transactional
	@DataProviderExceptionAnnotation
	public List<StatusTable> getStatusList() {

		Iterable<StatusTable> iterableList = statusDAO.findAll();
		List<StatusTable> statusList = new ArrayList<StatusTable>();
		for (StatusTable s : iterableList) {
			statusList.add(s);
		}
		return statusList;

	}

	@Transactional
	@DataProviderExceptionAnnotation
	public int saveJDBC(String message) {

		String sql = "insert into status(ID,MESSAGE) values(?,?)";
		int t = jdbcTemplate.update(sql, new Object[] { getID(), message });
		return t;

	}

	@Transactional
	// @DataProviderExceptionAnnotation
	public String save(String message) {
		try {
			return statusDAO.save(new StatusTable(getID(), message)).getId();
		} catch (Exception ex) {
			throw new DataProviderException("DataBase is down", ex);
		}
	}

	private String getID() {
		return UUID.randomUUID().toString();
	}

	@Override
	public String getResourceName() {
		return "H2 Service DataBase";
	}

	@Override
	@Transactional
	public boolean isHealthly() {
		try {
			String sql = "select 1";
			int count = jdbcTemplate.queryForObject(sql, Integer.class);
			return true;
		} catch (Exception ex) {

		}
		return false;
	}

}
