/*
 * COPYRIGHT
 * Copyright (c) 2009, Matt Williams <matt@mattwilliamsnyc.com>
 *
 * LICENSE
 *
 * This source file is subject to the new BSD license bundled with this package
 * in the file, LICENSE. This license is also available through the web at:
 * {@link http://www.opensource.org/licenses/bsd-license.php}. If you did not
 * receive a copy of the license, and are unable to obtain it through the web,
 * please send an email to matt@mattwilliamsnyc.com, and I will send you a copy.
 */
package com.mattwilliamsnyc.service.remix.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.mattwilliamsnyc.service.remix.RemixException;

/**
 * Formats a URL with an optional list of query parameters.
 * 
 * @author  Matt Williams <matt@mattwilliamsnyc.com>
 * @version $Id$
 */
public class RemixUtil {
    /**
     * Delimiter used to join multiple resource filters
     */
    public final static String AND_DELIMITER = "&";

    /**
     *  String delimiter used to separate query string parameters
     */
    public final static String OR_DELIMITER = "|";

    /**
     * Builds a resource URI filter string (e.g. "(name=foo&bar>=10)") from a list of filters.
     * 
     * @param  filters List of individual filters to be joined into this filter string
     * @return Resource URI filter string
     */
    public static String buildFilterString(List<String> filters, String delimiter) {
        StringBuffer filter = new StringBuffer();
        if(null != filters) {
            for(Iterator<String> iter = filters.iterator(); iter.hasNext();) {
                filter.append(iter.next());
                if(iter.hasNext()) {
                    filter.append(delimiter);
                }
            }
        }
        return 0 < filter.length() ? "(" + filter + ")" : "";
    }

    /**
     * Builds a query string from a map of name/value pairs.
     * 
     * @param  params Query parameters
     * @return Formatted query string
     */
    public static String buildQueryString(Map<String, String> params) {
        StringBuffer query = new StringBuffer();
        if(null != params) {
            List<String> fragments = new LinkedList<String>();      
            for(Iterator<String> iter = params.keySet().iterator(); iter.hasNext();) {
                String key = iter.next();
                String val = params.get(key);
                if(null != val) {
                    fragments.add(key + "=" + val);
                }   
            }
            for(Iterator<String> iter = fragments.iterator(); iter.hasNext();) {
                query.append(iter.next());
                if(iter.hasNext()) {
                    query.append(AND_DELIMITER);
                }
            }
        }
        return (0 < query.length()) ? "?" + query.toString() : "";
    }

    /**
     * Attempts to instantiate and return a SAX XMLReader; throws an exception on failure.
     * 
     * @link   http://java.sun.com/j2se/1.5.0/docs/api/org/xml/sax/XMLReader.html
     * @return SAX XMLReader
     * @throws RemixException
     */
    public static XMLReader createXMLReader() throws RemixException {
        try {
            return XMLReaderFactory.createXMLReader();
        } catch (SAXException e) {
            // Needed in 1.4
            System.setProperty("org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl");
        }
        try {
            return XMLReaderFactory.createXMLReader();
        } catch (SAXException e) {
            throw new RemixException("Couldn't initialize a SAX driver for an XMLReader");
        }
    }
}
