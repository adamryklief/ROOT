package com.adam.webapp.service;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.json.JSONObject;

import com.adam.webapp.dao.MySqlAttendanceDao;
import com.adam.webapp.entity.ChallengeEntity;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PushService {
	
	private static ServletContext servletContext;
	
	private static final Logger LOGGER = Logger.getLogger(PushService.class.getName());
	private static final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public static void setServletContext(ServletContext servletContext) {
		PushService.servletContext = servletContext;
	}
	
	public void pushRandomNumberToClients() {
		OkHttpClient client = new OkHttpClient();

        LocalDateTime dateTimeOfNextLecture = LocalDateTime.now().plusHours(1);
        ZonedDateTime utcZoned = ZonedDateTime.of(dateTimeOfNextLecture, ZoneOffset.UTC);
        ZoneId london = ZoneId.of("Europe/London");
        ZonedDateTime londonZoned = utcZoned.withZoneSameInstant(london);
        LocalDateTime londonLocal = londonZoned.toLocalDateTime();
        
        LocalDate dateOfNextLecture = londonLocal.toLocalDate();
        LocalTime timeOfNextLecture = londonLocal.with(ChronoField.MINUTE_OF_HOUR, 0)
                .with(ChronoField.SECOND_OF_MINUTE, 0).toLocalTime();
		
        ChallengeEntity challengeEntity;
		ArrayList<String> firebaseDeviceIds;
		
		MySqlAttendanceDao mySqlAttendanceDao = new MySqlAttendanceDao();
		mySqlAttendanceDao.openConnection();
		challengeEntity = mySqlAttendanceDao.getChallengeByDateTime(
				Date.valueOf(dateOfNextLecture), Time.valueOf(timeOfNextLecture));
		firebaseDeviceIds = mySqlAttendanceDao.getAllFirebaseDeviceIdsOfStudentsWithLecturesByDateTime(
				Date.valueOf(dateOfNextLecture), Time.valueOf(timeOfNextLecture));
		mySqlAttendanceDao.closeConnection();
		
		if(firebaseDeviceIds == null) {
			LOGGER.info("Currently no clients with lectures starting in the next hour");
			return;
		}
		if(challengeEntity == null) {
			return;
		}
		
		JSONObject jsonObjectDate = new JSONObject();
		jsonObjectDate.put(Time.valueOf(timeOfNextLecture).toString(),
				challengeEntity.getRandomNumber());
		JSONObject data = new JSONObject();
        data.put(Date.valueOf(dateOfNextLecture).toString(), jsonObjectDate);
        String[] group = firebaseDeviceIds.toArray(new String[firebaseDeviceIds.size()]);
        JSONObject message = new JSONObject();
        message.put("registration_ids", group);
        message.put("data", data);
        
        RequestBody body = RequestBody.create(JSON, message.toString());
        Request request = new Request.Builder()
                .url(API_URL_FCM)
                .header("Authorization", "key=" + servletContext.getInitParameter("FirebaseServerKey"))
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        try {
        	Response response = client.newCall(request).execute();
        	LOGGER.info("statusCode: " + response.code());
        	LOGGER.info("respBody: " + response.body().string());
        } catch (IOException e) {
        	LOGGER.severe(e.getMessage());
        }
	}
	

	
	
	
}