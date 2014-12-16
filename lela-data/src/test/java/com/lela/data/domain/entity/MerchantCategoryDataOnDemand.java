package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = MerchantCategory.class)
public class MerchantCategoryDataOnDemand {

    @Autowired
    MerchantDataOnDemand merchantDataOnDemand;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    public void setCategory(MerchantCategory obj, int index) {
        Category category = categoryDataOnDemand.getSpecificCategory(index);
        obj.setCategory(category);
    }

    public void setMerchant(MerchantCategory obj, int index) {
        Merchant merchant = merchantDataOnDemand.getSpecificMerchant(index);
        obj.setMerchant(merchant);
    }

}
