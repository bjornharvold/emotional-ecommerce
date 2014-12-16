/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository.impl;

import com.lela.commons.repository.ProductGridRepositoryCustom;
import com.lela.domain.document.ProductGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class ProductGridRepositoryImpl implements
        ProductGridRepositoryCustom {
	
	
    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
	}
    
	@Override
	public List<ProductGrid> findAll(Integer page, Integer maxResults, List<String> fields) {
        Query query = query(where("id").exists(true));

        if (page != null && maxResults != null) {
            query.skip(page*maxResults);
            query.limit(maxResults);
        }

        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }

        return mongoTemplate.find(query, ProductGrid.class);
	}

	@Override
	public List<ProductGrid> findAll(List<String> fields) {
		 return findAll(null, null, fields);
	}

}
