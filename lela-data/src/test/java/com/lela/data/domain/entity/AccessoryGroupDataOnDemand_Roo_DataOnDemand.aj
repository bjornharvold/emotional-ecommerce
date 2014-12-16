// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AccessoryGroup;
import com.lela.data.domain.entity.AccessoryGroupDataOnDemand;
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

privileged aspect AccessoryGroupDataOnDemand_Roo_DataOnDemand {
    
    declare @type: AccessoryGroupDataOnDemand: @Component;
    
    private Random AccessoryGroupDataOnDemand.rnd = new SecureRandom();
    
    private List<AccessoryGroup> AccessoryGroupDataOnDemand.data;
    
    public AccessoryGroup AccessoryGroupDataOnDemand.getNewTransientAccessoryGroup(int index) {
        AccessoryGroup obj = new AccessoryGroup();
        setAccessoryGroupLabel(obj, index);
        setAccessoryGroupName(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        return obj;
    }
    
    public void AccessoryGroupDataOnDemand.setAccessoryGroupLabel(AccessoryGroup obj, int index) {
        String accessoryGroupLabel = "accessoryGroupLabel_" + index;
        obj.setAccessoryGroupLabel(accessoryGroupLabel);
    }
    
    public void AccessoryGroupDataOnDemand.setAccessoryGroupName(AccessoryGroup obj, int index) {
        String accessoryGroupName = "accessoryGroupName_" + index;
        obj.setAccessoryGroupName(accessoryGroupName);
    }
    
    public void AccessoryGroupDataOnDemand.setDateCreated(AccessoryGroup obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void AccessoryGroupDataOnDemand.setDateModified(AccessoryGroup obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public AccessoryGroup AccessoryGroupDataOnDemand.getSpecificAccessoryGroup(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        AccessoryGroup obj = data.get(index);
        Long id = obj.getId();
        return AccessoryGroup.findAccessoryGroup(id);
    }
    
    public AccessoryGroup AccessoryGroupDataOnDemand.getRandomAccessoryGroup() {
        init();
        AccessoryGroup obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return AccessoryGroup.findAccessoryGroup(id);
    }
    
    public boolean AccessoryGroupDataOnDemand.modifyAccessoryGroup(AccessoryGroup obj) {
        return false;
    }
    
    public void AccessoryGroupDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = AccessoryGroup.findAccessoryGroupEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'AccessoryGroup' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<AccessoryGroup>();
        for (int i = 0; i < 10; i++) {
            AccessoryGroup obj = getNewTransientAccessoryGroup(i);
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