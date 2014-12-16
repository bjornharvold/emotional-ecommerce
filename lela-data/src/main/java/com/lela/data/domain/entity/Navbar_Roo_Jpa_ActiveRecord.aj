// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Navbar;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Navbar_Roo_Jpa_ActiveRecord {
    
    public static long Navbar.countNavbars() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Navbar o", Long.class).getSingleResult();
    }
    
    public static List<Navbar> Navbar.findAllNavbars() {
        return entityManager().createQuery("SELECT o FROM Navbar o", Navbar.class).getResultList();
    }
    
    public static Navbar Navbar.findNavbar(Long id) {
        if (id == null) return null;
        return entityManager().find(Navbar.class, id);
    }
    
    public static List<Navbar> Navbar.findNavbarEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Navbar o", Navbar.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Navbar Navbar.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Navbar merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
