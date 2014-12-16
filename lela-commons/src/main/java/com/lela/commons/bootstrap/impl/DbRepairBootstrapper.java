/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * User: bjorn
 * Date: Nov 4, 2007
 * Time: 11:19:22 AM
 * Inserts required categories into the system
 */
@SuppressWarnings("unchecked")
public class DbRepairBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(DbRepairBootstrapper.class);
    private final Boolean enabled;
    private final MongoTemplate mongoTemplate;

    public DbRepairBootstrapper(MongoTemplate mongoTemplate, Boolean enabled) {
        this.enabled = enabled;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void create() throws BootstrapperException {
        log.info("Running mongodb repairDatabase() to reclaim space...");

        mongoTemplate.getDb().eval("db.repairDatabase();");

        log.info("Running mongodb repairDatabase() to reclaim space COMPLETE");
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