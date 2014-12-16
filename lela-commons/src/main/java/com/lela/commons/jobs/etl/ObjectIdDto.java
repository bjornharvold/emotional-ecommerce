/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.etl;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * User: Chris Tallent
 * Date: 5/24/12
 * Time: 9:53 AM
 */
public class ObjectIdDto {
    @Id
    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
