// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.BrandCategoryMotivator;
import com.lela.data.domain.entity.BrandCategoryMotivatorDataOnDemand;
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

privileged aspect BrandCategoryMotivatorDataOnDemand_Roo_DataOnDemand {
    
    declare @type: BrandCategoryMotivatorDataOnDemand: @Component;
    
    private Random BrandCategoryMotivatorDataOnDemand.rnd = new SecureRandom();
    
    private List<BrandCategoryMotivator> BrandCategoryMotivatorDataOnDemand.data;
    
    public BrandCategoryMotivator BrandCategoryMotivatorDataOnDemand.getNewTransientBrandCategoryMotivator(int index) {
        BrandCategoryMotivator obj = new BrandCategoryMotivator();
        setBrand(obj, index);
        setCategory(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setMotivator(obj, index);
        setMotivatorScore(obj, index);
        return obj;
    }
    
    public void BrandCategoryMotivatorDataOnDemand.setDateCreated(BrandCategoryMotivator obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void BrandCategoryMotivatorDataOnDemand.setDateModified(BrandCategoryMotivator obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void BrandCategoryMotivatorDataOnDemand.setMotivator(BrandCategoryMotivator obj, int index) {
        Integer motivator = new Integer(index);
        obj.setMotivator(motivator);
    }
    
    public void BrandCategoryMotivatorDataOnDemand.setMotivatorScore(BrandCategoryMotivator obj, int index) {
        Integer motivatorScore = new Integer(index);
        obj.setMotivatorScore(motivatorScore);
    }
    
    public BrandCategoryMotivator BrandCategoryMotivatorDataOnDemand.getSpecificBrandCategoryMotivator(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        BrandCategoryMotivator obj = data.get(index);
        Long id = obj.getId();
        return BrandCategoryMotivator.findBrandCategoryMotivator(id);
    }
    
    public BrandCategoryMotivator BrandCategoryMotivatorDataOnDemand.getRandomBrandCategoryMotivator() {
        init();
        BrandCategoryMotivator obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return BrandCategoryMotivator.findBrandCategoryMotivator(id);
    }
    
    public boolean BrandCategoryMotivatorDataOnDemand.modifyBrandCategoryMotivator(BrandCategoryMotivator obj) {
        return false;
    }
    
    public void BrandCategoryMotivatorDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = BrandCategoryMotivator.findBrandCategoryMotivatorEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'BrandCategoryMotivator' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<BrandCategoryMotivator>();
        for (int i = 0; i < 10; i++) {
            BrandCategoryMotivator obj = getNewTransientBrandCategoryMotivator(i);
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