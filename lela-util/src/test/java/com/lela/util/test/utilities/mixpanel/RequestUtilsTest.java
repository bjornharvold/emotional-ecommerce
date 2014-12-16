/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.util.test.utilities.mixpanel;

import com.lela.util.utilities.mixpanel.RequestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import static junit.framework.Assert.assertEquals;

/**
 * User: Chris Tallent
 * Date: 10/24/12
 * Time: 9:26 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestUtilsTest {

    private static final String USER_AGENT = "User-Agent";
    private static final String REFERRER = "Referer";

    @Test
    public void testBrowser() {
        MockHttpServletRequest request = null;
        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.47 Safari/536.11");
        assertEquals("User Agent did not match Chrome", "Chrome", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_6; en-us) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27");
        assertEquals("User Agent did not match Safari", "Safari", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/5.0 (iPad; U; CPU OS 4_3 like Mac OS X; de-de) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8F191 Safari/6533.18.5");
        assertEquals("User Agent did not match Safari", "Mobile Safari", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/5.0 (Windows NT 5.1; rv:15.0) Gecko/20100101 Firefox/15.0");
        assertEquals("User Agent did not match Firefox", "Firefox", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/5.0 (compatible; Konqueror/4.0; Linux) KHTML/4.0.5 (like Gecko)");
        assertEquals("User Agent did not match Konqueror", "Konqueror", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
        assertEquals("User Agent did not match IE 10", "Internet Explorer", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; InfoPath.3; Creative AutoUpdate v1.40.02)");
        assertEquals("User Agent did not match IE 9", "Internet Explorer", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; chromeframe/13.0.782.218; chromeframe; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; .NET CLR 3.5.21022; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
        assertEquals("User Agent did not match IE 8", "Internet Explorer", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB6.4; .NET CLR 1.1.4322; FDM; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
        assertEquals("User Agent did not match IE 7", "Internet Explorer", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Opera/9.80 (Windows Mobile; WCE; Opera Mobi/WMD-50433; U; en) Presto/2.4.13 Version/10.00");
        assertEquals("User Agent did not match Opera", "Opera", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Opera/9.80 (J2ME/MIDP; Opera Mini/4.1.13907/27.1188; U; en) Presto/2.8.119 Version/11.10");
        assertEquals("User Agent did not match Opera Mini", "Opera Mini", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "BlackBerry9650/5.0.0.732 Profile/MIDP-2.1 Configuration/CLDC-1.1 VendorID/105");
        assertEquals("User Agent did not match Blackberry", "BlackBerry", RequestUtils.getBrowser(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "Mozilla/5.0 (PlayBook; U; RIM Tablet OS 1.0.0; en-US) AppleWebKit/534.11+ (KHTML, like Gecko) Version/7.1.0.7 Safari/534.11+");
        assertEquals("Playbook User Agent did not match Blackberry", "BlackBerry", RequestUtils.getBrowser(request));

    }

    @Test
    public void testGetOs() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Windows other text");
        assertEquals("OS did not match Windows", "Windows", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text windows other text");
        assertEquals("OS did not match windows", "Windows", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Windows other Phone text");
        assertEquals("OS did not match Windows Mobile", "Windows Mobile", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text iPhone other text");
        assertEquals("iPhone did not return iOS", "iOS", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text iPad other text");
        assertEquals("iPad did not return iOS", "iOS", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text iPod other text");
        assertEquals("iPod did not return iOS", "iOS", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Android other text");
        assertEquals("Android did not return Android", "Android", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Blackberry other text");
        assertEquals("Blackberry did not return BlackBerry", "BlackBerry", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Playbook other text");
        assertEquals("Playbook did not return BlackBerry", "BlackBerry", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Bb10 other text");
        assertEquals("Bb10 did not return BlackBerry", "BlackBerry", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Mac other text");
        assertEquals("Mac did not return Mac OS X", "Mac OS X", RequestUtils.getOs(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Linux other text");
        assertEquals("Linux did not return Linux", "Linux", RequestUtils.getOs(request));
    }

    @Test
    public void testGetDevice() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text iPhone other text");
        assertEquals("Device did not match iPhone", "iPhone", RequestUtils.getDevice(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text iPad other text");
        assertEquals("Device did not match iPad", "iPad", RequestUtils.getDevice(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text iPod other Phone text");
        assertEquals("Device did not match iPod Touch", "iPod Touch", RequestUtils.getDevice(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Blackberry other text");
        assertEquals("Blackberry did not return BlackBerry", "BlackBerry", RequestUtils.getDevice(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Playbook other text");
        assertEquals("Playbook did not return BlackBerry", "BlackBerry", RequestUtils.getDevice(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Bb10 other text");
        assertEquals("Bb10 did not return BlackBerry", "BlackBerry", RequestUtils.getDevice(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Windows phone other text");
        assertEquals("Device did not return Windows Phone", "Windows Phone", RequestUtils.getDevice(request));

        request = new MockHttpServletRequest();
        request.addHeader(USER_AGENT, "other text Android other text");
        assertEquals("Device did not return Android", "Android", RequestUtils.getDevice(request));
    }

    @Test
    public void testReferringDomain() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(REFERRER, "http://www.google.com/");
        assertEquals("Referrer should be www.google.com", "www.google.com", RequestUtils.getReferringDomain(request));
    }
}
