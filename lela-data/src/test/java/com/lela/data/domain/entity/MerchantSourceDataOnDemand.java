package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = MerchantSource.class)
public class MerchantSourceDataOnDemand {
    @Autowired
    MerchantSourceTypeDataOnDemand merchantSourceTypeDataOnDemand;

    public void setMerchantSourceType(MerchantSource obj, int index)
    {
        MerchantSourceType merchantSourceType = merchantSourceTypeDataOnDemand.getRandomMerchantSourceType();
        obj.setMerchantSourceType(merchantSourceType);
    }
}
