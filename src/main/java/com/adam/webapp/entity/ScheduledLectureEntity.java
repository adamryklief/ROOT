package com.adam.webapp.entity;

import java.sql.Date;
import java.sql.Time;

public class ScheduledLectureEntity {

	private int scheduledLectureId;
	private String roomId;
	private Date occurringOn;
	private Time startsAt;
	private Time endsAt;
	private String courseCode;
	private String moduleCode;
	private int challengeRandomNumber;
	
	@Override
	public String toString() {
		return "ScheduledLectureEntity [scheduledLectureId=" + scheduledLectureId + ", roomId=" + roomId
				+ ", occurringOn=" + occurringOn + ", startsAt=" + startsAt + ", endsAt=" + endsAt + ", courseCode="
				+ courseCode + ", moduleCode=" + moduleCode + ", challengeRandomNumber=" + challengeRandomNumber + "]";
	}

	public int getScheduledLectureId() {
		return scheduledLectureId;
	}
	public void setScheduledLectureId(int scheduledLectureId) {
		this.scheduledLectureId = scheduledLectureId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public Date getOccurringOn() {
		return occurringOn;
	}
	public void setOccurringOn(Date occurringOn) {
		this.occurringOn = occurringOn;
	}
	public Time getStartsAt() {
		return startsAt;
	}
	public void setStartsAt(Time startsAt) {
		this.startsAt = startsAt;
	}
	public Time getEndsAt() {
		return endsAt;
	}
	public void setEndsAt(Time endsAt) {
		this.endsAt = endsAt;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public int getChallengeRandomNumber() {
		return challengeRandomNumber;
	}
	public void setChallengeRandomNumber(int challengeRandomNumber) {
		this.challengeRandomNumber = challengeRandomNumber;
	}
}
