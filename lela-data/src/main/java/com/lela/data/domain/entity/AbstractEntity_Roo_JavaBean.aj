// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AbstractEntity;
import java.util.Date;

privileged aspect AbstractEntity_Roo_JavaBean {
    
    public Date AbstractEntity.getDateCreated() {
        return this.dateCreated;
    }
    
    public void AbstractEntity.setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Date AbstractEntity.getDateModified() {
        return this.dateModified;
    }
    
    public void AbstractEntity.setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
    
}
