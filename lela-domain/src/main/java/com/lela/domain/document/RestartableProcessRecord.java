/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.domain.document;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * User: Chris Tallent
 * Date: 9/24/12
 * Time: 2:55 PM
 */
@Document
public class RestartableProcessRecord extends AbstractDocument {

    public RestartableProcessRecord() {

    }

    public RestartableProcessRecord(String process, String key) {
        this.prcss = process;
        this.ky = key;
    }

    /**
     * Process tracking records for restart (e.g. - "Bulk User Import")
     */
    private String prcss;

    /**
     * A record key processed
     */
    private String ky;

    public String getPrcss() {
        return prcss;
    }

    public void setPrcss(String prcss) {
        this.prcss = prcss;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }
}
