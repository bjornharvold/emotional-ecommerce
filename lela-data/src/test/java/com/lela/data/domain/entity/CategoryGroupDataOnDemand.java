package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = CategoryGroup.class)
public class CategoryGroupDataOnDemand {

    @Autowired
    private NavbarDataOnDemand navbarDataOnDemand2;

    public CategoryGroup getNewTransientCategoryGroup(int index) {
        CategoryGroup obj = new CategoryGroup();
        setCategoryGroupName(obj, index);
        setCategoryGroupOrder(obj, index);
        setCategoryGroupStatus(obj, index);
        setCategoryGroupUrlName(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setDirty(obj, index);
        setNavbar(obj, index);
        return obj;
    }

    public void setNavbar(CategoryGroup obj, int index) {
        Navbar navbar = navbarDataOnDemand2.getRandomNavbar();
        obj.setNavbar(navbar);
    }
}
