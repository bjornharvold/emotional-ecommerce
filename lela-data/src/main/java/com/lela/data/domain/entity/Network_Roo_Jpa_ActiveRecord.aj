// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Network;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Network_Roo_Jpa_ActiveRecord {
    
    public static long Network.countNetworks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Network o", Long.class).getSingleResult();
    }
    
    public static List<Network> Network.findAllNetworks() {
        return entityManager().createQuery("SELECT o FROM Network o", Network.class).getResultList();
    }
    
    public static Network Network.findNetwork(Long id) {
        if (id == null) return null;
        return entityManager().find(Network.class, id);
    }
    
    public static List<Network> Network.findNetworkEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Network o", Network.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Network Network.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Network merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
