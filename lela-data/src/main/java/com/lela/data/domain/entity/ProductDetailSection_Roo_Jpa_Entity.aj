// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ProductDetailSection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect ProductDetailSection_Roo_Jpa_Entity {
    
    declare @type: ProductDetailSection: @Entity;
    
    declare @type: ProductDetailSection: @Table(name = "product_detail_section");
    
    declare @type: ProductDetailSection: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}