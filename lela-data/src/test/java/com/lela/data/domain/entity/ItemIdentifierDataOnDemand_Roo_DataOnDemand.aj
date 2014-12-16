// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemIdentifier;
import com.lela.data.domain.entity.ItemIdentifierDataOnDemand;
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

privileged aspect ItemIdentifierDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ItemIdentifierDataOnDemand: @Component;
    
    private Random ItemIdentifierDataOnDemand.rnd = new SecureRandom();
    
    private List<ItemIdentifier> ItemIdentifierDataOnDemand.data;
    
    public ItemIdentifier ItemIdentifierDataOnDemand.getNewTransientItemIdentifier(int index) {
        ItemIdentifier obj = new ItemIdentifier();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setIdentifierType(obj, index);
        setIdentifierValue(obj, index);
        setItem(obj, index);
        return obj;
    }
    
    public void ItemIdentifierDataOnDemand.setDateCreated(ItemIdentifier obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ItemIdentifierDataOnDemand.setDateModified(ItemIdentifier obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void ItemIdentifierDataOnDemand.setIdentifierValue(ItemIdentifier obj, int index) {
        String identifierValue = "identifierValue_" + index;
        obj.setIdentifierValue(identifierValue);
    }
    
    public ItemIdentifier ItemIdentifierDataOnDemand.getSpecificItemIdentifier(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ItemIdentifier obj = data.get(index);
        Long id = obj.getId();
        return ItemIdentifier.findItemIdentifier(id);
    }
    
    public ItemIdentifier ItemIdentifierDataOnDemand.getRandomItemIdentifier() {
        init();
        ItemIdentifier obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ItemIdentifier.findItemIdentifier(id);
    }
    
    public boolean ItemIdentifierDataOnDemand.modifyItemIdentifier(ItemIdentifier obj) {
        return false;
    }
    
    public void ItemIdentifierDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ItemIdentifier.findItemIdentifierEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ItemIdentifier' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ItemIdentifier>();
        for (int i = 0; i < 10; i++) {
            ItemIdentifier obj = getNewTransientItemIdentifier(i);
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
