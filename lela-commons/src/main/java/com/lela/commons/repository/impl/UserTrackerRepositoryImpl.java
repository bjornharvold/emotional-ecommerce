/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.commons.repository.UserTrackerRepositoryCustom;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.User;
import com.lela.domain.document.UserTracker;
import com.lela.domain.document.UserVisit;
import com.lela.domain.enums.AuthenticationType;
import com.lela.domain.enums.InteractionType;
import com.lela.domain.enums.RegistrationType;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * User: Chris Tallent
 * Date: 6/6/12
 * Time: 10:00 AM
 */
public class UserTrackerRepositoryImpl implements UserTrackerRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(UserTrackerRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void addVisit(String userCode, UserVisit visit) {
        // Add a visit to the current UserTracker
        Query query = query(where("srcd").is(userCode));
        Update update = new Update().push("vsts", visit).inc("vstcnt", 1);

        mongoTemplate.updateFirst(query, update, UserTracker.class);
    }

    @Override
    public boolean updateRegistrationAffiliateIdentifiers(String userCode, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName) {
        boolean result = false;
        if (StringUtils.hasText(userCode)) {
            log.debug(String.format("Update user tracker with user cd: %s to affiliate: %s", userCode, affiliateUrlName));

            // Only do the update at the UserTracker level if there isn't an existing association
            Query query = query(where("srcd").is(userCode).and("ffltccntrlnm").exists(false).and("vstcnt").is(1));
            Update update = update("ffltccntrlnm", affiliateUrlName)
                            .set("cmpgnrlnm", campaignUrlName)
                            .set("rfrrlnm", referrerAffiliateUrlName);

            WriteResult writeResult = mongoTemplate.updateMulti(query, update, UserTracker.class);
            result = writeResult.getN() > 0;
        }

        return result;
    }

    @Override
    public void updateVisitAffiliateIdentifiers(String userCode, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName) {
        if (StringUtils.hasText(userCode)) {
            // Update the current UserVisit with the affiliate, campaign and referrer
            // Get the current visit and update it
            ObjectId visitId = findCurrentVisit(userCode);
            if (visitId != null) {
                Query query = query(where("vsts._id").is(visitId));
                Update update = update("vsts.$.ffltccntrlnm", affiliateUrlName)
                        .set("vsts.$.cmpgnrlnm", campaignUrlName)
                        .set("vsts.$.rfrrlnm", referrerAffiliateUrlName);

                mongoTemplate.updateFirst(query, update, UserTracker.class);
            }
        }
    }

    @Override
    public boolean updateRegistrationDomainAffiliate(String userCode, String domainAffiliateUrlName, String domain) {
        boolean result = false;
        if (StringUtils.hasText(userCode)) {
            log.debug(String.format("Update user tracker with user cd: %s to domain: %s", userCode, domainAffiliateUrlName));

            // Only do the update at the UserTracker level if there isn't an existing association
            Query query = query(where("srcd").is(userCode).and("dmnffltrlnm").exists(false).and("vstcnt").is(1));
            Update update = update("dmnffltrlnm", domainAffiliateUrlName)
                    .set("dmn", domain);

            WriteResult writeResult = mongoTemplate.updateMulti(query, update, UserTracker.class);
            result = writeResult.getN() > 0;
        }

        return result;
    }

    @Override
    public void updateVisitDomainAffiliate(String userCode, String domainAffiliateUrlName, String domain) {
        if (StringUtils.hasText(userCode)) {
            // Update the current UserVisit with the affiliate, campaign and referrer
            // Get the current visit and update it
            ObjectId visitId = findCurrentVisit(userCode);
            if (visitId != null) {
                Query query = query(where("vsts._id").is(visitId));
                Update update = update("vsts.$.dmnffltrlnm", domainAffiliateUrlName)
                        .set("vsts.$.dmn", domain);

                mongoTemplate.updateFirst(query, update, UserTracker.class);
            }
        }
    }

    @Override
    public void updateWithLoggedInUser(String trackerUserCode, User user) {
        if (user != null && StringUtils.hasText(trackerUserCode)) {
            log.debug(String.format("Update user tracker with user cd: %s to user id: %s", user.getCd(), user.getId()));

            Query query = query(where("srcd").is(trackerUserCode));
            Update update = update("srid", user.getId()).set("srcd", user.getCd());

            mongoTemplate.updateMulti(query, update, UserTracker.class);
        }
    }

    @Override
    public boolean existsForUser(String userCode) {
        long result = 0;
        Query query = query(where("srcd").is(userCode));
        result = mongoTemplate.count(query, UserTracker.class);

        return result > 0;
    }

    @Override
    public ObjectId findCurrentVisit(String userCode) {
        Query query = query(where("srcd").is(userCode));
        query.fields().slice("vsts", -1).include("vsts._id");

        UserTracker tracker = mongoTemplate.findOne(query, UserTracker.class);
        if (tracker != null && tracker.getVsts() != null && !tracker.getVsts().isEmpty()) {
            return tracker.getVsts().get(0).getId();
        }

        return null;
    }

    @Override
    public Date findLastVisitDate(String userCode) {
        Query query = query(where("srcd").is(userCode));
        query.fields().slice("vsts", -1).include("vsts.ldt");

        UserTracker tracker = mongoTemplate.findOne(query, UserTracker.class);
        if (tracker != null && tracker.getVsts() != null && !tracker.getVsts().isEmpty()) {
            return tracker.getVsts().get(0).getLdt();
        }

        return null;
    }

    @Override
    public void trackRegistrationStart(String userCode) {
        Query query = query(where("srcd").is(userCode).and("rgstrtdt").exists(false));
        Update update = update("rgstrtdt", new Date());

        mongoTemplate.updateFirst(query, update, UserTracker.class);
    }

    @Override
    public void trackRegistrationComplete(String userCode, RegistrationType registrationType) {
        Query query = query(where("srcd").is(userCode));
        Update update = update("rgtp", registrationType).set("rgcmpltdt", new Date());

        mongoTemplate.updateFirst(query, update, UserTracker.class);
    }

    @Override
    public void trackQuizStart(String userCode) {
        Query query = query(where("srcd").is(userCode).and("qzstrtdt").exists(false));
        Update update = update("qzstrtdt", new Date());

        mongoTemplate.updateFirst(query, update, UserTracker.class);
    }

    @Override
    public void trackQuizComplete(String userCode, String quizUrlName, String applicationUrlName, String affiliateUrlName, InteractionType quizType) {
        Query query = query(where("srcd").is(userCode));
        Update update = update("qzcmpltdt", new Date()).set("qzrlnm", quizUrlName).set("qzpprlnm", applicationUrlName).set("qzffltrlnm", affiliateUrlName).set("qztp", quizType);

        mongoTemplate.updateFirst(query, update, UserTracker.class);
    }

    @Override
    public void trackClick(ObjectId visitId) {
        Query query = query(where("vsts._id").is(visitId));
        Update update = update("ldt", new Date()).set("vsts.$.ldt", new Date()).inc("vsts.$.clckcnt", 1);

        mongoTemplate.updateFirst(query, update, UserTracker.class);
    }

    @Override
    public void setVisitAuthType(ObjectId visitId, AuthenticationType authenticationType) {
        // Only update the auth type of a visit if it isn't already set
        // { vsts: { $elemMatch: { "_id": ObjectId("xxx"), lgntp: { $exists: false } }}}
        Query query = query(where("vsts").elemMatch(where("_id").is(visitId).and("lgntp").exists(false)));
        Update update = update("vsts.$.lgntp", authenticationType);

        mongoTemplate.updateFirst(query, update, UserTracker.class);
    }

    @Override
    public void addRedirect(String userCode, Redirect redirect) {
        // Add a visit to the current UserTracker
        Query query = query(where("srcd").is(userCode));
        Update update = new Update().push("rdrcts", redirect).inc("rdrctcnt", 1);

        mongoTemplate.updateFirst(query, update, UserTracker.class);
    }

    @Override
    public int findVisitCount(String userCode) {
        int result = 0;
        Query query = query(where("srcd").is(userCode));
        UserTracker userTracker = mongoTemplate.findOne(query, UserTracker.class);

        if (userTracker != null && userTracker.getVstcnt() != null) {
            result = userTracker.getVstcnt();
        }

        return result;
    }

    @Override
    public UserVisit findFirstVisit(String userCode) {
        UserVisit result = null;

        Query query = query(where("srcd").is(userCode));
        query.fields().slice("vsts", 1).include("_id");

        UserTracker tracker = mongoTemplate.findOne(query, UserTracker.class);
        if (tracker != null && tracker.getVsts() != null && !tracker.getVsts().isEmpty()) {
            result = tracker.getVsts().get(0);
        }

        return result;
    }

    @Override
    public UserTracker findUserTracker(String userCode, String[] fields) {
        Query q = query(where("srcd").is(userCode));
        if (fields != null) {
            for (String field : fields) {
                q.fields().include(field);
            }
        }

        return mongoTemplate.findOne(q, UserTracker.class);
    }
}
