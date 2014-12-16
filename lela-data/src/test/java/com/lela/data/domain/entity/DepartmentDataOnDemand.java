package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Department.class)
public class DepartmentDataOnDemand {

    @Autowired
    NavbarDataOnDemand navbarDataOnDemand;

    public void setNavbar(Department obj, int index) {
        Navbar navbar = navbarDataOnDemand.getRandomNavbar();
        obj.setNavbar(navbar);
    }
}
