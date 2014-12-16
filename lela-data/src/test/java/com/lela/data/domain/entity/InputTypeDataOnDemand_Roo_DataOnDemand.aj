// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.InputType;
import com.lela.data.domain.entity.InputTypeDataOnDemand;
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

privileged aspect InputTypeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: InputTypeDataOnDemand: @Component;
    
    private Random InputTypeDataOnDemand.rnd = new SecureRandom();
    
    private List<InputType> InputTypeDataOnDemand.data;
    
    public void InputTypeDataOnDemand.setDateCreated(InputType obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void InputTypeDataOnDemand.setDateModified(InputType obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void InputTypeDataOnDemand.setInputTypeName(InputType obj, int index) {
        String inputTypeName = "inputTypeName_" + index;
        obj.setInputTypeName(inputTypeName);
    }
    
    public InputType InputTypeDataOnDemand.getSpecificInputType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        InputType obj = data.get(index);
        Long id = obj.getId();
        return InputType.findInputType(id);
    }
    
    public InputType InputTypeDataOnDemand.getRandomInputType() {
        init();
        InputType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return InputType.findInputType(id);
    }
    
    public boolean InputTypeDataOnDemand.modifyInputType(InputType obj) {
        return false;
    }
    
    public void InputTypeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = InputType.findInputTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'InputType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<InputType>();
        for (int i = 0; i < 10; i++) {
            InputType obj = getNewTransientInputType(i);
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
