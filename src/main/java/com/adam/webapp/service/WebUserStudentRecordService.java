package com.adam.webapp.service;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.adam.webapp.dao.MySqlAttendanceDao;
import com.adam.webapp.entity.StudentEntity;
import com.adam.webapp.entity.StudentScheduledLectureEntity;

public class WebUserStudentRecordService {
	
	private static final Logger LOGGER = Logger.getLogger(WebUserStudentRecordService.class.getName());
	
	public Response getStudentAttendanceRecords(HttpServletRequest request, String studentId) {
		MySqlAttendanceDao mySqlAttendanceDao = new MySqlAttendanceDao();
		ResponseBuilder responseBuilder = Response.status(Response.Status.UNAUTHORIZED);
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			try {
				mySqlAttendanceDao.openConnection();
				StudentEntity studentEntity = mySqlAttendanceDao.getStudentById(studentId);
				if(studentEntity != null) {
					StudentScheduledLectureEntity[] studentScheduledLecturesUpToNowForSpecificStudent =
							mySqlAttendanceDao.getAllStudentScheduledLecturesUpToNowForSpecificStudent(studentId);
					session.setAttribute("studentScheduledLecturesUpToNowForSpecificStudent"
							, studentScheduledLecturesUpToNowForSpecificStudent);
					if(studentScheduledLecturesUpToNowForSpecificStudent != null) {
						int [] totalLecturesTotalAttended = getTotalLecturesTotalAttended(
								studentScheduledLecturesUpToNowForSpecificStudent);
						responseBuilder.status(Response.Status.OK).entity(getRecordTextOutput(studentEntity, totalLecturesTotalAttended));
						
					} else {
						responseBuilder.status(Response.Status.OK).entity("No scheduled lectures records exist");
					}
				} else {
					responseBuilder.status(Response.Status.NOT_FOUND).entity("Student does not exist");
				}
				
			} catch(Exception e) {
				LOGGER.severe(e.getMessage());
				responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity("Something went wrong. Please try again later");
			} finally {
				mySqlAttendanceDao.closeConnection();
			}
		} else {
			responseBuilder.status(Response.Status.UNAUTHORIZED).entity("You are not authorised to use this service");
		}
		return responseBuilder.build();
	}
	
	private int[] getTotalLecturesTotalAttended(
			StudentScheduledLectureEntity[] singleStudentScheduledLecturesRecords) {
		int [] totalLecturesTotalAttended = new int[2];
		int attendedCount = 0;
		totalLecturesTotalAttended[0] = singleStudentScheduledLecturesRecords.length;
		for(StudentScheduledLectureEntity studentScheduledLectureEntity : singleStudentScheduledLecturesRecords) {
			if(studentScheduledLectureEntity.hasAttended())
				attendedCount++;
		}
		totalLecturesTotalAttended[1] = attendedCount;
		return totalLecturesTotalAttended;
	}
	
	private String getRecordTextOutput(StudentEntity studentEntity, int [] totalLecturesTotalAttended) {
		StringBuilder sb = new StringBuilder("Student Attendance Record:\n\n")
				.append("Student Number: ")
				.append(studentEntity.getStudentId())
				.append("\n")
				.append("Name: ")
				.append(studentEntity.getFirstName())
				.append(" ")
				.append(studentEntity.getLastName())
				.append("\n")
				.append("Total Lectures: ")
				.append(totalLecturesTotalAttended[0])
				.append("\n")
				.append("Lectures attended: ")
				.append(totalLecturesTotalAttended[1]);
		return sb.toString();
	}

}
