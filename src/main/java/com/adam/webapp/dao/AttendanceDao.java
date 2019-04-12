package com.adam.webapp.dao;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import com.adam.webapp.entity.ChallengeEntity;
import com.adam.webapp.entity.ScheduledLectureEntity;
import com.adam.webapp.entity.StudentEntity;
import com.adam.webapp.entity.StudentScheduledLectureEntity;

public interface AttendanceDao {

	public void openConnection();
	public void closeConnection();
	public StudentEntity getStudentById(String studentId);
	public int updateStudent(StudentEntity studentEntity);
	public StudentEntity getStudentByAuthToken(String authToken);
	public ChallengeEntity getChallengeByDateTime(Date date, Time hour);
	public ChallengeEntity getChallengeByRandomNumber(int randomNumber);
	public ArrayList<String> getAllFirebaseDeviceIdsOfStudentsWithLecturesByDateTime(Date date, Time time);
	public ArrayList<String> getAllStudentAuthTokens();
	public ScheduledLectureEntity getScheduledLectureByRoomIdOccurringOnStartsAt(String roomId, Date occurringOn, Time startsAt);
	public StudentScheduledLectureEntity getStudentScheduledLectureByStudentIdScheduledlectureId(String studentId, int scheduledLectureId);
	public int updateStudentScheduledLecture(StudentScheduledLectureEntity studentScheduledLectureEntity);
	public StudentScheduledLectureEntity[] getAllStudentScheduledLecturesUpToNowForSpecificStudent(String studentId);
}
