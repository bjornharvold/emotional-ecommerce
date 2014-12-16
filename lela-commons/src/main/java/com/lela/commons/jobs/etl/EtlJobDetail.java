/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.etl;


import com.lela.commons.jobs.AbstractJobDetail;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class EtlJobDetail extends AbstractJobDetail {
    private String sqlQuery;
    private String jsonTemplate;
    private Class jsonType;
    private Class simpleType;
    private Object ingestService;
    private String ingestMethod;
    private String etlUpdateQuery;
    private Integer batchSize = 1;
    private Integer threadPool = 1;
    private List<String> preExecutionQueries;
    private List<String> postExecutionQueries;
    private String ingestLogProperty;

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public Object getIngestService() {
        return ingestService;
    }

    public void setIngestService(Object ingestService) {
        this.ingestService = ingestService;
    }

    public String getIngestMethod() {
        return ingestMethod;
    }

    public void setIngestMethod(String ingestMethod) {
        this.ingestMethod = ingestMethod;
    }

    public String getEtlUpdateQuery() {
        return etlUpdateQuery;
    }

    public void setEtlUpdateQuery(String etlUpdateQuery) {
        this.etlUpdateQuery = etlUpdateQuery;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(Integer threadPool) {
        this.threadPool = threadPool;
    }

    public String getJsonTemplate() {
        return jsonTemplate;
    }

    public void setJsonTemplate(String jsonTemplate) {
        this.jsonTemplate = jsonTemplate;
    }

    public Class getJsonType() {
        return jsonType;
    }

    public void setJsonType(Class jsonType) {
        this.jsonType = jsonType;
    }

    public Class getSimpleType() {
        return simpleType;
    }

    public void setSimpleType(Class simpleType) {
        this.simpleType = simpleType;
    }

    public List<String> getPreExecutionQueries() {
        return preExecutionQueries;
    }

    public void setPreExecutionQueries(List<String> preExecutionQueries) {
        this.preExecutionQueries = preExecutionQueries;
    }

    public List<String> getPostExecutionQueries() {
        return postExecutionQueries;
    }

    public void setPostExecutionQueries(List<String> postExecutionQueries) {
        this.postExecutionQueries = postExecutionQueries;
    }

    public String getIngestLogProperty() {
        return ingestLogProperty;
    }

    public void setIngestLogProperty(String ingestLogProperty) {
        this.ingestLogProperty = ingestLogProperty;
    }
}
