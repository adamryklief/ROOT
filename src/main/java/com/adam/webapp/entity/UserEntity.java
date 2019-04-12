package com.adam.webapp.entity;

public class UserEntity {
	
	private String userId;
    private String passwordHash;
	
	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", passwordHash=" + passwordHash + "]";
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
}
