package com.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.dataproviders.DataBaseProvider;
import com.example.dataproviders.database.domain.StatusTable;
import com.example.dataproviders.database.persistance.StatusDAO;

@Configuration
public class BeanConfiguration {

	/*@Bean
	public DataBaseProvider dataBaseProvider() {
		return new DataBaseProvider();
	}
	*/
	
}
