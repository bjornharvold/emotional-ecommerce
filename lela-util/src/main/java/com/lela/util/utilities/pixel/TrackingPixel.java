/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.util.utilities.pixel;

import org.bouncycastle.util.encoders.Base64;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User: Chris Tallent
 * Date: 10/29/12
 * Time: 1:33 PM
 */
public class TrackingPixel {
    private static final String base64EncodedTransparentGIF = "R0lGODlhAQABAIAAAMzMzAAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==";
    private static final byte[] base64DecodedTransparentGIF = Base64.decode(base64EncodedTransparentGIF);

    public void writeToResponse(HttpServletResponse response)
    throws IOException {
        response.setContentType("image/gif");
        response.setHeader("Cache-Control", "no-cache");
        OutputStream out = response.getOutputStream();
        out.write(base64DecodedTransparentGIF);
        out.close();
    }
}
