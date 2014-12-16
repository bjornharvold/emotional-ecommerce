package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = MerchantOffer.class)
public class MerchantOfferDataOnDemand {
    @Autowired
    private BrandDataOnDemand brandDataOnDemand2;

    @Autowired
    private ConditionTypeDataOnDemand conditionTypeDataOnDemand2;

    @Autowired
    private ReviewStatusDataOnDemand reviewStatusDataOnDemand2;

    @Autowired
    private ItemDataOnDemand itemDataOnDemand2;

    @Autowired
    private MerchantDataOnDemand merchantDataOnDemand2;

    @Autowired
    private MerchantSourceDataOnDemand merchantSourceDataOnDemand2;

    @Autowired
    private MerchantSourceTypeDataOnDemand merchantSourceTypeDataOnDemand2;

    public MerchantOffer getNewTransientMerchantOffer(int index) {
        MerchantOffer obj = new MerchantOffer();
        setAvailable(obj, index);
        setBrand(obj, index);
        setColor(obj, index);
        setConditionType(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setDirty(obj, index);
        setDoNotUse(obj, index);
        setInvestigationStatus(obj, index);
        setItem(obj, index);
        setLoadDateId(obj, index);
        setMerchant(obj, index);
        setMerchantName(obj, index);
        setMerchantSource(obj, index);
        setMerchantSourceType(obj, index);
        setModelNumber(obj, index);
        setOfferDate(obj, index);
        setOfferItemName(obj, index);
        setOfferPrice(obj, index);
        setOfferSalePrice(obj, index);
        setOfferUrl(obj, index);
        setSourceKey(obj, index);
        setUpc(obj, index);
        setUseThisOffer(obj, index);
        return obj;
    }

    public void setBrand(MerchantOffer obj, int index) {
        Brand brand = brandDataOnDemand2.getRandomBrand();
        obj.setBrand(brand);
    }

    public void setConditionType(MerchantOffer obj, int index) {
        ConditionType conditionType = conditionTypeDataOnDemand2.getRandomConditionType();
        obj.setConditionType(conditionType);
    }

    public void setItem(MerchantOffer obj, int index) {
        Item item = itemDataOnDemand2.getRandomItem();
        obj.setItem(item);
    }

    public void setMerchant(MerchantOffer obj, int index) {
        Merchant merchant = merchantDataOnDemand2.getRandomMerchant();
        obj.setMerchant(merchant);
    }

    public void setMerchantSource(MerchantOffer obj, int index) {
        MerchantSource merchantSource = merchantSourceDataOnDemand2.getRandomMerchantSource();
        obj.setMerchantSource(merchantSource);
    }

    public void setMerchantSourceType(MerchantOffer obj, int index) {
        MerchantSourceType merchantSourceType = merchantSourceTypeDataOnDemand2.getRandomMerchantSourceType();
        obj.setMerchantSourceType(merchantSourceType);
    }

    public void setInvestigationStatus(MerchantOffer obj, int index) {
        ReviewStatus reviewStatus = reviewStatusDataOnDemand2.getRandomReviewStatus();
        obj.setReviewStatus(reviewStatus);
    }

}
