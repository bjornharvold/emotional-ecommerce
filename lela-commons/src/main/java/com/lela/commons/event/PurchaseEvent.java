package com.lela.commons.event;

import com.lela.domain.document.Sale;
import com.lela.domain.document.User;
import com.lela.domain.document.UserTracker;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/6/12
 * Time: 11:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseEvent extends AbstractUserEvent {
    Sale sale;
    UserTracker userTracker;

    public PurchaseEvent(User user, UserTracker userTracker, Sale sale) {
        super(user);
        this.userTracker = userTracker;
        this.sale = sale;
    }

    public Sale getSale()
    {
        return this.sale;
    }

    public UserTracker getUserTracker()
    {
        return this.userTracker;
    }

}
