// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemDataOnDemand;
import com.lela.data.domain.entity.ItemReview;
import com.lela.data.domain.entity.ItemReviewDataOnDemand;
import com.lela.data.domain.entity.ReviewProvider;
import com.lela.data.domain.entity.ReviewProviderDataOnDemand;
import com.lela.data.domain.entity.ReviewSource;
import com.lela.data.domain.entity.ReviewSourceDataOnDemand;
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

privileged aspect ItemReviewDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ItemReviewDataOnDemand: @Component;
    
    private Random ItemReviewDataOnDemand.rnd = new SecureRandom();
    
    private List<ItemReview> ItemReviewDataOnDemand.data;
    
    @Autowired
    ItemDataOnDemand ItemReviewDataOnDemand.itemDataOnDemand;
    
    @Autowired
    ReviewProviderDataOnDemand ItemReviewDataOnDemand.reviewProviderDataOnDemand;
    
    @Autowired
    ReviewSourceDataOnDemand ItemReviewDataOnDemand.reviewSourceDataOnDemand;
    
    public ItemReview ItemReviewDataOnDemand.getNewTransientItemReview(int index) {
        ItemReview obj = new ItemReview();
        setAward(obj, index);
        setAwardImage(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setItem(obj, index);
        setReviewAuthor(obj, index);
        setReviewLanguage(obj, index);
        setReviewProvider(obj, index);
        setReviewProviderInternalId(obj, index);
        setReviewScore(obj, index);
        setReviewSource(obj, index);
        setReviewSummary(obj, index);
        setReviewTitle(obj, index);
        setReviewUrl(obj, index);
        setReviewVerdict(obj, index);
        setTestCons(obj, index);
        setTestDate(obj, index);
        setTestPros(obj, index);
        return obj;
    }
    
    public void ItemReviewDataOnDemand.setAward(ItemReview obj, int index) {
        String award = "award_" + index;
        obj.setAward(award);
    }
    
    public void ItemReviewDataOnDemand.setAwardImage(ItemReview obj, int index) {
        String awardImage = "awardImage_" + index;
        obj.setAwardImage(awardImage);
    }
    
    public void ItemReviewDataOnDemand.setDateCreated(ItemReview obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ItemReviewDataOnDemand.setDateModified(ItemReview obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void ItemReviewDataOnDemand.setItem(ItemReview obj, int index) {
        Item item = itemDataOnDemand.getRandomItem();
        obj.setItem(item);
    }
    
    public void ItemReviewDataOnDemand.setReviewAuthor(ItemReview obj, int index) {
        String reviewAuthor = "reviewAuthor_" + index;
        obj.setReviewAuthor(reviewAuthor);
    }
    
    public void ItemReviewDataOnDemand.setReviewLanguage(ItemReview obj, int index) {
        String reviewLanguage = "reviewLanguage_" + index;
        obj.setReviewLanguage(reviewLanguage);
    }
    
    public void ItemReviewDataOnDemand.setReviewProvider(ItemReview obj, int index) {
        ReviewProvider reviewProvider = reviewProviderDataOnDemand.getRandomReviewProvider();
        obj.setReviewProvider(reviewProvider);
    }
    
    public void ItemReviewDataOnDemand.setReviewProviderInternalId(ItemReview obj, int index) {
        String reviewProviderInternalId = "reviewProviderInternalId_" + index;
        obj.setReviewProviderInternalId(reviewProviderInternalId);
    }
    
    public void ItemReviewDataOnDemand.setReviewScore(ItemReview obj, int index) {
        Integer reviewScore = new Integer(index);
        obj.setReviewScore(reviewScore);
    }
    
    public void ItemReviewDataOnDemand.setReviewSource(ItemReview obj, int index) {
        ReviewSource reviewSource = reviewSourceDataOnDemand.getRandomReviewSource();
        obj.setReviewSource(reviewSource);
    }
    
    public void ItemReviewDataOnDemand.setReviewSummary(ItemReview obj, int index) {
        String reviewSummary = "reviewSummary_" + index;
        if (reviewSummary.length() > 1024) {
            reviewSummary = reviewSummary.substring(0, 1024);
        }
        obj.setReviewSummary(reviewSummary);
    }
    
    public void ItemReviewDataOnDemand.setReviewTitle(ItemReview obj, int index) {
        String reviewTitle = "reviewTitle_" + index;
        if (reviewTitle.length() > 1024) {
            reviewTitle = reviewTitle.substring(0, 1024);
        }
        obj.setReviewTitle(reviewTitle);
    }
    
    public void ItemReviewDataOnDemand.setReviewUrl(ItemReview obj, int index) {
        String reviewUrl = "reviewUrl_" + index;
        obj.setReviewUrl(reviewUrl);
    }
    
    public void ItemReviewDataOnDemand.setReviewVerdict(ItemReview obj, int index) {
        String reviewVerdict = "reviewVerdict_" + index;
        if (reviewVerdict.length() > 1024) {
            reviewVerdict = reviewVerdict.substring(0, 1024);
        }
        obj.setReviewVerdict(reviewVerdict);
    }
    
    public void ItemReviewDataOnDemand.setTestCons(ItemReview obj, int index) {
        String testCons = "testCons_" + index;
        if (testCons.length() > 1024) {
            testCons = testCons.substring(0, 1024);
        }
        obj.setTestCons(testCons);
    }
    
    public void ItemReviewDataOnDemand.setTestDate(ItemReview obj, int index) {
        Date testDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setTestDate(testDate);
    }
    
    public void ItemReviewDataOnDemand.setTestPros(ItemReview obj, int index) {
        String testPros = "testPros_" + index;
        if (testPros.length() > 1024) {
            testPros = testPros.substring(0, 1024);
        }
        obj.setTestPros(testPros);
    }
    
    public ItemReview ItemReviewDataOnDemand.getSpecificItemReview(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ItemReview obj = data.get(index);
        Long id = obj.getId();
        return ItemReview.findItemReview(id);
    }
    
    public ItemReview ItemReviewDataOnDemand.getRandomItemReview() {
        init();
        ItemReview obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ItemReview.findItemReview(id);
    }
    
    public boolean ItemReviewDataOnDemand.modifyItemReview(ItemReview obj) {
        return false;
    }
    
    public void ItemReviewDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ItemReview.findItemReviewEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ItemReview' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ItemReview>();
        for (int i = 0; i < 10; i++) {
            ItemReview obj = getNewTransientItemReview(i);
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
