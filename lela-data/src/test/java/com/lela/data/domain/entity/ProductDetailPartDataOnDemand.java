package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = ProductDetailPart.class)
public class ProductDetailPartDataOnDemand {

    @Autowired
    ProductDetailGroupAttributeDataOnDemand productDetailGroupAttributeDataOnDemand;

    public void setProductDetailGroupAttribute(ProductDetailPart obj, int index) {
        ProductDetailGroupAttribute productDetailGroupAttribute = productDetailGroupAttributeDataOnDemand.getRandomProductDetailGroupAttribute();
        obj.setProductDetailGroupAttribute(productDetailGroupAttribute);
    }
}
