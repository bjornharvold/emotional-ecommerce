/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.document.ProductGridFilter;
import com.lela.domain.dto.productgrid.EnrichProductGridQuery;
import com.lela.domain.enums.FunctionalSortType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 8/8/11
 * Time: 11:44 AM
 * Responsibility: Collects all information necessary to retrieve relevant items
 */
public class CategoryItemsQuery {

    /** Update flag to save user preset */
    private Boolean update = false;

    /** Whether to sort or not */
    private Boolean sort = true;

    /** Field description */
    private String categoryUrlName;

    /** Field description */
    private Map<String, Map<String, String>> filters;

    /** Field description */
    private Integer page;

    /** Field description */
    private Integer size;

    /** Sort items by */
    private FunctionalSortType sortBy;

    /** User */
    private String userCode;

    private List<FilterEventTrack> eventTracking;

    public CategoryItemsQuery() {
    }

    public CategoryItemsQuery(SimpleCategoryItemsQuery query) {
        this.update = query.getUpdate();
        this.sort = query.getSort();
        this.categoryUrlName = query.getRlnm();
        this.sortBy = query.getSortBy();
        this.userCode = query.getUserCode();

        // filters are a tiny bit trickier

        if (query.getFilters() != null && !query.getFilters().isEmpty()) {
            this.filters = new HashMap<String, Map<String, String>>(query.getFilters().size());

            for (String filterKey : query.getFilters().keySet()) {
                List<String> vals = query.getFilters().get(filterKey);
                Map<String, String> values = new HashMap<String, String>(vals.size());

                for (String value : vals) {
                    // we don't really care about the "true" value; only the key
                    values.put(value, "true");
                }

                this.filters.put(filterKey, values);
            }
        }
    }

    public CategoryItemsQuery(EnrichProductGridQuery query) {
        this.sort = query.getPg().getSrt() != null;
        this.sortBy = query.getPg().getSrt();
        this.categoryUrlName = query.getPg().getCrlnm();
        this.userCode = query.getUserCode();
        this.page = 0;
        this.size = query.getSize();

        if (query.getPg().getFltrs() != null && !query.getPg().getFltrs().isEmpty()) {
            this.filters = new HashMap<String, Map<String, String>>(query.getPg().getFltrs().size());

            for (ProductGridFilter pgf : query.getPg().getFltrs()) {
                Map<String, String> values = new HashMap<String, String>(pgf.getVl().size());

                for (String value : pgf.getVl()) {
                    // we don't really care about the "true" value; only the key
                    values.put(value, "true");
                }

                this.filters.put(pgf.getKy(), values);
            }
        }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getCategoryUrlName() {
        return categoryUrlName;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Map<String, Map<String, String>> getFilters() {
        return filters;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Boolean getSort() {
        return sort;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public FunctionalSortType getSortBy() {
        return sortBy;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Boolean getUpdate() {
        return update;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param categoryUrlName categoryUrlName
     */
    public void setCategoryUrlName(String categoryUrlName) {
        this.categoryUrlName = categoryUrlName;
    }

    /**
     * Method description
     *
     *
     * @param filters filters
     */
    public void setFilters(Map<String, Map<String, String>> filters) {
        this.filters = filters;
    }

    /**
     * Method description
     *
     *
     * @param page page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * Method description
     *
     *
     * @param size size
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Method description
     *
     *
     * @param sort sort
     */
    public void setSort(Boolean sort) {
        this.sort = sort;
    }

    /**
     * Method description
     *
     *
     * @param sortBy sortBy
     */
    public void setSortBy(FunctionalSortType sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Method description
     *
     *
     * @param update update
     */
    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public List<FilterEventTrack> getEventTracking() {
        return eventTracking;
    }

    public void setEventTracking(List<FilterEventTrack> eventTracking) {
        this.eventTracking = eventTracking;
    }
}
