// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AdvertiserNameMerchant;
import com.lela.data.domain.entity.Merchant;

privileged aspect AdvertiserNameMerchant_Roo_JavaBean {
    
    public Merchant AdvertiserNameMerchant.getMerchant() {
        return this.merchant;
    }
    
    public void AdvertiserNameMerchant.setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    
    public String AdvertiserNameMerchant.getAdvertiserName() {
        return this.advertiserName;
    }
    
    public void AdvertiserNameMerchant.setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }
    
}
