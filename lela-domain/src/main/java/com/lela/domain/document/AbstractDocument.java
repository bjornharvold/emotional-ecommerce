/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.bson.types.ObjectId;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 7/8/11
 * Time: 5:05 PM
 * Responsibility:
 */
public abstract class AbstractDocument {
    /** ID */
    @Id
    private ObjectId id;

    /** Unique Talend ID */
    private Long tld;

    /** Created date */
    private Date cdt;

    /** Last update */
    private Date ldt;

    public AbstractDocument() {

    }

    public AbstractDocument(AbstractDocument doc) {
        id = doc.id;
        tld = doc.tld;
        cdt = doc.cdt;
        ldt = doc.ldt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getTld() {
        return tld;
    }

    public void setTld(Long tld) {
        this.tld = tld;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }

    public Date getLdt() {
        return ldt;
    }

    public void setLdt(Date ldt) {
        this.ldt = ldt;
    }

    public String getIdString() {
        String result = null;

        if (id != null) {
            result = id.toString();
        }

        return result;
    }

    @Override
    public String toString() {
        return getIdString();
    }


}
