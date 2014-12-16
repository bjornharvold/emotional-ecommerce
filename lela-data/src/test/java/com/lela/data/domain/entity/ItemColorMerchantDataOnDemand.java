package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ItemColorMerchant.class)
public class ItemColorMerchantDataOnDemand {

    @Autowired
    private ItemColorDataOnDemand itemColorDataOnDemand;

    @Autowired
    private MerchantDataOnDemand merchantDataOnDemand2;

    public ItemColorMerchant getNewTransientItemColorMerchant(int index) {
        ItemColorMerchant obj = new ItemColorMerchant();

        setAvailable(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setMerchant(obj, index);
        setItemColor(obj, index);
        return obj;
    }

    public void setMerchant(ItemColorMerchant obj, int index) {
        Merchant merchant = merchantDataOnDemand2.getRandomMerchant();
        obj.setMerchant(merchant);
    }

    public void setItemColor(ItemColorMerchant obj, int index) {
        ItemColor itemColor = itemColorDataOnDemand.getRandomItemColor();
        obj.setItemColor(itemColor);
    }
}
