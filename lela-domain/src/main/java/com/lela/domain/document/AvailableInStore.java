/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.StoreType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 1/5/12
 * Time: 7:20 PM
 * Responsibility:
 */
public class AvailableInStore extends AbstractStore implements Serializable {
    private static final long serialVersionUID = -5401578318252610063L;

    /** Deals */
    private List<Deal> dls;

    /** Items */
    private List<Item> tms;

    public AvailableInStore() {
    }

    public AvailableInStore(String merchantId) {
        super(merchantId);
    }

    public AvailableInStore(String nm, String rlnm, StoreType... tp) {
        super(nm, rlnm, tp);
    }

    public List<Deal> getDls() {
        return dls;
    }

    public void setDls(List<Deal> dls) {
        this.dls = dls;
    }

    public List<Item> getTms() {
        return tms;
    }

    public void setTms(List<Item> tms) {
        this.tms = tms;
    }

    /**
     * Populates this object with another AbstractStore
     * @param store store
     */
    public void populateStore(AbstractStore store) {
        setLgrl(store.getLgrl());
        setMgqlty(store.getMgqlty());
        setMrchntd(store.getMrchntd());
        setNm(store.getNm());
        setNtwrkd(store.getNtwrkd());
        setRl(store.getRl());
        setRlnm(store.getRlnm());
        setTp(store.getTp());
        setPprvd(store.getPprvd());
    }
}
