// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.MerchantOutOfStockPattern;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MerchantOutOfStockPattern_Roo_Jpa_ActiveRecord {
    
    public static long MerchantOutOfStockPattern.countMerchantOutOfStockPatterns() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MerchantOutOfStockPattern o", Long.class).getSingleResult();
    }
    
    public static List<MerchantOutOfStockPattern> MerchantOutOfStockPattern.findAllMerchantOutOfStockPatterns() {
        return entityManager().createQuery("SELECT o FROM MerchantOutOfStockPattern o", MerchantOutOfStockPattern.class).getResultList();
    }
    
    public static MerchantOutOfStockPattern MerchantOutOfStockPattern.findMerchantOutOfStockPattern(Long id) {
        if (id == null) return null;
        return entityManager().find(MerchantOutOfStockPattern.class, id);
    }
    
    public static List<MerchantOutOfStockPattern> MerchantOutOfStockPattern.findMerchantOutOfStockPatternEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MerchantOutOfStockPattern o", MerchantOutOfStockPattern.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public MerchantOutOfStockPattern MerchantOutOfStockPattern.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MerchantOutOfStockPattern merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}