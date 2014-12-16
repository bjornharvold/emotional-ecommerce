/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 3/13/12
 * Time: 10:09 PM
 * Responsibility:
 */
public class AbstractSearchResult {
    private QueryResponse queryResponse;
    private KeywordTerms keywordTerms;
    private List<CategoryCount> categories;
    private Integer page;
    private Integer size;

    public AbstractSearchResult() {
    }

    public AbstractSearchResult(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public QueryResponse getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(QueryResponse queryResponse) {
        this.queryResponse = queryResponse;
    }

    public KeywordTerms getKeywordTerms() {
        return keywordTerms;
    }

    public void setKeywordTerms(KeywordTerms keywordTerms) {
        this.keywordTerms = keywordTerms;
    }

    public List<CategoryCount> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryCount> categories) {
        this.categories = categories;
    }

    public List<String> getCategoryUrlNames() {
        List<String> result = null;

        if (categories != null && !categories.isEmpty()) {
            result = new ArrayList<String>(categories.size());

            for (CategoryCount count : categories) {
                result.add(count.getCategory().getRlnm());
            }
        }
        
        return result;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
    
    public Long getCategoryCount(String urlName) {
        if (categories != null && !categories.isEmpty()) {
            for (CategoryCount category : categories) {
                if (StringUtils.equals(category.getCategory().getRlnm(), urlName)) {
                    return category.getCount();
                }
            }
        }

        return 0L;
    }
}
