// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Series;
import com.lela.data.domain.entity.SeriesDataOnDemand;
import com.lela.data.domain.entity.SeriesType;
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

privileged aspect SeriesDataOnDemand_Roo_DataOnDemand {
    
    declare @type: SeriesDataOnDemand: @Component;
    
    private Random SeriesDataOnDemand.rnd = new SecureRandom();
    
    private List<Series> SeriesDataOnDemand.data;
    
    @Autowired
    SeriesTypeDataOnDemand SeriesDataOnDemand.seriesTypeDataOnDemand;
    
    public Series SeriesDataOnDemand.getNewTransientSeries(int index) {
        Series obj = new Series();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setSeriesDisplayName(obj, index);
        setSeriesName(obj, index);
        setSeriesType(obj, index);
        setUseThisSeries(obj, index);
        return obj;
    }
    
    public void SeriesDataOnDemand.setDateCreated(Series obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void SeriesDataOnDemand.setDateModified(Series obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void SeriesDataOnDemand.setSeriesDisplayName(Series obj, int index) {
        String seriesDisplayName = "seriesDisplayName_" + index;
        obj.setSeriesDisplayName(seriesDisplayName);
    }
    
    public void SeriesDataOnDemand.setSeriesName(Series obj, int index) {
        String seriesName = "seriesName_" + index;
        obj.setSeriesName(seriesName);
    }
    
    public void SeriesDataOnDemand.setSeriesType(Series obj, int index) {
        SeriesType seriesType = seriesTypeDataOnDemand.getRandomSeriesType();
        obj.setSeriesType(seriesType);
    }
    
    public void SeriesDataOnDemand.setUseThisSeries(Series obj, int index) {
        Boolean useThisSeries = Boolean.TRUE;
        obj.setUseThisSeries(useThisSeries);
    }
    
    public Series SeriesDataOnDemand.getSpecificSeries(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Series obj = data.get(index);
        Long id = obj.getId();
        return Series.findSeries(id);
    }
    
    public Series SeriesDataOnDemand.getRandomSeries() {
        init();
        Series obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Series.findSeries(id);
    }
    
    public boolean SeriesDataOnDemand.modifySeries(Series obj) {
        return false;
    }
    
    public void SeriesDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Series.findSeriesEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Series' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Series>();
        for (int i = 0; i < 10; i++) {
            Series obj = getNewTransientSeries(i);
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
