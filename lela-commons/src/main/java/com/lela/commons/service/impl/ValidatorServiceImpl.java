/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.service.ValidatorService;
import com.lela.commons.validator.AttributeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 5/29/12
 * Time: 7:53 PM
 * Responsibility:
 */
public class ValidatorServiceImpl implements ValidatorService {
    private final static Logger log = LoggerFactory.getLogger(ValidatorServiceImpl.class);
    private Map<String, Map<String, AttributeValidator>> profiles;

    @Override
    public boolean isPayloadValid(String profile, Map<String, List<String>> map) {
        boolean result = true;

        if (profiles.containsKey(profile)) {
            Map<String, AttributeValidator> attributeValidators = profiles.get(profile);

            // loop through to check all keys and values
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {

                if (attributeValidators.containsKey(entry.getKey())) {
                    if (!attributeValidators.get(entry.getKey()).validate(entry.getValue())) {
                        result = false;
                    }
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn(String.format("%s is not a valid key", entry.getKey()));
                    }
                    result = false;
                }
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn(String.format("%s is not a valid profile", profile));
            }
            // profile doesn't even exist to validate against
            result = false;
        }

        return result;
    }

    @Required
    public void setProfiles(Map<String, Map<String, AttributeValidator>> profiles) {
        this.profiles = profiles;
    }
}
