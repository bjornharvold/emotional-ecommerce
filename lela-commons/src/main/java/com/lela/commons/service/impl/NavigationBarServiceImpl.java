/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.comparator.CategoryGroupComparator;
import com.lela.commons.comparator.CategoryOrderComparator;
import com.lela.commons.repository.NavigationBarRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.NavigationBarService;
import com.lela.domain.document.Category;
import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.document.QNavigationBar;
import com.lela.domain.enums.CacheType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 6/1/12
 * Time: 8:36 PM
 * Responsibility:
 */
@Service("navigationBarService")
public class NavigationBarServiceImpl extends AbstractCacheableService implements NavigationBarService {
    private final NavigationBarRepository navigationBarRepository;
    private final CategoryService categoryService;

    @Autowired
    public NavigationBarServiceImpl(CacheService cacheService,
                                    NavigationBarRepository navigationBarRepository,
                                    CategoryService categoryService) {
        super(cacheService);
        this.navigationBarRepository = navigationBarRepository;
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_NAVIGATIONBAR')")
    @Override
    public Page<NavigationBar> findNavigationBars(Integer page, Integer maxResults) {
        return navigationBarRepository.findAll(new PageRequest(page, maxResults));
    }
    
    @Override
    public List<NavigationBar> findNavigationBars(List<String> fields){
    	return navigationBarRepository.findNavigationBars(fields);
    }

    @Override
    public NavigationBar findDefaultNavigationBar(Locale locale) {
        NavigationBar result = null;

        if (locale != null) {
            Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.NAVIGATION_BAR_CACHE, ApplicationConstants.DEFAULT_NAVBAR + "_" + locale.toString());

            if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof NavigationBar) {
                result = (NavigationBar) wrapper.get();
            } else {
                result = navigationBarRepository.findOne(QNavigationBar.navigationBar.dflt.eq(true).and(QNavigationBar.navigationBar.lcl.eq(locale)));

                if (result != null) {
                    putInCache(ApplicationConstants.NAVIGATION_BAR_CACHE, ApplicationConstants.DEFAULT_NAVBAR + "_" + locale.toString(), result);
                }
            }
        }

