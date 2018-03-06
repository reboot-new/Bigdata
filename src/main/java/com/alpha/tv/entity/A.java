package com.alpha.tv.entity;

import java.util.Date;

public class A {

	private Date startTimeDate;
	private Date endTimeDate;
	private String pString;
	private String snString;
	private Long durationLong;
	
	public A() {
		// TODO Auto-generated constructor stub
	}
	
	public A(Date sTime, Date eTime, String pString,String snString,Long durationLong){
		this.startTimeDate = sTime;
		this.endTimeDate = eTime;
		this.pString = pString;
		this.snString = snString;
		this.durationLong = durationLong;
	}

	public String getpString() {
		return pString;
	}


	public void setpString(String pString) {
		this.pString = pString;
	}


	public String getSnString() {
		return snString;
	}


	public void setSnString(String snString) {
		this.snString = snString;
	}

	public Date getEndTimeDate() {
		return endTimeDate;
	}

	public void setEndTimeDate(Date endTimeDate) {
		this.endTimeDate = endTimeDate;
	}

	public Date getStartTimeDate() {
		return startTimeDate;
	}

	public void setStartTimeDate(Date startTimeDate) {
		this.startTimeDate = startTimeDate;
	}

	public Long getDurationLong() {
		return durationLong;
	}

	public void setDurationLong(Long durationLong) {
		this.durationLong = durationLong;
	}

}
