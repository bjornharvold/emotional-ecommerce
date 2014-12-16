// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemAttributeHistory;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemAttributeHistory_Roo_Jpa_ActiveRecord {
    
    public static long ItemAttributeHistory.countItemAttributeHistorys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItemAttributeHistory o", Long.class).getSingleResult();
    }
    
    public static List<ItemAttributeHistory> ItemAttributeHistory.findAllItemAttributeHistorys() {
        return entityManager().createQuery("SELECT o FROM ItemAttributeHistory o", ItemAttributeHistory.class).getResultList();
    }
    
    public static ItemAttributeHistory ItemAttributeHistory.findItemAttributeHistory(Long id) {
        if (id == null) return null;
        return entityManager().find(ItemAttributeHistory.class, id);
    }
    
    public static List<ItemAttributeHistory> ItemAttributeHistory.findItemAttributeHistoryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItemAttributeHistory o", ItemAttributeHistory.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ItemAttributeHistory ItemAttributeHistory.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItemAttributeHistory merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}