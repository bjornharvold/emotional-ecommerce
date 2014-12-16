package com.lela.commons.jobs;

import java.util.Date;

public class TriggerDetail {

	private String key;
	private String status;
	private Date nextRunTime;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getNextRunTime() {
		return nextRunTime;
	}
	public void setNextRunTime(Date nextRunTime) {
		this.nextRunTime = nextRunTime;
	}
}
