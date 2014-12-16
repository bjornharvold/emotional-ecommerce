/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository;

import com.lela.domain.document.JobMessage;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

/**
 * User: Chris Tallent
 * Date: 8/20/12
 * Time: 2:31 PM
 */
public interface JobMessageRepositoryCustom {
    List<JobMessage> findAllJobMessages(ObjectId jobExecutionId);

    void removeOlderThan(Date date);
}

