// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilterAnswer;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FunctionalFilterAnswer_Roo_Jpa_ActiveRecord {
    
    public static long FunctionalFilterAnswer.countFunctionalFilterAnswers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FunctionalFilterAnswer o", Long.class).getSingleResult();
    }
    
    public static List<FunctionalFilterAnswer> FunctionalFilterAnswer.findAllFunctionalFilterAnswers() {
        return entityManager().createQuery("SELECT o FROM FunctionalFilterAnswer o", FunctionalFilterAnswer.class).getResultList();
    }
    
    public static FunctionalFilterAnswer FunctionalFilterAnswer.findFunctionalFilterAnswer(Long id) {
        if (id == null) return null;
        return entityManager().find(FunctionalFilterAnswer.class, id);
    }
    
    public static List<FunctionalFilterAnswer> FunctionalFilterAnswer.findFunctionalFilterAnswerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FunctionalFilterAnswer o", FunctionalFilterAnswer.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public FunctionalFilterAnswer FunctionalFilterAnswer.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FunctionalFilterAnswer merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
