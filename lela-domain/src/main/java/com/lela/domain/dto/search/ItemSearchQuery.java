/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto.search;

import com.lela.domain.document.Category;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/28/11
 * Time: 2:23 PM
 * Responsibility:
 */
public class ItemSearchQuery {
    private List<String> list;
    private String terms;
    private Integer page;
    private Integer size;
    private String cat;

    public List<String> getList() {
        return list;
    }

    public void setTerms(String terms) {
        terms = StringUtils.trim(terms);

        if (!StringUtils.contains(terms, " ") && !terms.contains("\"")) {
            this.list = Arrays.asList(terms.split(" "));
        } else {
            // Remove all versions of 'OR' in the keyword string as the query is by default an 'OR' query
//            terms = terms.replaceAll(" or ", " ");
//            terms = terms.replaceAll(" OR ", " ");
//            terms = terms.replaceAll(" AND ", " ");
//            terms = terms.replaceAll(" and ", " ");
        }

        this.terms = terms;
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

    public String getTerms() {
        return terms;
    }


    public static void main(String[] args) {
        ItemSearchQuery query = new ItemSearchQuery();
        query.setTerms(" bugaboo bee  test ");

        for (String s : query.getList()) {
            System.out.println(s);
        }
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}
