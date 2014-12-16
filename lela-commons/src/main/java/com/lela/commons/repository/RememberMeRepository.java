/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.RememberMe;
import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Bjorn Harvold
 * Date: 7/18/11
 * Time: 12:00 PM
 * Responsibility:
 */
public interface RememberMeRepository extends CrudRepository<RememberMe, ObjectId> {
    RememberMe findBySrs(String series);
    RememberMe findByMl(String email);
}
