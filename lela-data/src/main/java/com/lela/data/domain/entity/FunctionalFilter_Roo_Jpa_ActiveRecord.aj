// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilter;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FunctionalFilter_Roo_Jpa_ActiveRecord {
    
    public static long FunctionalFilter.countFunctionalFilters() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FunctionalFilter o", Long.class).getSingleResult();
    }
    
    public static List<FunctionalFilter> FunctionalFilter.findAllFunctionalFilters() {
        return entityManager().createQuery("SELECT o FROM FunctionalFilter o", FunctionalFilter.class).getResultList();
    }
    
    public static FunctionalFilter FunctionalFilter.findFunctionalFilter(Long id) {
        if (id == null) return null;
        return entityManager().find(FunctionalFilter.class, id);
    }
    
    public static List<FunctionalFilter> FunctionalFilter.findFunctionalFilterEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FunctionalFilter o", FunctionalFilter.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public FunctionalFilter FunctionalFilter.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FunctionalFilter merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
