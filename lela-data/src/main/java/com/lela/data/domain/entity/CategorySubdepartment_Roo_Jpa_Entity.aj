// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.CategorySubdepartment;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect CategorySubdepartment_Roo_Jpa_Entity {
    
    declare @type: CategorySubdepartment: @Entity;
    
    declare @type: CategorySubdepartment: @Table(name = "category_subdepartment");
    
    declare @type: CategorySubdepartment: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
