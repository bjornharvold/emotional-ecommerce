// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ProductDetailAttributeType;
import com.lela.data.domain.entity.ProductDetailAttributeTypeDataOnDemand;
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

privileged aspect ProductDetailAttributeTypeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ProductDetailAttributeTypeDataOnDemand: @Component;
    
    private Random ProductDetailAttributeTypeDataOnDemand.rnd = new SecureRandom();
    
    private List<ProductDetailAttributeType> ProductDetailAttributeTypeDataOnDemand.data;
    
    public ProductDetailAttributeType ProductDetailAttributeTypeDataOnDemand.getNewTransientProductDetailAttributeType(int index) {
        ProductDetailAttributeType obj = new ProductDetailAttributeType();
        setAttributeType(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setProductDetailGroupAttribute(obj, index);
        return obj;
    }
    
    public void ProductDetailAttributeTypeDataOnDemand.setDateCreated(ProductDetailAttributeType obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ProductDetailAttributeTypeDataOnDemand.setDateModified(ProductDetailAttributeType obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public ProductDetailAttributeType ProductDetailAttributeTypeDataOnDemand.getSpecificProductDetailAttributeType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ProductDetailAttributeType obj = data.get(index);
        Long id = obj.getId();
        return ProductDetailAttributeType.findProductDetailAttributeType(id);
    }
    
    public ProductDetailAttributeType ProductDetailAttributeTypeDataOnDemand.getRandomProductDetailAttributeType() {
        init();
        ProductDetailAttributeType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ProductDetailAttributeType.findProductDetailAttributeType(id);
    }
    
    public boolean ProductDetailAttributeTypeDataOnDemand.modifyProductDetailAttributeType(ProductDetailAttributeType obj) {
        return false;
    }
    
    public void ProductDetailAttributeTypeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ProductDetailAttributeType.findProductDetailAttributeTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ProductDetailAttributeType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ProductDetailAttributeType>();
        for (int i = 0; i < 10; i++) {
            ProductDetailAttributeType obj = getNewTransientProductDetailAttributeType(i);
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