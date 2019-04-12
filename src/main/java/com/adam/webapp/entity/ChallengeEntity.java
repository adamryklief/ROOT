package com.adam.webapp.entity;

import java.sql.Date;
import java.sql.Time;

public class ChallengeEntity {
	
	private Date date;
	private Time hour;
	private int randomNumber;

	@Override
	public String toString() {
		return "ChallengeEntity [date=" + date + ", hour=" + hour + ", randomNumber=" + randomNumber + "]";
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Time getHour() {
		return hour;
	}
	public void setHour(Time hour) {
		this.hour = hour;
	}
	public int getRandomNumber() {
		return randomNumber;
	}
	public void setRandomNumber(int randomNumber) {
		this.randomNumber = randomNumber;
	}

}
