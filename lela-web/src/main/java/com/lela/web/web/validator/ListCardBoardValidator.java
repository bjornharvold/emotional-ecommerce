/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.domain.document.ListCardBoard;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ListCardBoardValidator implements Validator {
    public boolean supports(Class<?> clazz) {
        return ListCardBoard.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ListCardBoard lcb = (ListCardBoard) target;

        /*
        TODO We'll see if we have to restrict on certain characters
        if (errors.getFieldErrorCount("nm") == 0) {
            if (!lcb.getNm().matches("[a-zA-Z0-9 ]+")) {
                errors.rejectValue("nm", "invalid.characters");
            }
        }
        */
    }
}