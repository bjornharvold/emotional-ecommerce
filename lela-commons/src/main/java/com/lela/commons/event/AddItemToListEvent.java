package com.lela.commons.event;

import com.lela.domain.document.User;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/15/12
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddItemToListEvent extends AbstractEvent implements ListEvent{
    private final String itemUrlName;
    private final String userCode;

    public AddItemToListEvent(String userCode, String itemUrlName) {
        this.userCode = userCode;
        this.itemUrlName = itemUrlName;
    }

    public String getItemUrlName() {
        return itemUrlName;
    }

    public String getUserCode()
    {
        return userCode;
    }
}
