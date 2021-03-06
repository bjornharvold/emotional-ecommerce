// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeFormulaAttributeType;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AttributeFormulaAttributeType_Roo_Jpa_ActiveRecord {
    
    public static long AttributeFormulaAttributeType.countAttributeFormulaAttributeTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AttributeFormulaAttributeType o", Long.class).getSingleResult();
    }
    
    public static List<AttributeFormulaAttributeType> AttributeFormulaAttributeType.findAllAttributeFormulaAttributeTypes() {
        return entityManager().createQuery("SELECT o FROM AttributeFormulaAttributeType o", AttributeFormulaAttributeType.class).getResultList();
    }
    
    public static AttributeFormulaAttributeType AttributeFormulaAttributeType.findAttributeFormulaAttributeType(Long id) {
        if (id == null) return null;
        return entityManager().find(AttributeFormulaAttributeType.class, id);
    }
    
    public static List<AttributeFormulaAttributeType> AttributeFormulaAttributeType.findAttributeFormulaAttributeTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AttributeFormulaAttributeType o", AttributeFormulaAttributeType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public AttributeFormulaAttributeType AttributeFormulaAttributeType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AttributeFormulaAttributeType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
