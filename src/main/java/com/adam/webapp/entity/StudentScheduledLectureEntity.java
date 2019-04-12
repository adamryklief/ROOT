package com.adam.webapp.entity;

public class StudentScheduledLectureEntity {
	
	private String studentId;
	private int scheduledLectureId;
	private boolean hasAttended;
	private int randomNumberResponse;
	
	@Override
	public String toString() {
		return "StudentScheduledLecture [studentId=" + studentId + ", scheduledLectureId=" + scheduledLectureId
				+ ", hasAttended=" + hasAttended + ", randomNumberResponse=" + randomNumberResponse + "]";
	}

	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public int getScheduledLectureId() {
		return scheduledLectureId;
	}
	public void setScheduledLectureId(int scheduledLectureId) {
		this.scheduledLectureId = scheduledLectureId;
	}
	public boolean hasAttended() {
		return hasAttended;
	}
	public void setHasAttended(boolean hasAttended) {
		this.hasAttended = hasAttended;
	}
	public int getRandomNumberResponse() {
		return randomNumberResponse;
	}
	public void setRandomNumberResponse(int randomNumberResponse) {
		this.randomNumberResponse = randomNumberResponse;
	}
}
