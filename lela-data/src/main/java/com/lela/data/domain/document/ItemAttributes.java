package com.lela.data.domain.document;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import java.util.HashMap;
import java.util.Map;


public class ItemAttributes {
    Long itemId;

    private Map<String, ItemAttribute> itemAttributeMap;

    public ItemAttributes()
    {}

    public ItemAttributes(Long itemId) {
        this.itemId = itemId;
        this.itemAttributeMap = new HashMap<String, ItemAttribute>();
    }

    public ItemAttributes(Long itemId, Map<String, ItemAttribute> itemAttributeMap) {
        this.itemId = itemId;
        this.itemAttributeMap = itemAttributeMap;
    }

    public ItemAttributes addItemAttribute(String key, ItemAttribute itemAttribute) {
        this.itemAttributeMap.put(key, itemAttribute);
        return this;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Map<String, ItemAttribute> getItemAttributeMap() {
        return itemAttributeMap;
    }

    public void setItemAttributeMap(Map<String, ItemAttribute> itemAttributeMap) {
        this.itemAttributeMap = itemAttributeMap;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
