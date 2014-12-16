// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ReviewStatus;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ReviewStatus_Roo_Jpa_ActiveRecord {
    
    public static long ReviewStatus.countReviewStatuses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ReviewStatus o", Long.class).getSingleResult();
    }
    
    public static List<ReviewStatus> ReviewStatus.findAllReviewStatuses() {
        return entityManager().createQuery("SELECT o FROM ReviewStatus o", ReviewStatus.class).getResultList();
    }
    
    public static ReviewStatus ReviewStatus.findReviewStatus(Long id) {
        if (id == null) return null;
        return entityManager().find(ReviewStatus.class, id);
    }
    
    public static List<ReviewStatus> ReviewStatus.findReviewStatusEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ReviewStatus o", ReviewStatus.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ReviewStatus ReviewStatus.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ReviewStatus merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}