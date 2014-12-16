// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.DesignStyleMotivator;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect DesignStyleMotivator_Roo_Jpa_ActiveRecord {
    
    public static long DesignStyleMotivator.countDesignStyleMotivators() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DesignStyleMotivator o", Long.class).getSingleResult();
    }
    
    public static List<DesignStyleMotivator> DesignStyleMotivator.findAllDesignStyleMotivators() {
        return entityManager().createQuery("SELECT o FROM DesignStyleMotivator o", DesignStyleMotivator.class).getResultList();
    }
    
    public static DesignStyleMotivator DesignStyleMotivator.findDesignStyleMotivator(Long id) {
        if (id == null) return null;
        return entityManager().find(DesignStyleMotivator.class, id);
    }
    
    public static List<DesignStyleMotivator> DesignStyleMotivator.findDesignStyleMotivatorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DesignStyleMotivator o", DesignStyleMotivator.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public DesignStyleMotivator DesignStyleMotivator.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        DesignStyleMotivator merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}