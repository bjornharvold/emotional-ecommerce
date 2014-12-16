// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ValidationType;
import com.lela.data.domain.entity.ValidationTypeDataOnDemand;
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

privileged aspect ValidationTypeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ValidationTypeDataOnDemand: @Component;
    
    private Random ValidationTypeDataOnDemand.rnd = new SecureRandom();
    
    private List<ValidationType> ValidationTypeDataOnDemand.data;
    
    public ValidationType ValidationTypeDataOnDemand.getNewTransientValidationType(int index) {
        ValidationType obj = new ValidationType();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setValidationTypeName(obj, index);
        return obj;
    }
    
    public void ValidationTypeDataOnDemand.setDateCreated(ValidationType obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ValidationTypeDataOnDemand.setDateModified(ValidationType obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void ValidationTypeDataOnDemand.setValidationTypeName(ValidationType obj, int index) {
        String validationTypeName = "validationTypeName_" + index;
        if (validationTypeName.length() > 255) {
            validationTypeName = validationTypeName.substring(0, 255);
        }
        obj.setValidationTypeName(validationTypeName);
    }
    
    public ValidationType ValidationTypeDataOnDemand.getSpecificValidationType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ValidationType obj = data.get(index);
        Long id = obj.getId();
        return ValidationType.findValidationType(id);
    }
    
    public ValidationType ValidationTypeDataOnDemand.getRandomValidationType() {
        init();
        ValidationType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ValidationType.findValidationType(id);
    }
    
    public boolean ValidationTypeDataOnDemand.modifyValidationType(ValidationType obj) {
        return false;
    }
    
    public void ValidationTypeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ValidationType.findValidationTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ValidationType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ValidationType>();
        for (int i = 0; i < 10; i++) {
            ValidationType obj = getNewTransientValidationType(i);
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