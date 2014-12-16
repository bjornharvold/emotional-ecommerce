package com.lela.domain.dto;

import com.lela.domain.document.User;
import com.lela.domain.dto.search.ItemsForOwnerSearchQuery;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 2/3/12
 * Time: 9:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class RelevantItemsForOwnerSearchQuery extends ItemsForOwnerSearchQuery {

    /** Field description */
    private String userCode;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
