// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Department;
import com.lela.data.domain.entity.FunctionalFilter;
import com.lela.data.domain.entity.FunctionalFilterDepartment;

privileged aspect FunctionalFilterDepartment_Roo_JavaBean {
    
    public FunctionalFilter FunctionalFilterDepartment.getFunctionalFilter() {
        return this.functionalFilter;
    }
    
    public void FunctionalFilterDepartment.setFunctionalFilter(FunctionalFilter functionalFilter) {
        this.functionalFilter = functionalFilter;
    }
    
    public Department FunctionalFilterDepartment.getDepartment() {
        return this.department;
    }
    
    public void FunctionalFilterDepartment.setDepartment(Department department) {
        this.department = department;
    }
    
    public String FunctionalFilterDepartment.getObjectId() {
        return this.objectId;
    }
    
    public void FunctionalFilterDepartment.setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
}
