// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeFormula;
import com.lela.data.domain.entity.AttributeFormulaDataOnDemand;
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

privileged aspect AttributeFormulaDataOnDemand_Roo_DataOnDemand {
    
    declare @type: AttributeFormulaDataOnDemand: @Component;
    
    private Random AttributeFormulaDataOnDemand.rnd = new SecureRandom();
    
    private List<AttributeFormula> AttributeFormulaDataOnDemand.data;
    
    public AttributeFormula AttributeFormulaDataOnDemand.getNewTransientAttributeFormula(int index) {
        AttributeFormula obj = new AttributeFormula();
        setAttributeType(obj, index);
        setCategory(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setDescription(obj, index);
        setFromClause(obj, index);
        setSelectClause(obj, index);
        setSequenceGroup(obj, index);
        setWhereClause(obj, index);
        return obj;
    }
    
    public void AttributeFormulaDataOnDemand.setDateCreated(AttributeFormula obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void AttributeFormulaDataOnDemand.setDateModified(AttributeFormula obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void AttributeFormulaDataOnDemand.setDescription(AttributeFormula obj, int index) {
        String description = "description_" + index;
        obj.setDescription(description);
    }
    
    public void AttributeFormulaDataOnDemand.setFromClause(AttributeFormula obj, int index) {
        String fromClause = "fromClause_" + index;
        obj.setFromClause(fromClause);
    }
    
    public void AttributeFormulaDataOnDemand.setSelectClause(AttributeFormula obj, int index) {
        String selectClause = "selectClause_" + index;
        obj.setSelectClause(selectClause);
    }
    
    public void AttributeFormulaDataOnDemand.setSequenceGroup(AttributeFormula obj, int index) {
        Integer sequenceGroup = new Integer(index);
        obj.setSequenceGroup(sequenceGroup);
    }
    
    public void AttributeFormulaDataOnDemand.setWhereClause(AttributeFormula obj, int index) {
        String whereClause = "whereClause_" + index;
        obj.setWhereClause(whereClause);
    }
    
    public AttributeFormula AttributeFormulaDataOnDemand.getSpecificAttributeFormula(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        AttributeFormula obj = data.get(index);
        Long id = obj.getId();
        return AttributeFormula.findAttributeFormula(id);
    }
    
    public AttributeFormula AttributeFormulaDataOnDemand.getRandomAttributeFormula() {
        init();
        AttributeFormula obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return AttributeFormula.findAttributeFormula(id);
    }
    
    public boolean AttributeFormulaDataOnDemand.modifyAttributeFormula(AttributeFormula obj) {
        return false;
    }
    
    public void AttributeFormulaDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = AttributeFormula.findAttributeFormulaEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'AttributeFormula' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<AttributeFormula>();
        for (int i = 0; i < 10; i++) {
            AttributeFormula obj = getNewTransientAttributeFormula(i);
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