/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.util.utilities.geoip;

import com.maxmind.geoip.LookupService;
import org.springframework.core.io.Resource;

/**
 * User: Chris Tallent
 * Date: 11/18/11
 * Time: 4:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LookupServiceFactory {
    public LookupService createLookupService(Resource dataFile, int[] lookupServiceFlags);
}
