package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = AffiliateTransaction.class)
public class AffiliateTransactionDataOnDemand {

    @Autowired
    AffiliateReportDataOnDemand affiliateReportDataOnDemand;

    public void setAffiliateReport(AffiliateTransaction obj, int index) {
        AffiliateReport affiliateReport = affiliateReportDataOnDemand.getRandomAffiliateReport();
        obj.setAffiliateReport(affiliateReport);
    }

}
