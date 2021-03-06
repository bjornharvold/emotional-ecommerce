// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Country;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Country_Roo_Jpa_ActiveRecord {
    
    public static long Country.countCountrys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Country o", Long.class).getSingleResult();
    }
    
    public static List<Country> Country.findAllCountrys() {
        return entityManager().createQuery("SELECT o FROM Country o", Country.class).getResultList();
    }
    
    public static Country Country.findCountry(Long id) {
        if (id == null) return null;
        return entityManager().find(Country.class, id);
    }
    
    public static List<Country> Country.findCountryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Country o", Country.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Country Country.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Country merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
