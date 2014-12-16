/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.lela.commons.service.*;
import com.lela.domain.document.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lela.commons.LelaException;
import com.lela.commons.excel.AffiliateUserTrackingReport;
import com.lela.commons.excel.CampaignDailyBreakdown;
import com.lela.commons.excel.RegistrationsByAffiliateReport;
import com.lela.commons.excel.UserDetailsReport;
import com.lela.commons.repository.AffiliateAccountRepository;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.dto.AffiliateAccountAndImage;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.admin.AffiliateUserTracking;
import com.lela.domain.dto.admin.AffiliateUserTrackingResults;
import com.lela.domain.dto.admin.CampaignDailyTotal;
import com.lela.domain.dto.admin.RegistrationsByAffiliateResult;
import com.lela.domain.dto.admin.UserDetailReportRow;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.enums.AssociationType;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.number.NumberUtils;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 1/11/12
 * Time: 11:26 AM
 */
@Service("affiliateService")
public class AffiliateServiceImpl extends AbstractCacheableService implements AffiliateService {
    private static Logger log = LoggerFactory.getLogger(AffiliateServiceImpl.class);

    private final AffiliateAccountRepository affiliateRepository;
    private final UserService userService;
    private final CampaignService campaignService;
    private final UserTrackerService userTrackerService;
    private final MongoTemplate mongoTemplate;
    private final FileStorage fileStorage;
    private final NavigationBarService navigationBarService;

    @Autowired
    public AffiliateServiceImpl(CacheService cacheService,
                                AffiliateAccountRepository affiliateRepository,
                                UserService userService,
                                CampaignService campaignService,
                                UserTrackerService userTrackerService,
                                MongoTemplate mongoTemplate,
                                NavigationBarService navigationBarService,
                                @Qualifier("bannerImageFileStorage") FileStorage fileStorage) {
        super(cacheService);
        this.affiliateRepository = affiliateRepository;
        this.userService = userService;
        this.campaignService = campaignService;
        this.userTrackerService = userTrackerService;
        this.mongoTemplate = mongoTemplate;
        this.fileStorage = fileStorage;
        this.navigationBarService = navigationBarService;
    }

    @Override
    public AffiliateAccount findActiveAffiliateAccountByUrlName(String urlName) {

        AffiliateAccount account = affiliateRepository.findByUrlName(urlName);
        if (account != null && account.getCtv()) {
            return account;
        }

        // No valid or active affiliate
        return null;
    }
    
    @PreAuthorize("hasAnyRole('RIGHT_READ_AFFILIATE')")
    @Override
    public List<AffiliateAccount> findAffiliateAccounts(List<String> fields) {
        return affiliateRepository.findAll(fields);
    }

    /**
     * TODO an improvement to this would be to only return those fields that will be used
     * @param chunk chunk
     * @return
     */
    @Override
    public List<AffiliateAccount> paginateThroughAllAffiliateAccounts(Integer chunk) {
        List<AffiliateAccount> result = null;
        Long count;
        Integer iterations;
        count = findAffiliateAccountCount();

        if (count != null && count > 0) {
            result = new ArrayList<AffiliateAccount>(count.intValue());
            iterations = NumberUtils.calculateIterations(count, chunk.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findAffiliateAccounts(i, chunk.intValue()).getContent());
            }
        }

