package com.adam.webapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.adam.webapp.entity.UserEntity;

public class MySqlWebuserDao implements WebuserDao{
	
private static final Logger LOGGER = Logger.getLogger(MySqlWebuserDao.class.getName());
	
	private DataSource datasource;
	private Connection connection;
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	
	public MySqlWebuserDao() {
		try {
			datasource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/webuserDB");
		} catch (NamingException e) {
			LOGGER.severe(e.getMessage());
		}
	}

	@Override
	public void openConnection() {
		try {
			if(connection == null || connection.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = datasource.getConnection();
			}
		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.severe(e.getMessage());
		}
	}

	@Override
	public void closeConnection() {
		try {
			if(resultSet != null && !resultSet.isClosed())
				resultSet.close();
			if(preparedStatement != null && !preparedStatement.isClosed())
				preparedStatement.close();
			if(connection != null && !connection.isClosed())
				connection.close();
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
	}

	@Override
	public UserEntity getUserByUserId(String userId) {
		UserEntity userEntity = null;
		String sql = "SELECT * FROM user WHERE user_id = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				userEntity = new UserEntity();
				userEntity.setUserId(resultSet.getString("user_id"));
				userEntity.setPasswordHash(resultSet.getString("password_hash"));
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return userEntity;
	}

}
