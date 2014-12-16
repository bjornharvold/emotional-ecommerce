// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.MerchantOfferHistory;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MerchantOfferHistory_Roo_Jpa_ActiveRecord {
    
    public static long MerchantOfferHistory.countMerchantOfferHistorys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MerchantOfferHistory o", Long.class).getSingleResult();
    }
    
    public static List<MerchantOfferHistory> MerchantOfferHistory.findAllMerchantOfferHistorys() {
        return entityManager().createQuery("SELECT o FROM MerchantOfferHistory o", MerchantOfferHistory.class).getResultList();
    }
    
    public static MerchantOfferHistory MerchantOfferHistory.findMerchantOfferHistory(Long id) {
        if (id == null) return null;
        return entityManager().find(MerchantOfferHistory.class, id);
    }
    
    public static List<MerchantOfferHistory> MerchantOfferHistory.findMerchantOfferHistoryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MerchantOfferHistory o", MerchantOfferHistory.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public MerchantOfferHistory MerchantOfferHistory.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MerchantOfferHistory merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}