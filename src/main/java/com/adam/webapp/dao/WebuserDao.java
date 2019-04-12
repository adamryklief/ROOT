package com.adam.webapp.dao;

import com.adam.webapp.entity.UserEntity;

public interface WebuserDao {
	
	public void openConnection();
	public void closeConnection();
	public UserEntity getUserByUserId(String userId);

}
