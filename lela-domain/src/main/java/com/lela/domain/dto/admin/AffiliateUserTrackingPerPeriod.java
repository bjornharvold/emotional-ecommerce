/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 8/13/12
 * Time: 3:18 PM
 */
public class AffiliateUserTrackingPerPeriod {

    private Date period;

    private Integer unique;
    private Integer visits;
    private Integer pages;
    private Integer returned;
    private Integer bounced;
    private Integer stores;
    private Integer purchases;

    private Double pagesPerVisit;
    private Double bounceRate;
    private Double returnedRate;
    private Double sales;
    private Double commission;
    
    private HashMap<String, Double> quizRate;
    private HashMap<String, Double> quizStartDayAvg;
    private HashMap<String, Double> quizCompleteDayAvg;
    private HashMap<String, Map<String, Double>> regRate;
    private HashMap<String, Map<String, Double>> regStartDayAvg;
    private HashMap<String, Map<String, Double>> regCompleteDayAvg;

    private HashMap<String, Double> quizStart;
    private HashMap<String, Double> quizComplete;
    private Integer regStartTotal;
    private Double regStartTotalDayAvg;
    private HashMap<String, Map<String, Double>> regStart;
    private Integer regCompleteTotal;
    private Double regCompleteTotalDayAvg;
    private HashMap<String, Map<String, Double>> regComplete;
    private Double regRateTotal;

    private Double pagesPerVisitDayAvg;
    private Double uniqueDayAvg;
    private Double visitsDayAvg;
    private Double pagesDayAvg;
    private Double returnedDayAvg;
    private Double storesDayAvg;
    private Double purchasesDayAvg;
    private Double salesDayAvg;
    private Double commissionDayAvg;

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public Integer getUnique() {
        return unique;
    }

