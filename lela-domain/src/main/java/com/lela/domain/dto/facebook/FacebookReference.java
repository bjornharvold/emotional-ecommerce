/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.facebook;

/**
 * User: Chris Tallent
 * Date: 3/21/12
 * Time: 3:03 PM
 */
public class FacebookReference {
    private String id;
    private String name;
    private String category;
    
    public FacebookReference()
    {
        
    }
    
    public FacebookReference(String id, String name) {
        this(id, name, null);
    }
    
    public FacebookReference(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
