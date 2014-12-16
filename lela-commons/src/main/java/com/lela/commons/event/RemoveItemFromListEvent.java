package com.lela.commons.event;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/15/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoveItemFromListEvent extends AbstractEvent implements ListEvent{
    private final String itemUrlName;
    private final String userCode;

    public RemoveItemFromListEvent(String userCode, String itemUrlName) {
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
