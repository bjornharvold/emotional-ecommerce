package com.lela.web.web.mailchimp;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import mailjimp.dom.response.list.MemberInfo;

public class WebHookData implements Serializable{
	  private WebHookType type;
	  private Date firedAt;
	  private Map<String, Object> rawData;
	  private MemberInfo memberInfo;

	  public WebHookData(WebHookType type) {
	    setType(type);
	  }

	  /**
	* Get the type of this callback.
	*
	* @return What is this all about?
	*/
	  public WebHookType getType() {
	    return type;
	  }

	  public void setType(WebHookType type) {
	    this.type = type;
	  }

	  /**
	* The time of this callback.
	*
	* @return When did it happen?
	*/
	  public Date getFiredAt() {
	    return firedAt;
	  }

	  public void setFiredAt(Date firedAt) {
	    this.firedAt = firedAt;
	  }

	  /**
	* Everything else. Including the raw data of a possible already parsed
	* {@link mailjimp.dom.list.MemberInfo MemberInfo} object.
	*
	* @return All we got.
	*/
	  public Map<String, Object> getRawData() {
	    return rawData;
	  }

	  public void setRawData(Map<String, Object> rawData) {
	    this.rawData = rawData;
	  }

	  /**
	* If there was MemberInfo in the request, here is the place to get it.
	*
	* @return Parsed member info or null;
	*/
	  public MemberInfo getMemberInfo() {
	    return memberInfo;
	  }

	  public void setMemberInfo(MemberInfo memberInfo) {
	    this.memberInfo = memberInfo;
	  }
}
