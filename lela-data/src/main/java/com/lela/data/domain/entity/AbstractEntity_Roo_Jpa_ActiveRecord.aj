// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AbstractEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AbstractEntity_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager AbstractEntity.entityManager;
    
    public static final EntityManager AbstractEntity.entityManager() {
        EntityManager em = new AbstractEntity() {
        }.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long AbstractEntity.countAbstractEntitys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AbstractEntity o", Long.class).getSingleResult();
    }
    
    public static List<AbstractEntity> AbstractEntity.findAllAbstractEntitys() {
        return entityManager().createQuery("SELECT o FROM AbstractEntity o", AbstractEntity.class).getResultList();
    }
    
    public static AbstractEntity AbstractEntity.findAbstractEntity(Long id) {
        if (id == null) return null;
        return entityManager().find(AbstractEntity.class, id);
    }
    
    public static List<AbstractEntity> AbstractEntity.findAbstractEntityEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AbstractEntity o", AbstractEntity.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void AbstractEntity.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void AbstractEntity.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AbstractEntity attached = AbstractEntity.findAbstractEntity(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void AbstractEntity.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void AbstractEntity.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public AbstractEntity AbstractEntity.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AbstractEntity merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
