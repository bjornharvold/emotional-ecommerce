/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

/**
 * Created by Bjorn Harvold
 * Date: 1/6/12
 * Time: 2:48 AM
 * Responsibility:
 */
public interface ComputedCategoryRepositoryCustom {

    void setAllComputedCategoriesToDirty();

    void setAllComputedCategoryToDirty(String categoryUrlName);
}
