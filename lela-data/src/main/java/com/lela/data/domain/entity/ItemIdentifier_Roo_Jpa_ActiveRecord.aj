// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemIdentifier;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemIdentifier_Roo_Jpa_ActiveRecord {
    
    public static long ItemIdentifier.countItemIdentifiers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItemIdentifier o", Long.class).getSingleResult();
    }
    
    public static List<ItemIdentifier> ItemIdentifier.findAllItemIdentifiers() {
        return entityManager().createQuery("SELECT o FROM ItemIdentifier o", ItemIdentifier.class).getResultList();
    }
    
    public static ItemIdentifier ItemIdentifier.findItemIdentifier(Long id) {
        if (id == null) return null;
        return entityManager().find(ItemIdentifier.class, id);
    }
    
    public static List<ItemIdentifier> ItemIdentifier.findItemIdentifierEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItemIdentifier o", ItemIdentifier.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ItemIdentifier ItemIdentifier.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItemIdentifier merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
