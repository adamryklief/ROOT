package com.adam.webapp.service;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import java.util.logging.Logger;

import com.adam.webapp.dao.MySqlAttendanceDao;
import com.adam.webapp.entity.StudentEntity;
import com.adam.webapp.model.Student;
import com.adam.webapp.util.AuthUtil;
import com.adam.webapp.util.JsonUtil;

public class AuthService {
	
	private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
	
	public Response authenticateUsingBasic(Student student) {
		MySqlAttendanceDao mySqlAttendanceDao = new MySqlAttendanceDao();
		ResponseBuilder responseBuilder = Response.status(Response.Status.FORBIDDEN);
		try {
			mySqlAttendanceDao.openConnection();
			StudentEntity studentEntity = mySqlAttendanceDao.getStudentById(student.getStudent_id());
			
			if(studentEntity != null) {
				if(AuthUtil.isHashVerified(student.getPassword(), studentEntity.getPasswordHash())) {
					if(studentEntity.getImei() == null || studentEntity.getImei().contentEquals(student.getImei())) {
						String token = AuthUtil.issueToken(mySqlAttendanceDao.getAllStudentAuthTokens());
						studentEntity.setAuthToken(token);
						studentEntity.setImei(student.getImei());
						studentEntity.setFirebaseDeviceId(student.getFirebase_device_id());
						if(mySqlAttendanceDao.updateStudent(studentEntity) > 0)
							responseBuilder.status(Response.Status.OK).type(MediaType.APPLICATION_JSON)
									.entity(JsonUtil.makeJsonBasicAuthResponse("auth_token", token));
					} else {
						responseBuilder.entity("You can only login on the registered device");
					}
				} else {
					responseBuilder.entity("Invalid password");
				}
			} else {
				responseBuilder.status(Response.Status.UNAUTHORIZED).entity("You are not authorised to use this service");
			}
		} catch(Exception e) {
			LOGGER.severe(e.getMessage());
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Please try logging in again");
		} finally {
			mySqlAttendanceDao.closeConnection();
		}
		return responseBuilder.build();
	}
	
	public Response logOutStudent(ContainerRequestContext containerRequestContext) {
		MySqlAttendanceDao mySqlAttendanceDao = new MySqlAttendanceDao();
		ResponseBuilder responseBuilder = Response.status(
				Response.Status.INTERNAL_SERVER_ERROR).entity("Please try logging out again");
		try {
			mySqlAttendanceDao.openConnection();
			StudentEntity studentEntity = (StudentEntity)containerRequestContext.getProperty("student");
			studentEntity.setAuthToken(null);
			studentEntity.setFirebaseDeviceId(null);
			if(mySqlAttendanceDao.updateStudent(studentEntity) > 0)
				responseBuilder.status(Response.Status.OK).entity("You have been successfully logged out");
		} finally {
			mySqlAttendanceDao.closeConnection();
		}
		return responseBuilder.build();
	}
	
	
}
