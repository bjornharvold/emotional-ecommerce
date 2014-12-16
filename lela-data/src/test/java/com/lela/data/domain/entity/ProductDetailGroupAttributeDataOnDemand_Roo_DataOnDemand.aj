// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ProductDetailGroupAttribute;
import com.lela.data.domain.entity.ProductDetailGroupAttributeDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect ProductDetailGroupAttributeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ProductDetailGroupAttributeDataOnDemand: @Component;
    
    private Random ProductDetailGroupAttributeDataOnDemand.rnd = new SecureRandom();
    
    private List<ProductDetailGroupAttribute> ProductDetailGroupAttributeDataOnDemand.data;
    
    public ProductDetailGroupAttribute ProductDetailGroupAttributeDataOnDemand.getNewTransientProductDetailGroupAttribute(int index) {
        ProductDetailGroupAttribute obj = new ProductDetailGroupAttribute();
        setAttrLabel(obj, index);
        setAttrName(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setOrderInGroup(obj, index);
        setProductDetailAttributeValueTypeId(obj, index);
        setProductDetailGroup(obj, index);
        return obj;
    }
    
    public void ProductDetailGroupAttributeDataOnDemand.setAttrLabel(ProductDetailGroupAttribute obj, int index) {
        String attrLabel = "attrLabel_" + index;
        obj.setAttrLabel(attrLabel);
    }
    
    public void ProductDetailGroupAttributeDataOnDemand.setAttrName(ProductDetailGroupAttribute obj, int index) {
        String attrName = "attrName_" + index;
        obj.setAttrName(attrName);
    }
    
    public void ProductDetailGroupAttributeDataOnDemand.setDateCreated(ProductDetailGroupAttribute obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ProductDetailGroupAttributeDataOnDemand.setDateModified(ProductDetailGroupAttribute obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void ProductDetailGroupAttributeDataOnDemand.setOrderInGroup(ProductDetailGroupAttribute obj, int index) {
        Integer orderInGroup = new Integer(index);
        obj.setOrderInGroup(orderInGroup);
    }
    
    public ProductDetailGroupAttribute ProductDetailGroupAttributeDataOnDemand.getSpecificProductDetailGroupAttribute(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ProductDetailGroupAttribute obj = data.get(index);
        Long id = obj.getId();
        return ProductDetailGroupAttribute.findProductDetailGroupAttribute(id);
    }
    
    public ProductDetailGroupAttribute ProductDetailGroupAttributeDataOnDemand.getRandomProductDetailGroupAttribute() {
        init();
        ProductDetailGroupAttribute obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ProductDetailGroupAttribute.findProductDetailGroupAttribute(id);
    }
    
    public boolean ProductDetailGroupAttributeDataOnDemand.modifyProductDetailGroupAttribute(ProductDetailGroupAttribute obj) {
        return false;
    }
    
    public void ProductDetailGroupAttributeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ProductDetailGroupAttribute.findProductDetailGroupAttributeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ProductDetailGroupAttribute' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ProductDetailGroupAttribute>();
        for (int i = 0; i < 10; i++) {
            ProductDetailGroupAttribute obj = getNewTransientProductDetailGroupAttribute(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
