// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Navbar;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

privileged aspect Navbar_Roo_Jpa_Entity {
    
    declare @type: Navbar: @Entity;
    
    declare @type: Navbar: @Table(name = "navbar");
    
    declare @type: Navbar: @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS);
    
}
