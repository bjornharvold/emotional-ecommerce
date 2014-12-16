/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.MetricType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/2/11
 * Time: 2:33 PM
 * Responsibility:
 */
@Document
public class Metric extends AbstractDocument implements Serializable {

    private static final long serialVersionUID = 8960542079492976622L;
    private List<MetricValue> vls;
    private MetricType tp;

    public List<MetricValue> getVls() {
        return vls;
    }

    public void setVls(List<MetricValue> vls) {
        this.vls = vls;
    }

    public MetricType getTp() {
        return tp;
    }

    public void setTp(MetricType tp) {
        this.tp = tp;
    }
}
