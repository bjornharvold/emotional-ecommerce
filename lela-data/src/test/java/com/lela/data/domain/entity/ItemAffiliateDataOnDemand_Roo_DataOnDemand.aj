// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Affiliate;
import com.lela.data.domain.entity.AffiliateDataOnDemand;
import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemAffiliate;
import com.lela.data.domain.entity.ItemAffiliateDataOnDemand;
import com.lela.data.domain.entity.ItemDataOnDemand;
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

privileged aspect ItemAffiliateDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ItemAffiliateDataOnDemand: @Component;
    
    private Random ItemAffiliateDataOnDemand.rnd = new SecureRandom();
    
    private List<ItemAffiliate> ItemAffiliateDataOnDemand.data;
    
    @Autowired
    AffiliateDataOnDemand ItemAffiliateDataOnDemand.affiliateDataOnDemand;
    
    @Autowired
    ItemDataOnDemand ItemAffiliateDataOnDemand.itemDataOnDemand;
    
    public ItemAffiliate ItemAffiliateDataOnDemand.getNewTransientItemAffiliate(int index) {
        ItemAffiliate obj = new ItemAffiliate();
        setAffiliate(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setUniqueItemAffiliateId(obj, index);
        setUniqueItemLelaId(obj, index);
        return obj;
    }
    
    public void ItemAffiliateDataOnDemand.setAffiliate(ItemAffiliate obj, int index) {
        Affiliate affiliate = affiliateDataOnDemand.getRandomAffiliate();
        obj.setAffiliate(affiliate);
    }
    
    public void ItemAffiliateDataOnDemand.setDateCreated(ItemAffiliate obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ItemAffiliateDataOnDemand.setDateModified(ItemAffiliate obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void ItemAffiliateDataOnDemand.setUniqueItemAffiliateId(ItemAffiliate obj, int index) {
        Item uniqueItemAffiliateId = itemDataOnDemand.getRandomItem();
        obj.setUniqueItemAffiliateId(uniqueItemAffiliateId);
    }
    
    public void ItemAffiliateDataOnDemand.setUniqueItemLelaId(ItemAffiliate obj, int index) {
        Item uniqueItemLelaId = itemDataOnDemand.getRandomItem();
        obj.setUniqueItemLelaId(uniqueItemLelaId);
    }
    
    public ItemAffiliate ItemAffiliateDataOnDemand.getSpecificItemAffiliate(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ItemAffiliate obj = data.get(index);
        Long id = obj.getId();
        return ItemAffiliate.findItemAffiliate(id);
    }
    
    public ItemAffiliate ItemAffiliateDataOnDemand.getRandomItemAffiliate() {
        init();
        ItemAffiliate obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ItemAffiliate.findItemAffiliate(id);
    }
    
    public boolean ItemAffiliateDataOnDemand.modifyItemAffiliate(ItemAffiliate obj) {
        return false;
    }
    
    public void ItemAffiliateDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ItemAffiliate.findItemAffiliateEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ItemAffiliate' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ItemAffiliate>();
        for (int i = 0; i < 10; i++) {
            ItemAffiliate obj = getNewTransientItemAffiliate(i);
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
