// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AttributeTypeDataOnDemand;
import com.lela.data.domain.entity.CategoryDataOnDemand;
import com.lela.data.domain.entity.SeriesAttributeMap;
import com.lela.data.domain.entity.SeriesAttributeMapDataOnDemand;
import com.lela.data.domain.entity.SeriesTypeDataOnDemand;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect SeriesAttributeMapDataOnDemand_Roo_DataOnDemand {
    
    declare @type: SeriesAttributeMapDataOnDemand: @Component;
    
    private Random SeriesAttributeMapDataOnDemand.rnd = new SecureRandom();
    
    private List<SeriesAttributeMap> SeriesAttributeMapDataOnDemand.data;
    
    @Autowired
    AttributeTypeDataOnDemand SeriesAttributeMapDataOnDemand.attributeTypeDataOnDemand;
    
    @Autowired
    CategoryDataOnDemand SeriesAttributeMapDataOnDemand.categoryDataOnDemand;
    
    @Autowired
    SeriesTypeDataOnDemand SeriesAttributeMapDataOnDemand.seriesTypeDataOnDemand;
    
    public SeriesAttributeMap SeriesAttributeMapDataOnDemand.getNewTransientSeriesAttributeMap(int index) {
        SeriesAttributeMap obj = new SeriesAttributeMap();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        return obj;
    }
    
    public void SeriesAttributeMapDataOnDemand.setDateCreated(SeriesAttributeMap obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void SeriesAttributeMapDataOnDemand.setDateModified(SeriesAttributeMap obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public SeriesAttributeMap SeriesAttributeMapDataOnDemand.getSpecificSeriesAttributeMap(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        SeriesAttributeMap obj = data.get(index);
        Long id = obj.getId();
        return SeriesAttributeMap.findSeriesAttributeMap(id);
    }
    
    public SeriesAttributeMap SeriesAttributeMapDataOnDemand.getRandomSeriesAttributeMap() {
        init();
        SeriesAttributeMap obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return SeriesAttributeMap.findSeriesAttributeMap(id);
    }
    
    public boolean SeriesAttributeMapDataOnDemand.modifySeriesAttributeMap(SeriesAttributeMap obj) {
        return false;
    }
    
    public void SeriesAttributeMapDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = SeriesAttributeMap.findSeriesAttributeMapEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'SeriesAttributeMap' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<SeriesAttributeMap>();
        for (int i = 0; i < 10; i++) {
            SeriesAttributeMap obj = getNewTransientSeriesAttributeMap(i);
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
