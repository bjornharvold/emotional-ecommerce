/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.ComputedCategory;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 8/4/12
 * Time: 10:19 PM
 * Responsibility:
 */
public interface ComputedCategoryService {
    ComputedCategory findComputedCategory(String userCode, String categoryUrlName);

    void removeComputedCategory(String userCode, String categoryUrlName);

    void setComputedCategoryToDirty(String userCode, String categoryUrlName);

    void setComputedCategoriesToDirty(String userCode);

    void setAllComputedCategoriesToDirty();

    ComputedCategory saveComputedCategory(ComputedCategory cc);

    void setAllComputedCategoryToDirty(String categoryUrlName);
}
