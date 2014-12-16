/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 2/17/12
 * Time: 9:45 PM
 * Responsibility:
 */
public class DeleteByQuerySolrDocument implements Serializable {
    private static final long serialVersionUID = 9024000909938292854L;

    private String query;

    public DeleteByQuerySolrDocument(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
