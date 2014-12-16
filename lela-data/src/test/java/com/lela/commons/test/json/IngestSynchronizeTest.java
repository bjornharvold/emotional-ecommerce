/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.json;

import com.lela.ingest.test.AbstractFunctionalTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.junit.Assert.fail;

/**
 * User: Chris Tallent
 * Date: 4/21/12
 * Time: 8:15 PM
 */
public class IngestSynchronizeTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(IngestSynchronizeTest.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSync() {

        try {
            Query query = new Query(Criteria.where("ctgry.rlnm").is("television"));
            query.fields().include("id");
            List<RlnmDto> items = mongoTemplate.find(query, RlnmDto.class, "item");

            System.out.println(items.size());

//            ListMultimap<Map<String, Object>, Map<String, Object>> grouped = null;
//
//            List<Map<String, Object>> list = engine.queryForList(queryName, params);
//            if (list != null && !list.isEmpty()) {
//                // Build a multi-map based on the group by set
//                grouped = Multimaps.index(list, new Function<Map<String, Object>, Map<String, Object>>() {
//                    @Override
//                    public Map<String, Object> apply(@Nullable Map<String, Object> data) {
//
//                        // Key will be a map based on the columns named in the GroupBy
//                        Map<String, Object> key = new HashMap<String, Object>();
//                        for (String name : groupBy.names) {
//                            key.put(name, data.get(name));
//                        }
//
//                        return key;
//                    }
//                });
//            }

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }
}
