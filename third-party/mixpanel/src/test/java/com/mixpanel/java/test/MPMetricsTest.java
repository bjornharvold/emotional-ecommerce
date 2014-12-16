package com.mixpanel.java.test;

import com.mixpanel.java.mpmetrics.LowPriorityThreadFactory;
import com.mixpanel.java.mpmetrics.MPConfig;
import com.mixpanel.java.mpmetrics.MPMetrics;
import junit.framework.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MPMetricsTest {

    private static final String testToken = "";
    private static final String apiKey = "";

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 180000,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new LowPriorityThreadFactory()
    );

    private static final MPConfig mpConfig = new MPConfig(testToken, apiKey, executor);

    @Test
    public void testEvent() {

        MPMetrics mpMetrics = MPMetrics.getInstance(mpConfig);

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("ip", "123.123.123.123");
        properties.put("action", "test");

        mpMetrics.track("testEvent", properties);
        mpMetrics.shutdown();

        Assert.assertTrue(true);
    }

    @Test
    public void testUser() {

        MPMetrics mpMetrics = MPMetrics.getInstance(mpConfig);
        mpMetrics.identify("123456789");

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", "Konstantin");
        properties.put("$email", "scalascope@gmail.com");

        mpMetrics.set(properties);
        mpMetrics.shutdown();

        Assert.assertTrue(true);
    }

    @Test
    public void testEventWithUser() {

        MPMetrics mpMetrics = MPMetrics.getInstance(mpConfig);
        mpMetrics.identify("123456789");

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("ip", "123.123.123.123");
        properties.put("action", "test");

        mpMetrics.track("testEventWithUser", properties);
        mpMetrics.shutdown();

        Assert.assertTrue(true);
    }

}
