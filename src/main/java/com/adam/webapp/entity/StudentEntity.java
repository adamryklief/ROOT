package com.adam.webapp.entity;

public class StudentEntity {
	
	private String studentId;
	private String firstName;
	private String lastName;
	private String imei;
	private String passwordHash;
	private String authToken;
	private String firebaseDeviceId;
	
	@Override
	public String toString() {
		return 	"\nid: " + studentId +
				"\nfName: " + firstName +
				"\nlName: " + lastName +
				"\nimei: " + imei +
				"\npassHash: " + passwordHash +
				"\nauthTok: " + authToken +
				"\nfireBase: " + firebaseDeviceId;
	}

	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getFirebaseDeviceId() {
		return firebaseDeviceId;
	}
	public void setFirebaseDeviceId(String firebaseDeviceId) {
		this.firebaseDeviceId = firebaseDeviceId;
	}
	
}