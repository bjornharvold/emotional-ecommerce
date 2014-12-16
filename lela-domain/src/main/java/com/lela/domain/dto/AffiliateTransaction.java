package com.lela.domain.dto;

import com.lela.domain.document.Redirect;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/23/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class AffiliateTransaction {

    Redirect redirect;
    int id;

    public AffiliateTransaction(){}

    public Redirect getRedirect() {
        return redirect;
    }

    public void setRedirect(Redirect redirect) {
        this.redirect = redirect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
