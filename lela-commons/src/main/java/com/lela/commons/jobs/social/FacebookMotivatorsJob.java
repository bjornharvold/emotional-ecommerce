/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.social;

import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.java.JavaExecutionContext;
import com.lela.commons.jobs.java.JavaJob;
import com.lela.commons.jobs.java.JavaJobDetail;
import com.lela.commons.service.FacebookUserService;
import com.lela.domain.document.FacebookSnapshot;

import java.util.List;

/**
 * User: Chris Tallent
 * Date: 5/14/12
 * Time: 10:26 AM
 */
public class FacebookMotivatorsJob extends JavaJobDetail implements JavaJob {

    private final FacebookUserService facebookUserService;

    public FacebookMotivatorsJob(FacebookUserService facebookUserService) {
        this.facebookUserService = facebookUserService;
    }

    @Override
    public void execute(JavaExecutionContext context) {
        context.message("Facebook Motivator Job executing");

        List<FacebookSnapshot> snapshots = facebookUserService.findFacebookSnapshotsWithoutMotivators();
        if (snapshots != null && !snapshots.isEmpty()) {
            for (FacebookSnapshot snapshot : snapshots) {
                try {
                    context.message("Calculate facebook motivators for " + snapshot.getLelaEmail());
                    facebookUserService.calculateFacebookUserMotivators(snapshot);
                } catch (Exception e) {
                    context.message("Error processing motivators", e);
                }
            }
        } else {
            context.message("No snapshots found to process");
        }
    }

    @Override
    public JavaJob getJob() {
        return this;
    }
}
