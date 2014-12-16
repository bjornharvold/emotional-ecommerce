/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.report;

import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;

/**
 * Created by Bjorn Harvold
 * Date: 8/14/12
 * Time: 12:58 PM
 * Responsibility:
 */
public class UserUserSupplementEntry {
    private final User user;
    private final UserSupplement userSupplement;

    public UserUserSupplementEntry(User user, UserSupplement us) {
        this.user = user;
        this.userSupplement = us;
    }

    public User getUser() {
        return user;
    }

    public UserSupplement getUserSupplement() {
        return userSupplement;
    }
}
