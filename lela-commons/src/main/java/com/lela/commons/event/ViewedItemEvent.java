/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.domain.document.User;

/**
 * User: Chris Tallent
 * Date: 11/1/12
 * Time: 1:45 PM
 */
public class ViewedItemEvent extends AbstractUserEvent {

    public enum ItemViewType {
        DETAILS, PRINT, SNAPSHOT
    }

    private final String itemUrlName;
    private final ItemViewType itemViewType;

    public ViewedItemEvent(User user, String itemUrlName, ItemViewType itemViewType) {
        super(user);
        this.itemUrlName = itemUrlName;
        this.itemViewType = itemViewType;
    }

    public String getItemUrlName() {
        return itemUrlName;
    }

    public ItemViewType getItemViewType() {
        return itemViewType;
    }
}