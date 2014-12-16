/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.Blog;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.Owner;
import com.lela.domain.document.StaticContent;
import com.lela.domain.document.Store;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 4/12/12
 * Time: 10:24 PM
 * Responsibility:
 */
public class Sitemap {
    private Map<String, List<Item>> tms;
    private List<Category> ctgrs;
    private List<Blog> blgs;
    private List<StaticContent> sttc;
    private List<Owner> wnrs;
    private List<Store> strs;

    public Map<String, List<Item>> getTms() {
        return tms;
    }

    public void setTms(Map<String, List<Item>> tms) {
        this.tms = tms;
    }

    public List<Category> getCtgrs() {
        return ctgrs;
    }

    public void setCtgrs(List<Category> ctgrs) {
        this.ctgrs = ctgrs;
    }

    public List<Blog> getBlgs() {
        return blgs;
    }

    public void setBlgs(List<Blog> blgs) {
        this.blgs = blgs;
    }

    public List<StaticContent> getSttc() {
        return sttc;
    }

    public void setSttc(List<StaticContent> sttc) {
        this.sttc = sttc;
    }

    public List<Owner> getWnrs() {
        return wnrs;
    }

    public void setWnrs(List<Owner> wnrs) {
        this.wnrs = wnrs;
    }

    public List<Store> getStrs() {
        return strs;
    }

    public void setStrs(List<Store> strs) {
        this.strs = strs;
    }
}
