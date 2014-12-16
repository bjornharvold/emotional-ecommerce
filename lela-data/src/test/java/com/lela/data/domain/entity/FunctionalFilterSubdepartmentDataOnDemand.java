package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = FunctionalFilterSubdepartment.class)
public class FunctionalFilterSubdepartmentDataOnDemand {

    @Autowired
    FunctionalFilterDataOnDemand functionalFilterDataOnDemand;

    @Autowired
    SubdepartmentDataOnDemand subdepartmentDataOnDemand;

    public void setFunctionalFilter(FunctionalFilterSubdepartment obj, int index) {
        FunctionalFilter functionalFilter = functionalFilterDataOnDemand.getSpecificFunctionalFilter(index);
        obj.setFunctionalFilter(functionalFilter);
    }

    public void setSubdepartment(FunctionalFilterSubdepartment obj, int index) {
        Subdepartment subdepartment = subdepartmentDataOnDemand.getSpecificSubdepartment(index);
        obj.setSubdepartment(subdepartment);
    }
}
