/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.jobs.social;

import com.lela.commons.jobs.java.JavaExecutionContext;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.jobs.ExecutionContext;
import com.lela.commons.jobs.java.JavaJob;
import com.lela.commons.jobs.java.JavaJobDetail;
import com.lela.commons.service.FacebookUserService;
import com.lela.commons.service.UserService;
import com.lela.domain.document.FacebookSnapshot;
import com.lela.domain.document.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: Chris Tallent
 * Date: 5/14/12
 * Time: 10:26 AM
 */
public class FacebookSnapshotJob extends JavaJobDetail implements JavaJob {

    private static final Logger log = LoggerFactory.getLogger(FacebookSnapshotJob.class);

    private final FacebookUserService facebookUserService;
    private final UserService userService;
    private final int threadCount;

    private ExecutorService executor;
    private ExecutorCompletionService<User> completionService;

    public FacebookSnapshotJob(FacebookUserService facebookUserService,
                               UserService userService,
                               int threadCount) {
        this.facebookUserService = facebookUserService;
        this.userService = userService;
        this.threadCount = threadCount;

        // Create the thread executor service
        executor = Executors.newFixedThreadPool(threadCount);
        completionService = new ExecutorCompletionService<User>(executor);
    }

    @Override
    public void execute(JavaExecutionContext context) {
        context.message("FacebookSnapshotJob executing");

        // Get the list of users that have logged in via facebook
        List<User> users = userService.findUsersBySocialNetwork(ApplicationConstants.FACEBOOK);
        context.message(String.format("Processing %d users with Facebook association", users.size()));

        // For each user, IF a facebook snapshot does not exist, Retrieve the snapshot
        int submitCount = 0;
        for (User user : users) {
            completionService.submit(new Execution(user, context));
            submitCount++;
        }

        context.message("Wait for all executions");
        for (int i=0; i<submitCount; i++) {
            try {
                completionService.take();
            } catch (InterruptedException e) {
                log.error(context.message("Caught InterruptedException while waiting for Facebook snapshot execution", e));
            }
        }

        log.info(context.message("Finished Facebook Snapshot job"));

    }

    private class Execution implements Callable<User> {

        private User user;
        private ExecutionContext context;

        public Execution(User user, ExecutionContext context) {
            this.user = user;
            this.context = context;
        }

        @Override
        public User call() throws Exception {

            try {
                // Retrieve a snapshot if there isn't one
                // Or if the prior was an error and the user has logged in since the snapshot date
                FacebookSnapshot snapshot = facebookUserService.findFacebookSnapshot(user);
                if (snapshot == null || (snapshot.getError() != null && user.getLgndt() != null && user.getLgndt().after(snapshot.getSnapshotDate()))) {
                    context.message("Start snapshot for user: " + user.getMl());
                    snapshot = facebookUserService.generateSnapshot(user);
                    context.message("End snapshot for user: " + user.getMl());

                    if (snapshot.getError() != null) {
                        context.message(snapshot.getError());
                    } else {
                        context.message("Snapshot Success\n");
                    }
                }

            } catch (Exception e) {
                context.setErrorResult();
                log.error(context.message("Unexpected error during facebook profile retrieval", e));
            }

            return user;
        }
    }

    @Override
    public JavaJob getJob() {
        return this;
    }
}
