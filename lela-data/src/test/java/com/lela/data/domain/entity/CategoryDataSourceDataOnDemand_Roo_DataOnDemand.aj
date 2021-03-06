// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.CategoryDataSource;
import com.lela.data.domain.entity.CategoryDataSourceDataOnDemand;
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

privileged aspect CategoryDataSourceDataOnDemand_Roo_DataOnDemand {
    
    declare @type: CategoryDataSourceDataOnDemand: @Component;
    
    private Random CategoryDataSourceDataOnDemand.rnd = new SecureRandom();
    
    private List<CategoryDataSource> CategoryDataSourceDataOnDemand.data;
    
    public CategoryDataSource CategoryDataSourceDataOnDemand.getNewTransientCategoryDataSource(int index) {
        CategoryDataSource obj = new CategoryDataSource();
        setAllowCategoryChange(obj, index);
        setCanCreate(obj, index);
        setCategory(obj, index);
        setDataSourceString(obj, index);
        setDataSourceType(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setGetAttributesFromSource(obj, index);
        setGetDescriptionsFromSource(obj, index);
        setGetImagesFromSource(obj, index);
        setHasLelaId(obj, index);
        setIsPrimaryFromSource(obj, index);
        setUseCategoryDataSourceMap(obj, index);
        return obj;
    }
    
    public void CategoryDataSourceDataOnDemand.setAllowCategoryChange(CategoryDataSource obj, int index) {
        Boolean allowCategoryChange = Boolean.TRUE;
        obj.setAllowCategoryChange(allowCategoryChange);
    }
    
    public void CategoryDataSourceDataOnDemand.setCanCreate(CategoryDataSource obj, int index) {
        Boolean canCreate = Boolean.TRUE;
        obj.setCanCreate(canCreate);
    }
    
    public void CategoryDataSourceDataOnDemand.setDataSourceString(CategoryDataSource obj, int index) {
        String dataSourceString = "dataSourceString_" + index;
        obj.setDataSourceString(dataSourceString);
    }
    
    public void CategoryDataSourceDataOnDemand.setDateCreated(CategoryDataSource obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void CategoryDataSourceDataOnDemand.setDateModified(CategoryDataSource obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void CategoryDataSourceDataOnDemand.setGetAttributesFromSource(CategoryDataSource obj, int index) {
        Boolean getAttributesFromSource = Boolean.TRUE;
        obj.setGetAttributesFromSource(getAttributesFromSource);
    }
    
    public void CategoryDataSourceDataOnDemand.setGetDescriptionsFromSource(CategoryDataSource obj, int index) {
        Boolean getDescriptionsFromSource = Boolean.TRUE;
        obj.setGetDescriptionsFromSource(getDescriptionsFromSource);
    }
    
    public void CategoryDataSourceDataOnDemand.setGetImagesFromSource(CategoryDataSource obj, int index) {
        Boolean getImagesFromSource = Boolean.TRUE;
        obj.setGetImagesFromSource(getImagesFromSource);
    }
    
    public void CategoryDataSourceDataOnDemand.setHasLelaId(CategoryDataSource obj, int index) {
        Boolean hasLelaId = Boolean.TRUE;
        obj.setHasLelaId(hasLelaId);
    }
    
    public void CategoryDataSourceDataOnDemand.setIsPrimaryFromSource(CategoryDataSource obj, int index) {
        Boolean isPrimaryFromSource = Boolean.TRUE;
        obj.setIsPrimaryFromSource(isPrimaryFromSource);
    }
    
    public void CategoryDataSourceDataOnDemand.setUseCategoryDataSourceMap(CategoryDataSource obj, int index) {
        Boolean useCategoryDataSourceMap = Boolean.TRUE;
        obj.setUseCategoryDataSourceMap(useCategoryDataSourceMap);
    }
    
    public CategoryDataSource CategoryDataSourceDataOnDemand.getSpecificCategoryDataSource(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        CategoryDataSource obj = data.get(index);
        Long id = obj.getId();
        return CategoryDataSource.findCategoryDataSource(id);
    }
    
    public CategoryDataSource CategoryDataSourceDataOnDemand.getRandomCategoryDataSource() {
        init();
        CategoryDataSource obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return CategoryDataSource.findCategoryDataSource(id);
    }
    
    public boolean CategoryDataSourceDataOnDemand.modifyCategoryDataSource(CategoryDataSource obj) {
        return false;
    }
    
    public void CategoryDataSourceDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = CategoryDataSource.findCategoryDataSourceEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'CategoryDataSource' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<CategoryDataSource>();
        for (int i = 0; i < 10; i++) {
            CategoryDataSource obj = getNewTransientCategoryDataSource(i);
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
