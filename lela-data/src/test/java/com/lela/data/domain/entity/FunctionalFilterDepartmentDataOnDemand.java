package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = FunctionalFilterDepartment.class)
public class FunctionalFilterDepartmentDataOnDemand {

    @Autowired
    FunctionalFilterDataOnDemand functionalFilterDataOnDemand;

    @Autowired
    DepartmentDataOnDemand departmentDataOnDemand;


    public void setDepartment(FunctionalFilterDepartment obj, int index) {
        Department department = departmentDataOnDemand.getSpecificDepartment(index);
        obj.setDepartment(department);
    }

    public void setFunctionalFilter(FunctionalFilterDepartment obj, int index) {
        FunctionalFilter functionalFilter = functionalFilterDataOnDemand.getSpecificFunctionalFilter(index);
        obj.setFunctionalFilter(functionalFilter);
    }
}
