package com.example.osds.query;

public class ColdStartResponseMessage {

	private String name;
	private String description;

	public ColdStartResponseMessage(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
