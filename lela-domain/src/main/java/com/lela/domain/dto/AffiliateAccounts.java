/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.AffiliateAccount;

import java.util.List;

public class AffiliateAccounts extends AbstractJSONPayload {
    private List<AffiliateAccount> list;

    public AffiliateAccounts() {
    }

    public AffiliateAccounts(List<AffiliateAccount> list) {
        this.list = list;
    }

    public List<AffiliateAccount> getList() {
        return list;
    }

    public void setList(List<AffiliateAccount> list) {
        this.list = list;
    }
}
