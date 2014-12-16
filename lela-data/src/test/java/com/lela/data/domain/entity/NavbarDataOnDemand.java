package com.lela.data.domain.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.dod.RooDataOnDemand;

@RooDataOnDemand(entity = Navbar.class)
public class NavbarDataOnDemand {

    @Autowired
    private LocaleDataOnDemand localeDataOnDemand;

    @Autowired
    private AffiliateDataOnDemand affiliateDataOnDemand;

    public void setLocale(Navbar obj, int index) {
        Locale locale = localeDataOnDemand.getRandomLocale();
        obj.setLocale(locale);
    }

    public void setAffiliate(Navbar obj, int index) {
        Affiliate affiliate = affiliateDataOnDemand.getRandomAffiliate();
        obj.setAffiliate(affiliate);
    }
}
