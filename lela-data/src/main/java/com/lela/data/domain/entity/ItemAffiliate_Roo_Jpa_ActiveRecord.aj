// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemAffiliate;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemAffiliate_Roo_Jpa_ActiveRecord {
    
    public static long ItemAffiliate.countItemAffiliates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItemAffiliate o", Long.class).getSingleResult();
    }
    
    public static List<ItemAffiliate> ItemAffiliate.findAllItemAffiliates() {
        return entityManager().createQuery("SELECT o FROM ItemAffiliate o", ItemAffiliate.class).getResultList();
    }
    
    public static ItemAffiliate ItemAffiliate.findItemAffiliate(Long id) {
        if (id == null) return null;
        return entityManager().find(ItemAffiliate.class, id);
    }
    
    public static List<ItemAffiliate> ItemAffiliate.findItemAffiliateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItemAffiliate o", ItemAffiliate.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ItemAffiliate ItemAffiliate.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItemAffiliate merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
