package com.lela.commons.repository.impl;

import com.lela.domain.document.Offer;
import com.lela.commons.repository.OfferRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/31/12
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OfferRepositoryImpl implements OfferRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Offer> findValidByBranchUrlName(String branchUrlName) {
        Date date = new Date();
        List<Offer> offers = mongoTemplate.find(query(new Criteria().andOperator(where("brnchrlnm").is(branchUrlName),
                where("xprtndt").gte(date))), Offer.class);
        return offers;
    }
}
