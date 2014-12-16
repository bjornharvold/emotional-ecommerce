package com.lela.domain.document;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CronTrigger extends AbstractDocument implements Serializable{

	private static final long serialVersionUID = -3810060165370573380L;
	public static String UN_ASSIGNED_TRIGGER_URLNAME = "NONE";
	public static String UN_ASSIGNED_TRIGGER_DESC = "None Assigned";
	public static String CONFIG_ASSIGNED_TRIGGER_URLNAME = "CONFIG";
	public static String CONFIG_ASSIGNED_TRIGGER_DESC = "Assigned via Config";
	public CronTrigger(){}
	
	public CronTrigger(String urlName, String description){
		this.rlnm = urlName;
		this.dscrptn = description;
	}
	/**
	 * URL Name
	 */
	private String rlnm;
	
	/**
	 * Description of CRON expression in plain english
	 */
	private String dscrptn;
	
	/**
	 * CRON Expression
	 */
	private String crnxprsn;
	
	

	public String getRlnm() {
		return rlnm;
	}

	public void setRlnm(String rlnm) {
		this.rlnm = rlnm;
	}

	public String getDscrptn() {
		return dscrptn;
	}

	public void setDscrptn(String dscrptn) {
		this.dscrptn = dscrptn;
	}

	public String getCrnxprsn() {
		return crnxprsn;
	}

	public void setCrnxprsn(String crnxprsn) {
		this.crnxprsn = crnxprsn;
	}
	
}
