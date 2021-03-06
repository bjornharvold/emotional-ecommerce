// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.LocalStore;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LocalStore_Roo_Jpa_ActiveRecord {
    
    public static long LocalStore.countLocalStores() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LocalStore o", Long.class).getSingleResult();
    }
    
    public static List<LocalStore> LocalStore.findAllLocalStores() {
        return entityManager().createQuery("SELECT o FROM LocalStore o", LocalStore.class).getResultList();
    }
    
    public static LocalStore LocalStore.findLocalStore(Long id) {
        if (id == null) return null;
        return entityManager().find(LocalStore.class, id);
    }
    
    public static List<LocalStore> LocalStore.findLocalStoreEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LocalStore o", LocalStore.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public LocalStore LocalStore.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LocalStore merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
