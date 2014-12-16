/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Color;
import com.lela.domain.document.Item;
import com.lela.domain.document.Tag;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.beans.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/15/12
 * Time: 11:10 PM
 * Responsibility:
 */
public class ItemSolrDocument extends AbstractSolrDocument {
    /** Item url name */
    private String rlnm;

    /** Item name */
    private String nm;

    /** Item UPC code */
    private String pc;

    /** Item Lela UPC code */
    private String llpc;

    /** Item category url name */
    private String ctgry;

    /** Item category url name */
    private String sctgry;

    /** Item attributes */
    private List<String> attrs;

    /** Item owner name */
    private String wnrnm;

    /** Item owner url name */
    private String wnrrlnm;

    /** Item owner url name */
    private String wnrsrlnm;

    /** Item stores url names */
    private List<String> strrlnms;

    /** Item stores url names */
    private List<String> strsrlnms;

    /** Item stores names */
    private List<String> strnms;

    /** Item colors */
    private List<String> clrs;

    /** Item Search Tags */
    private List<String> tgs;

    public ItemSolrDocument() {
    }

    public ItemSolrDocument(Item item) {
        setId(item.getIdString());
        setLdt(item.getLdt());
        this.rlnm = item.getRlnm();
        this.nm = item.getNm();

        if (item.getCtgry() != null) {
            this.ctgry = item.getCtgry().getRlnm();
            this.sctgry = item.getCtgry().getSrlnm();
        }

        if (item.getWnr() != null) {
            this.wnrnm = item.getWnr().getNm();
            this.wnrrlnm = item.getWnr().getRlnm();
            this.wnrsrlnm = item.getWnr().getSrlnm();
        }

        this.pc = item.getPc();
        this.llpc = item.getLlpc();

        if (item.getStrs() != null && !item.getStrs().isEmpty()) {
            this.strrlnms = new ArrayList<String>(item.getStrs().size());
            this.strsrlnms = new ArrayList<String>(item.getStrs().size());
            this.strnms = new ArrayList<String>(item.getStrs().size());
            for (AvailableInStore store : item.getStrs()) {
                if (StringUtils.isNotBlank(store.getRlnm())) {
                    this.strrlnms.add(store.getRlnm());
                }
                if (StringUtils.isNotBlank(store.getSrlnm())) {
                    this.strsrlnms.add(store.getSrlnm());
                }
                if (StringUtils.isNotBlank(store.getNm())) {
                    this.strnms.add(store.getNm());
                }
            }
        }
        
        if (item.getClrs() != null && !item.getClrs().isEmpty()) {
            this.clrs = new ArrayList<String>(item.getClrs().size());
            for (Color color : item.getClrs()) {
                if (StringUtils.isNotBlank(color.getNm())) {
                    this.clrs.add(color.getNm());
                }
            }
        }

        if (item.getTgs() != null && !item.getTgs().isEmpty()) {
            this.tgs = new ArrayList<String>(item.getTgs().size());
            for (Tag tag : item.getTgs()) {
                if (StringUtils.isNotBlank(tag.getTg())) {
                    this.tgs.add(tag.getTg());
                }
            }
        }
    }

    public String getRlnm() {
        return rlnm;
    }

    @Field
    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getNm() {
        return nm;
    }

    @Field
    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getPc() {
        return pc;
    }

    @Field
    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getCtgry() {
        return ctgry;
    }

    @Field
    public void setCtgry(String ctgry) {
        this.ctgry = ctgry;
    }

    public List<String> getAttrs() {
        return attrs;
    }

    @Field
    public void setAttrs(List<String> attrs) {
        this.attrs = attrs;
    }

    public String getWnrnm() {
        return wnrnm;
    }

    @Field
    public void setWnrnm(String wnrnm) {
        this.wnrnm = wnrnm;
    }

    public List<String> getStrrlnms() {
        return strrlnms;
    }

    @Field
    public void setStrrlnms(List<String> strrlnms) {
        this.strrlnms = strrlnms;
    }

    public List<String> getClrs() {
        return clrs;
    }

    @Field
    public void setClrs(List<String> clrs) {
        this.clrs = clrs;
    }

    public String getLlpc() {
        return llpc;
    }

    @Field
    public void setLlpc(String llpc) {
        this.llpc = llpc;
    }

    public String getWnrrlnm() {
        return wnrrlnm;
    }

    @Field
    public void setWnrrlnm(String wnrrlnm) {
        this.wnrrlnm = wnrrlnm;
    }

    public List<String> getStrnms() {
        return strnms;
    }

    @Field
    public void setStrnms(List<String> strnms) {
        this.strnms = strnms;
    }

    public List<String> getTgs() {
        return tgs;
    }

    @Field
    public void setTgs(List<String> tgs) {
        this.tgs = tgs;
    }

    public String getSctgry() {
        return sctgry;
    }

    @Field
    public void setSctgry(String sctgry) {
        this.sctgry = sctgry;
    }

    public String getWnrsrlnm() {
        return wnrsrlnm;
    }

    @Field
    public void setWnrsrlnm(String wnrsrlnm) {
        this.wnrsrlnm = wnrsrlnm;
    }

    public List<String> getStrsrlnms() {
        return strsrlnms;
    }

    @Field
    public void setStrsrlnms(List<String> strsrlnms) {
        this.strsrlnms = strsrlnms;
    }
}
