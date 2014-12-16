// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemReview;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemReview_Roo_Jpa_ActiveRecord {
    
    public static long ItemReview.countItemReviews() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItemReview o", Long.class).getSingleResult();
    }
    
    public static List<ItemReview> ItemReview.findAllItemReviews() {
        return entityManager().createQuery("SELECT o FROM ItemReview o", ItemReview.class).getResultList();
    }
    
    public static ItemReview ItemReview.findItemReview(Long id) {
        if (id == null) return null;
        return entityManager().find(ItemReview.class, id);
    }
    
    public static List<ItemReview> ItemReview.findItemReviewEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItemReview o", ItemReview.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ItemReview ItemReview.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItemReview merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}