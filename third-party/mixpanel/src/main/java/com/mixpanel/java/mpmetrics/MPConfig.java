package com.mixpanel.java.mpmetrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.String;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Stores global configuration options for the Mixpanel library.
 */
public class MPConfig {

    private final static Logger log = LoggerFactory.getLogger(MPConfig.class);

    // Base url that track requests will be sent to. Events will be sent to /track
    // and people requests will be sent to /engage
    public static final String BASE_ENDPOINT = "http://api.mixpanel.com";

    public static final String EXPORT_BASE_ENDPOINT = "mixpanel.com/api/2.0";

    public static final String EXPORT_RAW_BASE_ENDPOINT = "data.mixpanel.com/api/2.0";

    private final ThreadPoolExecutor executor;

    private String token;
    private String key;

    public MPConfig(String token, String key, ThreadPoolExecutor executor) {
        this.token = token;
        this.key = key;
        this.executor = executor;
    }

    public ThreadPoolExecutor getExecutor()
    {
        return this.executor;
    }

    public String getToken()
    {
        return this.token;
    }

    public String getKey()
    {
        return this.key;
    }
}