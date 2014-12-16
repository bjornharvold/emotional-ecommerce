/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.enums;

/**
 * Created by Bjorn Harvold
 * Date: 8/4/11
 * Time: 11:04 AM
 * Responsibility:
 *
    GENDER = Male or Female
    IMAGE = is the new simpler version of IMAGE_MULTIPLE_CHOICE_SINGLE_ANSWER
    SLIDER = Similar to range but the user can only select a single integer value
    DYNAMIC_SLIDER = Similar to dynamic range but the user can only select a single integer value
    deprecated IMAGE_MULTIPLE_CHOICE_SINGLE_ANSWER = Similar to multiple choice - single answer. This will display a list of images for the user to select.
 */
public enum QuestionType {
    SLIDER,
    IMAGE_MULTIPLE_CHOICE_SINGLE_ANSWER,
    STYLE_SLIDER,
    GENDER,
    IMAGE
}
