// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.BrandMotivator;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect BrandMotivator_Roo_Jpa_ActiveRecord {
    
    public static long BrandMotivator.countBrandMotivators() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BrandMotivator o", Long.class).getSingleResult();
    }
    
    public static List<BrandMotivator> BrandMotivator.findAllBrandMotivators() {
        return entityManager().createQuery("SELECT o FROM BrandMotivator o", BrandMotivator.class).getResultList();
    }
    
    public static BrandMotivator BrandMotivator.findBrandMotivator(Long id) {
        if (id == null) return null;
        return entityManager().find(BrandMotivator.class, id);
    }
    
    public static List<BrandMotivator> BrandMotivator.findBrandMotivatorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BrandMotivator o", BrandMotivator.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public BrandMotivator BrandMotivator.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BrandMotivator merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}