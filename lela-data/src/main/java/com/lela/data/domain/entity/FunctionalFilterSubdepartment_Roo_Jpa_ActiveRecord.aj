// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilterSubdepartment;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FunctionalFilterSubdepartment_Roo_Jpa_ActiveRecord {
    
    public static long FunctionalFilterSubdepartment.countFunctionalFilterSubdepartments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FunctionalFilterSubdepartment o", Long.class).getSingleResult();
    }
    
    public static List<FunctionalFilterSubdepartment> FunctionalFilterSubdepartment.findAllFunctionalFilterSubdepartments() {
        return entityManager().createQuery("SELECT o FROM FunctionalFilterSubdepartment o", FunctionalFilterSubdepartment.class).getResultList();
    }
    
    public static FunctionalFilterSubdepartment FunctionalFilterSubdepartment.findFunctionalFilterSubdepartment(Long id) {
        if (id == null) return null;
        return entityManager().find(FunctionalFilterSubdepartment.class, id);
    }
    
    public static List<FunctionalFilterSubdepartment> FunctionalFilterSubdepartment.findFunctionalFilterSubdepartmentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FunctionalFilterSubdepartment o", FunctionalFilterSubdepartment.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public FunctionalFilterSubdepartment FunctionalFilterSubdepartment.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FunctionalFilterSubdepartment merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
