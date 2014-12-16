/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.ingest.json;

/**
 * User: Chris Tallent
 * Date: 4/21/12
 * Time: 7:09 PM
 */
public class JsonUtils  {

    public static final String NULL = "null";
    public static final String COMMA = ", ";

    public static String list(Object ... args) {
        String result = null;

        if (args != null && args.length > 0) {
            StringBuilder sb = new StringBuilder();

            sb.append("[");
            for (Object arg : args) {
                if (arg != null) {
                    String asString = arg.toString();
                    if (asString.length() > 0) {
                        sb.append(arg).append(COMMA);
                    }
                }
            }

            // Remove the trailing comma
            if (sb.length() > 1) {
                sb.setLength(sb.length()-COMMA.length());
            }

            sb.append("]");

            result = sb.toString();
        } else {
            result = NULL;
        }

        return result;
    }
}
