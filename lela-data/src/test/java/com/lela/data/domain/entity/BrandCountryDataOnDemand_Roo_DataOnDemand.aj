// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.BrandCountry;
import com.lela.data.domain.entity.BrandCountryDataOnDemand;
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

privileged aspect BrandCountryDataOnDemand_Roo_DataOnDemand {
    
    declare @type: BrandCountryDataOnDemand: @Component;
    
    private Random BrandCountryDataOnDemand.rnd = new SecureRandom();
    
    private List<BrandCountry> BrandCountryDataOnDemand.data;
    
    public BrandCountry BrandCountryDataOnDemand.getNewTransientBrandCountry(int index) {
        BrandCountry obj = new BrandCountry();
        setAvailable(obj, index);
        setBrand(obj, index);
        setBrandName(obj, index);
        setCountry(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        return obj;
    }
    
    public void BrandCountryDataOnDemand.setAvailable(BrandCountry obj, int index) {
        Boolean available = Boolean.TRUE;
        obj.setAvailable(available);
    }
    
    public void BrandCountryDataOnDemand.setBrandName(BrandCountry obj, int index) {
        String brandName = "brandName_" + index;
        obj.setBrandName(brandName);
    }
    
    public void BrandCountryDataOnDemand.setDateCreated(BrandCountry obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void BrandCountryDataOnDemand.setDateModified(BrandCountry obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public BrandCountry BrandCountryDataOnDemand.getSpecificBrandCountry(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        BrandCountry obj = data.get(index);
        Long id = obj.getId();
        return BrandCountry.findBrandCountry(id);
    }
    
    public BrandCountry BrandCountryDataOnDemand.getRandomBrandCountry() {
        init();
        BrandCountry obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return BrandCountry.findBrandCountry(id);
    }
    
    public boolean BrandCountryDataOnDemand.modifyBrandCountry(BrandCountry obj) {
        return false;
    }
    
    public void BrandCountryDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = BrandCountry.findBrandCountryEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'BrandCountry' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<BrandCountry>();
        for (int i = 0; i < 10; i++) {
            BrandCountry obj = getNewTransientBrandCountry(i);
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
