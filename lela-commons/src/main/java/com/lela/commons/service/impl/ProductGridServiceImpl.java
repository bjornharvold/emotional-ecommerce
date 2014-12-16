/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.ProductGridRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.ProductGridService;
import com.lela.commons.service.UserService;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.ProductGrid;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.productgrid.EnrichProductGridQuery;
import com.lela.domain.enums.CacheType;
import com.lela.domain.enums.FunctionalSortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/21/12
 * Time: 2:06 PM
 * Responsibility:
 */
@Service("productGridService")
public class ProductGridServiceImpl extends AbstractCacheableService implements ProductGridService {
    private final static Logger log = LoggerFactory.getLogger(ProductGridServiceImpl.class);

    private final ProductGridRepository productGridRepository;
    private final UserService userService;
    private final ProductEngineService productEngineService;
    private final CategoryService categoryService;

    @Autowired
    public ProductGridServiceImpl(CacheService cacheService,
                                  ProductGridRepository productGridRepository,
                                  UserService userService,
                                  ProductEngineService productEngineService,
                                  CategoryService categoryService) {
        super(cacheService);
        this.productGridRepository = productGridRepository;
        this.userService = userService;
        this.productEngineService = productEngineService;
        this.categoryService = categoryService;
    }

    @Override
    public List<ProductGrid> findProductGrids(List<String> fields) {
        return productGridRepository.findAll(fields);
    }

    @Override
    @PreAuthorize("hasAnyRole('RIGHT_READ_PRODUCT_GRID')")
    public Page<ProductGrid> findProductGrids(Integer page, Integer maxResults) {
        return productGridRepository.findAll(new PageRequest(page, maxResults));
    }

    @Override
    public ProductGrid findProductGridByUrlName(String urlName) {
        ProductGrid result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.PRODUCT_GRID_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof ProductGrid) {
            result = (ProductGrid) wrapper.get();
        } else {
            result = productGridRepository.findByUrlName(urlName);

            if (result != null) {
                // put the fully organized object in the cache
                putInCache(ApplicationConstants.PRODUCT_GRID_CACHE, urlName, result);
            }
        }

        return result;
    }

    @Override
    @PreAuthorize("hasAnyRole('RIGHT_INSERT_PRODUCT_GRID')")
    public ProductGrid saveProductGrid(ProductGrid productGrid) {
        // verify that the product exists
        Category category = categoryService.findCategoryByUrlName(productGrid.getCrlnm());

        if (category != null) {
            productGrid.setSrlnm(category.getSrlnm());
            productGrid = productGridRepository.save(productGrid);

            removeFromCache(CacheType.PRODUCT_GRID, productGrid.getRlnm());
        }

        return productGrid;
    }

    @Override
    @PreAuthorize("hasAnyRole('RIGHT_DELETE_PRODUCT_GRID')")
    public void removeProductGrid(String urlName) {
        ProductGrid pg = findProductGridByUrlName(urlName);
        
        if (pg != null) {
            productGridRepository.delete(pg);
            
            removeFromCache(CacheType.PRODUCT_GRID, urlName);
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('RIGHT_INSERT_PRODUCT_GRID')")
    public void saveProductGridFilters(String urlName, Map<String, List<String>> map) {
        ProductGrid pg = findProductGridByUrlName(urlName);

        if (pg != null) {
            pg.addFilters(map);
            saveProductGrid(pg);
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Url name doesn't exist: " + urlName);
            }
        }
    }

    @Override
    public void removeProductGridFilter(String urlName, String key) {
        ProductGrid pg = findProductGridByUrlName(urlName);

        if (pg != null) {
            pg.removeFilter(key);
            saveProductGrid(pg);
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Url name doesn't exist: " + urlName);
            }
        }
    }

    @Override
    public ProductGrid findPublishedProductGridByUrlName(String urlName) {
        ProductGrid result = findProductGridByUrlName(urlName);

        if (result != null && result.getPblshd()) {
            return result;
        } else {
            if (log.isWarnEnabled()) {
                log.warn(String.format("ProductGrid: %s either doesn't exist or it is not yet published", urlName));
            }

            result = null;
        }

        return result;
    }

    @Override
    public void enrichProductGrid(EnrichProductGridQuery query) {
        CategoryItemsQuery ciq = new CategoryItemsQuery(query);

        // determine whether to retrieve items matching user or not
        if (userService.shouldUserSeeRecommendedProducts(query.getUserCode())) {
            if (query.getPg().getVrrd()) {
                ciq.setSort(true);
                ciq.setSortBy(FunctionalSortType.RECOMMENDED);
            }

            ItemPage<RelevantItem> page = productEngineService.findRelevantItemsByCategory(ciq);
            query.getPg().setTotalElements(page.getTotalElements());
            query.getPg().setRelevantItems(page.getContent());
        } else {
            ItemPage<Item> page = productEngineService.findItemsByCategory(ciq);
            query.getPg().setTotalElements(page.getTotalElements());
            query.getPg().setItems(page.getContent());
        }

    }


}
