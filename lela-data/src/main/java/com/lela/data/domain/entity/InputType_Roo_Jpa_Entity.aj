// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.InputType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

privileged aspect InputType_Roo_Jpa_Entity {
    
    declare @type: InputType: @Entity;
    
    declare @type: InputType: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