        return result;
    }

    /**
     * @param account affiliate
     *
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_INSERT_AFFILIATE')")
    @Override
    public AffiliateAccount saveAffiliateAccount(AffiliateAccount account) {
        AffiliateAccount result = affiliateRepository.save(account);

        // Remove from cache
        removeFromCache(CacheType.AFFILIATE_ACCOUNT, account.getRlnm());

        return result;
    }

    /**
     * @param accountAndImage affiliate and uploaded image
     *
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_INSERT_AFFILIATE')")
    @Override
    public AffiliateAccountAndImage saveAffiliateAccountAndImage(AffiliateAccountAndImage accountAndImage) {
        try {
            //First save the image if any, ignoring the deleteFlag is set
            if (accountAndImage.getBannerImageFile() != null && !accountAndImage.getBannerImageFile().isEmpty()){
                //Load up the campaign to get at the old URL
                AffiliateAccount reloadedCampaign = affiliateRepository.findByUrlName(accountAndImage.getAffiliate().getRlnm());
                //then delete the file if it already exists.
                if (reloadedCampaign != null && !StringUtils.isEmpty(reloadedCampaign.getBnrmgrl())) {
                    if (log.isDebugEnabled()){
                        List<String> existingFiles = fileStorage.listAllObjectsInABucket(fileStorage.getBucketName());
                        log.debug("Files before adding: " + (existingFiles == null?"[]":(existingFiles.toString())) + ", Total: " + (existingFiles == null?0:(existingFiles.size())));
                    }
                    removeBannerImageAssociatedToCampaign(
                            accountAndImage.getAffiliate(), reloadedCampaign.getBnrmgrl());
                }

                MultipartFile file = accountAndImage.getBannerImageFile();
                FileData data = new FileData(accountAndImage.getAffiliate().getRlnm() + "-" + file.getOriginalFilename(), file.getBytes(), file.getContentType());
                String url = fileStorage.storeFile(data);
                log.info(String.format("Stored data %s at url: %s  ", data.toString(), url) );
                accountAndImage.getAffiliate().setBnrmgrl(url);
                if (log.isDebugEnabled()){
                    List<String> existingFiles = fileStorage.listAllObjectsInABucket(fileStorage.getBucketName());
                    log.debug("Files after adding: " + (existingFiles == null?"[]":(existingFiles.toString())) + ", Total: " + (existingFiles == null?0:(existingFiles.size())));
                }
            } else {
                //check to see if image needs deleted
                if (accountAndImage.isDeleteImage()){
                    String urlToDelete = accountAndImage.getAffiliate().getBnrmgrl();
                    if (!StringUtils.isEmpty(urlToDelete)){
                        removeBannerImageAssociatedToCampaign(
                                accountAndImage.getAffiliate(), urlToDelete);
                    } else {
                        //Should happen only of user has checked box to delete image when none were uploaded in the past.
                        log.warn("Attempt to delete image with missing URL!");
                    }
                }
            }
        } catch (Exception e){
            throw new LelaException("Error saving banner image", e);
        }

        //Place the saved campaign in the wrapper
        accountAndImage.setAffiliate(saveAffiliateAccount(accountAndImage.getAffiliate()));
        return accountAndImage;
    }

    private void removeBannerImageAssociatedToCampaign(AffiliateAccount affiliate, String urlToDelete) {
        String fileToRemove = fileStorage.getObjectKeyForURL(urlToDelete);
        fileStorage.removeObjectFromBucket(fileStorage.getBucketName(), fileToRemove);
        affiliate.setBnrmgrl(null);
        log.info("Deleted image with URL: " + urlToDelete);
    }

    /**
     * @param list list
     *
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_INSERT_AFFILIATE')")
    @Override
    public List<AffiliateAccount> saveAffiliateAccounts(List<AffiliateAccount> list) {
        List<AffiliateAccount> result = (List<AffiliateAccount>) affiliateRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size());

            for (AffiliateAccount item : list) {
                cacheKeys.add(item.getRlnm());
            }

            removeFromCache(CacheType.AFFILIATE_ACCOUNT, cacheKeys);
        }

        return result;
    }

    @Override
    public AffiliateAccount findAffiliateAccountByUrlName(String urlName) {
        AffiliateAccount result = null;
        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.AFFILIATE_ACCOUNT_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof AffiliateAccount) {
            result = (AffiliateAccount) wrapper.get();
        } else {
            result = affiliateRepository.findByUrlName(urlName);

            if (result != null) {
                putInCache(ApplicationConstants.AFFILIATE_ACCOUNT_CACHE, urlName, result);
            }
        }

        return result;
    }

    @Override
    public AffiliateAccount findAffiliateAccountByDomain(String domain) {
        AffiliateAccount result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.DOMAIN_CACHE, domain);
        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof AffiliateAccount) {
            result = (AffiliateAccount) wrapper.get();
        } else {
            result = affiliateRepository.findByDomain(domain);

            if (result != null) {
                putInCache(ApplicationConstants.DOMAIN_CACHE, domain, result);
            }
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_AFFILIATE')")
    @Override
    public Page<AffiliateAccount> findAffiliateAccounts(boolean includeInactive, Integer page, Integer size) {

        List<AffiliateAccount> list = affiliateRepository.findAll(includeInactive, page, size);

        long totalAffiliates = affiliateRepository.countBasedOnStatus(includeInactive);    
        CustomPage<AffiliateAccount> result = new CustomPage<AffiliateAccount>(list, page, size, totalAffiliates);
        return result;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_AFFILIATE')")
    @Override
    public List<AffiliateAccount> findAllAffiliateAccountsWithNavbar() {

        List<AffiliateAccount> list = affiliateRepository.findWithNavigationBar();
        return list;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_AFFILIATE')")
    @Override
    public Page<AffiliateAccount> findAffiliateAccounts(Integer page, Integer size) {
        return affiliateRepository.findAll(new PageRequest(page, size));
    }

    /**
     * TODO this method should have a right associated with it [BH 2012-02-14]
     * @param rlnm rlnm
     */
    @PreAuthorize("hasAnyRole('RIGHT_DELETE_AFFILIATE')")
    @Override
    public void removeAffiliateAccount(String rlnm) {
        AffiliateAccount account = affiliateRepository.findByUrlName(rlnm);

        if (account != null) {
            //First delete any related banner image
            if (account.hasAssociatedBannerImage()) {
                this.removeBannerImageAssociatedToCampaign(account, account.getBnrmgrl());
            }

            affiliateRepository.delete(account);

            // Remove from cache
            removeFromCache(CacheType.AFFILIATE_ACCOUNT, account.getRlnm());
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_AFFILIATE')")
    @Override
    public void removeAffiliateAccountApplication(String urlName, String applicationUrlName) {
        AffiliateAccount affiliateAccount = findAffiliateAccountByUrlName(urlName);

        if (affiliateAccount != null) {
            boolean removed = affiliateAccount.removeAffiliateAccountApplication(applicationUrlName);

            if (removed) {
                saveAffiliateAccount(affiliateAccount);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_AFFILIATE')")
    @Override
    public void removeAffiliateAccountCssStyle(String urlName, String styleUrlName) {
        AffiliateAccount affiliateAccount = findAffiliateAccountByUrlName(urlName);

        if (affiliateAccount != null) {
            boolean removed = affiliateAccount.removeCssStyle(styleUrlName);

            if (removed) {
                saveAffiliateAccount(affiliateAccount);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_DELETE_AFFILIATE')")
    @Override
    public void removeAffiliateAccountStore(String urlName, String storeUrlName) {
        AffiliateAccount affiliateAccount = findAffiliateAccountByUrlName(urlName);

        if (affiliateAccount != null) {
            boolean removed = affiliateAccount.removeAffiliateAccountStore(storeUrlName);

            if (removed) {
                saveAffiliateAccount(affiliateAccount);
            }
        }
    }

    @Override
    public Long findAffiliateAccountCount() {
        return affiliateRepository.count();
    }

    @Override
    public void trackAffiliateIdentifiers(String userCode, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName) {
        trackAffiliateIdentifiers(userCode, null, affiliateUrlName, campaignUrlName, referrerAffiliateUrlName);
    }

    @Override
    public void trackAffiliateIdentifiers(String userCode, AffiliateAccount domainAffiliate, String affiliateUrlName, String campaignUrlName, String referrerAffiliateUrlName) {
        boolean trackedRegistration = userTrackerService.trackAffiliateIdentifiers(userCode, domainAffiliate, affiliateUrlName, campaignUrlName, referrerAffiliateUrlName);

        if (trackedRegistration) {
            UserAssociation association = new UserAssociation();
            association.setTp(AssociationType.AFFILIATE);
            association.setCdt(new Date());

            if (campaignUrlName != null) {
                Campaign campaign = campaignService.findActiveCampaignByUrlName(campaignUrlName);
                if (campaign != null) {
                    UserAssociationAttribute attrib = new UserAssociationAttribute(WebConstants.USER_ASSOCIATION_ATTRIBUTE_CAMPAIGN_KEY, campaign.getRlnm());
                    association.getAttrs().add(attrib);

                    // If the affiliate url name is not set, go ahead and set it from the campaign
                    if (affiliateUrlName == null) {
                        affiliateUrlName = campaign.getFfltrlnm();
                    }
                }
            }

            if (referrerAffiliateUrlName != null) {
                AffiliateAccount referrer = findActiveAffiliateAccountByUrlName(referrerAffiliateUrlName);
                if (referrer != null) {
                    UserAssociationAttribute attrib = new UserAssociationAttribute(WebConstants.USER_ASSOCIATION_ATTRIBUTE_REFERRING_AFFILIATE_KEY, referrer.getRlnm());
                    association.getAttrs().add(attrib);
                }
            }

            if (affiliateUrlName == null && domainAffiliate != null) {
                affiliateUrlName = domainAffiliate.getRlnm();
            }

            if (affiliateUrlName != null) {
                AffiliateAccount account = findActiveAffiliateAccountByUrlName(affiliateUrlName);
                if (account != null) {
                    association.setNm(account.getNm());
                    UserAssociationAttribute attrib = new UserAssociationAttribute(WebConstants.USER_ASSOCIATION_ATTRIBUTE_AFFILIATE_ACCOUNT_KEY, account.getRlnm());
                    association.getAttrs().add(attrib);
                }
            }

            // If there were any association attributes, save it to the user now
            if (!association.getAttrs().isEmpty()) {
                List<UserAssociation> associations = userService.findUserAssociations(userCode);

                if (associations == null) {
                    associations = new ArrayList<UserAssociation>();
                }

                associations.add(association);

                // Save the updated user association
                userService.saveUserAssociations(userCode, associations);
            }
        }
    }

    
    @Async
    //@PreAuthorize("hasAnyRole('RIGHT_READ_AFFILIATE')")
    @Override
    public Future<Workbook> generateAffiliateUserDetailsReport(String affiliateUrlName) {  

        log.debug("Start generating affiliate user details report");
        DateTime start = new DateTime();

        Workbook workbook = null;
        List<UserUserSupplementEntry> entries = userService.findsUsersForAffiliateReport(affiliateUrlName);

        log.warn("Retrieved users for affiliate use report in " + Seconds.secondsBetween(start, new DateTime()).getSeconds() + " sec");
        ArrayList<UserDetailReportRow> list = new ArrayList<UserDetailReportRow>();
        if (entries != null) {
            for (UserUserSupplementEntry entry : entries) {
                UserDetailReportRow row = new UserDetailReportRow(entry.getUser(), entry.getUserSupplement());
                list.add(row);

                // Check for an analytics registration event
                UserVisit visit = userTrackerService.findFirstVisit(entry.getUser());
                if (visit != null) {
                    row.setRegistrationDeviceType(visit.getDvctp());
                }

                row.setQuizComplete(userTrackerService.isQuizComplete(entry.getUser().getCd()));

                // Is a repeat visitor
                row.setRepeatUser(userTrackerService.isRepeatUser(entry.getUser()));
            }

        }

        UserDetailsReport report = new UserDetailsReport(list);
        workbook = report.generate();

        log.warn("Finish generating campaign user report in " + Seconds.secondsBetween(start, new DateTime()).getSeconds() + " sec");

        return new AsyncResult<Workbook>(workbook);
    }


    public Workbook generateCampaignUserDetailsReport(String campaignUrlName) {

        log.debug("Start generating campaign user report");
        DateTime start = new DateTime();

        Workbook workbook = null;

        List<UserUserSupplementEntry> entries = userService.findsUsersForCampaignReport(campaignUrlName);

        log.warn("Retrieved users for campaign use report in " + Seconds.secondsBetween(start, new DateTime()).getSeconds() + " sec");
        ArrayList<UserDetailReportRow> list = new ArrayList<UserDetailReportRow>();
        if (entries != null) {
            for (UserUserSupplementEntry entry : entries) {
                UserDetailReportRow row = new UserDetailReportRow(entry.getUser(), entry.getUserSupplement());
                list.add(row);

                // Check for an analytics registration event
                UserVisit visit = userTrackerService.findFirstVisit(entry.getUser());
                if (visit != null) {
                    row.setRegistrationDeviceType(visit.getDvctp());
                }

                row.setQuizComplete(userTrackerService.isQuizComplete(entry.getUser().getCd()));

                // Is a repeat visitor
                row.setRepeatUser(userTrackerService.isRepeatUser(entry.getUser()));
            }

        }

        UserDetailsReport report = new UserDetailsReport(list);
        workbook = report.generate();

        log.warn("Finish generating campaign user report in " + Seconds.secondsBetween(start, new DateTime()).getSeconds() + " sec");

        return workbook;
    }
    @PreAuthorize("hasAnyRole('RIGHT_READ_CAMPAIGN')")
    @Override
    public Workbook generateCampaignDailyBreakdown(String campaignUrlName) {

        // Get the campaign
        Campaign campaign = campaignService.findCampaignByUrlName(campaignUrlName);
        if (campaign == null) {
            throw new IllegalArgumentException("Invalid campaign name: " + campaignUrlName);
        }

        // Perform the map-reduce
        Map<String, Object> scope = new HashMap<String, Object>();
        scope.put("rlnm", campaignUrlName);
        MapReduceOptions options = MapReduceOptions.options().outputTypeInline().scopeVariables(scope);

        Query query = Query.query(Criteria.where("cdt").gte(campaign.getStrtdt()).andOperator(Criteria.where("cdt").lte(campaign.getNddt())));
        MapReduceResults<CampaignDailyTotal> results = mongoTemplate.mapReduce(query, "userSupplement", "classpath:mapreduce/campaign-daily-breakdown-map.js", "classpath:mapreduce/campaign-daily-breakdown-reduce.js", options, CampaignDailyTotal.class);

        Workbook workbook = null;

        // Get the list of campaign users
        List<CampaignDailyTotal> totals = new ArrayList<CampaignDailyTotal>(results.getCounts().getOutputCount());
        CollectionUtils.addAll(totals, results.iterator());

        CampaignDailyBreakdown report = new CampaignDailyBreakdown(totals);
        workbook = report.generate();

        return workbook;
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_AFFILIATE')")
    @Override
    public void saveAffiliateAccountApplication(AffiliateAccountApplication application) {
        AffiliateAccount affiliateAccount = findAffiliateAccountByUrlName(application.getAffiliateAccountUrlName());

        if (affiliateAccount != null) {

            // new creation date
            application.setDt(new Date());
            boolean saved = affiliateAccount.addApplication(application);

            if (saved) {
                saveAffiliateAccount(affiliateAccount);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_AFFILIATE')")
    @Override
    public void saveAffiliateAccountCssStyle(AffiliateAccountCssStyle style) {
        AffiliateAccount affiliateAccount = findAffiliateAccountByUrlName(style.getAffiliateAccountUrlName());

        if (affiliateAccount != null) {

            // new creation date
            style.setDt(new Date());
            boolean saved = affiliateAccount.addCssStyle(style);

            if (saved) {
                saveAffiliateAccount(affiliateAccount);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_INSERT_AFFILIATE')")
    @Override
    public void saveAffiliateAccountStore(AffiliateAccountStore store) {
        AffiliateAccount affiliateAccount = findAffiliateAccountByUrlName(store.getAffiliateAccountUrlName());

        if (affiliateAccount != null) {

            // new creation date
            store.setDt(new Date());
            boolean saved = affiliateAccount.addStore(store);

            if (saved) {
                saveAffiliateAccount(affiliateAccount);
            }
        }
    }

    @Async
    //@PreAuthorize("hasAnyRole('RIGHT_READ_AFFILIATE')")
    @Override
    public Future<Workbook> generateAffiliateUserTrackingReport(String affiliateUrlName) { 

        // Get the affiliate
        AffiliateAccount affiliate = affiliateRepository.findByUrlName(affiliateUrlName);
        if (affiliate == null) {
            throw new IllegalArgumentException("Invalid affiliate url name: " + affiliateUrlName);
        }

        // Perform the map-reduce
        Map<String, Object> scope = new HashMap<String, Object>();
        MapReduceOptions options = MapReduceOptions.options().outputTypeInline().scopeVariables(scope);
        options.finalizeFunction("classpath:mapreduce/affiliate-user-tracking-finalize.js");

        Query query = Query.query(where("ffltccntrlnm").is(affiliateUrlName));
        MapReduceResults<AffiliateUserTrackingResults> results = mongoTemplate.mapReduce(query, "userTracker", "classpath:mapreduce/affiliate-user-tracking-map.js", "classpath:mapreduce/affiliate-user-tracking-reduce.js", options, AffiliateUserTrackingResults.class);

        if(log.isDebugEnabled())
          log.debug(results.toString());

        Workbook workbook = null;

        AffiliateUserTracking data = null;
        if (results.getCounts().getOutputCount() > 0) {
            data = results.iterator().next().getValue();
        }

        AffiliateUserTrackingReport report = new AffiliateUserTrackingReport(affiliate, data);
        workbook = report.generate();

        return new AsyncResult<Workbook>(workbook);
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_CAMPAIGN')")
    @Override
    public Workbook generateCampaignUserTrackingReport(String campaignUrlName) {

        // Get the campaign
        Campaign campaign = campaignService.findCampaignByUrlName(campaignUrlName);
        if (campaign == null) {
            throw new IllegalArgumentException("Invalid campaign url name: " + campaignUrlName);
        }

        // Get the affiliate
        AffiliateAccount affiliate = affiliateRepository.findByUrlName(campaign.getFfltrlnm());
        if (affiliate == null) {
            throw new IllegalArgumentException("Invalid affiliate url name: " + campaign.getFfltrlnm());
        }

        // Perform the map-reduce
        Map<String, Object> scope = new HashMap<String, Object>();
        MapReduceOptions options = MapReduceOptions.options().outputTypeInline().scopeVariables(scope);
        options.finalizeFunction("classpath:mapreduce/affiliate-user-tracking-finalize.js");

        Query query = Query.query(where("ffltccntrlnm").is(campaign.getFfltrlnm()).and("cmpgnrlnm").is(campaignUrlName));
        MapReduceResults<AffiliateUserTrackingResults> results = mongoTemplate.mapReduce(query, "userTracker", "classpath:mapreduce/affiliate-user-tracking-map.js", "classpath:mapreduce/affiliate-user-tracking-reduce.js", options, AffiliateUserTrackingResults.class);

        System.out.println(results);
        Workbook workbook = null;

        AffiliateUserTracking data = null;
        if (results.getCounts().getOutputCount() > 0) {
            data = results.iterator().next().getValue();
        }

        AffiliateUserTrackingReport report = new AffiliateUserTrackingReport(affiliate, campaign, data);
        workbook = report.generate();

        return workbook;
    }

    @Async
    //@PreAuthorize("hasAnyRole('RIGHT_READ_AFFILIATE')")
    @Override
    public Future<Workbook> generateRegistrationsByAffiliateReport() { 

    	Query query = Query.query(where("ml").exists(true));
        // Perform the map-reduce     on what
        MapReduceResults<RegistrationsByAffiliateResult> results = mongoTemplate.mapReduce(query, "userSupplement", "classpath:mapreduce/registration-by-affiliate-map.js", "classpath:mapreduce/registration-by-affiliate-reduce.js", RegistrationsByAffiliateResult.class);

        System.out.println(results);
        Workbook workbook = null;

        List<RegistrationsByAffiliateResult> resultList = new ArrayList<RegistrationsByAffiliateResult>();
        if (results.getCounts().getOutputCount() > 0) {
            for (RegistrationsByAffiliateResult result : results) {
                resultList.add(result);
            }
        }

        RegistrationsByAffiliateReport report = new RegistrationsByAffiliateReport(resultList);
        workbook = report.generate();

        return new AsyncResult(workbook);
    }

    @Override
    public List<Category> findExcludedCategories() {
        List<Category> excludedCategories = new ArrayList<Category>();
        List<AffiliateAccount> affiliates = findAllAffiliateAccountsWithNavbar();
        for(AffiliateAccount affiliateAccount: affiliates)
        {
            String domainNavBar = affiliateAccount.getDmnnvbr();
            NavigationBar navBar = navigationBarService.findNavigationBarByUrlName(domainNavBar);
            if(navBar!=null)
            {
                List<CategoryGroup> categoryGroups = navBar.getGrps();

                for(CategoryGroup categoryGroup:categoryGroups)
                {
                    excludedCategories.addAll(categoryGroup.getCtgrs());
                }
            }
        }
        return excludedCategories;
    }
}
