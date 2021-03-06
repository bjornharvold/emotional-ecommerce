// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ActionType;
import com.lela.data.domain.entity.ActionTypeDataOnDemand;
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

privileged aspect ActionTypeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ActionTypeDataOnDemand: @Component;
    
    private Random ActionTypeDataOnDemand.rnd = new SecureRandom();
    
    private List<ActionType> ActionTypeDataOnDemand.data;
    
    public ActionType ActionTypeDataOnDemand.getNewTransientActionType(int index) {
        ActionType obj = new ActionType();
        setActionTypeName(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        return obj;
    }
    
    public void ActionTypeDataOnDemand.setActionTypeName(ActionType obj, int index) {
        String actionTypeName = "actionTypeName_" + index;
        obj.setActionTypeName(actionTypeName);
    }
    
    public void ActionTypeDataOnDemand.setDateCreated(ActionType obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ActionTypeDataOnDemand.setDateModified(ActionType obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public ActionType ActionTypeDataOnDemand.getSpecificActionType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ActionType obj = data.get(index);
        Long id = obj.getId();
        return ActionType.findActionType(id);
    }
    
    public ActionType ActionTypeDataOnDemand.getRandomActionType() {
        init();
        ActionType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ActionType.findActionType(id);
    }
    
    public boolean ActionTypeDataOnDemand.modifyActionType(ActionType obj) {
        return false;
    }
    
    public void ActionTypeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ActionType.findActionTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ActionType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ActionType>();
        for (int i = 0; i < 10; i++) {
            ActionType obj = getNewTransientActionType(i);
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
