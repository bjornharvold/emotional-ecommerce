// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Affiliate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Affiliate_Roo_Jpa_ActiveRecord {
    
    public static long Affiliate.countAffiliates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Affiliate o", Long.class).getSingleResult();
    }
    
    public static List<Affiliate> Affiliate.findAllAffiliates() {
        return entityManager().createQuery("SELECT o FROM Affiliate o", Affiliate.class).getResultList();
    }
    
    public static Affiliate Affiliate.findAffiliate(Long id) {
        if (id == null) return null;
        return entityManager().find(Affiliate.class, id);
    }
    
    public static List<Affiliate> Affiliate.findAffiliateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Affiliate o", Affiliate.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Affiliate Affiliate.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Affiliate merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}