package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Motivator.class)
public class MotivatorDataOnDemand {
    @Autowired
    private CategoryDataOnDemand categoryDataOnDemand2;

    public Motivator getNewTransientMotivator(int index) {
        Motivator obj = new Motivator();
        setCategory(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setMotivatorLabel(obj, index);
        setRulesEngineDt(obj, index);
        setRulesEngineEdd(obj, index);
        setSubCategoryId(obj, index);
        return obj;
    }

    public void setCategory(Motivator obj, int index) {
        Category category = categoryDataOnDemand2.getRandomCategory();
        obj.setCategory(category);
    }
}