        return result;
    }

    @Override
    public CategoryGroup findCategoryGroup(String navigationBarUrlName, String categoryGroupId) {
        CategoryGroup result = null;
        NavigationBar nb = findNavigationBarByUrlName(navigationBarUrlName);

        if (nb != null) {
            result = nb.findCategoryGroupById(categoryGroupId);
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_NAVIGATIONBAR')")
    @Override
    public void saveCategoryGroup(String navigationBarUrlName, CategoryGroup categoryGroup) {
        NavigationBar nb = findNavigationBarByUrlName(navigationBarUrlName);

        if (nb != null) {
            boolean saved = nb.addCategoryGroup(categoryGroup);

            if (saved) {
                saveNavigationBar(nb);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_NAVIGATIONBAR')")
    @Override
    public void removeCategoryGroup(String navigationBarUrlName, String categoryGroupId) {
        NavigationBar nb = findNavigationBarByUrlName(navigationBarUrlName);

        if (nb != null) {
            boolean removed = nb.removeCategoryGroup(categoryGroupId);

            if (removed) {
                saveNavigationBar(nb);
            }
        }
    }

    @Override
    public NavigationBar findNavigationBarByUrlName(String urlName) {
        NavigationBar     result  = null;
        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.NAVIGATION_BAR_CACHE, urlName);

        if ((wrapper != null) && (wrapper.get() instanceof NavigationBar)) {
            result = (NavigationBar) wrapper.get();
        }

        // retrieve from db then put in cache
        if (result == null) {
            result = navigationBarRepository.findByUrlName(urlName);

            if (result != null) {
                // Pre-load transient categories per groups
                if (result.getGrps() != null && !result.getGrps().isEmpty()) {
                    processCategoryGroups(result.getGrps());
                }

                // Pre-load transient categories at the navbar level
                if (result.getCtgrrlnms() != null) {
                    result.setCtgrs(new ArrayList<Category>(result.getCtgrrlnms().size()));
                    for (String categoryUrlName : result.getCtgrrlnms()) {
                        result.getCtgrs().add(categoryService.findCategoryByUrlName(categoryUrlName));
                    }
                }

                // finally, we add the navbar to the cache
                putInCache(ApplicationConstants.NAVIGATION_BAR_CACHE, urlName, result);
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_INSERT_NAVIGATIONBAR')")
    @Override
    public List<NavigationBar> saveNavigationBars(List<NavigationBar> nbs) {
        List<NavigationBar> result = new ArrayList<NavigationBar>(nbs.size());
        List<String> cacheKeys = new ArrayList<String>(nbs.size());
        for (NavigationBar nb : nbs) {
            // Explicitly call saveNavigationBar() for pre-calculating of tree
            result.add(saveNavigationBar(nb));
            cacheKeys.add(nb.getRlnm());
        }

        // Remove from cache
        if (!cacheKeys.isEmpty()) {
            removeFromCache(CacheType.NAVIGATION_BAR, cacheKeys);
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_INSERT_NAVIGATIONBAR')")
    @Override
    public NavigationBar saveNavigationBar(NavigationBar nb) {

        // Calculate tree information on NavigationBar and Category instances
        if (nb.getCtgrrlnms() == null) {
            nb.setCtgrrlnms(new ArrayList<String>());
        }
        recurseNavigationBar(nb, nb.getGrps(), null);

        // if the default flag is set to true, we have to set all other default flags to false
        if (nb.getDflt() != null && nb.getDflt()) {
            // set all navigation bar default flag to false
            navigationBarRepository.setDefaultFlagOnCollection(nb.getLcl(), false);

            // evict the default quiz that might already be in the cache
            removeFromCache(CacheType.NAVIGATION_BAR, ApplicationConstants.DEFAULT_NAVBAR + "_" + nb.getLcl().toString());
        }

        NavigationBar result = navigationBarRepository.save(nb);

        // Remove from cache
        List<String> cacheKeys = new ArrayList<String>(1);
        cacheKeys.add(nb.getRlnm());

        removeFromCache(CacheType.NAVIGATION_BAR, cacheKeys);

        return result;
    }

    private void recurseNavigationBar(NavigationBar navigationBar, List<CategoryGroup> groups, CategoryGroup department) {
        boolean departmentLevel = department == null;
        if (groups != null) {
            for (CategoryGroup group : groups) {
                if (departmentLevel) {
                    department = group;
                }

                // Retrieve categories from DB in case this in an ingest and the category tree hasn't been processed
                for (Category category : categoryService.findCategoriesByCategoryGroupUrlName(group.getRlnm())) {
                    boolean dirty = false;
                    if (category.getDprtmnts() == null) {
                        category.setDprtmnts(new ArrayList<String>());
                        dirty = true;
                    }

                    if (!category.getDprtmnts().contains(department.getRlnm())) {
                        category.getDprtmnts().add(department.getRlnm());
                        dirty = true;
                    }

                    if (category.getNvbrs() == null) {
                        category.setNvbrs(new ArrayList<String>());
                        dirty = true;
                    }

                    if (!category.getNvbrs().contains(navigationBar.getRlnm())) {
                        category.getNvbrs().add(navigationBar.getRlnm());
                        dirty = true;
                    }

                    if (dirty) {
                        categoryService.saveCategory(category);
                    }

                    if (!navigationBar.getCtgrrlnms().contains(category.getRlnm())) {
                        navigationBar.getCtgrrlnms().add(category.getRlnm());
                    }
                }

                if (group.getChldrn() != null) {
                    recurseNavigationBar(navigationBar, group.getChldrn(), department);
                }
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_DELETE_NAVIGATIONBAR')")
    @Override
    public NavigationBar removeNavigationBar(String rlnm) {
        NavigationBar nb = navigationBarRepository.findByUrlName(rlnm);
        if (nb != null) {
            navigationBarRepository.delete(nb);

            // Remove from cache
            List<String> cacheKeys = new ArrayList<String>(1);
            cacheKeys.add(rlnm);

            removeFromCache(CacheType.NAVIGATION_BAR, cacheKeys);

            if (nb.getDflt()) {
                // evict the default quiz that might already be in the cache
                removeFromCache(CacheType.NAVIGATION_BAR, ApplicationConstants.DEFAULT_NAVBAR + "_" + nb.getLcl().toString());
            }

            // For each category in the navbar, remove the navbar and department references
            if (nb.getCtgrrlnms() != null) {
                for (String categoryUrlName : nb.getCtgrrlnms()) {
                    boolean dirty = false;
                    Category category = categoryService.findCategoryByUrlName(categoryUrlName);
                    if (category != null) {
                        // Remove the navbar Url Name
                        if (category.getNvbrs() != null) {
                            category.getNvbrs().remove(nb.getRlnm());
                            dirty = true;
                        }

                        // Remove the Department url name UNLESS the department still exists in a different navbar
                        // This only works if the navbar is already deleted
                        if (category.getDprtmnts() != null) {
                            for (CategoryGroup department : nb.getGrps()){
                                if (!navigationBarRepository.departmentExists(department.getRlnm())) {
                                    category.getDprtmnts().remove(department.getRlnm());
                                    dirty = true;
                                }
                            }
                        }

                        if (dirty) {
                            categoryService.saveCategory(category);
                        }
                    }
                }
            }
        }

        return nb;
    }

    private void processCategoryGroups(List<CategoryGroup> groups) {
        // first we sort the category groups within navbar
        Collections.sort(groups, new CategoryGroupComparator());

        // then we add all categories that are part of each category group
        for (CategoryGroup cg : groups) {
            // handle children groups
            if (cg.getChldrn() != null && !cg.getChldrn().isEmpty()) {
                processCategoryGroups(cg.getChldrn());
            }

            cg.setCtgrs(categoryService.findCategoriesByCategoryGroupUrlName(cg.getRlnm()));

            if (cg.getCtgrs() != null && !cg.getCtgrs().isEmpty()) {
                // sort categories
                // TODO We currently only support one order for a category [BH 2011-2-13]
                // TODO Which means, if a category is associated with 2 category groups, it will be sorted the same way
                Collections.sort(cg.getCtgrs(), new CategoryOrderComparator());
            }
        }
    }

    public NavigationBar findNavigationBarByGroup(String categoryGroupUrlName) {
        return navigationBarRepository.findByGroupUrlName(categoryGroupUrlName);
    }
}
