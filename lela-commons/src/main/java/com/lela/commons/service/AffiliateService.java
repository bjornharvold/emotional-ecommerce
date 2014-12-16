/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import java.util.List;
import java.util.concurrent.Future;

import com.lela.domain.document.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import com.lela.domain.dto.AffiliateAccountAndImage;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 1/11/12
 * Time: 11:22 AM
 */
public interface AffiliateService {
    List<AffiliateAccount> saveAffiliateAccounts(List<AffiliateAccount> list);
    AffiliateAccount saveAffiliateAccount(AffiliateAccount affiliate);
    AffiliateAccount findAffiliateAccountByUrlName(String urlName);
    void removeAffiliateAccount(String rlnm);
    Page<AffiliateAccount> findAffiliateAccounts(boolean includeInactive, Integer page, Integer size);
    Long findAffiliateAccountCount();
    List<AffiliateAccount> paginateThroughAllAffiliateAccounts(Integer chunk);
    void trackAffiliateIdentifiers(String userCode, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName);
    Workbook generateCampaignUserDetailsReport(String campaignUrlName);
    Workbook generateCampaignDailyBreakdown(String campaignUrlName);
    void saveAffiliateAccountApplication(AffiliateAccountApplication application);
    void removeAffiliateAccountApplication(String urlName, String applicationUrlName);
    AffiliateAccount findActiveAffiliateAccountByUrlName(String urlName);
    List<AffiliateAccount> findAffiliateAccounts(List<String> fields);
    Future<Workbook> generateAffiliateUserTrackingReport(String affiliateUrlName);
    Workbook generateCampaignUserTrackingReport(String campaignUrlName);
    Future<Workbook> generateRegistrationsByAffiliateReport(); 
    AffiliateAccountAndImage saveAffiliateAccountAndImage(AffiliateAccountAndImage accountAndImage);
    Future<Workbook> generateAffiliateUserDetailsReport(String affiliateUrlName);
    Page<AffiliateAccount> findAffiliateAccounts(Integer page, Integer size);
    AffiliateAccount findAffiliateAccountByDomain(String domain);
    void removeAffiliateAccountCssStyle(String urlName, String styleUrlName);
    void saveAffiliateAccountCssStyle(AffiliateAccountCssStyle style);
    void removeAffiliateAccountStore(String urlName, String storeUrlName);
    void saveAffiliateAccountStore(AffiliateAccountStore store);
    List<AffiliateAccount> findAllAffiliateAccountsWithNavbar();
    List<Category> findExcludedCategories();

    void trackAffiliateIdentifiers(String userCode, AffiliateAccount domain, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName);
}
