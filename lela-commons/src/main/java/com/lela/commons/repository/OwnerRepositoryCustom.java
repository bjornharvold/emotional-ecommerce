/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.Owner;
import com.lela.domain.document.Store;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 5/10/12
 * Time: 10:19 PM
 * Responsibility:
 */
public interface OwnerRepositoryCustom {
    List<Owner> findOwners(List<String> fields);
}
