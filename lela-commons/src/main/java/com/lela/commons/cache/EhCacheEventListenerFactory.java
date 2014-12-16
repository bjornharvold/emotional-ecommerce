/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.cache;

//~--- non-JDK imports --------------------------------------------------------

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.util.Properties;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 4/7/11
 * Time: 3:18 PM
 * Responsibility:
 */
public class EhCacheEventListenerFactory extends CacheEventListenerFactory {

    /**
     * Method description
     *
     *
     * @param properties properties
     *
     * @return Return value
     */
    @Override
    public CacheEventListener createCacheEventListener(Properties properties) {
        return new EhCacheEventListener();
    }
}
