// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Merchant;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Merchant_Roo_Jpa_ActiveRecord {
    
    public static long Merchant.countMerchants() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Merchant o", Long.class).getSingleResult();
    }
    
    public static List<Merchant> Merchant.findAllMerchants() {
        return entityManager().createQuery("SELECT o FROM Merchant o", Merchant.class).getResultList();
    }
    
    public static Merchant Merchant.findMerchant(Long id) {
        if (id == null) return null;
        return entityManager().find(Merchant.class, id);
    }
    
    public static List<Merchant> Merchant.findMerchantEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Merchant o", Merchant.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Merchant Merchant.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Merchant merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
