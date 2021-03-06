// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.MerchantSource;
import com.lela.data.domain.entity.MerchantSourceDataOnDemand;
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

privileged aspect MerchantSourceDataOnDemand_Roo_DataOnDemand {
    
    declare @type: MerchantSourceDataOnDemand: @Component;
    
    private Random MerchantSourceDataOnDemand.rnd = new SecureRandom();
    
    private List<MerchantSource> MerchantSourceDataOnDemand.data;
    
    public MerchantSource MerchantSourceDataOnDemand.getNewTransientMerchantSource(int index) {
        MerchantSource obj = new MerchantSource();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setMerchantSourceType(obj, index);
        setSourceId(obj, index);
        setSourceName(obj, index);
        return obj;
    }
    
    public void MerchantSourceDataOnDemand.setDateCreated(MerchantSource obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void MerchantSourceDataOnDemand.setDateModified(MerchantSource obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void MerchantSourceDataOnDemand.setSourceId(MerchantSource obj, int index) {
        Integer sourceId = new Integer(index);
        obj.setSourceId(sourceId);
    }
    
    public void MerchantSourceDataOnDemand.setSourceName(MerchantSource obj, int index) {
        String sourceName = "sourceName_" + index;
        obj.setSourceName(sourceName);
    }
    
    public MerchantSource MerchantSourceDataOnDemand.getSpecificMerchantSource(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        MerchantSource obj = data.get(index);
        Long id = obj.getId();
        return MerchantSource.findMerchantSource(id);
    }
    
    public MerchantSource MerchantSourceDataOnDemand.getRandomMerchantSource() {
        init();
        MerchantSource obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return MerchantSource.findMerchantSource(id);
    }
    
    public boolean MerchantSourceDataOnDemand.modifyMerchantSource(MerchantSource obj) {
        return false;
    }
    
    public void MerchantSourceDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = MerchantSource.findMerchantSourceEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'MerchantSource' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<MerchantSource>();
        for (int i = 0; i < 10; i++) {
            MerchantSource obj = getNewTransientMerchantSource(i);
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
