// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ProductDetailPart;
import com.lela.data.domain.entity.ProductDetailPartDataOnDemand;
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

privileged aspect ProductDetailPartDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ProductDetailPartDataOnDemand: @Component;
    
    private Random ProductDetailPartDataOnDemand.rnd = new SecureRandom();
    
    private List<ProductDetailPart> ProductDetailPartDataOnDemand.data;
    
    public ProductDetailPart ProductDetailPartDataOnDemand.getNewTransientProductDetailPart(int index) {
        ProductDetailPart obj = new ProductDetailPart();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setPartName(obj, index);
        setPartSeq(obj, index);
        setProductDetailGroupAttribute(obj, index);
        return obj;
    }
    
    public void ProductDetailPartDataOnDemand.setDateCreated(ProductDetailPart obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ProductDetailPartDataOnDemand.setDateModified(ProductDetailPart obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void ProductDetailPartDataOnDemand.setPartName(ProductDetailPart obj, int index) {
        String partName = "partName_" + index;
        obj.setPartName(partName);
    }
    
    public void ProductDetailPartDataOnDemand.setPartSeq(ProductDetailPart obj, int index) {
        Integer partSeq = new Integer(index);
        obj.setPartSeq(partSeq);
    }
    
    public ProductDetailPart ProductDetailPartDataOnDemand.getSpecificProductDetailPart(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ProductDetailPart obj = data.get(index);
        Long id = obj.getId();
        return ProductDetailPart.findProductDetailPart(id);
    }
    
    public ProductDetailPart ProductDetailPartDataOnDemand.getRandomProductDetailPart() {
        init();
        ProductDetailPart obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ProductDetailPart.findProductDetailPart(id);
    }
    
    public boolean ProductDetailPartDataOnDemand.modifyProductDetailPart(ProductDetailPart obj) {
        return false;
    }
    
    public void ProductDetailPartDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ProductDetailPart.findProductDetailPartEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ProductDetailPart' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ProductDetailPart>();
        for (int i = 0; i < 10; i++) {
            ProductDetailPart obj = getNewTransientProductDetailPart(i);
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
