package com.lela.commons.monitoring.mongo;

public class Cursors {

	private Integer totalOpen;
	private Integer clientCursors_size;
	private Integer timedOut;
	public Integer getTotalOpen() {
		return totalOpen;
	}
	public void setTotalOpen(Integer totalOpen) {
		this.totalOpen = totalOpen;
	}
	public Integer getClientCursors_size() {
		return clientCursors_size;
	}
	public void setClientCursors_size(Integer clientCursors_size) {
		this.clientCursors_size = clientCursors_size;
	}
	public Integer getTimedOut() {
		return timedOut;
	}
	public void setTimedOut(Integer timedOut) {
		this.timedOut = timedOut;
	}
	

	
}
