package com.lela.data.domain.dto;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/10/12
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemAttributeDto {

    public Long getUniqueItemId() {
        return uniqueItemId;
    }

    public void setUniqueItemId(Long uniqueItemId) {
        this.uniqueItemId = uniqueItemId;
    }

    public Long getAttributeItemId() {
        return attributeItemId;
    }

    public void setAttributeItemId(Long attributeItemId) {
        this.attributeItemId = attributeItemId;
    }

    public String getItemAttributeValue() {
        return itemAttributeValue;
    }

    public void setItemAttributeValue(String itemAttributeValue) {
        this.itemAttributeValue = itemAttributeValue;
    }

    Long uniqueItemId;
    Long attributeItemId;
    String itemAttributeValue;
}
