/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Chris Tallent
 * Date: 2/7/12
 * Time: 10:55 AM
 */
public class ObjectIds extends AbstractJSONPayload {
    private List<ObjectId> list = new ArrayList<ObjectId>();

    public List<ObjectId> getList() {
        return list;
    }

    public void setList(List<ObjectId> list) {
        this.list = list;
    }
}