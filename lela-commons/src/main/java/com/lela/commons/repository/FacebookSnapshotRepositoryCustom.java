/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository;

import org.bson.types.ObjectId;

/**
 * User: Chris Tallent
 * Date: 8/29/12
 * Time: 8:58 PM
 */
public interface FacebookSnapshotRepositoryCustom {
    void flagAsMotivatorsDone(ObjectId id);
    void removeByLelaEmail(String lelaEmail);
}
