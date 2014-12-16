package com.lela.web.web.mailchimp;

public enum WebHookType {
	SUBSCRIBE("subscribe"), UNSUBSCRIBE("unsubscribe"), UPDATE_PROFILE("profile"), UPDATE_EMAIL("upemail"), CLEANED("cleaned");
	  private String type;

	  WebHookType(String type) {
	    this.type = type;
	  }

	  public String getType() {
	    return type;
	  }
}
