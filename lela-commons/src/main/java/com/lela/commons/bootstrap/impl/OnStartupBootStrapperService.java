/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.BootStrapperService;
import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.spring.security.SpringSecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:17:07 AM
 */
public class OnStartupBootStrapperService implements BootStrapperService {
    private final static Logger log = LoggerFactory.getLogger(OnStartupBootStrapperService.class);
    private Boolean enabled = null;
    private Boolean complete = false;
    private List<Bootstrapper> bootstrappers = null;

    @Override
    public void init() {

        if (enabled) {

            // bind entityManager to current thread
            if (bootstrappers != null && bootstrappers.size() > 0) {

                try {
                    // set a fake user in the security context
                    SpringSecurityHelper.secureChannel();

                    for (Bootstrapper bs : bootstrappers) {

                        if (bs.getEnabled()) {
                            log.info("Creating data with: " + bs.toString());
                            bs.create();
                            log.info("Success: " + bs.toString());
                        } else {
                            log.info(String.format("%s is not enabled in this environment", bs.toString()));
                        }

                    }

                    complete = true;

                } catch (BootstrapperException e) {
                    log.error("Error creating data! " + e.getMessage(), e);
                }


            }
        } else {
            log.info("OnStartupBootStrapperService is currently disabled. Check application.properties file for property: 'data.creation.enabled'.");
        }
    }

    @Override
    public Boolean isComplete() {
        return complete;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setBootstrappers(List<Bootstrapper> bootstrappers) {
        this.bootstrappers = bootstrappers;
    }
}
