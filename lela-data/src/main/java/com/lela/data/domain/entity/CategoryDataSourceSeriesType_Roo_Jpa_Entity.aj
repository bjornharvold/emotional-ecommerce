// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.CategoryDataSourceSeriesType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

privileged aspect CategoryDataSourceSeriesType_Roo_Jpa_Entity {
    
    declare @type: CategoryDataSourceSeriesType: @Entity;
    
    declare @type: CategoryDataSourceSeriesType: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
