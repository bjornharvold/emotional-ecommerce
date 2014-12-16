/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

/**
 * Created by Bjorn Harvold
 * Date: 10/13/11
 * Time: 12:01 PM
 * Responsibility:
 */
public final class UnsubscribeFromEmailList extends AbstractJSONPayload {
    private String email;
    private String listId;

    public UnsubscribeFromEmailList() {
    }

    public UnsubscribeFromEmailList(String email, String listId) {
        this.email = email;
        this.listId = listId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getEmail() {
        return email;
    }

    public String getListId() {
        return listId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnsubscribeFromEmailList that = (UnsubscribeFromEmailList) o;

        if (!email.equals(that.email)) return false;
        if (!listId.equals(that.listId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + listId.hashCode();
        return result;
    }
}
