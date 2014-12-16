/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.User;
import com.lela.domain.dto.search.ItemSearchQuery;

/**
 * Created by Bjorn Harvold
 * Date: 9/28/11
 * Time: 5:03 PM
 * Responsibility:
 */
public class RelevantItemSearchQuery extends ItemSearchQuery {

    /** Field description */
    private String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
