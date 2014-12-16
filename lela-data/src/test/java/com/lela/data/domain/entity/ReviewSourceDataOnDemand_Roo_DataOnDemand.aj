// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

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

privileged aspect ReviewSourceDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ReviewSourceDataOnDemand: @Component;
    
    private Random ReviewSourceDataOnDemand.rnd = new SecureRandom();
    
    private List<ReviewSource> ReviewSourceDataOnDemand.data;
    
    @Autowired
    ReviewProviderDataOnDemand ReviewSourceDataOnDemand.reviewProviderDataOnDemand;
    
    public ReviewSource ReviewSourceDataOnDemand.getNewTransientReviewSource(int index) {
        ReviewSource obj = new ReviewSource();
        setCountryCode(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setLanguageCode(obj, index);
        setReviewProvider(obj, index);
        setReviewProviderSourceId(obj, index);
        setReviewSourceName(obj, index);
        setSourceLogo(obj, index);
        setSourceLogoHeight(obj, index);
        setSourceLogoWidth(obj, index);
        setSourcePictureRequired(obj, index);
        setSourceRank(obj, index);
        setSourceURLRequired(obj, index);
        setSourceWWW(obj, index);
        return obj;
    }
    
    public void ReviewSourceDataOnDemand.setCountryCode(ReviewSource obj, int index) {
        String countryCode = "countryCode_" + index;
        obj.setCountryCode(countryCode);
    }
    
    public void ReviewSourceDataOnDemand.setDateCreated(ReviewSource obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void ReviewSourceDataOnDemand.setDateModified(ReviewSource obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void ReviewSourceDataOnDemand.setLanguageCode(ReviewSource obj, int index) {
        String languageCode = "languageCode_" + index;
        obj.setLanguageCode(languageCode);
    }
    
    public void ReviewSourceDataOnDemand.setReviewProvider(ReviewSource obj, int index) {
        ReviewProvider reviewProvider = reviewProviderDataOnDemand.getRandomReviewProvider();
        obj.setReviewProvider(reviewProvider);
    }
    
    public void ReviewSourceDataOnDemand.setReviewProviderSourceId(ReviewSource obj, int index) {
        Integer reviewProviderSourceId = new Integer(index);
        obj.setReviewProviderSourceId(reviewProviderSourceId);
    }
    
    public void ReviewSourceDataOnDemand.setReviewSourceName(ReviewSource obj, int index) {
        String reviewSourceName = "reviewSourceName_" + index;
        obj.setReviewSourceName(reviewSourceName);
    }
    
    public void ReviewSourceDataOnDemand.setSourceLogo(ReviewSource obj, int index) {
        String sourceLogo = "sourceLogo_" + index;
        obj.setSourceLogo(sourceLogo);
    }
    
    public void ReviewSourceDataOnDemand.setSourceLogoHeight(ReviewSource obj, int index) {
        Integer sourceLogoHeight = new Integer(index);
        obj.setSourceLogoHeight(sourceLogoHeight);
    }
    
    public void ReviewSourceDataOnDemand.setSourceLogoWidth(ReviewSource obj, int index) {
        Integer sourceLogoWidth = new Integer(index);
        obj.setSourceLogoWidth(sourceLogoWidth);
    }
    
    public void ReviewSourceDataOnDemand.setSourcePictureRequired(ReviewSource obj, int index) {
        Boolean sourcePictureRequired = Boolean.TRUE;
        obj.setSourcePictureRequired(sourcePictureRequired);
    }
    
    public void ReviewSourceDataOnDemand.setSourceRank(ReviewSource obj, int index) {
        Integer sourceRank = new Integer(index);
        obj.setSourceRank(sourceRank);
    }
    
    public void ReviewSourceDataOnDemand.setSourceURLRequired(ReviewSource obj, int index) {
        Boolean sourceURLRequired = Boolean.TRUE;
        obj.setSourceURLRequired(sourceURLRequired);
    }
    
    public void ReviewSourceDataOnDemand.setSourceWWW(ReviewSource obj, int index) {
        String sourceWWW = "sourceWWW_" + index;
        obj.setSourceWWW(sourceWWW);
    }
    
    public ReviewSource ReviewSourceDataOnDemand.getSpecificReviewSource(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ReviewSource obj = data.get(index);
        Long id = obj.getId();
        return ReviewSource.findReviewSource(id);
    }
    
    public ReviewSource ReviewSourceDataOnDemand.getRandomReviewSource() {
        init();
        ReviewSource obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ReviewSource.findReviewSource(id);
    }
    
    public boolean ReviewSourceDataOnDemand.modifyReviewSource(ReviewSource obj) {
        return false;
    }
    
    public void ReviewSourceDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ReviewSource.findReviewSourceEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ReviewSource' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ReviewSource>();
        for (int i = 0; i < 10; i++) {
            ReviewSource obj = getNewTransientReviewSource(i);
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
