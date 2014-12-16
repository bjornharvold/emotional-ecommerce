/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.User;
import com.lela.domain.dto.search.ItemsInStoreSearchQuery;

/**
 * Created by Bjorn Harvold
 * Date: 9/28/11
 * Time: 5:03 PM
 * Responsibility:
 */
public class RelevantItemsInStoreSearchQuery extends ItemsInStoreSearchQuery {

    /** Field description */
    private String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
