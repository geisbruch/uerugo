package com.uerugo.model;

public class Location {
	Integer id;
	Float latitude;
	Float longitude;
	String place;
	
	public Location(Integer id, Float latitude, Float longitude, String name) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.place = name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String name) {
		this.place = name;
	}
	
	
}
