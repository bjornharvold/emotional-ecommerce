/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.alert;

import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.java.JavaExecutionContext;
import com.lela.commons.jobs.java.JavaJob;
import com.lela.commons.jobs.java.JavaJobDetail;
import com.lela.commons.service.FavoritesListService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.MailService;
import com.lela.commons.service.UserService;
import com.lela.domain.document.Alert;
import com.lela.domain.document.Item;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.ListCardBoard;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.enums.MailParameter;
import com.lela.domain.enums.list.ListCardType;
import com.lela.util.utilities.number.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 5/30/12
 * Time: 2:00 PM
 */
public class PriceAlertJob extends JavaJobDetail implements JavaJob {

    private static final Logger log = LoggerFactory.getLogger(PriceAlertJob.class);

    private final UserService userService;
    private final FavoritesListService favoritesListService;

    public PriceAlertJob(UserService userService,
                         FavoritesListService favoritesListService) {
        this.userService = userService;
        this.favoritesListService = favoritesListService;
    }

    @Override
    public void execute(JavaExecutionContext context) {
        context.message("Price Alert Job executing");

        context.startProcessingTimer();
        List<UserUserSupplementEntry> entries = userService.findUsersWithAlerts();
        context.setTotalCount(entries != null ? entries.size() : 0);

        if (entries != null && !entries.isEmpty()) {
            for (UserUserSupplementEntry entry : entries) {
                User user = entry.getUser();

                List<Alert> sent = favoritesListService.sendValidPriceAlerts(user.getCd());
                if (sent != null && !sent.isEmpty()) {
                    context.addProcessedCount(1);
                } else {
                    context.addSkippedCount(1);
                }
            }
        } else {
            context.message("No User alerts found to process");
        }
    }

    @Override
    public JavaJob getJob() {
        return this;
    }
}
