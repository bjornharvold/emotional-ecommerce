// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilter;
import com.lela.data.domain.entity.FunctionalFilterType;
import java.util.Set;

privileged aspect FunctionalFilterType_Roo_JavaBean {
    
    public Set<FunctionalFilter> FunctionalFilterType.getFunctionalFilters() {
        return this.functionalFilters;
    }
    
    public void FunctionalFilterType.setFunctionalFilters(Set<FunctionalFilter> functionalFilters) {
        this.functionalFilters = functionalFilters;
    }
    
    public String FunctionalFilterType.getFunctionalFilterTypeName() {
        return this.functionalFilterTypeName;
    }
    
    public void FunctionalFilterType.setFunctionalFilterTypeName(String functionalFilterTypeName) {
        this.functionalFilterTypeName = functionalFilterTypeName;
    }
    
}
