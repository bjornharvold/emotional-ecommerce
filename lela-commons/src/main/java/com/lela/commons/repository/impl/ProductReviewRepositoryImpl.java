package com.lela.commons.repository.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.lela.commons.repository.ProductReviewRepositoryCustom;
import com.lela.domain.document.ProductReview;

public class ProductReviewRepositoryImpl implements
		ProductReviewRepositoryCustom {

	
    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
	@Override
	public List<ProductReview> findProductReviews(List<String> fields) {
        Query query = query(where("id").exists(true));
        
        if (fields != null && !fields.isEmpty()) {
            for (String field : fields) {
                query.fields().include(field);
            }
        }
        
        return mongoTemplate.find(query, ProductReview.class);
        
	}


}
