// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemReview;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect ItemReview_Roo_Jpa_Entity {
    
    declare @type: ItemReview: @Entity;
    
    declare @type: ItemReview: @Table(name = "item_review");
    
    declare @type: ItemReview: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
