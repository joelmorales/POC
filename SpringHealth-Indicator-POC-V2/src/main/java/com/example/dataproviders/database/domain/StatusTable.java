package com.example.dataproviders.database.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name="Status")
//@Transactional
public class StatusTable {

	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, name="ID", nullable=false)
	private String id;
	
	@Column(name="Message",length=50)
	private String message;

	public StatusTable() {
		
	}
	
	public StatusTable(String id,String message) {
		this.message = message;
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}
	
	
	
}
