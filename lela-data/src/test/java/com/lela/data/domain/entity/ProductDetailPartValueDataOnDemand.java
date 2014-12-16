package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooDataOnDemand(entity = ProductDetailPartValue.class)
public class ProductDetailPartValueDataOnDemand {

    @Autowired
    ProductDetailPartDataOnDemand productDetailPartDataOnDemand;

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    public void setAttributeType(ProductDetailPartValue obj, int index) {
        AttributeType attributeType = attributeTypeDataOnDemand.getRandomAttributeType();
        obj.setAttributeType(attributeType);
    }

    public void setProductDetailPart(ProductDetailPartValue obj, int index) {
        ProductDetailPart productDetailPart = productDetailPartDataOnDemand.getRandomProductDetailPart();
        obj.setProductDetailPart(productDetailPart);
    }

}
