package com.adam.webapp.model;

public class Student {

	private String student_id;
	private String first_name;
	private String last_name;
	private String imei;
	private String password;
	private String token;
	private String firebase_device_id;
	
	public String getStudent_id() {
		return student_id;
	}
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password_hash) {
		this.password = password_hash;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getFirebase_device_id() {
		return firebase_device_id;
	}
	public void setFirebase_device_id(String firebase_device_id) {
		this.firebase_device_id = firebase_device_id;
	}
	
}
