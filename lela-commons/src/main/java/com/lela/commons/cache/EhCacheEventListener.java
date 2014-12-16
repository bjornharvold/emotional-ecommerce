/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.cache;

//~--- non-JDK imports --------------------------------------------------------

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 4/7/11
 * Time: 3:19 PM
 * Responsibility:
 */
public class EhCacheEventListener implements CacheEventListener {

    /** Field description */
    private final static Logger log = LoggerFactory.getLogger(EhCacheEventListener.class);

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     *
     * @throws CloneNotSupportedException CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Method description
     *
     */
    @Override
    public void dispose() {
        if (log.isDebugEnabled()) {
            log.debug("dispose() called on cache listener");
        }
    }

    /**
     * Method description
     *
     *
     * @param ehcache ehcache
     * @param element element
     */
    @Override
    public void notifyElementEvicted(Ehcache ehcache, Element element) {
        if (log.isDebugEnabled()) {
            log.debug("Cache element evicted: " + element.getKey() + " in cache: " + ehcache.getName());
        }
    }

    /**
     * Method description
     *
     *
     * @param ehcache ehcache
     * @param element element
     */
    @Override
    public void notifyElementExpired(Ehcache ehcache, Element element) {
        if (log.isDebugEnabled()) {
            log.debug("Cache element expired: " + element.getKey() + " in cache: " + ehcache.getName());
        }
    }

    /**
     * Method description
     *
     *
     * @param ehcache ehcache
     * @param element element
     *
     * @throws net.sf.ehcache.CacheException CacheException
     */
    @Override
    public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Cache element put: " + element.getKey() + " in cache: " + ehcache.getName());
        }
    }

    /**
     * Method description
     *
     *
     * @param ehcache ehcache
     * @param element element
     *
     * @throws net.sf.ehcache.CacheException CacheException
     */
    @Override
    public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Cache element removed: " + element.getKey() + " in cache: " + ehcache.getName());
        }
    }

    /**
     * Method description
     *
     *
     * @param ehcache ehcache
     * @param element element
     *
     * @throws net.sf.ehcache.CacheException CacheException
     */
    @Override
    public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Cache element updated: " + element.getKey() + " in cache: " + ehcache.getName());
        }
    }

    /**
     * Method description
     *
     *
     * @param ehcache ehcache
     */
    @Override
    public void notifyRemoveAll(Ehcache ehcache) {
        if (log.isDebugEnabled()) {
            log.debug("All cache elements removed: in cache: " + ehcache.getName());
        }
    }
}
