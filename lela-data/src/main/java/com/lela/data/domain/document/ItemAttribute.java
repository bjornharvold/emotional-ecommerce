package com.lela.data.domain.document;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ItemAttribute {

    private String attributeName;

    private String attributeValue;

    private Integer attributeSequence;

    public ItemAttribute(){}

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public Integer getAttributeSequence() {
        return attributeSequence;
    }

    public void setAttributeSequence(Integer attributeSequence) {
        this.attributeSequence = attributeSequence;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
