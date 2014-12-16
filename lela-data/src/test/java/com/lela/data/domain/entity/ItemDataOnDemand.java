package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.HashSet;
import java.util.Set;

@RooDataOnDemand(entity = Item.class)
public class ItemDataOnDemand {

    @Autowired
    BrandDataOnDemand brandDataOnDemain;

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Autowired
    ConditionTypeDataOnDemand conditionTypeDataOnDemand;

    @Autowired
    ReviewStatusDataOnDemand reviewStatusDataOnDemand;

    @Autowired
    ItemStatusDataOnDemand itemStatusDataOnDemand;

    @Autowired
    SiteStatusDataOnDemand siteStatusDataOnDemand;

    @Autowired
    ProductImageItemDataOnDemand productImageItemDataOnDemand;

    public void setBrand(Item obj, int index) {
        Brand brand = brandDataOnDemain.getRandomBrand();
        obj.setBrand(brand);
    }

    public void setCategory(Item obj, int index) {
        Category category = categoryDataOnDemand.getRandomCategory();
        obj.setCategory(category);
    }

    public void setConditionType(Item obj, int index) {
        ConditionType conditionType = conditionTypeDataOnDemand.getRandomConditionType();
        obj.setConditionType(conditionType);
    }

    public void setReviewStatus(Item obj, int index) {
        ReviewStatus reviewStatus = reviewStatusDataOnDemand.getRandomReviewStatus();
        obj.setReviewStatus(reviewStatus);
    }

    public void setSiteStatus(Item obj, int index) {
        SiteStatus siteStatus = siteStatusDataOnDemand.getRandomSiteStatus();
        obj.setSiteStatus(siteStatus);
    }

    public void setItemStatus(Item obj, int index) {
        ItemStatus itemStatus = itemStatusDataOnDemand.getRandomItemStatus();
        obj.setItemStatus(itemStatus);
    }

    public void setProductImageItems(Item obj, int index) {
        Set productImageItems = new HashSet();
        obj.setProductImageItems(productImageItems);
    }

}
