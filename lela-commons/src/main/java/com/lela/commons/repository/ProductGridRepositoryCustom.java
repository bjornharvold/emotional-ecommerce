/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.repository;

import com.lela.domain.document.ProductGrid;

import java.util.List;

public interface ProductGridRepositoryCustom {
    List<ProductGrid> findAll(Integer page, Integer maxResults, List<String> fields);
    List<ProductGrid> findAll(List<String> fields);
}
