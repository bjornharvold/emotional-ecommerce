/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.report;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/7/12
 * Time: 3:47 PM
 * Responsibility:
 */
public class UserMotivatorWorkbookData {
    private final List<UserUserSupplementEntry> users;

    public UserMotivatorWorkbookData(List<UserUserSupplementEntry> users) {
        this.users = users;
    }

    public List<UserUserSupplementEntry> getUsers() {
        return users;
    }
}
