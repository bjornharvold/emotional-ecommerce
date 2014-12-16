/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.repository.MetricRepository;
import com.lela.commons.service.MetricService;
import com.lela.domain.document.Metric;
import com.lela.domain.enums.MetricType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 8:56 PM
 * Responsibility:
 */
@Service("metricService")
public class MetricServiceImpl implements MetricService {
    private final MetricRepository metricRepository;

    @Autowired
    public MetricServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    /**
     * Method description
     *
     * @param type type
     * @return Return value
     */
    @Override
    public Metric findMetricByType(MetricType type) {
        return metricRepository.findByType(type);
    }

    /**
     * Method description
     *
     * @param id metric
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Metric removeMetric(ObjectId id) {
        Metric metric = metricRepository.findOne(id);

        if (metric != null) {
            metricRepository.delete(metric);
        }

        return metric;
    }

    /**
     * Method description
     *
     * @param metric metric
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Metric saveMetric(Metric metric) {
        return metricRepository.save(metric);
    }

    /**
     * Method description
     *
     * @param list list
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<Metric> saveMetrics(List<Metric> list) {
        return (List<Metric>) metricRepository.save(list);
    }
}
