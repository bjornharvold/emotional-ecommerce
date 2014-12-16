// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AffiliateTransaction;
import com.lela.data.domain.entity.AffiliateTransactionDataOnDemand;
import java.math.BigDecimal;
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

privileged aspect AffiliateTransactionDataOnDemand_Roo_DataOnDemand {
    
    declare @type: AffiliateTransactionDataOnDemand: @Component;
    
    private Random AffiliateTransactionDataOnDemand.rnd = new SecureRandom();
    
    private List<AffiliateTransaction> AffiliateTransactionDataOnDemand.data;
    
    public AffiliateTransaction AffiliateTransactionDataOnDemand.getNewTransientAffiliateTransaction(int index) {
        AffiliateTransaction obj = new AffiliateTransaction();
        setAdvertiserName(obj, index);
        setAffiliateReport(obj, index);
        setAssociation(obj, index);
        setClickDate(obj, index);
        setCommissionAmount(obj, index);
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setEmail(obj, index);
        setEventDate(obj, index);
        setFirstName(obj, index);
        setLastName(obj, index);
        setNetwork(obj, index);
        setOrderId(obj, index);
        setOrganizationId(obj, index);
        setProcessDate(obj, index);
        setProductCategory(obj, index);
        setProductId(obj, index);
        setProductName(obj, index);
        setProvider(obj, index);
        setProviderId(obj, index);
        setQuantity(obj, index);
        setRedirectId(obj, index);
        setRedirectUrl(obj, index);
        setReferralDate(obj, index);
        setReferringProductId(obj, index);
        setRevenueId(obj, index);
        setSalesAmount(obj, index);
        setSubId(obj, index);
        setSyncdMongo(obj, index);
        setTrackingId(obj, index);
        setTransactionDate(obj, index);
        setUserId(obj, index);
        return obj;
    }
    
    public void AffiliateTransactionDataOnDemand.setAdvertiserName(AffiliateTransaction obj, int index) {
        String advertiserName = "advertiserName_" + index;
        obj.setAdvertiserName(advertiserName);
    }
    
    public void AffiliateTransactionDataOnDemand.setAssociation(AffiliateTransaction obj, int index) {
        String association = "association_" + index;
        obj.setAssociation(association);
    }
    
    public void AffiliateTransactionDataOnDemand.setClickDate(AffiliateTransaction obj, int index) {
        Date clickDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setClickDate(clickDate);
    }
    
    public void AffiliateTransactionDataOnDemand.setCommissionAmount(AffiliateTransaction obj, int index) {
        BigDecimal commissionAmount = BigDecimal.valueOf(index);
        obj.setCommissionAmount(commissionAmount);
    }
    
    public void AffiliateTransactionDataOnDemand.setDateCreated(AffiliateTransaction obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void AffiliateTransactionDataOnDemand.setDateModified(AffiliateTransaction obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void AffiliateTransactionDataOnDemand.setEmail(AffiliateTransaction obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }
    
    public void AffiliateTransactionDataOnDemand.setEventDate(AffiliateTransaction obj, int index) {
        Date eventDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setEventDate(eventDate);
    }
    
    public void AffiliateTransactionDataOnDemand.setFirstName(AffiliateTransaction obj, int index) {
        String firstName = "firstName_" + index;
        obj.setFirstName(firstName);
    }
    
    public void AffiliateTransactionDataOnDemand.setLastName(AffiliateTransaction obj, int index) {
        String lastName = "lastName_" + index;
        obj.setLastName(lastName);
    }
    
    public void AffiliateTransactionDataOnDemand.setNetwork(AffiliateTransaction obj, int index) {
        String network = "network_" + index;
        obj.setNetwork(network);
    }
    
    public void AffiliateTransactionDataOnDemand.setOrderId(AffiliateTransaction obj, int index) {
        String orderId = "orderId_" + index;
        obj.setOrderId(orderId);
    }
    
    public void AffiliateTransactionDataOnDemand.setOrganizationId(AffiliateTransaction obj, int index) {
        Integer organizationId = new Integer(index);
        obj.setOrganizationId(organizationId);
    }
    
    public void AffiliateTransactionDataOnDemand.setProcessDate(AffiliateTransaction obj, int index) {
        Date processDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setProcessDate(processDate);
    }
    
    public void AffiliateTransactionDataOnDemand.setProductCategory(AffiliateTransaction obj, int index) {
        String productCategory = "productCategory_" + index;
        obj.setProductCategory(productCategory);
    }
    
    public void AffiliateTransactionDataOnDemand.setProductId(AffiliateTransaction obj, int index) {
        String productId = "productId_" + index;
        obj.setProductId(productId);
    }
    
    public void AffiliateTransactionDataOnDemand.setProductName(AffiliateTransaction obj, int index) {
        String productName = "productName_" + index;
        obj.setProductName(productName);
    }
    
    public void AffiliateTransactionDataOnDemand.setProvider(AffiliateTransaction obj, int index) {
        String provider = "provider_" + index;
        obj.setProvider(provider);
    }
    
    public void AffiliateTransactionDataOnDemand.setProviderId(AffiliateTransaction obj, int index) {
        Integer providerId = new Integer(index);
        obj.setProviderId(providerId);
    }
    
    public void AffiliateTransactionDataOnDemand.setQuantity(AffiliateTransaction obj, int index) {
        Integer quantity = new Integer(index);
        obj.setQuantity(quantity);
    }
    
    public void AffiliateTransactionDataOnDemand.setRedirectId(AffiliateTransaction obj, int index) {
        String redirectId = "redirectId_" + index;
        obj.setRedirectId(redirectId);
    }
    
    public void AffiliateTransactionDataOnDemand.setRedirectUrl(AffiliateTransaction obj, int index) {
        String redirectUrl = "redirectUrl_" + index;
        obj.setRedirectUrl(redirectUrl);
    }
    
    public void AffiliateTransactionDataOnDemand.setReferralDate(AffiliateTransaction obj, int index) {
        Date referralDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setReferralDate(referralDate);
    }
    
    public void AffiliateTransactionDataOnDemand.setReferringProductId(AffiliateTransaction obj, int index) {
        String referringProductId = "referringProductId_" + index;
        obj.setReferringProductId(referringProductId);
    }
    
    public void AffiliateTransactionDataOnDemand.setRevenueId(AffiliateTransaction obj, int index) {
        Long revenueId = new Integer(index).longValue();
        obj.setRevenueId(revenueId);
    }
    
    public void AffiliateTransactionDataOnDemand.setSalesAmount(AffiliateTransaction obj, int index) {
        BigDecimal salesAmount = BigDecimal.valueOf(index);
        obj.setSalesAmount(salesAmount);
    }
    
    public void AffiliateTransactionDataOnDemand.setSubId(AffiliateTransaction obj, int index) {
        String subId = "subId_" + index;
        obj.setSubId(subId);
    }
    
    public void AffiliateTransactionDataOnDemand.setSyncdMongo(AffiliateTransaction obj, int index) {
        Boolean syncdMongo = Boolean.TRUE;
        obj.setSyncdMongo(syncdMongo);
    }
    
    public void AffiliateTransactionDataOnDemand.setTrackingId(AffiliateTransaction obj, int index) {
        String trackingId = "trackingId_" + index;
        obj.setTrackingId(trackingId);
    }
    
    public void AffiliateTransactionDataOnDemand.setTransactionDate(AffiliateTransaction obj, int index) {
        Date transactionDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setTransactionDate(transactionDate);
    }
    
    public void AffiliateTransactionDataOnDemand.setUserId(AffiliateTransaction obj, int index) {
        String userId = "userId_" + index;
        obj.setUserId(userId);
    }
    
    public AffiliateTransaction AffiliateTransactionDataOnDemand.getSpecificAffiliateTransaction(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        AffiliateTransaction obj = data.get(index);
        Long id = obj.getId();
        return AffiliateTransaction.findAffiliateTransaction(id);
    }
    
    public AffiliateTransaction AffiliateTransactionDataOnDemand.getRandomAffiliateTransaction() {
        init();
        AffiliateTransaction obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return AffiliateTransaction.findAffiliateTransaction(id);
    }
    
    public boolean AffiliateTransactionDataOnDemand.modifyAffiliateTransaction(AffiliateTransaction obj) {
        return false;
    }
    
    public void AffiliateTransactionDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = AffiliateTransaction.findAffiliateTransactionEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'AffiliateTransaction' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<AffiliateTransaction>();
        for (int i = 0; i < 10; i++) {
            AffiliateTransaction obj = getNewTransientAffiliateTransaction(i);
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
