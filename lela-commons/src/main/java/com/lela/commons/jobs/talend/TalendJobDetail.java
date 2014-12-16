/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.talend;

import com.lela.commons.jobs.AbstractJobDetail;

import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 2/28/12
 * Time: 2:59 PM
 */
public class TalendJobDetail extends AbstractJobDetail {

    private String talendClass;
    private Map<String, String> context;

    public String getTalendClass() {
        return talendClass;
    }

    public void setTalendClass(String talendClass) {
        this.talendClass = talendClass;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }
}
