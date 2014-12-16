/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.enums;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/4/11
 * Time: 11:04 AM
 * Responsibility:
 *
 */
public enum FunctionalFilterType implements Serializable {
    MULTIPLE_CHOICE_MULTIPLE_ANSWER_AND,
    MULTIPLE_CHOICE_MULTIPLE_ANSWER_OR,
    MULTIPLE_CHOICE_SINGLE_ANSWER,
    DYNAMIC_RANGE,
    BRAND,
    STORE,
    CATEGORY
}