    public void setUnique(Integer unique) {
        this.unique = unique;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getReturned() {
        return returned;
    }

    public void setReturned(Integer returned) {
        this.returned = returned;
    }

    public Double getReturnedRate() {
        return returnedRate;
    }

    public void setReturnedRate(Double returnedRate) {
        this.returnedRate = returnedRate;
    }

    public Double getSales() {
        return this.sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public Double getCommission() {
        return this.commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Integer getBounced() {
        return bounced;
    }

    public void setBounced(Integer bounced) {
        this.bounced = bounced;
    }

    public Double getBounceRate() {
        return bounceRate;
    }

    public void setBounceRate(Double bounceRate) {
        this.bounceRate = bounceRate;
    }

    public Integer getStores() {
        return stores;
    }

    public void setStores(Integer stores) {
        this.stores = stores;
    }

    public Integer getPurchases() {
        return this.purchases;
    }

    public void setPurchases(Integer purchases) {
        this.purchases = purchases;
    }

    public Double getPagesPerVisit() {
        return pagesPerVisit;
    }

    public void setPagesPerVisit(Double pagesPerVisit) {
        this.pagesPerVisit = pagesPerVisit;
    }

    public HashMap<String, Double> getQuizRate() {
        return quizRate;
    }

    public void setQuizRate(HashMap<String, Double> quizRate) {
        this.quizRate = quizRate;
    }

    public HashMap<String, Map<String, Double>> getRegRate() {
        return regRate;
    }

    public void setRegRate(HashMap<String, Map<String, Double>> regRate) {
        this.regRate = regRate;
    }

    public HashMap<String, Double> getQuizStart() {
        return quizStart;
    }

    public void setQuizStart(HashMap<String, Double> quizStart) {
        this.quizStart = quizStart;
    }

    public HashMap<String, Double> getQuizComplete() {
        return quizComplete;
    }

    public void setQuizComplete(HashMap<String, Double> quizComplete) {
        this.quizComplete = quizComplete;
    }

    public HashMap<String, Map<String, Double>> getRegStart() {
        return regStart;
    }

    public void setRegStart(HashMap<String, Map<String, Double>> regStart) {
        this.regStart = regStart;
    }

    public HashMap<String, Map<String, Double>> getRegComplete() {
        return regComplete;
    }

    public void setRegComplete(HashMap<String, Map<String, Double>> regComplete) {
        this.regComplete = regComplete;
    }

    public Integer getRegStartTotal() {
        return regStartTotal;
    }

    public void setRegStartTotal(Integer regStartTotal) {
        this.regStartTotal = regStartTotal;
    }

    public Integer getRegCompleteTotal() {
        return regCompleteTotal;
    }

    public void setRegCompleteTotal(Integer regCompleteTotal) {
        this.regCompleteTotal = regCompleteTotal;
    }

    public Double getRegRateTotal() {
        return regRateTotal;
    }

    public void setRegRateTotal(Double regRateTotal) {
        this.regRateTotal = regRateTotal;
    }

    public Double getPagesPerVisitDayAvg() {
        return pagesPerVisitDayAvg;
    }

    public void setPagesPerVisitDayAvg(Double pagesPerVisitDayAvg) {
        this.pagesPerVisitDayAvg = pagesPerVisitDayAvg;
    }

    public Double getUniqueDayAvg() {
        return uniqueDayAvg;
    }

    public void setUniqueDayAvg(Double uniqueDayAvg) {
        this.uniqueDayAvg = uniqueDayAvg;
    }

    public Double getVisitsDayAvg() {
        return visitsDayAvg;
    }

    public void setVisitsDayAvg(Double visitsDayAvg) {
        this.visitsDayAvg = visitsDayAvg;
    }

    public Double getPagesDayAvg() {
        return pagesDayAvg;
    }

    public void setPagesDayAvg(Double pagesDayAvg) {
        this.pagesDayAvg = pagesDayAvg;
    }

    public Double getReturnedDayAvg() {
        return returnedDayAvg;
    }

    public void setReturnedDayAvg(Double returnedDayAvg) {
        this.returnedDayAvg = returnedDayAvg;
    }

    public Double getStoresDayAvg() {
        return storesDayAvg;
    }

    public void setStoresDayAvg(Double storesDayAvg) {
        this.storesDayAvg = storesDayAvg;
    }

    public Double getPurchasesDayAvg() {
        return purchasesDayAvg;
    }

    public void setPurchasesDayAvg(Double purchasesDayAvg) {
        this.purchasesDayAvg = purchasesDayAvg;
    }

    public Double getSalesDayAvg() {
        return salesDayAvg;
    }

    public void setSalesDayAvg(Double salesDayAvg) {
        this.salesDayAvg = salesDayAvg;
    }

    public Double getCommissionDayAvg() {
        return commissionDayAvg;
    }

    public void setCommissionDayAvg(Double commissionDayAvg) {
        this.commissionDayAvg = commissionDayAvg;
    }

    public HashMap<String, Double> getQuizStartDayAvg() {
        return quizStartDayAvg;
    }

    public void setQuizStartDayAvg(HashMap<String, Double> quizStartDayAvg) {
        this.quizStartDayAvg = quizStartDayAvg;
    }

    public HashMap<String, Double> getQuizCompleteDayAvg() {
        return quizCompleteDayAvg;
    }

    public void setQuizCompleteDayAvg(HashMap<String, Double> quizCompleteDayAvg) {
        this.quizCompleteDayAvg = quizCompleteDayAvg;
    }

    public HashMap<String, Map<String, Double>> getRegStartDayAvg() {
        return regStartDayAvg;
    }

    public void setRegStartDayAvg(HashMap<String, Map<String, Double>> regStartDayAvg) {
        this.regStartDayAvg = regStartDayAvg;
    }

    public HashMap<String, Map<String, Double>> getRegCompleteDayAvg() {
        return regCompleteDayAvg;
    }

    public void setRegCompleteDayAvg(HashMap<String, Map<String, Double>> regCompleteDayAvg) {
        this.regCompleteDayAvg = regCompleteDayAvg;
    }

    public Double getRegStartTotalDayAvg() {
        return regStartTotalDayAvg;
    }

    public void setRegStartTotalDayAvg(Double regStartTotalDayAvg) {
        this.regStartTotalDayAvg = regStartTotalDayAvg;
    }

    public Double getRegCompleteTotalDayAvg() {
        return regCompleteTotalDayAvg;
    }

    public void setRegCompleteTotalDayAvg(Double regCompleteTotalDayAvg) {
        this.regCompleteTotalDayAvg = regCompleteTotalDayAvg;
    }
}
