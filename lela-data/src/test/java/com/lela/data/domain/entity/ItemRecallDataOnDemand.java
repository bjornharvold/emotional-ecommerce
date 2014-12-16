package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RooDataOnDemand(entity = ItemRecall.class)
public class ItemRecallDataOnDemand {

    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    public void setItem(ItemRecall obj, int index)
    {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }

    public void setRecallDate(ItemRecall obj, int index) {
        Date recallDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), index, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecallDate(recallDate);
    }

}
