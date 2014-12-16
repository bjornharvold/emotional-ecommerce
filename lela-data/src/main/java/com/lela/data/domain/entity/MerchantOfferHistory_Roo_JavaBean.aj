// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.Merchant;
import com.lela.data.domain.entity.MerchantOfferHistory;
import java.util.Date;

privileged aspect MerchantOfferHistory_Roo_JavaBean {
    
    public Item MerchantOfferHistory.getItem() {
        return this.item;
    }
    
    public void MerchantOfferHistory.setItem(Item item) {
        this.item = item;
    }
    
    public Boolean MerchantOfferHistory.getAvailable() {
        return this.available;
    }
    
    public void MerchantOfferHistory.setAvailable(Boolean available) {
        this.available = available;
    }
    
    public Merchant MerchantOfferHistory.getMerchant() {
        return this.merchant;
    }
    
    public void MerchantOfferHistory.setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    
    public String MerchantOfferHistory.getOfferItemName() {
        return this.offerItemName;
    }
    
    public void MerchantOfferHistory.setOfferItemName(String offerItemName) {
        this.offerItemName = offerItemName;
    }
    
    public String MerchantOfferHistory.getOfferPrice() {
        return this.offerPrice;
    }
    
    public void MerchantOfferHistory.setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }
    
    public String MerchantOfferHistory.getOfferSalePrice() {
        return this.offerSalePrice;
    }
    
    public void MerchantOfferHistory.setOfferSalePrice(String offerSalePrice) {
        this.offerSalePrice = offerSalePrice;
    }
    
    public Date MerchantOfferHistory.getStartDate() {
        return this.startDate;
    }
    
    public void MerchantOfferHistory.setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date MerchantOfferHistory.getEndDate() {
        return this.endDate;
    }
    
    public void MerchantOfferHistory.setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Integer MerchantOfferHistory.getOfferId() {
        return this.offerId;
    }
    
    public void MerchantOfferHistory.setOfferId(Integer offerId) {
        this.offerId = offerId;
    }
    
    public String MerchantOfferHistory.getSourceId() {
        return this.sourceId;
    }
    
    public void MerchantOfferHistory.setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    
    public String MerchantOfferHistory.getSourceKey() {
        return this.sourceKey;
    }
    
    public void MerchantOfferHistory.setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }
    
    public String MerchantOfferHistory.getSourceTypeId() {
        return this.sourceTypeId;
    }
    
    public void MerchantOfferHistory.setSourceTypeId(String sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }
    
}
