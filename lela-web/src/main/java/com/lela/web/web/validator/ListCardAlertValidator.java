/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.domain.document.Alert;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ListCardAlertValidator implements Validator {
    public boolean supports(Class<?> clazz) {
        return Alert.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Alert alert = (Alert) target;

        if (errors.getFieldErrorCount("prc") == 0) {
            if (alert.getPrclrt() && alert.getPrc() == null) {
                errors.rejectValue("prc", "list.card.alert.error", null, "Please specify a number: e.g. 98.50");
            }
        }
    }
}