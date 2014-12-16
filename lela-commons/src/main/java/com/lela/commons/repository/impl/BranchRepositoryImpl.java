/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.commons.repository.impl;

import com.lela.domain.document.Branch;
import com.lela.domain.dto.BranchSearchResult;
import com.lela.commons.repository.BranchRepositoryCustom;
import com.lela.domain.dto.store.BranchDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 11/10/11
 * Time: 9:01 AM
 */
public class BranchRepositoryImpl implements BranchRepositoryCustom {

    private static final Logger log = LoggerFactory.getLogger(BranchRepositoryImpl.class);

    private static final Float MAX_GEONEAR_MILES = 500f;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<BranchSearchResult> findNearest(Float longitude, Float latitude, Float radiusInMiles, Set<String> merchantIds) {

        log.debug("findNearest for lon: " + longitude + ", lat: " + latitude + ", radius: " + radiusInMiles);

        // Don't do a search if the lat or long is null or outside of -180 to 180 degrees
        if (longitude == null || latitude == null ||
                longitude < -180 || longitude > 180 ||
                latitude < -180 || latitude > 180) {

            log.warn("Invalid search parameters - long: " + longitude + ", lat: " + latitude);
            return Collections.<BranchSearchResult>emptyList();
        }

        // Check vs max radius
        if (radiusInMiles > MAX_GEONEAR_MILES) {
            radiusInMiles = MAX_GEONEAR_MILES;
        }

        // Only include approved merchants
        Criteria criteria = where("pprvd").is(true);

        // If a list of merchants was provided, only include those filtered merchants
        // AND any merchant listed as local only
        if (merchantIds != null && !merchantIds.isEmpty()) {
            criteria.andOperator(new Criteria().orOperator(where("mrchntd").in(merchantIds), where("lclnly").is(true)));
        }

        Point location = new Point(longitude, latitude);
        NearQuery query = NearQuery.near(location, Metrics.MILES).query(query(criteria)).maxDistance(radiusInMiles);

        List<BranchSearchResult> results = new ArrayList<BranchSearchResult>();
//          try {
        GeoResults<Branch> geoResults = mongoTemplate.geoNear(query, Branch.class);
        log.debug("geoNear found: " + (geoResults != null ? geoResults.getContent().size() : "null") + " results");

        for (GeoResult<Branch> geoResult : geoResults) {
            results.add(new BranchSearchResult(geoResult.getContent(), geoResult.getDistance().getValue()));
        }
//        } catch (NullPointerException e) {
//            // Catch null pointer exception sometimes thrown by mongo template
//            log.error("Null pointer exception on geoNear query", e);
//        }

        return results;
    }

    @Override
    public List<BranchDistance> findNearest(Float longitude, Float latitude, Float radiusInMiles) {
        List<BranchDistance> result = null;

        log.debug("findNearest for lon: " + longitude + ", lat: " + latitude + ", radius: " + radiusInMiles);

        // Don't do a search if the lat or long is null or outside of -180 to 180 degrees
        if (longitude == null || latitude == null ||
                longitude < -180 || longitude > 180 ||
                latitude < -180 || latitude > 180) {

            log.warn("Invalid search parameters - long: " + longitude + ", lat: " + latitude);
        } else {

            // Check vs max radius
            if (radiusInMiles > MAX_GEONEAR_MILES) {
                radiusInMiles = MAX_GEONEAR_MILES;
            }

            // Only include approved merchants
            Criteria criteria = where("pprvd").is(true);

            Point location = new Point(longitude, latitude);
            NearQuery query = NearQuery.near(location, Metrics.MILES).query(query(criteria)).maxDistance(radiusInMiles);

            GeoResults<Branch> geoResults = mongoTemplate.geoNear(query, Branch.class);

            if (log.isDebugEnabled()) {
                log.debug("geoNear found: " + (geoResults != null ? geoResults.getContent().size() : "null") + " results");
            }

            if (geoResults != null) {
                result = new ArrayList<BranchDistance>();
                for (GeoResult<Branch> geoResult : geoResults) {
                    result.add(new BranchDistance(geoResult.getContent(), geoResult.getDistance().getValue()));
                }
            }
        }

        return result;
    }
}
