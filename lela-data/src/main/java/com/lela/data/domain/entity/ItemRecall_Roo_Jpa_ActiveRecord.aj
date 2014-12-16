// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemRecall;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemRecall_Roo_Jpa_ActiveRecord {
    
    public static long ItemRecall.countItemRecalls() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItemRecall o", Long.class).getSingleResult();
    }
    
    public static List<ItemRecall> ItemRecall.findAllItemRecalls() {
        return entityManager().createQuery("SELECT o FROM ItemRecall o", ItemRecall.class).getResultList();
    }
    
    public static ItemRecall ItemRecall.findItemRecall(Long id) {
        if (id == null) return null;
        return entityManager().find(ItemRecall.class, id);
    }
    
    public static List<ItemRecall> ItemRecall.findItemRecallEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItemRecall o", ItemRecall.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ItemRecall ItemRecall.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItemRecall merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
