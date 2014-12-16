/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Offer;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 8:40 PM
 * Responsibility:
 */
public interface OfferService {
    List<Offer> saveOffers(List<Offer> list);
    Offer saveOffer(Offer offer);
    Offer findOfferByUrlName(String urlName);
    List<Offer> findOffersByBranchUrlName(String branchUrlName);
    List<Offer> findValidOffersByBranchUrlName(String branchUrlName);
    Offer removeOffer(String rlnm);
}
