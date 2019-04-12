package com.adam.webapp.service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.adam.webapp.dao.MySqlAttendanceDao;
import com.adam.webapp.entity.ScheduledLectureEntity;
import com.adam.webapp.entity.StudentEntity;
import com.adam.webapp.entity.StudentScheduledLectureEntity;
import com.adam.webapp.model.AttendancePayload;
import com.adam.webapp.util.CryptUtil;

public class AttendanceService {
	
	private static ServletContext servletContext;
	
	private static final Logger LOGGER = Logger.getLogger(AttendanceService.class.getName());
	
	public static void setServletContext(ServletContext servletContext) {
		AttendanceService.servletContext = servletContext;
	}
	
	public Response processLogAttendanceRequest(ContainerRequestContext containerRequestContext,
			AttendancePayload attendancePayload) {
		MySqlAttendanceDao mySqlAttendanceDao = new MySqlAttendanceDao();
		ResponseBuilder responseBuilder = Response.status(Response.Status.FORBIDDEN);
		
		try {
			byte[] encoded = attendancePayload.getEncodedPayload().getBytes(StandardCharsets.ISO_8859_1);
			byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(encoded);
			byte[] decrypted = CryptUtil.getDecryptedBytes(decoded, servletContext.getInitParameter("SmartCardSecret"));
			String decryptedDecoded = new String(decrypted, "ISO_8859_1");
			String[] roomIdRandomNumberResponse = decryptedDecoded.split("\\.");
			String roomId = roomIdRandomNumberResponse[0];
			int randomNumberResponse = Integer.valueOf(roomIdRandomNumberResponse[1]);
			
			LocalDateTime localDateTimeNow = LocalDateTime.now();
			ZonedDateTime utcZoned = ZonedDateTime.of(localDateTimeNow, ZoneOffset.UTC);
	        ZoneId london = ZoneId.of("Europe/London");
	        ZonedDateTime londonZoned = utcZoned.withZoneSameInstant(london);
	        LocalDateTime londonLocal = londonZoned.toLocalDateTime();

			LocalDate dateOfCurrentLecture = londonLocal.toLocalDate();
			LocalTime timeOfCurrentLecture = londonLocal
	                .with(ChronoField.MINUTE_OF_HOUR, 0)
	                .with(ChronoField.SECOND_OF_MINUTE, 0).toLocalTime();
			
			mySqlAttendanceDao.openConnection();
			ScheduledLectureEntity scheduledLectureEntity =
					mySqlAttendanceDao.getScheduledLectureByRoomIdOccurringOnStartsAt(
					roomId, Date.valueOf(dateOfCurrentLecture), Time.valueOf(timeOfCurrentLecture));
			
			if(scheduledLectureEntity != null) {
				if(scheduledLectureEntity.getChallengeRandomNumber() == randomNumberResponse) {
					StudentEntity studentEntity = (StudentEntity)containerRequestContext.getProperty("student");
					StudentScheduledLectureEntity studentScheduledLectureEntity =
							mySqlAttendanceDao.getStudentScheduledLectureByStudentIdScheduledlectureId(
									studentEntity.getStudentId(), scheduledLectureEntity.getScheduledLectureId());
					if(studentScheduledLectureEntity != null) {
						studentScheduledLectureEntity.setStudentId(studentEntity.getStudentId());
						studentScheduledLectureEntity.setScheduledLectureId(scheduledLectureEntity.getScheduledLectureId());
						studentScheduledLectureEntity.setHasAttended(true);
						studentScheduledLectureEntity.setRandomNumberResponse(randomNumberResponse);
						if(mySqlAttendanceDao.updateStudentScheduledLecture(studentScheduledLectureEntity) > 0) {
							responseBuilder.status(Response.Status.OK).entity("You have been signed in");
						}
					} else {
						responseBuilder.entity("Can't sign you. You are not scheduled to attent a lecture in this room");
					}
				}
			} else {
				responseBuilder.entity("Can't sign you. No lecture is currently scheduled in this room");
			}
		} catch (Exception e) {
			LOGGER.severe(e.getMessage());
			responseBuilder.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Cannot sign you in right now. Please try again later");
		} finally {
			mySqlAttendanceDao.closeConnection();
		}
		return responseBuilder.build();
	}
	

}
