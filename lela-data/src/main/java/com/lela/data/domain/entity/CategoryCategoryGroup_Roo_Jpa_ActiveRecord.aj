// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.CategoryCategoryGroup;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CategoryCategoryGroup_Roo_Jpa_ActiveRecord {
    
    public static long CategoryCategoryGroup.countCategoryCategoryGroups() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CategoryCategoryGroup o", Long.class).getSingleResult();
    }
    
    public static List<CategoryCategoryGroup> CategoryCategoryGroup.findAllCategoryCategoryGroups() {
        return entityManager().createQuery("SELECT o FROM CategoryCategoryGroup o", CategoryCategoryGroup.class).getResultList();
    }
    
    public static CategoryCategoryGroup CategoryCategoryGroup.findCategoryCategoryGroup(Long id) {
        if (id == null) return null;
        return entityManager().find(CategoryCategoryGroup.class, id);
    }
    
    public static List<CategoryCategoryGroup> CategoryCategoryGroup.findCategoryCategoryGroupEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CategoryCategoryGroup o", CategoryCategoryGroup.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public CategoryCategoryGroup CategoryCategoryGroup.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CategoryCategoryGroup merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}