// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.InputValidationListElement;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect InputValidationListElement_Roo_Jpa_ActiveRecord {
    
    public static long InputValidationListElement.countInputValidationListElements() {
        return entityManager().createQuery("SELECT COUNT(o) FROM InputValidationListElement o", Long.class).getSingleResult();
    }
    
    public static List<InputValidationListElement> InputValidationListElement.findAllInputValidationListElements() {
        return entityManager().createQuery("SELECT o FROM InputValidationListElement o", InputValidationListElement.class).getResultList();
    }
    
    public static InputValidationListElement InputValidationListElement.findInputValidationListElement(Long id) {
        if (id == null) return null;
        return entityManager().find(InputValidationListElement.class, id);
    }
    
    public static List<InputValidationListElement> InputValidationListElement.findInputValidationListElementEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM InputValidationListElement o", InputValidationListElement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public InputValidationListElement InputValidationListElement.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        InputValidationListElement merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
