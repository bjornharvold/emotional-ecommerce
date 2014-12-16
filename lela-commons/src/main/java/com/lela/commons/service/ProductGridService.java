/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.ProductGrid;
import com.lela.domain.dto.productgrid.EnrichProductGridQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/22/12
 * Time: 11:30 AM
 * Responsibility:
 */
public interface ProductGridService {
    List<ProductGrid> findProductGrids(List<String> fields);
    Page<ProductGrid> findProductGrids(Integer page, Integer maxResults);
    ProductGrid findProductGridByUrlName(String urlName);
    ProductGrid saveProductGrid(ProductGrid productGrid);
    void removeProductGrid(String urlName);
    void saveProductGridFilters(String urlName, Map<String, List<String>> map);
    void removeProductGridFilter(String urlName, String key);
    ProductGrid findPublishedProductGridByUrlName(String urlName);
    void enrichProductGrid(EnrichProductGridQuery query);
}
