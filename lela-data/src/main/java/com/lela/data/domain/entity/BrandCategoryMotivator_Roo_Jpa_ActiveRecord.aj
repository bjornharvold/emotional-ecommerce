// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.BrandCategoryMotivator;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect BrandCategoryMotivator_Roo_Jpa_ActiveRecord {
    
    public static long BrandCategoryMotivator.countBrandCategoryMotivators() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BrandCategoryMotivator o", Long.class).getSingleResult();
    }
    
    public static List<BrandCategoryMotivator> BrandCategoryMotivator.findAllBrandCategoryMotivators() {
        return entityManager().createQuery("SELECT o FROM BrandCategoryMotivator o", BrandCategoryMotivator.class).getResultList();
    }
    
    public static BrandCategoryMotivator BrandCategoryMotivator.findBrandCategoryMotivator(Long id) {
        if (id == null) return null;
        return entityManager().find(BrandCategoryMotivator.class, id);
    }
    
    public static List<BrandCategoryMotivator> BrandCategoryMotivator.findBrandCategoryMotivatorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BrandCategoryMotivator o", BrandCategoryMotivator.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public BrandCategoryMotivator BrandCategoryMotivator.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BrandCategoryMotivator merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}