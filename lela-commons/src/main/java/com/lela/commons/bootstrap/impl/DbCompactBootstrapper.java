/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.domain.document.AbstractDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:19:22 AM
 * Inserts required categories into the system
 */
@SuppressWarnings("unchecked")
public class DbCompactBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(DbCompactBootstrapper.class);
    private final List<AbstractDocument> repositories;
    private final Boolean enabled;
    private final MongoTemplate mongoTemplate;

    public DbCompactBootstrapper(MongoTemplate mongoTemplate, List<AbstractDocument> repositories, Boolean enabled) {
        this.enabled = enabled;
        this.repositories = repositories;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void create() throws BootstrapperException {
        log.info("Compression repository collections after inserting...");

        if (repositories != null) {
            for (AbstractDocument repo : repositories) {

                String collectionName = mongoTemplate.getCollectionName(repo.getClass());
                log.info("Compresssing collection: " + collectionName);

                String command = String.format("{compact:'%s'}", collectionName);
                mongoTemplate.executeCommand(command);
            }
        }

        log.info("Compression repository collections after inserting complete");
    }


    @Override
    public String toString() {
        return "CompressDbBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}