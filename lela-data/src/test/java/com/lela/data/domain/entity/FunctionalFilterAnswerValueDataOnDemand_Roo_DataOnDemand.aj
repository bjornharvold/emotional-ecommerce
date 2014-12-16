// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilterAnswerValue;
import com.lela.data.domain.entity.FunctionalFilterAnswerValueDataOnDemand;
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

privileged aspect FunctionalFilterAnswerValueDataOnDemand_Roo_DataOnDemand {
    
    declare @type: FunctionalFilterAnswerValueDataOnDemand: @Component;
    
    private Random FunctionalFilterAnswerValueDataOnDemand.rnd = new SecureRandom();
    
    private List<FunctionalFilterAnswerValue> FunctionalFilterAnswerValueDataOnDemand.data;
    
    public FunctionalFilterAnswerValue FunctionalFilterAnswerValueDataOnDemand.getNewTransientFunctionalFilterAnswerValue(int index) {
        FunctionalFilterAnswerValue obj = new FunctionalFilterAnswerValue();
        setAnswerValue(obj, index);
        setAttributeType(obj, index);
        setAttributeValue(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setFunctionalFilterAnswer(obj, index);
        setRequiredValue(obj, index);
        return obj;
    }
    
    public void FunctionalFilterAnswerValueDataOnDemand.setAnswerValue(FunctionalFilterAnswerValue obj, int index) {
        String answerValue = "answerValue_" + index;
        obj.setAnswerValue(answerValue);
    }
    
    public void FunctionalFilterAnswerValueDataOnDemand.setAttributeValue(FunctionalFilterAnswerValue obj, int index) {
        String attributeValue = "attributeValue_" + index;
        obj.setAttributeValue(attributeValue);
    }
    
    public void FunctionalFilterAnswerValueDataOnDemand.setDateCreated(FunctionalFilterAnswerValue obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void FunctionalFilterAnswerValueDataOnDemand.setDateModified(FunctionalFilterAnswerValue obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void FunctionalFilterAnswerValueDataOnDemand.setRequiredValue(FunctionalFilterAnswerValue obj, int index) {
        Boolean requiredValue = Boolean.TRUE;
        obj.setRequiredValue(requiredValue);
    }
    
    public FunctionalFilterAnswerValue FunctionalFilterAnswerValueDataOnDemand.getSpecificFunctionalFilterAnswerValue(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        FunctionalFilterAnswerValue obj = data.get(index);
        Long id = obj.getId();
        return FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id);
    }
    
    public FunctionalFilterAnswerValue FunctionalFilterAnswerValueDataOnDemand.getRandomFunctionalFilterAnswerValue() {
        init();
        FunctionalFilterAnswerValue obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id);
    }
    
    public boolean FunctionalFilterAnswerValueDataOnDemand.modifyFunctionalFilterAnswerValue(FunctionalFilterAnswerValue obj) {
        return false;
    }
    
    public void FunctionalFilterAnswerValueDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = FunctionalFilterAnswerValue.findFunctionalFilterAnswerValueEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'FunctionalFilterAnswerValue' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<FunctionalFilterAnswerValue>();
        for (int i = 0; i < 10; i++) {
            FunctionalFilterAnswerValue obj = getNewTransientFunctionalFilterAnswerValue(i);
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