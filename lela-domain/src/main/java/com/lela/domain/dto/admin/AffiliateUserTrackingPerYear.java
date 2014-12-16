package com.lela.domain.dto.admin;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 8/13/12
 * Time: 4:48 PM
 */
public class AffiliateUserTrackingPerYear extends AffiliateUserTrackingPerPeriod {
    private Double pagesPerVisitMonthAvg;
    private Double uniqueMonthAvg;
    private Double visitsMonthAvg;
    private Double pagesMonthAvg;
    private Double returnedMonthAvg;
    private Double storesMonthAvg;
    private Double purchasesMonthAvg;
    private Double salesMonthAvg;
    private Double commissionMonthAvg;

    private HashMap<String, Double> quizStartMonthAvg;
    private HashMap<String, Double> quizCompleteMonthAvg;
    private HashMap<String, Map<String, Double>> regStartMonthAvg;
    private HashMap<String, Map<String, Double>> regCompleteMonthAvg;

    private Double regStartTotalMonthAvg;
    private Double regCompleteTotalMonthAvg;

    public Double getPagesPerVisitMonthAvg() {
        return pagesPerVisitMonthAvg;
    }

    public void setPagesPerVisitMonthAvg(Double pagesPerVisitMonthAvg) {
        this.pagesPerVisitMonthAvg = pagesPerVisitMonthAvg;
    }

    public Double getUniqueMonthAvg() {
        return uniqueMonthAvg;
    }

    public void setUniqueMonthAvg(Double uniqueMonthAvg) {
        this.uniqueMonthAvg = uniqueMonthAvg;
    }

    public Double getVisitsMonthAvg() {
        return visitsMonthAvg;
    }

    public void setVisitsMonthAvg(Double visitsMonthAvg) {
        this.visitsMonthAvg = visitsMonthAvg;
    }

    public Double getPagesMonthAvg() {
        return pagesMonthAvg;
    }

    public void setPagesMonthAvg(Double pagesMonthAvg) {
        this.pagesMonthAvg = pagesMonthAvg;
    }

    public Double getReturnedMonthAvg() {
        return returnedMonthAvg;
    }

    public void setReturnedMonthAvg(Double returnedMonthAvg) {
        this.returnedMonthAvg = returnedMonthAvg;
    }

    public Double getStoresMonthAvg() {
        return storesMonthAvg;
    }

    public void setStoresMonthAvg(Double storesMonthAvg) {
        this.storesMonthAvg = storesMonthAvg;
    }

    public Double getPurchasesMonthAvg() {
        return purchasesMonthAvg;
    }

    public void setPurchasesMonthAvg(Double purchasesMonthAvg) {
        this.purchasesMonthAvg = purchasesMonthAvg;
    }

    public Double getSalesMonthAvg() {
        return salesMonthAvg;
    }

    public void setSalesMonthAvg(Double salesMonthAvg) {
        this.salesMonthAvg = salesMonthAvg;
    }

    public Double getCommissionMonthAvg() {
        return commissionMonthAvg;
    }

    public void setCommissionMonthAvg(Double commissionMonthAvg) {
        this.commissionMonthAvg = commissionMonthAvg;
    }

    public Double getRegStartTotalMonthAvg() {
        return regStartTotalMonthAvg;
    }

    public void setRegStartTotalMonthAvg(Double regStartTotalMonthAvg) {
        this.regStartTotalMonthAvg = regStartTotalMonthAvg;
    }

    public Double getRegCompleteTotalMonthAvg() {
        return regCompleteTotalMonthAvg;
    }

    public void setRegCompleteTotalMonthAvg(Double regCompleteTotalMonthAvg) {
        this.regCompleteTotalMonthAvg = regCompleteTotalMonthAvg;
    }

    public HashMap<String, Double> getQuizStartMonthAvg() {
        return quizStartMonthAvg;
    }

    public void setQuizStartMonthAvg(HashMap<String, Double> quizStartMonthAvg) {
        this.quizStartMonthAvg = quizStartMonthAvg;
    }

    public HashMap<String, Double> getQuizCompleteMonthAvg() {
        return quizCompleteMonthAvg;
    }

    public void setQuizCompleteMonthAvg(HashMap<String, Double> quizCompleteMonthAvg) {
        this.quizCompleteMonthAvg = quizCompleteMonthAvg;
    }

    public HashMap<String, Map<String, Double>> getRegStartMonthAvg() {
        return regStartMonthAvg;
    }

    public void setRegStartMonthAvg(HashMap<String, Map<String, Double>> regStartMonthAvg) {
        this.regStartMonthAvg = regStartMonthAvg;
    }

    public HashMap<String, Map<String, Double>> getRegCompleteMonthAvg() {
        return regCompleteMonthAvg;
    }

    public void setRegCompleteMonthAvg(HashMap<String, Map<String, Double>> regCompleteMonthAvg) {
        this.regCompleteMonthAvg = regCompleteMonthAvg;
    }
}
