/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 8/31/12
 * Time: 3:58 PM
 * Responsibility:
 */
public class Review extends AbstractNote implements Serializable {
    private static final long serialVersionUID = -8974021080362671540L;

    /** Title */
    private String ttl;

    /** Rating */
    private Integer rtng;

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public Integer getRtng() {
        return rtng;
    }

    public void setRtng(Integer rtng) {
        this.rtng = rtng;
    }

}
