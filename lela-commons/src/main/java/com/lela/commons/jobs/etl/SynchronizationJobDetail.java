/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs.etl;


import com.lela.commons.jobs.AbstractJobDetail;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class SynchronizationJobDetail extends AbstractJobDetail {

    private String sqlQuery;
    private String dirtyProperty;
    private String idProperty;

    private String mongoQuery;
    private String mongoFields;
    private Class mongoDto;

    private Object ingestService;

    private ConvertAndSaveArgs upsert;
    private ConvertAndSaveArgs delete;

    private Integer batchSize = 1;
    private Integer threadPool = 1;

    private List<String> preExecutionQueries;
    private List<String> postExecutionQueries;

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getDirtyProperty() {
        return dirtyProperty;
    }

    public void setDirtyProperty(String dirtyProperty) {
        this.dirtyProperty = dirtyProperty;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public String getMongoQuery() {
        return mongoQuery;
    }

    public void setMongoQuery(String mongoQuery) {
        this.mongoQuery = mongoQuery;
    }

    public String getMongoFields() {
        return mongoFields;
    }

    public void setMongoFields(String mongoFields) {
        this.mongoFields = mongoFields;
    }

    public Class getMongoDto() {
        return mongoDto;
    }

    public void setMongoDto(Class mongoDto) {
        this.mongoDto = mongoDto;
    }

    public Object getIngestService() {
        return ingestService;
    }

    public void setIngestService(Object ingestService) {
        this.ingestService = ingestService;
    }

    public ConvertAndSaveArgs getUpsert() {
        return upsert;
    }

    public void setUpsert(ConvertAndSaveArgs upsert) {
        this.upsert = upsert;
    }

    public ConvertAndSaveArgs getDelete() {
        return delete;
    }

    public void setDelete(ConvertAndSaveArgs delete) {
        this.delete = delete;
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
}
