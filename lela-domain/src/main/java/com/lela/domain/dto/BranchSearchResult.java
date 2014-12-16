/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Branch;
import com.lela.domain.document.Offer;
import com.lela.domain.document.Store;

import java.util.List;

/**
 * Created by Chris Tallent
 * Date: 11/8/11
 * Time: 11:41 AM
 * Responsibility:
 */
public class BranchSearchResult {

    private Branch branch;
    private Store store;
    private Double distanceInMiles;
    private Double listPrice;
    private List<Offer> offers;

    public BranchSearchResult() {

    }

    public BranchSearchResult(Branch branch, Double distanceInMiles) {
        this.branch = branch;
        this.distanceInMiles = distanceInMiles;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Double getDistanceInMiles() {
        return distanceInMiles;
    }

    public void setDistanceInMiles(Double distanceInMiles) {
        this.distanceInMiles = distanceInMiles;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public void setListPrice(Double listPrice) {
        this.listPrice = listPrice;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
