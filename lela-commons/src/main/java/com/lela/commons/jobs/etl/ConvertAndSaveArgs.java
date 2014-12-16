/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.etl;

import com.lela.commons.sort.ObjectSorter;

import javax.validation.Validator;

/**
 * User: Chris Tallent
 * Date: 5/24/12
 * Time: 2:07 PM
 */
public class ConvertAndSaveArgs {

    private Object ingestService;
    private String ingestMethod;
    private String jsonTemplate;
    private Class jsonType;
    private Class simpleType;
    private String etlUpdateQuery;
    private String ingestLogProperty;
    private ObjectSorter sorter;
    private Validator validator;

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

    public String getEtlUpdateQuery() {
        return etlUpdateQuery;
    }

    public void setEtlUpdateQuery(String etlUpdateQuery) {
        this.etlUpdateQuery = etlUpdateQuery;
    }

    public String getIngestLogProperty() {
        return ingestLogProperty;
    }

    public void setIngestLogProperty(String ingestLogProperty) {
        this.ingestLogProperty = ingestLogProperty;
    }

    public ObjectSorter getSorter() {
        return sorter;
    }

    public void setSorter(ObjectSorter sorter) {
        this.sorter = sorter;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}
