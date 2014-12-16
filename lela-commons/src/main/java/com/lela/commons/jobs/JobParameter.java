/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs;

/**
 * User: Chris Tallent
 * Date: 8/25/12
 * Time: 2:02 PM
 */
public class JobParameter {
    private String name;
    private String label;
    private JobParameterType type;
    private boolean required;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public JobParameterType getType() {
        return type;
    }

    public void setType(JobParameterType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
