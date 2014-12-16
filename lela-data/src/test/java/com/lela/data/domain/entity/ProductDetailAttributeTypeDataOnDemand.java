package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooDataOnDemand(entity = ProductDetailAttributeType.class)
public class ProductDetailAttributeTypeDataOnDemand {
    @Autowired
    ProductDetailGroupAttributeDataOnDemand productDetailGroupAttributeDataOnDemand;

    @Autowired
    AttributeTypeDataOnDemand attributeTypeDataOnDemand;

    public void setAttributeType(ProductDetailAttributeType obj, int index) {
        AttributeType attributeType = attributeTypeDataOnDemand.getSpecificAttributeType(index);
        obj.setAttributeType(attributeType);
    }


    public void setProductDetailGroupAttribute(ProductDetailAttributeType obj, int index) {
        ProductDetailGroupAttribute productDetailGroupAttribute =
                productDetailGroupAttributeDataOnDemand.getSpecificProductDetailGroupAttribute(index);
        obj.setProductDetailGroupAttribute(productDetailGroupAttribute);
    }

}
