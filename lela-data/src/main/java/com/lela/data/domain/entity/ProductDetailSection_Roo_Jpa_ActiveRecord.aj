// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ProductDetailSection;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ProductDetailSection_Roo_Jpa_ActiveRecord {
    
    public static long ProductDetailSection.countProductDetailSections() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProductDetailSection o", Long.class).getSingleResult();
    }
    
    public static List<ProductDetailSection> ProductDetailSection.findAllProductDetailSections() {
        return entityManager().createQuery("SELECT o FROM ProductDetailSection o", ProductDetailSection.class).getResultList();
    }
    
    public static ProductDetailSection ProductDetailSection.findProductDetailSection(Long id) {
        if (id == null) return null;
        return entityManager().find(ProductDetailSection.class, id);
    }
    
    public static List<ProductDetailSection> ProductDetailSection.findProductDetailSectionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProductDetailSection o", ProductDetailSection.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ProductDetailSection ProductDetailSection.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ProductDetailSection merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
