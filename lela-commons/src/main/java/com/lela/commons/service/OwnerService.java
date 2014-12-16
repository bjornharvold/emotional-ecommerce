/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Owner;
import com.lela.domain.dto.owner.OwnerAggregateQuery;
import com.lela.domain.dto.owner.OwnersSearchResult;
import com.lela.domain.dto.search.NameValueAggregate;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 5/2/12
 * Time: 2:43 PM
 * Responsibility:
 */
public interface OwnerService {
    List<Owner> saveOwners(List<Owner> list);
    Owner findOwnerByUrlName(String urlName);
    Owner removeOwner(String rlnm);
    Owner saveOwner(Owner owner);
    Long findOwnerCount();
    Page<Owner> findOwners(Integer page, Integer maxResults, String firstLetter);
    Page<Owner> findOwners(Integer page, Integer maxResults);
    List<Owner> findAllOwners(Integer chunk);
    List<NameValueAggregate> findOwnerAggregates(OwnerAggregateQuery oaq);
    OwnersSearchResult findOwnerAggregateDetails(OwnerAggregateQuery oaq);
    List<Owner> findOwners(List<String> fields);
}
