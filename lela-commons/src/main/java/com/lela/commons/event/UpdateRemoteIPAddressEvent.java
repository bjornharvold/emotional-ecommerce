package com.lela.commons.event;

import com.lela.domain.document.User;

public class UpdateRemoteIPAddressEvent extends AbstractUserEvent {

	private String remoteIPAddress;
	public String getRemoteIPAddress() {
		return remoteIPAddress;
	}
	public UpdateRemoteIPAddressEvent(User user, String remoteIPAddress) {
		super(user);
		this.remoteIPAddress = remoteIPAddress; 
	}
	
	

}
