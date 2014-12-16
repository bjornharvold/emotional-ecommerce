/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util.utilities.geoip.impl;

import com.lela.util.utilities.geoip.LookupServiceFactory;
import com.maxmind.geoip.LookupService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: Chris Tallent
 * Date: 11/18/11
 * Time: 4:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class LookupServiceFactoryImpl implements LookupServiceFactory {
    private static final Logger log = LoggerFactory.getLogger(LookupServiceFactoryImpl.class);

    public LookupService createLookupService(Resource dataFile, int[] lookupServiceFlags) {
        LookupService lookupService = null;
        
        int flags = LookupService.GEOIP_STANDARD;
        if (lookupServiceFlags != null) {
            for (int flag : lookupServiceFlags) {
                flags |= flag;
            }
        }

        try {
            
            if (dataFile.isReadable()) {
//                log.info("We can read the geo ip data file successfully");
                
                // the file is located inside a jar which the LookupService doesn't like
                // we'll have to suck it out of the jar and write the file to the file system
                File geoIp = new File(dataFile.getFilename());

                // if it exists, we'll delete the old copy to make room for the new
                if (geoIp.exists()) {
                    geoIp.delete();
                }

                IOUtils.copy(dataFile.getInputStream(), new FileOutputStream(geoIp));
                
                lookupService = new LookupService(geoIp, flags);
            } 
            
            return lookupService;
        } catch (IOException e) {
            log.error("Could not load MaxMind Geo IP data file from : " + dataFile);
            throw new RuntimeException("Could not load MaxMind Geo IP data file from : " + dataFile, e);
        }
    }
}
