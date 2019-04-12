package com.adam.webapp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.adam.webapp.entity.ChallengeEntity;
import com.adam.webapp.entity.ScheduledLectureEntity;
import com.adam.webapp.entity.StudentEntity;
import com.adam.webapp.entity.StudentScheduledLectureEntity;

public class MySqlAttendanceDao implements AttendanceDao {
	
	private static final Logger LOGGER = Logger.getLogger(MySqlAttendanceDao.class.getName());
	
	private DataSource datasource;
	private Connection connection;
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	
	public MySqlAttendanceDao() {
		try {
			datasource = (DataSource) new InitialContext().lookup("java:/comp/env/jdbc/GCMySQLDB");
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
	public StudentEntity getStudentById(String studentId) {
		StudentEntity studentEntity = null;
		String sql = "SELECT * FROM student WHERE student_id = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, studentId);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				studentEntity = new StudentEntity();
				studentEntity.setStudentId(resultSet.getString("student_id"));
				studentEntity.setFirstName(resultSet.getString("first_name"));
				studentEntity.setLastName(resultSet.getString("last_name"));
				studentEntity.setImei(resultSet.getString("imei"));
				studentEntity.setPasswordHash(resultSet.getString("password_hash"));
				studentEntity.setAuthToken(resultSet.getString("auth_token"));
				studentEntity.setFirebaseDeviceId(resultSet.getString("firebase_device_id"));
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return studentEntity;
	}
	
	@Override
	// Alternate key on student table is auth_token (unique value)
	public StudentEntity getStudentByAuthToken(String authToken) {
		StudentEntity studentEntity = null;
		String sql = "SELECT * FROM student WHERE auth_token = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, authToken);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				studentEntity = new StudentEntity();
				studentEntity.setStudentId(resultSet.getString("student_id"));
				studentEntity.setFirstName(resultSet.getString("first_name"));
				studentEntity.setLastName(resultSet.getString("last_name"));
				studentEntity.setImei(resultSet.getString("imei"));
				studentEntity.setPasswordHash(resultSet.getString("password_hash"));
				studentEntity.setAuthToken(resultSet.getString("auth_token"));
				studentEntity.setFirebaseDeviceId(resultSet.getString("firebase_device_id"));
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return studentEntity;
	}
	
	@Override
	// Alternate key on student table is auth_token
	public ArrayList<String> getAllStudentAuthTokens() {
		ArrayList<String> authTokens = null;
		String sql = "SELECT auth_token FROM student";
		try {
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				authTokens = new ArrayList<>();
				do {
					authTokens.add(resultSet.getString("auth_token"));
				} while(resultSet.next());
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return authTokens;
	}
	
	@Override
	public int updateStudent(StudentEntity studentEntity) {
		int rowsAffected = 0;
		String sql = "UPDATE student SET first_name = ?, last_name = ?, imei = ?, password_hash = ?,"
				+ "auth_token = ?, firebase_device_id = ? WHERE student_id = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, studentEntity.getFirstName());
			preparedStatement.setString(2, studentEntity.getLastName());
			preparedStatement.setString(3, studentEntity.getImei());
			preparedStatement.setString(4, studentEntity.getPasswordHash());
			preparedStatement.setString(5, studentEntity.getAuthToken());
			preparedStatement.setString(6, studentEntity.getFirebaseDeviceId());
			preparedStatement.setString(7, studentEntity.getStudentId());
			rowsAffected = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return rowsAffected;
	 }

	@Override
	// Alternate key on challenge table is <date, hour>
	public ChallengeEntity getChallengeByDateTime(Date date, Time hour) {
		ChallengeEntity challengeEntity = null;
		String sql = "SELECT * "
				+ "FROM challenge "
				+ "WHERE date = ? AND hour = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, date);
			preparedStatement.setTime(2, hour);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				challengeEntity = new ChallengeEntity();
				challengeEntity.setDate(resultSet.getDate("date"));
				challengeEntity.setHour(resultSet.getTime("hour"));
				challengeEntity.setRandomNumber(resultSet.getInt("random_number"));
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return challengeEntity;
	}
	
	@Override
	public ChallengeEntity getChallengeByRandomNumber(int randomNumber) {
		ChallengeEntity challengeEntity = null;
		String sql = "SELECT * "
				+ "FROM challenge "
				+ "WHERE random_number = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, randomNumber);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				challengeEntity = new ChallengeEntity();
				challengeEntity.setDate(resultSet.getDate("date"));
				challengeEntity.setHour(resultSet.getTime("hour"));
				challengeEntity.setRandomNumber(resultSet.getInt("random_number"));
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return challengeEntity;
	}

	@Override
	public ArrayList<String> getAllFirebaseDeviceIdsOfStudentsWithLecturesByDateTime(Date date, Time time) {
		ArrayList<String> firebaseDeviceIds = null;
		String sql = "SELECT DISTINCT(firebase_device_id) "
				+ "FROM student JOIN student_scheduled_lecture USING(student_id) "
				+ "JOIN scheduled_lecture USING(scheduled_lecture_id) "
				+ "WHERE occurring_on = ? AND starts_at = ?"
				+ "AND firebase_device_id IS NOT NULL";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDate(1, date);
			preparedStatement.setTime(2, time);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				firebaseDeviceIds = new ArrayList<>();
				do {
					firebaseDeviceIds.add(resultSet.getString("firebase_device_id"));
				} while(resultSet.next());
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return firebaseDeviceIds;
	}

	@Override
	// Alternate key on scheduled_lecture table is <room_id, occurring_on, starts_at>
	public ScheduledLectureEntity getScheduledLectureByRoomIdOccurringOnStartsAt(
			String roomId, Date occurringOn, Time startsAt) {
		ScheduledLectureEntity scheduledLectureEntity = null;
		String sql = "SELECT * "
				+ "FROM scheduled_lecture "
				+ "WHERE room_id = ? "
				+ "AND occurring_on = ? "
				+ "AND starts_at = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, roomId);
			preparedStatement.setDate(2, occurringOn);
			preparedStatement.setTime(3, startsAt);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				scheduledLectureEntity = new ScheduledLectureEntity();
				scheduledLectureEntity.setScheduledLectureId(resultSet.getInt("scheduled_lecture_id"));
				scheduledLectureEntity.setRoomId(resultSet.getString("room_id"));
				scheduledLectureEntity.setOccurringOn(resultSet.getDate("occurring_on"));
				scheduledLectureEntity.setStartsAt(resultSet.getTime("starts_at"));
				scheduledLectureEntity.setEndsAt(resultSet.getTime("ends_at"));
				scheduledLectureEntity.setCourseCode(resultSet.getString("course_code"));
				scheduledLectureEntity.setModuleCode(resultSet.getString("module_code"));
				scheduledLectureEntity.setChallengeRandomNumber(resultSet.getInt("challenge_random_number"));
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return scheduledLectureEntity;
	}
	
	@Override
	public StudentScheduledLectureEntity getStudentScheduledLectureByStudentIdScheduledlectureId(
			String studentId, int scheduledLectureId) {
		StudentScheduledLectureEntity studentScheduledLectureEntity = null;
		String sql = "SELECT * "
				+ "FROM student_scheduled_lecture "
				+ "WHERE student_id = ? "
				+ "AND scheduled_lecture_id = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, studentId);
			preparedStatement.setInt(2, scheduledLectureId);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				studentScheduledLectureEntity = new StudentScheduledLectureEntity();
				studentScheduledLectureEntity.setStudentId(resultSet.getString("student_id"));
				studentScheduledLectureEntity.setScheduledLectureId(resultSet.getInt("scheduled_lecture_id"));
				studentScheduledLectureEntity.setHasAttended(resultSet.getBoolean("has_attended"));
				studentScheduledLectureEntity.setRandomNumberResponse(resultSet.getInt("random_number_response"));
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return studentScheduledLectureEntity;
	}

	@Override
	public int updateStudentScheduledLecture(StudentScheduledLectureEntity studentScheduledLectureEntity) {
		int rowsAffected = 0;
		String sql = "UPDATE student_scheduled_lecture "
				+ "SET student_id = ?, "
				+ "scheduled_lecture_id = ?, "
				+ "has_attended = ?, "
				+ "random_number_response = ? "
				+ "WHERE student_id = ? "
				+ "AND scheduled_lecture_id = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, studentScheduledLectureEntity.getStudentId());
			preparedStatement.setInt(2, studentScheduledLectureEntity.getScheduledLectureId());
			preparedStatement.setBoolean(3, studentScheduledLectureEntity.hasAttended());
			preparedStatement.setInt(4, studentScheduledLectureEntity.getRandomNumberResponse());
			preparedStatement.setString(5, studentScheduledLectureEntity.getStudentId());
			preparedStatement.setInt(6, studentScheduledLectureEntity.getScheduledLectureId());
			rowsAffected = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return rowsAffected;
	}
	
	public StudentScheduledLectureEntity[] getAllStudentScheduledLecturesUpToNowForSpecificStudent(
			String studentId) {
		StudentScheduledLectureEntity studentScheduledLectureEntity;
		List<StudentScheduledLectureEntity> studentScheduledLectureEntities = null;
		String sql = "SELECT * "
				+ "FROM student_scheduled_lecture "
				+ "JOIN scheduled_lecture USING(scheduled_lecture_id) "
				+ "WHERE occurring_on <= CURDATE() "
				+ "AND student_id = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, studentId);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				studentScheduledLectureEntities = new ArrayList<>();
				do {
					studentScheduledLectureEntity = new StudentScheduledLectureEntity();
					studentScheduledLectureEntity.setStudentId(resultSet.getString("student_id"));
					studentScheduledLectureEntity.setScheduledLectureId(resultSet.getInt("scheduled_lecture_id"));
					studentScheduledLectureEntity.setHasAttended(resultSet.getBoolean("has_attended"));
					studentScheduledLectureEntity.setRandomNumberResponse(resultSet.getInt("random_number_response"));
					studentScheduledLectureEntities.add(studentScheduledLectureEntity);
				} while(resultSet.next());
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		if(studentScheduledLectureEntities != null) {
			return studentScheduledLectureEntities.toArray(new StudentScheduledLectureEntity[0]);
		} else
			return null;
	}

}
