package com.lela.commons.service;

import com.lela.domain.document.SeoUrlName;
import com.lela.domain.enums.SeoUrlNameType;
import org.springframework.data.domain.Page;

/**
 * Created by Bjorn Harvold
 * Date: 4/14/12
 * Time: 1:44 AM
 * Responsibility:
 */
public interface SeoUrlNameService {
    SeoUrlName saveSeoUrlName(SeoUrlName seoUrlName);

    SeoUrlName findSeoUrlName(String seoUrlName);

    SeoUrlName validateSeoUrlName(String seoUrlName, SeoUrlNameType type);

    void removeSeoUrlName(SeoUrlName seoUrlName);

    Long findSeoUrlNameCount();

    Page<SeoUrlName> findSeoUrlNames(Integer page, Integer maxResults);
}
