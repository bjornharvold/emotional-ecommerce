// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.CategoryGroup;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CategoryGroup_Roo_Jpa_ActiveRecord {
    
    public static long CategoryGroup.countCategoryGroups() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CategoryGroup o", Long.class).getSingleResult();
    }
    
    public static List<CategoryGroup> CategoryGroup.findAllCategoryGroups() {
        return entityManager().createQuery("SELECT o FROM CategoryGroup o", CategoryGroup.class).getResultList();
    }
    
    public static CategoryGroup CategoryGroup.findCategoryGroup(Long id) {
        if (id == null) return null;
        return entityManager().find(CategoryGroup.class, id);
    }
    
    public static List<CategoryGroup> CategoryGroup.findCategoryGroupEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CategoryGroup o", CategoryGroup.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public CategoryGroup CategoryGroup.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CategoryGroup merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
