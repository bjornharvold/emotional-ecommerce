package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooDataOnDemand(entity = BrandHistory.class)
public class BrandHistoryDataOnDemand {

    @Autowired
    BrandDataOnDemand brandDataOnDemand;

    public void setBrand(BrandHistory obj, int index) {
        Brand brand = brandDataOnDemand.getRandomBrand();
        obj.setBrand(brand);
    }

    public void setAsOfDate(BrandHistory obj, int index) {
        Date asOfDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), index, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND)).getTime();
        obj.setAsOfDate(asOfDate);
    }
}
