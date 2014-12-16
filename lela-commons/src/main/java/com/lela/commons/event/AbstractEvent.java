/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.event;

import com.lela.commons.web.utils.HttpServletRequestAdapter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 10/19/12
 * Time: 8:40 AM
 */
public class AbstractEvent {
    private static Logger log = LoggerFactory.getLogger(AbstractEvent.class);
    private HttpServletRequest request;

    /**
     * Create a new ApplicationEvent.
     */
    public AbstractEvent() {

        // Get the request on the current thread so that if event handling is asynchronous, it can still be passed
        try {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = ((ServletRequestAttributes)attrs).getRequest();
                if(request!=null){
                  this.request = new HttpServletRequestAdapter(request);
                }
            }
        } catch (IllegalStateException e) {
            log.warn("Could not retrieve HttpServletRequest... this might happen during server startup", e);
        }
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
