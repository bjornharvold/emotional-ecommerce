// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeTypeHistory;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AttributeTypeHistory_Roo_Jpa_ActiveRecord {
    
    public static long AttributeTypeHistory.countAttributeTypeHistorys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AttributeTypeHistory o", Long.class).getSingleResult();
    }
    
    public static List<AttributeTypeHistory> AttributeTypeHistory.findAllAttributeTypeHistorys() {
        return entityManager().createQuery("SELECT o FROM AttributeTypeHistory o", AttributeTypeHistory.class).getResultList();
    }
    
    public static AttributeTypeHistory AttributeTypeHistory.findAttributeTypeHistory(Long id) {
        if (id == null) return null;
        return entityManager().find(AttributeTypeHistory.class, id);
    }
    
    public static List<AttributeTypeHistory> AttributeTypeHistory.findAttributeTypeHistoryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AttributeTypeHistory o", AttributeTypeHistory.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public AttributeTypeHistory AttributeTypeHistory.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AttributeTypeHistory merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}