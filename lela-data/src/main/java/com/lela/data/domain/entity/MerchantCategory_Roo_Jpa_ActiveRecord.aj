// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.MerchantCategory;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MerchantCategory_Roo_Jpa_ActiveRecord {
    
    public static long MerchantCategory.countMerchantCategorys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MerchantCategory o", Long.class).getSingleResult();
    }
    
    public static List<MerchantCategory> MerchantCategory.findAllMerchantCategorys() {
        return entityManager().createQuery("SELECT o FROM MerchantCategory o", MerchantCategory.class).getResultList();
    }
    
    public static MerchantCategory MerchantCategory.findMerchantCategory(Long id) {
        if (id == null) return null;
        return entityManager().find(MerchantCategory.class, id);
    }
    
    public static List<MerchantCategory> MerchantCategory.findMerchantCategoryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MerchantCategory o", MerchantCategory.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public MerchantCategory MerchantCategory.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MerchantCategory merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}