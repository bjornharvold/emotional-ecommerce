// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

privileged aspect AbstractEntity_Roo_Jpa_Entity {
    
    declare @type: AbstractEntity: @MappedSuperclass;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long AbstractEntity.id;
    
    @Version
    @Column(name = "Version")
    private Integer AbstractEntity.version;
    
    public Long AbstractEntity.getId() {
        return this.id;
    }
    
    public void AbstractEntity.setId(Long id) {
        this.id = id;
    }
    
    public Integer AbstractEntity.getVersion() {
        return this.version;
    }
    
    public void AbstractEntity.setVersion(Integer version) {
        this.version = version;
    }
    
}
