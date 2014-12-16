/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.validator;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 5/29/12
 * Time: 5:22 PM
 * Responsibility:
 */
public interface AttributeValidator {
    boolean validate(List<String> list);
}
