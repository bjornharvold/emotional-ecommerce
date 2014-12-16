/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Question;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/9/11
 * Time: 4:15 PM
 * Responsibility:
 */
public class Questions extends AbstractJSONPayload {
    private List<Question> list;

    public Questions() {
    }

    public Questions(List<Question> list) {
        this.list = list;
    }

    public List<Question> getList() {
        return list;
    }

    public void setList(List<Question> list) {
        this.list = list;
    }
}
