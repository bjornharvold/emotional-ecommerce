// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemAttribute;
import com.lela.data.domain.entity.ItemAttributeDataOnDemand;
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

privileged aspect ItemAttributeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ItemAttributeDataOnDemand: @Component;
    
    private Random ItemAttributeDataOnDemand.rnd = new SecureRandom();
    
    private List<ItemAttribute> ItemAttributeDataOnDemand.data;
    
    public ItemAttribute ItemAttributeDataOnDemand.getNewTransientItemAttribute(int index) {
        ItemAttribute obj = new ItemAttribute();
        setAttributeSequence(obj, index);
        setAttributeType(obj, index);
        setAttributeValue(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setItem(obj, index);
        return obj;
    }
    
    public void ItemAttributeDataOnDemand.setAttributeSequence(ItemAttribute obj, int index) {
        Integer attributeSequence = new Integer(index);
        obj.setAttributeSequence(attributeSequence);
    }
    
    public void ItemAttributeDataOnDemand.setAttributeValue(ItemAttribute obj, int index) {
        String attributeValue = "attributeValue_" + index;
        obj.setAttributeValue(attributeValue);
    }
    
    public void ItemAttributeDataOnDemand.setDateCreated(ItemAttribute obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ItemAttributeDataOnDemand.setDateModified(ItemAttribute obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public ItemAttribute ItemAttributeDataOnDemand.getSpecificItemAttribute(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ItemAttribute obj = data.get(index);
        Long id = obj.getId();
        return ItemAttribute.findItemAttribute(id);
    }
    
    public ItemAttribute ItemAttributeDataOnDemand.getRandomItemAttribute() {
        init();
        ItemAttribute obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ItemAttribute.findItemAttribute(id);
    }
    
    public boolean ItemAttributeDataOnDemand.modifyItemAttribute(ItemAttribute obj) {
        return false;
    }
    
    public void ItemAttributeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ItemAttribute.findItemAttributeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ItemAttribute' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ItemAttribute>();
        for (int i = 0; i < 10; i++) {
            ItemAttribute obj = getNewTransientItemAttribute(i);
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
