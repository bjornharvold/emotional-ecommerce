/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.jobs;

import org.springframework.expression.ParserContext;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 12/1/11
 * Time: 10:49 AM
 */
public class IngestParserContext implements ParserContext {
    @Override
    public boolean isTemplate() {
        return true;
    }

    @Override
    public String getExpressionPrefix() {
        return "@{";
    }

    @Override
    public String getExpressionSuffix() {
        return "}";
    }
}
