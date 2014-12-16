package com.lela.commons.service;

import com.lela.commons.event.RegistrationEvent;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/10/12
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserRegistrationEventService {
    public void registerUser(RegistrationEvent event);
}
