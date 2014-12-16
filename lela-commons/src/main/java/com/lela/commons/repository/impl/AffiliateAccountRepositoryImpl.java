package com.lela.commons.repository.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.lela.commons.repository.AffiliateAccountRepositoryCustom;
import com.lela.domain.document.AffiliateAccount;

public class AffiliateAccountRepositoryImpl implements
		AffiliateAccountRepositoryCustom {
	
	
    @Autowired
    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
	}
    
	@Override
	public List<AffiliateAccount> findAll(Integer page, Integer maxResults,
			List<String> fields) {
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

        return mongoTemplate.find(query, AffiliateAccount.class);
	}

    @Override
    public List<AffiliateAccount> findAll(boolean includeInactive, Integer page, Integer maxResults) {
        Criteria where = where("id").exists(true);
        if (!includeInactive) {
            where = where.and("ctv").is(true);
        }

        Query query = query(where);

        if (page != null && maxResults != null) {
            query.skip(page*maxResults);
            query.limit(maxResults);
        }

        return mongoTemplate.find(query, AffiliateAccount.class);
    }

	@Override
	public List<AffiliateAccount> findAll(List<String> fields) {
		 return findAll(null, null, fields);
	}
	
	
	@Override
	public long countBasedOnStatus(boolean includeInactive){ 
        Criteria where = where("id").exists(true);
        if (!includeInactive) {
            where = where.and("ctv").is(true);
        }
        Query query = query(where);
        return mongoTemplate.count(query, AffiliateAccount.class);
	}
}
