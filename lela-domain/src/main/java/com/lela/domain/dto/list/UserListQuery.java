/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import com.lela.domain.ApplicationConstants;
import com.lela.domain.enums.list.ListContentType;
import com.lela.domain.enums.list.ListSortType;

/**
 * Created by Bjorn Harvold
 * Date: 9/4/12
 * Time: 10:45 PM
 * Responsibility:
 */
public class UserListQuery {
    private String userCode;
    private Integer page = 0;
    private Integer maxResults = 12;
    private String boardCode = ApplicationConstants.DEFAULT_BOARD_NAME;
    private ListSortType sortType = ListSortType.CUSTOM_ORDER;
    private ListContentType contentType = ListContentType.ALL;

    /** For filtering products on a specific category */
    private String categoryUrlName = null;

    /** For filtering products on a specific store */
    private String storeUrlName = null;

    /** For filtering products on a specific owner */
    private String ownerUrlName = null;

    /** For filtering products on a specific branch */
    private String branchUrlName = null;

    /**
     * This will be used by the /user/list POST method. Make sure to inject userCode!!
     */
    public UserListQuery() {
    }

    public UserListQuery(String userCode, String boardCode) {
        this.userCode = userCode;
        this.boardCode = boardCode;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public ListSortType getSortType() {
        return sortType;
    }

    public void setSortType(ListSortType sortType) {
        this.sortType = sortType;
    }

    public ListContentType getContentType() {
        return contentType;
    }

    public void setContentType(ListContentType contentType) {
        this.contentType = contentType;
    }

    public String getCategoryUrlName() {
        return categoryUrlName;
    }

    public void setCategoryUrlName(String categoryUrlName) {
        this.categoryUrlName = categoryUrlName;
    }

    public String getStoreUrlName() {
        return storeUrlName;
    }

    public void setStoreUrlName(String storeUrlName) {
        this.storeUrlName = storeUrlName;
    }

    public String getOwnerUrlName() {
        return ownerUrlName;
    }

    public void setOwnerUrlName(String ownerUrlName) {
        this.ownerUrlName = ownerUrlName;
    }

    public String getBranchUrlName() {
        return branchUrlName;
    }

    public void setBranchUrlName(String branchUrlName) {
        this.branchUrlName = branchUrlName;
    }
}
