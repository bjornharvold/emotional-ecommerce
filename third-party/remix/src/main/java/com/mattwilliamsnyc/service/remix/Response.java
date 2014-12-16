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
package com.mattwilliamsnyc.service.remix;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.String;
import java.lang.System;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.mattwilliamsnyc.service.remix.util.RemixUtil;
import com.mattwilliamsnyc.service.remix.util.XMLHandler;

/**
 * Base class for all responses to a Remix API call.
 * 
 * @author  Matt Williams <matt@mattwilliamsnyc.com>
 * @version $Id$
 */
public class Response {
    /**
     * Root element of the response document tree
     */
    private Element documentRoot;

    /**
     * Error document constructed from an error response
     */
    private ErrorDocument error;

    /**
     * HTTP headers returned with this response
     */
    private Map<String,List<String>> httpHeaders;

    /**
     * HTTP status code associated with this response
     */
    private int responseCode;

    /**
     * Creates a new Response from an HTTP connection.
     * 
     * @param  connection HTTP connection associated with a Remix API call
     * @throws RemixException Thrown if an error occurs during the API request/response
     */
    public Response(HttpURLConnection connection) throws RemixException {
        try {
            XMLReader  reader  = RemixUtil.createXMLReader();
            XMLHandler handler = new XMLHandler();
            reader.setContentHandler(handler);
            reader.setErrorHandler(handler);
            responseCode = connection.getResponseCode();
            httpHeaders  = connection.getHeaderFields();
            InputStream is = isError() ? connection.getErrorStream() : connection.getInputStream();
            BufferedInputStream buf = new BufferedInputStream(is);
            String xml = IOUtils.toString(buf);
            System.out.println(xml);
            InputStream bios = new ByteArrayInputStream(xml.getBytes());
            reader.parse(new InputSource(bios));
            documentRoot = handler.getDocumentRoot();
            connection.disconnect();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RemixException("Error parsing HTTP response", e);
        }
    }   

    /**
     * Returns an attribute of the response document's root element (the collection element).
     * 
     * @param  name Name of the attribute to be accessed
     * @return Value of a document root element attribute
     */
    public String getAttribute(String name) {
        return null == documentRoot ? null : documentRoot.getAttribute(name);
    }

    /**
     * Retrieves the root element of the response document.
     * 
     * @return Root element of the response document
     */
    public Element getDocumentRoot() {
        return documentRoot;
    }

    /**
     * Returns an error document parsed from an error response.
     * 
     * @return ErrorDocument parsed from an error response
     */
    public ErrorDocument getError() {
        if(!isError()) {
            return null;
        } else if(null == error) {
            error = new ErrorDocument(responseCode, getDocumentRoot());
        }
        return error;
    }

    /**
     * Returns a List of values associated with a specified HTTP response header.
     * 
     * @return Map containing HTTP response headers
     */
    public List<String> getHeader(String name) {
        return httpHeaders.get(name);
    }

    /**
     * Returns an unmodifiable Map of response headers.
     * 
     * @return Map containing HTTP response headers
     */
    public Map<String,List<String>> getHeaders() {
        return httpHeaders;
    }

    /**
     * Returns the HTTP status code returned with this Response.
     * 
     * @return HTTP status code returned with this response
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Indicates whether an API call resulted in an error.
     * 
     * @return Whether any error occurred during an API call
     */
    public boolean isError() {
        return HttpURLConnection.HTTP_BAD_REQUEST <= responseCode;
    }
}
