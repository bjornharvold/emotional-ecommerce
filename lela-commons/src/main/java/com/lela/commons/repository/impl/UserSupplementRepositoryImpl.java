/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.UserSupplementRepositoryCustom;
import com.lela.commons.spring.data.FieldBasicQuery;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.document.UserTracker;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.user.UserSearchQuery;
import com.lela.domain.enums.AssociationType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * Custom user repository
 */
public class UserSupplementRepositoryImpl implements UserSupplementRepositoryCustom {
    private final static Logger log = LoggerFactory.getLogger(UserSupplementRepositoryImpl.class);
    private static final String[] CAMPAIGN_USER_REPORT_FIELDS = { "cd", "lnm", "fnm", "ml", "cdt" };

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<UserSupplement> findByCodes(List<String> userCodes, List<String> fields) {
        List<UserSupplement> result = null;

        if (userCodes != null && !userCodes.isEmpty()) {
            Query q = query(where("cd").in(userCodes));

            if (fields != null && !fields.isEmpty()) {
                for (String field : fields) {
                    q.fields().include(field);
                }
            }
            result = mongoTemplate.find(q, UserSupplement.class);
        }

        return result;
    }

    @Override
    public Long findUserCountByCouponCode(String couponCode) {
        return mongoTemplate.count(query(where("cpns.cpncd").is(couponCode)), UserSupplement.class);
    }

    @Override
    public List<UserSupplement> findUsersWithMotivators(Integer page, Integer maxResults, List<String> fields) {
        Criteria where = where("mtvtrmp").exists(true);

        Query q = query(where);

        if (page != null && maxResults != null) {
            q.skip(page * maxResults);
            q.limit(maxResults);
        }

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                q.fields().include(field);
            }
        }

        return mongoTemplate.find(q, UserSupplement.class);
    }

    @Override
    public Page<UserSupplement> findByAffiliateUrlName(String urlName, Integer page, Integer maxResults) {
        Query query = query(where("ssctns")
                .elemMatch(where("tp").is(AssociationType.AFFILIATE.name()).and("attrs")
                        .elemMatch(where("ky").is("ffltrlnm").and("vl").is(urlName))));
        Page<UserSupplement> result = null;

        Long count = mongoTemplate.count(query, UserSupplement.class);

        if (count > 0) {
            if (page != null && maxResults != null) {
                query.skip(page * maxResults);
                query.limit(maxResults);
            }

            List<UserSupplement> list = mongoTemplate.find(query, UserSupplement.class);
            result = new CustomPage<UserSupplement>(list, new PageRequest(page, maxResults), count);
        }

        return result;
    }

    @Override
    public Long findFacebookUserCount(List<String> userCodes) {
        Query q = query(where("scls").elemMatch(where("providerId").is(ApplicationConstants.FACEBOOK)));

        if (userCodes != null && !userCodes.isEmpty()) {
            q.addCriteria(where("cd").in(userCodes));
        }

        return mongoTemplate.count(q, UserSupplement.class);
    }

    @Override
    public Long findByOfferUrlNameCount(String offerUrlName) {
        Query q = query(where("cpns").elemMatch(where("ffrrlnm").is(offerUrlName)));

        return mongoTemplate.count(q, UserSupplement.class);
    }

    @Override
    public List<UserSupplement> findUserSupplementsForCampaign(String campaignUrlName) {
        // Get the list of campaign users
        String queryString = String.format("{ ssctns: { $elemMatch: { tp: 'AFFILIATE', attrs: { $elemMatch: { ky: 'cmpgnrlnm', vl: '%s'  }  } } } }", campaignUrlName);
        FieldBasicQuery query = new FieldBasicQuery(queryString);

        for (String field : CAMPAIGN_USER_REPORT_FIELDS) {
            query.fields().include(field);
        }

        return mongoTemplate.find(query, UserSupplement.class);
    }

    @Override
    public UserSupplement findUserSupplement(String userCode, List<String> fields) {
        Query query = query(where("cd").is(userCode));

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.findOne(query, UserSupplement.class);
    }

    @Override
    public List<UserSupplement> findUserSupplementsByQuery(UserSearchQuery query) {
        List<UserSupplement> result = null;

        Query q = createUserSearchQuery(query);

        if (q != null) {
            result = mongoTemplate.find(q, UserSupplement.class);
        }

        return result;
    }

    @Override
    public Long findUserSupplementCountByQuery(UserSearchQuery query) {
        Long result = null;

        Query q = createUserSearchQuery(query);

        if (q != null) {
            result = mongoTemplate.count(q, UserSupplement.class);
        }

        return result;
    }

    private Query createUserSearchQuery(UserSearchQuery query) {
        Query q = null;

        if (query != null) {
            q = new Query();

            if (StringUtils.isNotBlank(query.getFnm())) {
                q.addCriteria(where("fnm").is(query.getFnm()));
            }
            if (StringUtils.isNotBlank(query.getLnm())) {
                q.addCriteria(where("lnm").is(query.getLnm()));
            }
            if (StringUtils.isNotBlank(query.getMl())) {
                q.addCriteria(where("ml").is(query.getMl()));
            }
            if (query.getCdt() != null) {
                q.addCriteria(where("cdt").gte(query.getCdt()));
            }
            if (query.getLdt() != null) {
                q.addCriteria(where("ldt").lte(query.getLdt()));
            }

            if (query.getFields() != null && !query.getFields().isEmpty()) {
                for (String field : query.getFields()) {
                    q.fields().include(field);
                }
            }
        }
        return q;
    }

    @Override
    public void trackAffiliateNotified(String userCode, Boolean notified) {
        Query query = query(where("cd").is(userCode));
        Update update = update("ffltntfd", notified);

        mongoTemplate.updateFirst(query, update, UserSupplement.class);
    }
}
