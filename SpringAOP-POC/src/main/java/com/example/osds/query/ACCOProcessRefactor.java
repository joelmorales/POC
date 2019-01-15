package com.example.osds.query;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import com.example.aspect.exceptions.ExceptionAnnotation;
import com.example.crosscutting.BusinessException;
import com.example.crosscutting.DataProviderException;

@Service
public class ACCOProcessRefactor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ACCOProcessRefactor.class);
	private static final String ACCODomain = "ACCO";
	private static final String ACCRDomain = "ACCR";

	public ACCOProcessRefactor() {
		
	}
	
	@ExceptionAnnotation
	public String queryACCRMaintenanceMessages(String cases) {
		String messageResponse;
		String csDomainDescription = ACCRDomain;
		LOGGER.info("Received Coldstart Jmx call: Start " + csDomainDescription);
		messageResponse = ((ACCOProcessRefactor)AopContext.currentProxy()).getResponseMessage(cases);
		return messageResponse;
	}

	public String queryACCOMaintenanceMessages() {
		String messageResponse;
		String csDomainDescription = ACCODomain;
		try {
			LOGGER.info("Received Coldstart Jmx call: Start " + csDomainDescription);

			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
			String correlationID = ColdStartOutputMessages.CORRELATION_ID_PREFIX + currentTimestamp.getTime();

			// correlationID = getExistingCorrelationId(CsSource.MTX,
			// CsDomain.ACCR).orElse(correlationID);

			// String correlationID = ColdStartOutputMessages.CORRELATION_ID_PREFIX +
			// currentTimestamp.getTime();

			// correlationID = getExistingCorrelationId(CsSource.MTX,
			// CsDomain.ACCR).orElse(correlationID);

			ColdStartResponseMessage response = performRequestToMTXForACCR(correlationID);

			messageResponse = response.toString();

		} catch (Exception e) {
			messageResponse = String.format(ColdStartOutputMessages.UNEXPECTED_EXCEPTION, csDomainDescription,
					e.getMessage());
			LOGGER.error("SERVICE_LOGGER-" + messageResponse, e);
			LOGGER.error("ITOPS_LOGGER-" + messageResponse, e);
		}
		return messageResponse;
	}

	@ExceptionAnnotation
	private String getResponseMessage(String cases) {
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		String correlationID = ColdStartOutputMessages.CORRELATION_ID_PREFIX + currentTimestamp.getTime();
		String messageResponse = String.format(ColdStartOutputMessages.UNEXPECTED_EXCEPTION, "ACCRDomain");
		if(cases.equals("A")) {
		 	throw new BusinessException("Bussiness Error");
		}
		else if(cases.equals("B")) {
		 	throw new DataProviderException("DataProvider Error");
		}
		else if(cases.equals("C")) {
		 	throw new IndexOutOfBoundsException("IndexOutOfBounds Error");
		}
		ColdStartResponseMessage response = performRequestToMTXForACCR(correlationID);
		messageResponse = response.toString();
		return messageResponse;
	}

	private ColdStartResponseMessage performRequestToMTXForACCR(String correlationID) {
		return new ColdStartResponseMessage("MTX", "Message:"+correlationID);
	}

}
