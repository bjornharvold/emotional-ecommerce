// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.InputValidationList;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect InputValidationList_Roo_Jpa_Entity {
    
    declare @type: InputValidationList: @Entity;
    
    declare @type: InputValidationList: @Table(name = "input_validation_list");
    
    declare @type: InputValidationList: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
