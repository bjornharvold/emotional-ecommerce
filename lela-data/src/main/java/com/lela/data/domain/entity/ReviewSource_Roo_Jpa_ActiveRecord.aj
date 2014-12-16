// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ReviewSource;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ReviewSource_Roo_Jpa_ActiveRecord {
    
    public static long ReviewSource.countReviewSources() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ReviewSource o", Long.class).getSingleResult();
    }
    
    public static List<ReviewSource> ReviewSource.findAllReviewSources() {
        return entityManager().createQuery("SELECT o FROM ReviewSource o", ReviewSource.class).getResultList();
    }
    
    public static ReviewSource ReviewSource.findReviewSource(Long id) {
        if (id == null) return null;
        return entityManager().find(ReviewSource.class, id);
    }
    
    public static List<ReviewSource> ReviewSource.findReviewSourceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ReviewSource o", ReviewSource.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ReviewSource ReviewSource.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ReviewSource merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}