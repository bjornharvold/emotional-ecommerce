package com.lela.domain.dto.search;

import com.lela.domain.document.Category;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 2/3/12
 * Time: 9:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class ItemsForOwnerSearchQuery {
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
