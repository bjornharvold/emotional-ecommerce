/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.validator.impl;

import com.lela.commons.validator.AttributeValidator;

import java.util.List;

/**
 * Created by Chris Tallent
 * Date: 6/4/12
 */
public class HasTextValidator implements AttributeValidator {

    @Override
    public boolean validate(List<String> list) {
        for (String value : list) {
            if (value == null || value.length() == 0) {
                return false;
            }
        }

        return true;
    }
}
