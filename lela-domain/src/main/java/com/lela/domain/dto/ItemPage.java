/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import com.lela.domain.enums.FunctionalSortType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/15/11
 * Time: 12:11 PM
 * Responsibility:
 */
public class ItemPage<T> extends CustomPage<T> {

    /** Tuner question templates */
    private List<FunctionalFilter> filters;
    private FunctionalSortType sortBy;
    private AvailableFilters availableFilters;

    public ItemPage(List<T> content) {
        super(content, new PageRequest(0, content.size()), new Integer(content.size()).longValue());
    }

    public ItemPage(List<T> content, Pageable pageable, long total, List<FunctionalFilter> filters) {
        super(content, pageable, total);
        this.filters = filters;
    }

    public ItemPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public ItemPage(List<T> content, Pageable pageable, long total, FunctionalSortType sortBy) {
        super(content, pageable, total);
        this.sortBy = sortBy;
    }

    public ItemPage(List<T> content, Pageable pageable, long total, List<FunctionalFilter> filters, FunctionalSortType sortBy, AvailableFilters availableFilters) {
        super(content, pageable, total);
        this.filters = filters;
        this.sortBy = sortBy;
        this.availableFilters = availableFilters;
    }

    public ItemPage(List<T> content, List<FunctionalFilter> filters) {
        super(content, new PageRequest(0, content.size()), new Integer(content.size()).longValue());
        this.filters = filters;
    }

    public ItemPage(List<T> content, List<FunctionalFilter> filters, FunctionalSortType sortBy) {
        super(content, new PageRequest(0, content.size()), new Integer(content.size()).longValue());
        this.filters = filters;
        this.sortBy = sortBy;
    }

    public ItemPage(List<FunctionalFilter> filters, FunctionalSortType sortBy, AvailableFilters availableFilters) {
        super(new ArrayList<T>());
        this.filters = filters;
        this.sortBy = sortBy;
        this.availableFilters = availableFilters;
    }

    public ItemPage(List<FunctionalFilter> filters, FunctionalSortType sortBy, Integer page, Integer size) {
        super(new ArrayList<T>(), new PageRequest(page, size), 0l);
        this.filters = filters;
        this.sortBy = sortBy;
    }

    public ItemPage(List<FunctionalFilter> filters, FunctionalSortType sortBy, Integer page, Integer size, AvailableFilters availableFilters) {
        super(new ArrayList<T>(), new PageRequest(page, size), 0l);
        this.filters = filters;
        this.sortBy = sortBy;
        this.availableFilters = availableFilters;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public List<FunctionalFilter> getFilters() {
        return filters;
    }

    public Class getPageClass() {
        Class result = null;

        if (getContent() != null && !getContent().isEmpty()) {
            result = getContent().get(0).getClass();
        }

        return result;
    }

    public FunctionalSortType getSortBy() {
        return sortBy;
    }

    public AvailableFilters getAvailableFilters() {
        return availableFilters;
    }

    public void setAvailableFilters(AvailableFilters availableFilters) {
        this.availableFilters = availableFilters;
    }

    public static void main(String[] args) {
        ItemPage<Item> page = new ItemPage<Item>(new ArrayList<FunctionalFilter>(), FunctionalSortType.POPULARITY, 5, 12);

        System.out.println(page.getTotalElements());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        System.out.println(page.getTotalPages());
    }
}
