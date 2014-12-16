/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 10/13/11
 * Time: 12:01 PM
 * Responsibility:
 */
public final class SubscribeToEmailList extends AbstractJSONPayload {
    private String email;
    private String listId;
    private Map<String, Object> mergeVars;

    public SubscribeToEmailList() {
    }

    public SubscribeToEmailList(String email, String listId, Map<String, Object> mergeVars) {
        this.email = email;
        this.listId = listId;
        this.mergeVars = mergeVars;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public void setMergeVars(Map<String, Object> mergeVars) {
        this.mergeVars = mergeVars;
    }

    public String getEmail() {
        return email;
    }

    public String getListId() {
        return listId;
    }

    public Map<String, Object> getMergeVars() {
        return mergeVars;
    }
}
