// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ReviewStatus;
import com.lela.data.domain.entity.ReviewStatusDataOnDemand;
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

privileged aspect ReviewStatusDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ReviewStatusDataOnDemand: @Component;
    
    private Random ReviewStatusDataOnDemand.rnd = new SecureRandom();
    
    private List<ReviewStatus> ReviewStatusDataOnDemand.data;
    
    public ReviewStatus ReviewStatusDataOnDemand.getNewTransientReviewStatus(int index) {
        ReviewStatus obj = new ReviewStatus();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setReviewStatusName(obj, index);
        return obj;
    }
    
    public void ReviewStatusDataOnDemand.setDateCreated(ReviewStatus obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ReviewStatusDataOnDemand.setDateModified(ReviewStatus obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void ReviewStatusDataOnDemand.setReviewStatusName(ReviewStatus obj, int index) {
        String reviewStatusName = "reviewStatusName_" + index;
        obj.setReviewStatusName(reviewStatusName);
    }
    
    public ReviewStatus ReviewStatusDataOnDemand.getSpecificReviewStatus(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ReviewStatus obj = data.get(index);
        Long id = obj.getId();
        return ReviewStatus.findReviewStatus(id);
    }
    
    public ReviewStatus ReviewStatusDataOnDemand.getRandomReviewStatus() {
        init();
        ReviewStatus obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ReviewStatus.findReviewStatus(id);
    }
    
    public boolean ReviewStatusDataOnDemand.modifyReviewStatus(ReviewStatus obj) {
        return false;
    }
    
    public void ReviewStatusDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ReviewStatus.findReviewStatusEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ReviewStatus' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ReviewStatus>();
        for (int i = 0; i < 10; i++) {
            ReviewStatus obj = getNewTransientReviewStatus(i);
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
