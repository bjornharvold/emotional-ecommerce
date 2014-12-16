/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import com.lela.domain.document.Category;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/28/11
 * Time: 2:23 PM
 * Responsibility:
 */
public class ItemsInStoreSearchQuery {
    private String urlName;
    private Integer page;
    private Integer size;
    private String cat;

    public void setUrlName(String urlName) {
        this.urlName = urlName;
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

    public String getUrlName() {
        return urlName;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}
