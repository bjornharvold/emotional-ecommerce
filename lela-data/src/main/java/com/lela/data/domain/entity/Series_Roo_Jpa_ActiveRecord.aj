// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Series;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Series_Roo_Jpa_ActiveRecord {
    
    public static long Series.countSeries() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Series o", Long.class).getSingleResult();
    }
    
    public static List<Series> Series.findAllSeries() {
        return entityManager().createQuery("SELECT o FROM Series o", Series.class).getResultList();
    }
    
    public static Series Series.findSeries(Long id) {
        if (id == null) return null;
        return entityManager().find(Series.class, id);
    }
    
    public static List<Series> Series.findSeriesEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Series o", Series.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Series Series.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Series merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
