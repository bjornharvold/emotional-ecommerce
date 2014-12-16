/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.ProfileService;
import com.lela.domain.document.User;
import com.lela.domain.dto.Users;
import com.lela.commons.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:19:22 AM
 * Inserts required categories into the system
 */
@SuppressWarnings("unchecked")
@Component("userBootstrapper")
public class UserBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(UserBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/users.json");

    @Value("${bootstrapper.user.enabled:true}")
    private Boolean enabled;

    private final UserService userService;
    private final ProfileService profileService;

    @Autowired
    public UserBootstrapper(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }


    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("JSON file could not be found");
        }

        log.info("Populated " + populated + " users in db");
        log.info("Omitted " + omitted + " users from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private Users parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), Users.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param users questions
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(Users users) throws BootstrapperException {
        List<User> dbList = new ArrayList<User>();

        try {

            for (User user : users.getList()) {
                User tmp = userService.findUserByEmail(user.getMl());

                if (tmp == null) {
                    dbList.add(user);
                    populated++;
                } else if (tmp.getRrlnms().size() != user.getRrlnms().size()) {
                    log.info("User has updated roles. Need to update user instead");
                    user.setId(tmp.getId());
                    dbList.add(user);
                    populated++;
                } else {
                    log.info("User already exists with email: " + user.getMl());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                profileService.registerUsers(dbList, false);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "UserBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}