// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AffiliateTransaction;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AffiliateTransaction_Roo_Jpa_ActiveRecord {
    
    public static long AffiliateTransaction.countAffiliateTransactions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AffiliateTransaction o", Long.class).getSingleResult();
    }
    
    public static List<AffiliateTransaction> AffiliateTransaction.findAllAffiliateTransactions() {
        return entityManager().createQuery("SELECT o FROM AffiliateTransaction o", AffiliateTransaction.class).getResultList();
    }
    
    public static AffiliateTransaction AffiliateTransaction.findAffiliateTransaction(Long id) {
        if (id == null) return null;
        return entityManager().find(AffiliateTransaction.class, id);
    }
    
    public static List<AffiliateTransaction> AffiliateTransaction.findAffiliateTransactionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AffiliateTransaction o", AffiliateTransaction.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public AffiliateTransaction AffiliateTransaction.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AffiliateTransaction merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
