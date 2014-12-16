package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = CategoryMotivator.class)
public class CategoryMotivatorDataOnDemand {

    @Autowired
    private CategoryDataOnDemand categoryDataOnDemand2;

    @Autowired
    private MotivatorDataOnDemand motivatorDataOnDemand2;

    public CategoryMotivator getNewTransientCategoryMotivator(int index) {
        CategoryMotivator obj = new CategoryMotivator();
        setCategory(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setDtRuleDataTag(obj, index);
        setDtRuleSet(obj, index);
        setDtRuleSheet(obj, index);
        setDtRulesQuery(obj, index);
        setMotivator(obj, index);
        return obj;
    }

    public void setCategory(CategoryMotivator obj, int index) {
        Category category = categoryDataOnDemand2.getRandomCategory();
        obj.setCategory(category);
    }

    public void setMotivator(CategoryMotivator obj, int index) {
        Motivator motivator = motivatorDataOnDemand2.getRandomMotivator();
        obj.setMotivator(motivator);
    }
}
