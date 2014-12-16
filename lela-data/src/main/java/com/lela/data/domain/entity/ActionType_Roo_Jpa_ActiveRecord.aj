// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ActionType;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ActionType_Roo_Jpa_ActiveRecord {
    
    public static long ActionType.countActionTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ActionType o", Long.class).getSingleResult();
    }
    
    public static List<ActionType> ActionType.findAllActionTypes() {
        return entityManager().createQuery("SELECT o FROM ActionType o", ActionType.class).getResultList();
    }
    
    public static ActionType ActionType.findActionType(Long id) {
        if (id == null) return null;
        return entityManager().find(ActionType.class, id);
    }
    
    public static List<ActionType> ActionType.findActionTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ActionType o", ActionType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ActionType ActionType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ActionType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
