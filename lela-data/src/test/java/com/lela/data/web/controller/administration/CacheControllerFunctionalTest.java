/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.data.web.controller.administration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.NavigationBarService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.PressService;
import com.lela.commons.service.QuizService;
import com.lela.commons.service.StoreService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.data.web.AbstractFunctionalTest;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Campaign;
import com.lela.domain.document.Category;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.Item;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.document.Owner;
import com.lela.domain.document.Press;
import com.lela.domain.document.Question;
import com.lela.domain.document.Quiz;
import com.lela.domain.document.Store;
import com.lela.domain.dto.cache.EvictCacheRequest;
import com.lela.domain.enums.FunctionalFilterDomainType;
import com.lela.domain.enums.FunctionalFilterType;
import com.lela.domain.enums.QuestionType;


/**
 * Created by Bjorn Harvold
 * Date: 5/31/12
 * Time: 10:05 AM
 * Responsibility: Tests cache controller
 */
public class CacheControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(CacheControllerFunctionalTest.class);
    private static final String CATEGORY_URL_NAME = "CacheControllerFunctionalTestCategory";
    private static final String ITEM_URL_NAME = "CacheControllerFunctionalTestItem";
    private static final String CAMPAIGN_URL_NAME = "CacheControllerFunctionalTestCampaign";
    private static final String AFFILIATE_ACCOUNT_URL_NAME = "CacheControllerFunctionalTestAffiliateAccount";
    private static final String PRESS_URL_NAME = "CacheControllerFunctionalTestPress";
    private static final String QUESTION_URL_NAME = "CacheControllerFunctionalTestQuestion";
    private static final String QUIZ_URL_NAME = "CacheControllerFunctionalTestQuiz";
    private static final String STORE_URL_NAME = "CacheControllerFunctionalTestStore";
    private static final String OWNER_URL_NAME = "CacheControllerFunctionalTestOwner";
    private static final String NAVIGATION_BAR_URL_NAME = "CacheControllerFunctionalTestNavigationBar";

    @Autowired
    private CacheController cacheController;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private FunctionalFilterService functionalFilterService;

    @Autowired
    private PressService pressService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OwnerService ownerService;
    
    @Autowired
    private NavigationBarService navigationBarService;
    

    private Category category = null;
    private Item item = null;
    private Campaign campaign;
    private FunctionalFilter functionalFilter;
    private AffiliateAccount affiliateAccount;
    private Press press;
    private Question question;
    private Quiz quiz;
    private Store store;
    private Owner owner;
    private NavigationBar navigationBar;

    @Before
    public void setUp() {
        log.info("Setting up...");
        SpringSecurityHelper.secureChannel();

        category = new Category();
        category.setRlnm(CATEGORY_URL_NAME);
        category.setNm(CATEGORY_URL_NAME);

        category = categoryService.saveCategory(category);
        assertNotNull("category is null", category);
        assertNotNull("category ID is null", category.getId());

        item = new Item();
        item.setRlnm(ITEM_URL_NAME);
        item.setNm(ITEM_URL_NAME);
        item.setCtgry(category);  
        item = itemService.saveItem(item);
        assertNotNull("item is null", item);
        assertNotNull("item ID is null", item.getId());

        campaign = new Campaign();
        campaign.setRlnm(CAMPAIGN_URL_NAME);

        campaign = campaignService.saveCampaign(campaign);
        assertNotNull("campaign is null", campaign);
        assertNotNull("campaign ID is null", campaign.getId());

        affiliateAccount = new AffiliateAccount();
        affiliateAccount.setRlnm(AFFILIATE_ACCOUNT_URL_NAME);

        affiliateAccount = affiliateService.saveAffiliateAccount(affiliateAccount);
        assertNotNull("affiliate account is null", affiliateAccount);
        assertNotNull("affiliate account ID is null", affiliateAccount.getId());

        functionalFilter = new FunctionalFilter();
        functionalFilter.setRlnm(CATEGORY_URL_NAME);
        functionalFilter.setTp(FunctionalFilterType.MULTIPLE_CHOICE_SINGLE_ANSWER);
        functionalFilter.setDtp(FunctionalFilterDomainType.CATEGORY);

        functionalFilter = functionalFilterService.saveFunctionalFilter(functionalFilter);
        assertNotNull("functional filter is null", functionalFilter);
        assertNotNull("functional filter ID is null", functionalFilter.getId());

        press = new Press();
        press.setRlnm(PRESS_URL_NAME);
        press = pressService.savePress(press);
        assertNotNull("press is null", press);
        assertNotNull("press ID is null", press.getId());

        question = new Question();
        question.setRlnm(QUESTION_URL_NAME);
        question.setNm(QUESTION_URL_NAME);
        question.setTp(QuestionType.IMAGE_MULTIPLE_CHOICE_SINGLE_ANSWER);
        question = quizService.saveQuestion(question);
        assertNotNull("question is null", question);
        assertNotNull("question ID is null", question.getId());

        quiz = new Quiz();
        quiz.setRlnm(QUIZ_URL_NAME);
        quiz.setDflt(false);
        quiz.setNm(QUIZ_URL_NAME);
        quiz.setPblshd(true);
        quiz.setSrlnm(QUIZ_URL_NAME);
        quiz.setLng("en");
        quiz = quizService.saveQuiz(quiz);
        assertNotNull("quiz is null", quiz);
        assertNotNull("quiz ID is null", quiz.getId());

        store = new Store();
        store.setRlnm(STORE_URL_NAME);
        store = storeService.saveStore(store);
        assertNotNull("store is null", store);
        assertNotNull("store ID is null", store.getId());

        owner = new Owner();
        owner.setRlnm(OWNER_URL_NAME);
        owner = ownerService.saveOwner(owner);
        assertNotNull("owner is null", owner);
        assertNotNull("owner ID is null", owner.getId());
        
        navigationBar = new NavigationBar();
        navigationBar.setRlnm(NAVIGATION_BAR_URL_NAME);
        navigationBar.setDflt(Boolean.FALSE);
        navigationBar.setLcl(Locale.US);
        navigationBar = navigationBarService.saveNavigationBar(navigationBar);
        assertNotNull("navigationBar is null", navigationBar);
        assertNotNull("navigationBar ID is null", navigationBar.getId());

        SpringSecurityHelper.unsecureChannel();
        log.info("Set up complete");
    }

    @After
    public void tearDown() {
        log.info("Tearing down...");
        SpringSecurityHelper.secureChannel();

        if (category != null) {
            categoryService.removeCategory(category.getRlnm());
        }

        if (item != null) {
            itemService.removeItem(item.getRlnm());
        }

        if (campaign != null) {
            campaignService.removeCampaign(campaign.getRlnm());
        }

        if (functionalFilter != null) {
            functionalFilterService.removeFunctionalFilter(functionalFilter.getId());
        }

        if (affiliateAccount != null) {
            affiliateService.removeAffiliateAccount(affiliateAccount.getRlnm());
        }

        if (press != null) {
            pressService.removePress(press.getRlnm());
        }

        if (question != null) {
            quizService.removeQuestion(question.getRlnm());
        }

        if (quiz != null) {
            quizService.removeQuiz(quiz.getRlnm());
        }

        if (store != null) {
            storeService.removeStore(store.getRlnm());
        }

        if (owner != null) {
            ownerService.removeOwner(owner.getRlnm());
        }
        
        if (navigationBar != null){
        	navigationBarService.removeNavigationBar(navigationBar.getRlnm());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Tear down complete");
    }

    @Test
    public void testCacheController() {
        log.info("Testing cache controller...");
        SpringSecurityHelper.secureChannel();

        Model model = new BindingAwareModelMap();
        RedirectAttributes ra = new RedirectAttributesModelMap();

        String view = cacheController.showCacheConsole(model);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "cache.console", view);

        log.info("Evicting non-existent category cache");
        EvictCacheRequest request = new EvictCacheRequest();
        request.setUrlName("somecategorythatdoesnotexist");
        view = cacheController.evictCategoryCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        String message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.fail"));

        log.info("Evicting existing category cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(category.getRlnm());
        view = cacheController.evictCategoryCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        log.info("Evicting non-existent item cache");
        request = new EvictCacheRequest();
        request.setUrlName("someitemthatdoesnotexist");
        view = cacheController.evictItemCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.fail"));

        log.info("Evicting existing item cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(item.getRlnm());
        view = cacheController.evictItemCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));       
        
        log.info("Evicting all items from item cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        view = cacheController.evictItemCacheForAllItems(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));
        
        log.info("Evicting non-existent campaign cache");
        request = new EvictCacheRequest();
        request.setUrlName("somecampaignthatdoesnotexist");
        view = cacheController.evictCampaignCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.fail"));

        log.info("Evicting existing campaign cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(campaign.getRlnm());
        view = cacheController.evictCampaignCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        log.info("Evicting non-existent functional filter cache");
        request = new EvictCacheRequest();
        request.setUrlName("somefilterthatdoesnotexist");
        view = cacheController.evictFunctionalFiltersCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.fail"));

        log.info("Evicting existing functional filter cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(category.getRlnm());
        view = cacheController.evictFunctionalFiltersCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        log.info("Evicting non-existent affiliate account cache");
        request = new EvictCacheRequest();
        request.setUrlName("someaccountthatdoesnotexist");
        view = cacheController.evictAffiliateAccountCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.fail"));

        log.info("Evicting existing affiliate account cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(affiliateAccount.getRlnm());
        view = cacheController.evictAffiliateAccountCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        log.info("Evicting existing press cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(press.getRlnm());
        view = cacheController.evictPressCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        log.info("Evicting existing question cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(question.getRlnm());
        view = cacheController.evictQuestionCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        log.info("Evicting existing quiz cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(quiz.getRlnm());
        view = cacheController.evictQuizCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        log.info("Evicting existing store cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(store.getRlnm());
        view = cacheController.evictStoreCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        log.info("Evicting existing navigationBar cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(navigationBar.getRlnm());
        view = cacheController.evictNavigationBarCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));
        
        log.info("Evicting existing owner cache");
        ra = new RedirectAttributesModelMap();
        request = new EvictCacheRequest();
        request.setUrlName(owner.getRlnm());
        view = cacheController.evictOwnerCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.eviction.success"));

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing cache controller complete");
    }
    
    @Test
    public void testTestCache(){
        log.info("Testing testing of caches...");
        SpringSecurityHelper.secureChannel();    
 
        RedirectAttributes ra = new RedirectAttributesModelMap();


        log.info("Evicting non-existent category cache");
        EvictCacheRequest request = new EvictCacheRequest();
        request.setUrlName(CAMPAIGN_URL_NAME); 
        request.setCacheName("campaign");
        log.info("Evicting campaign cache for URL: " + CAMPAIGN_URL_NAME);
        cacheController.evictCampaignCache(request, ra, Locale.US);
        String view = cacheController.testCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        String message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.test.cache.not.exists"));
        String value = (String) ra.getFlashAttributes().get("value");
        assertNull("Value should not exist", value);
        
        //Load up the campaign cache for this campaign URL
        campaign = campaignService.findCampaignByUrlName(CAMPAIGN_URL_NAME);
        
        view = cacheController.testCache(request, ra, Locale.US);
        assertNotNull("view is null", view);
        assertEquals("view is incorrect", "redirect:/administration/cache/console", view);
        message = (String) ra.getFlashAttributes().get(WebConstants.MESSAGE);
        assertTrue("Message is incorrect", message.contains("cache.test.cache.exists"));
        value = (String) ra.getFlashAttributes().get("value");
        assertNotNull("Value should exist", value);
       
        SpringSecurityHelper.unsecureChannel();
        log.info("Testing testing of caches complete");
    }
}
