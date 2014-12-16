package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = MerchantOfferKeyword.class)
public class MerchantOfferKeywordDataOnDemand {

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Autowired
    ActionTypeDataOnDemand actionTypeDataOnDemand;

    public void setActionType(MerchantOfferKeyword obj, int index) {
        ActionType actionType = actionTypeDataOnDemand.getRandomActionType();
        obj.setActionType(actionType);
    }

    public void setCategory(MerchantOfferKeyword obj, int index) {
        Category category = categoryDataOnDemand.getRandomCategory();
        obj.setCategory(category);
    }
}
