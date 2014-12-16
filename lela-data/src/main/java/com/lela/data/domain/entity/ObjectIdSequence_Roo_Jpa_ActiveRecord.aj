// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ObjectIdSequence;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ObjectIdSequence_Roo_Jpa_ActiveRecord {
    
    public static long ObjectIdSequence.countObjectIdSequences() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ObjectIdSequence o", Long.class).getSingleResult();
    }
    
    public static List<ObjectIdSequence> ObjectIdSequence.findAllObjectIdSequences() {
        return entityManager().createQuery("SELECT o FROM ObjectIdSequence o", ObjectIdSequence.class).getResultList();
    }
    
    public static ObjectIdSequence ObjectIdSequence.findObjectIdSequence(Long id) {
        if (id == null) return null;
        return entityManager().find(ObjectIdSequence.class, id);
    }
    
    public static List<ObjectIdSequence> ObjectIdSequence.findObjectIdSequenceEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ObjectIdSequence o", ObjectIdSequence.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ObjectIdSequence ObjectIdSequence.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ObjectIdSequence merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
