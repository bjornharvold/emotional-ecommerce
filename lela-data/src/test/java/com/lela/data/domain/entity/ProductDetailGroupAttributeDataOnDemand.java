package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ProductDetailGroupAttribute.class)
public class ProductDetailGroupAttributeDataOnDemand {

    @Autowired
    ProductDetailGroupDataOnDemand productDetailGroupDataOnDemand;

    @Autowired
    ProductDetailAttributeValueTypeDataOnDemand productDetailAttributeValueTypeDataOnDemand;

    public void setProductDetailGroup(ProductDetailGroupAttribute obj, int index) {
        ProductDetailGroup productDetailGroup = productDetailGroupDataOnDemand.getRandomProductDetailGroup();
        obj.setProductDetailGroup(productDetailGroup);
    }

    public void setProductDetailAttributeValueTypeId(ProductDetailGroupAttribute obj, int index) {
        ProductDetailAttributeValueType productDetailAttributeValueTypeId = productDetailAttributeValueTypeDataOnDemand.getRandomProductDetailAttributeValueType();
        obj.setProductDetailAttributeValueTypeId(productDetailAttributeValueTypeId);
    }
}
