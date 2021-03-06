// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.MerchantSource;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MerchantSource_Roo_Jpa_ActiveRecord {
    
    public static long MerchantSource.countMerchantSources() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MerchantSource o", Long.class).getSingleResult();
    }
    
    public static List<MerchantSource> MerchantSource.findAllMerchantSources() {
        return entityManager().createQuery("SELECT o FROM MerchantSource o", MerchantSource.class).getResultList();
    }
    
    public static MerchantSource MerchantSource.findMerchantSource(Long id) {
        if (id == null) return null;
        return entityManager().find(MerchantSource.class, id);
    }
    
    public static List<MerchantSource> MerchantSource.findMerchantSourceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MerchantSource o", MerchantSource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public MerchantSource MerchantSource.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MerchantSource merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
