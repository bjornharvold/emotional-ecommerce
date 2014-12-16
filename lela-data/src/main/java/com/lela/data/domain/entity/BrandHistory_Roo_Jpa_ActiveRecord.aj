// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.BrandHistory;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect BrandHistory_Roo_Jpa_ActiveRecord {
    
    public static long BrandHistory.countBrandHistorys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BrandHistory o", Long.class).getSingleResult();
    }
    
    public static List<BrandHistory> BrandHistory.findAllBrandHistorys() {
        return entityManager().createQuery("SELECT o FROM BrandHistory o", BrandHistory.class).getResultList();
    }
    
    public static BrandHistory BrandHistory.findBrandHistory(Long id) {
        if (id == null) return null;
        return entityManager().find(BrandHistory.class, id);
    }
    
    public static List<BrandHistory> BrandHistory.findBrandHistoryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BrandHistory o", BrandHistory.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public BrandHistory BrandHistory.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BrandHistory merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
