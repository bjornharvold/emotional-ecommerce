// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ReviewSource;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect ReviewSource_Roo_Jpa_Entity {
    
    declare @type: ReviewSource: @Entity;
    
    declare @type: ReviewSource: @Table(name = "review_source");
    
    declare @type: ReviewSource: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
