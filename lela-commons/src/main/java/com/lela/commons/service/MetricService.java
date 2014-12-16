/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Metric;
import com.lela.domain.enums.MetricType;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 8:57 PM
 * Responsibility:
 */
public interface MetricService {
    List<Metric> saveMetrics(List<Metric> list);
    Metric saveMetric(Metric metric);
    Metric findMetricByType(MetricType type);
    Metric removeMetric(ObjectId id);
}
