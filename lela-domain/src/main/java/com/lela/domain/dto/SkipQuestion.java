/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.enums.QuestionType;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/11
 * Time: 9:47 AM
 * Responsibility:
 */
public class SkipQuestion extends AbstractJSONPayload {
    private Integer group;
    private String urlName;
    private QuestionType type;
    
    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }
}
