/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:19:22 AM
 * Inserts required categories into the system
 */
@SuppressWarnings("unchecked")
public class EmptyCollectionsBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(EmptyCollectionsBootstrapper.class);
    private final List<CrudRepository> repositories;
    private final Boolean enabled;
    private final MongoTemplate mongoTemplate;

    public EmptyCollectionsBootstrapper(MongoTemplate mongoTemplate, List<CrudRepository> repositories, Boolean enabled) {
        this.enabled = enabled;
        this.repositories = repositories;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void create() throws BootstrapperException {
        log.info("Deleting all repository collections before inserting...");

        if (repositories != null) {
            for (CrudRepository repo : repositories) {
                log.info(String.format("Deleting all data from %s collection", repo.getClass().getCanonicalName()));

                long count = mongoTemplate.count(Query.query(Criteria.where("id").exists(true)), repo.getClass());
                log.info(String.format("Collection count before delete: %d", count));
                repo.deleteAll();

                count = mongoTemplate.count(Query.query(Criteria.where("id").exists(true)), repo.getClass());
                log.info(String.format("Collection count after delete: %d", count));
            }
        }

        log.info("Deleting all repository collections before inserting complete");
    }


    @Override
    public String toString() {
        return "EmptyCollectionsBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}