package com.lela.commons.monitoring.mongo;

public class Connections {

	private Integer current;
	private Integer available;
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	public Integer getAvailable() {
		return available;
	}
	public void setAvailable(Integer available) {
		this.available = available;
	}

	
}
