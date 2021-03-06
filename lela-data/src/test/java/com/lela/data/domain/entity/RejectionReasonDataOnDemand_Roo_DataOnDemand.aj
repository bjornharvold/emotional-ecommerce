// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.RejectionReason;
import com.lela.data.domain.entity.RejectionReasonDataOnDemand;
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

privileged aspect RejectionReasonDataOnDemand_Roo_DataOnDemand {
    
    declare @type: RejectionReasonDataOnDemand: @Component;
    
    private Random RejectionReasonDataOnDemand.rnd = new SecureRandom();
    
    private List<RejectionReason> RejectionReasonDataOnDemand.data;
    
    public RejectionReason RejectionReasonDataOnDemand.getNewTransientRejectionReason(int index) {
        RejectionReason obj = new RejectionReason();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setRejectionReasonName(obj, index);
        return obj;
    }
    
    public void RejectionReasonDataOnDemand.setDateCreated(RejectionReason obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void RejectionReasonDataOnDemand.setDateModified(RejectionReason obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void RejectionReasonDataOnDemand.setRejectionReasonName(RejectionReason obj, int index) {
        String rejectionReasonName = "rejectionReasonName_" + index;
        obj.setRejectionReasonName(rejectionReasonName);
    }
    
    public RejectionReason RejectionReasonDataOnDemand.getSpecificRejectionReason(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        RejectionReason obj = data.get(index);
        Long id = obj.getId();
        return RejectionReason.findRejectionReason(id);
    }
    
    public RejectionReason RejectionReasonDataOnDemand.getRandomRejectionReason() {
        init();
        RejectionReason obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return RejectionReason.findRejectionReason(id);
    }
    
    public boolean RejectionReasonDataOnDemand.modifyRejectionReason(RejectionReason obj) {
        return false;
    }
    
    public void RejectionReasonDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = RejectionReason.findRejectionReasonEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'RejectionReason' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<RejectionReason>();
        for (int i = 0; i < 10; i++) {
            RejectionReason obj = getNewTransientRejectionReason(i);
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
