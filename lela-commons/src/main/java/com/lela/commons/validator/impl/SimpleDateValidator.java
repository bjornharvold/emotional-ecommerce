/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.validator.impl;

import com.lela.commons.validator.AttributeValidator;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 5/29/12
 * Time: 5:20 PM
 * Responsibility:
 */
public class SimpleDateValidator implements AttributeValidator {
    private DateTimeFormatter df;

    public SimpleDateValidator(String dateFormat) {
        this.df = DateTimeFormat.forPattern(dateFormat);
    }

    @Override
    public boolean validate(List<String> list) {
        boolean result = false;

        try {
            for (String value : list) {
                df.parseDateTime(value);
            }
            result = true;
        } catch (IllegalFieldValueException e) {
            result = false;
        }

        return result;
    }
}
