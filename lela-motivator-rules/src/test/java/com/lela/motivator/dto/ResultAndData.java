/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.motivator.dto;

import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.util.HashMap;

/**
 * User: Chris Tallent
 * Date: 2/17/12
 * Time: 4:10 PM
 */
public class ResultAndData {
    private String ruleSet;
    private String ruleSheet;
    private String entity;
    private File containingFile;
    private Sheet containingSheet;
    private int rowInSheet;
    private int result;
    private HashMap<String, String> dataMap = new HashMap<String, String>();

    public ResultAndData(String ruleSet, String ruleSheet, String entity, File containingFile, Sheet containingSheet, int rowInSheet) {
        this.ruleSet = ruleSet;
        this.ruleSheet = ruleSheet;
        this.entity = entity;
        this.containingFile = containingFile;
        this.containingSheet = containingSheet;
        this.rowInSheet = rowInSheet;
    }
    
    public void addData(String field, String value) {
        dataMap.put(field, value);
    }

    public String getRuleSet() {
        return ruleSet;
    }

    public String getRuleSheet() {
        return ruleSheet;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public HashMap<String, String> getDataMap() {
        return dataMap;
    }

    public File getContainingFile() {
        return containingFile;
    }

    public Sheet getContainingSheet() {
        return containingSheet;
    }

    public int getRowInSheet() {
        return rowInSheet;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "Result=" + getResult() + " " + dataMap;
    }
}
