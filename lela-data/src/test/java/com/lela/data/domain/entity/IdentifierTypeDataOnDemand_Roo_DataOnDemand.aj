// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeTypeDataOnDemand;
import com.lela.data.domain.entity.IdentifierType;
import com.lela.data.domain.entity.IdentifierTypeDataOnDemand;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect IdentifierTypeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: IdentifierTypeDataOnDemand: @Component;
    
    private Random IdentifierTypeDataOnDemand.rnd = new SecureRandom();
    
    private List<IdentifierType> IdentifierTypeDataOnDemand.data;
    
    @Autowired
    AttributeTypeDataOnDemand IdentifierTypeDataOnDemand.attributeTypeDataOnDemand;
    
    public IdentifierType IdentifierTypeDataOnDemand.getNewTransientIdentifierType(int index) {
        IdentifierType obj = new IdentifierType();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setIdentifierTypeName(obj, index);
        setMultiValued(obj, index);
        return obj;
    }
    
    public void IdentifierTypeDataOnDemand.setDateCreated(IdentifierType obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void IdentifierTypeDataOnDemand.setDateModified(IdentifierType obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void IdentifierTypeDataOnDemand.setIdentifierTypeName(IdentifierType obj, int index) {
        String identifierTypeName = "identifierTypeName_" + index;
        if (identifierTypeName.length() > 255) {
            identifierTypeName = identifierTypeName.substring(0, 255);
        }
        obj.setIdentifierTypeName(identifierTypeName);
    }
    
    public void IdentifierTypeDataOnDemand.setMultiValued(IdentifierType obj, int index) {
        Boolean multiValued = Boolean.TRUE;
        obj.setMultiValued(multiValued);
    }
    
    public IdentifierType IdentifierTypeDataOnDemand.getSpecificIdentifierType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        IdentifierType obj = data.get(index);
        Long id = obj.getId();
        return IdentifierType.findIdentifierType(id);
    }
    
    public IdentifierType IdentifierTypeDataOnDemand.getRandomIdentifierType() {
        init();
        IdentifierType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return IdentifierType.findIdentifierType(id);
    }
    
    public boolean IdentifierTypeDataOnDemand.modifyIdentifierType(IdentifierType obj) {
        return false;
    }
    
    public void IdentifierTypeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = IdentifierType.findIdentifierTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'IdentifierType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<IdentifierType>();
        for (int i = 0; i < 10; i++) {
            IdentifierType obj = getNewTransientIdentifierType(i);
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