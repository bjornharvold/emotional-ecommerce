package com.lela.data.domain.dto;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 6/4/12
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class AttributeMotivator {
    private Integer attributeTypeId;
    private String attributeName;
    private String lelaAttributeName;
    private String attributeTypeSourceName;
    private String motivator;
    private String eddDataType;

    public Integer getAttributeTypeId() {
        return attributeTypeId;
    }

    public void setAttributeTypeId(Integer attributeTypeId) {
        this.attributeTypeId = attributeTypeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getLelaAttributeName() {
        return lelaAttributeName;
    }

    public void setLelaAttributeName(String lelaAttributeName) {
        this.lelaAttributeName = lelaAttributeName;
    }

    public String getAttributeTypeSourceName() {
        return attributeTypeSourceName;
    }

    public void setAttributeTypeSourceName(String attributeTypeSourceName) {
        this.attributeTypeSourceName = attributeTypeSourceName;
    }

    public String getMotivator() {
        return motivator;
    }

    public void setMotivator(String motivator) {
        this.motivator = motivator;
    }

    public String getEddDataType() {
        return eddDataType;
    }

    public void setEddDataType(String eddDataType) {
        this.eddDataType = eddDataType;
    }
}
