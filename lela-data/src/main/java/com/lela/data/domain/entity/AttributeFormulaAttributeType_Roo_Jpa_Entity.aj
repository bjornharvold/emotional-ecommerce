// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeFormulaAttributeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

privileged aspect AttributeFormulaAttributeType_Roo_Jpa_Entity {
    
    declare @type: AttributeFormulaAttributeType: @Entity;
    
    declare @type: AttributeFormulaAttributeType: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
