package com.example.osds.location;

public class Location {

	private String coordinates;
	private String address;

	public Location() {
		
	}
	
	public Location(String coordinates, String address) {

		this.coordinates = coordinates;
		this.address = address;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return "Location [coordinates=" + coordinates + ", address=" + address + "]";
	}

}
