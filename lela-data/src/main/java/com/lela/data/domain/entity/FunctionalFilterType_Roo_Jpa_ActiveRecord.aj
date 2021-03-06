// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilterType;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FunctionalFilterType_Roo_Jpa_ActiveRecord {
    
    public static long FunctionalFilterType.countFunctionalFilterTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FunctionalFilterType o", Long.class).getSingleResult();
    }
    
    public static List<FunctionalFilterType> FunctionalFilterType.findAllFunctionalFilterTypes() {
        return entityManager().createQuery("SELECT o FROM FunctionalFilterType o", FunctionalFilterType.class).getResultList();
    }
    
    public static FunctionalFilterType FunctionalFilterType.findFunctionalFilterType(Long id) {
        if (id == null) return null;
        return entityManager().find(FunctionalFilterType.class, id);
    }
    
    public static List<FunctionalFilterType> FunctionalFilterType.findFunctionalFilterTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FunctionalFilterType o", FunctionalFilterType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public FunctionalFilterType FunctionalFilterType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FunctionalFilterType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
