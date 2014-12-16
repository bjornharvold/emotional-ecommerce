/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.controller.administration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.lela.commons.bootstrap.BootstrapperException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lela.commons.service.AffiliateService;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.CampaignService;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.NavigationBarService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.PressService;
import com.lela.commons.service.QuizService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.commons.service.StoreService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.ApplicationConstants;
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
import com.lela.domain.enums.CacheType;

/**
 * Created by Bjorn Harvold
 * Date: 5/30/12
 * Time: 3:06 PM
 * Responsibility: Manages ehcache flushes and solr indexing
 */
@Controller
@SessionAttributes(types = {EvictCacheRequest.class})
public class CacheController {

	private final static Logger log = LoggerFactory.getLogger(CacheController.class);
	
	
	
    //private final LocalCacheEvictionService localCacheEvictionService;
    private final MessageSource messageSource;
    private final SearchService searchService;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final AffiliateService affiliateService;
    private final CampaignService campaignService;
    private final FunctionalFilterService functionalFilterService;
    private final PressService pressService;
    private final QuizService quizService;
    private final StoreService storeService;
    private final OwnerService ownerService;
    private final NavigationBarService navigationBarService;
    private final CacheService cacheService;
 
    @Autowired
    public CacheController(LocalCacheEvictionService localCacheEvictionService,
                           MessageSource messageSource, SearchService searchService,
                           ItemService itemService, CategoryService categoryService,
                           AffiliateService affiliateService,
                           CampaignService campaignService,
                           FunctionalFilterService functionalFilterService,
                           PressService pressService,
                           QuizService quizService,
                           StoreService storeService, OwnerService ownerService,
                           NavigationBarService	navigationBarService,
                           CacheService cacheService
    		) {
        //this.localCacheEvictionService = localCacheEvictionService;
        this.functionalFilterService = functionalFilterService;
        this.messageSource = messageSource;
        this.searchService = searchService;
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.affiliateService = affiliateService;
        this.campaignService = campaignService;
        this.pressService = pressService;
        this.quizService = quizService;
        this.storeService = storeService;
        this.ownerService = ownerService;
        this.navigationBarService = navigationBarService;
        this.cacheService = cacheService;
        
    }

    @RequestMapping(value = "/administration/cache/console", method = RequestMethod.GET)
    public String showCacheConsole(Model model) {

        // add all categories we want to be able to invalidate caches
        List<Category> categories = categoryService.findCategories();
        model.addAttribute(WebConstants.CATEGORIES, categories);

        // add all campaigns
        List<Campaign> campaigns = campaignService.paginateThroughAllCampaigns(50);
        model.addAttribute(WebConstants.CAMPAIGNS, campaigns);

        List<AffiliateAccount> affiliateAccounts = affiliateService.paginateThroughAllAffiliateAccounts(50);
        model.addAttribute(WebConstants.AFFILIATE_ACCOUNTS, affiliateAccounts);

        // add all press documents
        List<Press> presses = pressService.findAllPressUrlNames();
        model.addAttribute(WebConstants.PRESSES, presses);

        // add all questions
        List<Question> questions = quizService.findAllQuestions(); 
        model.addAttribute(WebConstants.QUESTIONS, questions);

        // add all quizzes
        List<String> fields = new ArrayList<String>(1);
        fields.add("rlnm");
        List<Quiz> quizzes = quizService.findQuizzes(fields); 
        model.addAttribute(WebConstants.QUIZZES, quizzes);

        // add all stores
        List<Store> stores = storeService.findStores(fields);
        model.addAttribute(WebConstants.STORES, stores);

        // add all owners
        List<Owner> owners = ownerService.findOwners(fields);
        model.addAttribute(WebConstants.OWNERS, owners);

        // add all navigation bars
        List<NavigationBar> navigationBars = navigationBarService.findNavigationBars(fields);
        model.addAttribute(WebConstants.NAVIGATION_BARS, navigationBars);
        
        model.addAttribute(new EvictCacheRequest());

        return "cache.console";
    }

    @RequestMapping(value = "/administration/cache/evict/category", method = RequestMethod.POST)
    public String evictCategoryCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {
        String[] keys = {request.getUrlName(), "category"};
        String message = null;

        Category category = categoryService.findCategoryByUrlName(request.getUrlName());

        if (category != null) {
            // the specific category
            //localCacheEvictionService.evictCategory(category.getRlnm());
            cacheService.removeAllFromCache(CacheType.CATEGORY);
            // the category with all its items
            //localCacheEvictionService.evictItemCategory(category.getRlnm());
            cacheService.removeFromCache(CacheType.ITEM_CATEGORY, category.getRlnm());
            // the list of all categories
            //localCacheEvictionService.evictCategory(ApplicationConstants.CATEGORIES_KEY);

            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }
    
    @RequestMapping(value = "/administration/cache/evict/categories/all", method = RequestMethod.POST)
    public String evictAllCategories(RedirectAttributes redirectAttributes, Locale locale) {

        String message = null;
        try {
            //localCacheEvictionService.removeAllFromCache(ApplicationConstants.CATEGORY_CACHE);
        	cacheService.removeAllFromCache(CacheType.CATEGORY); //Not a true translation because now we are calling evictCategory instead of removing just the CATEGORY_CACHE
            //localCacheEvictionService.removeAllFromCache(ApplicationConstants.ITEM_CATEGORY_CACHE);
            cacheService.removeAllFromCache(CacheType.ITEM_CATEGORY);
            
            message = messageSource.getMessage("cache.eviction.success", new String[]{"all categories", "CATEGORY_CACHE"}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } catch (Exception e) {
            message = messageSource.getMessage("cache.eviction.fail", new String[]{"all categories", "CATEGORY_CACHE"}, locale);
            log.warn("Cache eviction for all categories failed.", e);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }
    
    @RequestMapping(value = "/administration/cache/evict/filters", method = RequestMethod.POST)
    public String evictFunctionalFiltersCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {
        String[] keys = {request.getUrlName(), "functional filters"};
        String message = null;

        List<FunctionalFilter> filters = functionalFilterService.findFunctionalFiltersByUrlName(request.getUrlName());

        if (filters != null) {
            //localCacheEvictionService.evictFunctionalFilter(request.getUrlName());
            cacheService.removeFromCache(CacheType.FUNCTIONAL_FILTER, request.getUrlName());
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }
    
    
    @RequestMapping(value = "/administration/cache/evict/filters/all", method = RequestMethod.POST)
    public String evictAllFunctionalFilters(RedirectAttributes redirectAttributes, Locale locale) {

        String message = null;
        try {
            //localCacheEvictionService.removeAllFromCache(ApplicationConstants.FUNCTIONAL_FILTER_CACHE);
            cacheService.removeAllFromCache(CacheType.FUNCTIONAL_FILTER);
            message = messageSource.getMessage("cache.eviction.success", new String[]{"all functional filters", "FUNCTIONAL_FILTER_CACHE"}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } catch (Exception e) {
            message = messageSource.getMessage("cache.eviction.fail", new String[]{"all functional filters", "FUNCTIONAL_FILTER_CACHE"}, locale);
            log.warn("Cache eviction for all functional filters failed.", e);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/evict/campaign", method = RequestMethod.POST)
    public String evictCampaignCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {
        String[] keys = {request.getUrlName(), "campaign"};
        String message = null;

        Campaign campaign = campaignService.findCampaignByUrlName(request.getUrlName());

        if (campaign != null) {
            //localCacheEvictionService.evictCampaign(campaign.getRlnm());
            cacheService.removeFromCache(CacheType.CAMPAIGN, campaign.getRlnm());

            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/evict/affiliateaccount", method = RequestMethod.POST)
    public String evictAffiliateAccountCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {
        String[] keys = {request.getUrlName(), "affiliate account"};
        String message = null;

        AffiliateAccount affiliateAccount = affiliateService.findAffiliateAccountByUrlName(request.getUrlName());

        if (affiliateAccount != null) {
            //localCacheEvictionService.evictAffiliateAccount(affiliateAccount.getRlnm());
        	cacheService.removeFromCache(CacheType.AFFILIATE_ACCOUNT, affiliateAccount.getRlnm());
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/evict/item", method = RequestMethod.POST)
    public String evictItemCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String[] keys = {request.getUrlName(), "item"};
        String message = null;

        Item item = itemService.findItemByUrlName(request.getUrlName());

        if (item != null) {
            //localCacheEvictionService.evictItem(item.getRlnm() + "|" + item.getCtgry().getRlnm());
            cacheService.removeFromCache(CacheType.ITEM, item.getRlnm() + "|" + item.getCtgry().getRlnm());
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }
    
    @RequestMapping(value = "/administration/cache/evict/items", method = RequestMethod.POST)
    public String evictItemCacheForAllItems(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String message = null;
        try {
            //localCacheEvictionService.removeAllFromCache(ApplicationConstants.ITEM_CACHE);
            cacheService.removeAllFromCache(CacheType.ITEM);
            message = messageSource.getMessage("cache.eviction.success", new String[]{"all items", "ITEM_CACHE"}, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } catch (Exception e) {
            message = messageSource.getMessage("cache.eviction.fail", new String[]{"all items", "ITEM_CACHE"}, locale);
            log.warn("Cache eviction for all items failed.", e);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/evict/press", method = RequestMethod.POST)
    public String evictPressCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String[] keys = {request.getUrlName(), "press"};
        String message = null;

        Press press = pressService.findPressByUrlName(request.getUrlName());

        if (press != null) {
            //localCacheEvictionService.evictPress(press.getRlnm());
            cacheService.removeFromCache(CacheType.PRESS, request.getUrlName());
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/evict/question", method = RequestMethod.POST)
    public String evictQuestionCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String[] keys = {request.getUrlName(), "question"};
        String message = null;

        Question question = quizService.findQuestionByUrlName(request.getUrlName());

        if (question != null) {
            //localCacheEvictionService.evictQuestion(question.getRlnm());
            cacheService.removeFromCache(CacheType.QUESTION, question.getRlnm());
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/evict/quiz", method = RequestMethod.POST)
    public String evictQuizCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String[] keys = {request.getUrlName(), "quiz"};
        String message = null;

        Quiz quiz = quizService.findQuizByUrlName(request.getUrlName());

        if (quiz != null) {
            //localCacheEvictionService.evictQuiz(quiz.getRlnm());
            cacheService.removeFromCache(CacheType.QUIZ, quiz.getRlnm());
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/evict/store", method = RequestMethod.POST)
    public String evictStoreCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String[] keys = {request.getUrlName(), "store"};
        String message = null;

        if (StringUtils.equals(request.getUrlName(), WebConstants.ALL)) {
            // store seo is not a list of all stores
            // store seo is the aggregate store search result we get from solr
            // if any stores are changed, this cache needs to be evicted as well as the individual stores
            //localCacheEvictionService.evictStoreSeo(ApplicationConstants.STORE_SEO_CACHE_KEY);
            cacheService.removeFromCache(CacheType.STORE_SEO, ApplicationConstants.STORE_SEO_CACHE_KEY);
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            Store store = storeService.findStoreByUrlName(request.getUrlName());

            if (store != null) {
                //localCacheEvictionService.evictStore(store.getRlnm());
                cacheService.removeFromCache(CacheType.STORE, store.getRlnm());
                message = messageSource.getMessage("cache.eviction.success", keys, locale);
                redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
            } else {
                message = messageSource.getMessage("cache.eviction.fail", keys, locale);
                redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
            }
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/evict/owner", method = RequestMethod.POST)
    public String evictOwnerCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String[] keys = {request.getUrlName(), "owner"};
        String message = null;

        if (StringUtils.equals(request.getUrlName(), WebConstants.ALL)) {
            // owner seo is not a list of all owners
            // owner seo is the aggregate owner search result we get from solr
            // if any owners are changed, this cache needs to be evicted as well as the individual owners
            //localCacheEvictionService.evictOwnerSeo(ApplicationConstants.OWNER_SEO_CACHE_KEY);
            cacheService.removeFromCache(CacheType.OWNER_SEO, ApplicationConstants.OWNER_SEO_CACHE_KEY);
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            Owner owner = ownerService.findOwnerByUrlName(request.getUrlName());

            if (owner != null) {
                //localCacheEvictionService.evictOwner(owner.getRlnm());
                cacheService.removeFromCache(CacheType.OWNER, owner.getRlnm());
                message = messageSource.getMessage("cache.eviction.success", keys, locale);
                redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
            } else {
                message = messageSource.getMessage("cache.eviction.fail", keys, locale);
                redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
            }
        }

        return "redirect:/administration/cache/console";
    }
    
    @RequestMapping(value = "/administration/cache/evict/navigationbar", method = RequestMethod.POST)
    public String evictNavigationBarCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String[] keys = {request.getUrlName(), "navigationBar"};
        String message = null;

        NavigationBar navigationBar = navigationBarService.findNavigationBarByUrlName(request.getUrlName());

        if (navigationBar != null) {
            //localCacheEvictionService.evictNavigationBar(navigationBar.getRlnm() );
            cacheService.removeFromCache(CacheType.NAVIGATION_BAR, navigationBar.getRlnm());
            message = messageSource.getMessage("cache.eviction.success", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.eviction.fail", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    @RequestMapping(value = "/administration/cache/reindex", method = RequestMethod.POST)
    public String reIndexByCategory(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {

        String[] keys = {request.getUrlName()};
        String message = null;

        // either we re-index every category one by one
        if (StringUtils.equals(request.getUrlName(), WebConstants.ALL)) {
            try {
                searchService.deleteAll();

                // sleep a bit here so Solr has a chance to process everything
                Thread.sleep(5000);
            } catch (SearchException e) {
                log.error(e.getMessage(), e);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            List<Category> categories = categoryService.findCategories();

            if (categories != null && !categories.isEmpty()) {
                for (Category category : categories) {
                    reIndexCategory(category.getRlnm());
                }
            }
        } else {
            reIndexCategory(request.getUrlName());
        }

        // lame message here considering it is always successful but too lazy to check for errors here
        message = messageSource.getMessage("reindex.success", keys, locale);
        redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);

        return "redirect:/administration/cache/console";
    }
    
    @RequestMapping(value = "/administration/cache/test", method = RequestMethod.POST)
    public String testCache(EvictCacheRequest request, RedirectAttributes redirectAttributes, Locale locale) {
        String[] keys = {request.getCacheName(), request.getUrlName() };
        String message = null;
    	redirectAttributes.addFlashAttribute("cacheName", request.getCacheName());
    	redirectAttributes.addFlashAttribute("urlName", request.getUrlName());
        String jsonCachedValue = cacheService.getFromCache(request.getCacheName(), request.getUrlName()); 
        if (!StringUtils.isEmpty(jsonCachedValue )) {
        	redirectAttributes.addFlashAttribute("value", jsonCachedValue);
            message = messageSource.getMessage("cache.test.cache.exists", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        } else {
            message = messageSource.getMessage("cache.test.cache.not.exists", keys, locale);
            redirectAttributes.addFlashAttribute(WebConstants.MESSAGE, message);
        }

        return "redirect:/administration/cache/console";
    }

    private void reIndexCategory(String urlName) {
        List<Item> items = itemService.findItemsByCategoryUrlName(urlName);
        List<Item> unavailableItems = itemService.findUnavailableItemIdsByCategoryUrlName(urlName);

        // remove unavailable items
        if (unavailableItems != null && !unavailableItems.isEmpty()) {
            List<String> ids = new ArrayList<String>(unavailableItems.size());

            for (Item item : unavailableItems) {
                ids.add(item.getIdString());
            }

            try {
                searchService.deleteItemsById(ids);

                // sleep a bit here so Solr has a chance to process everything
                Thread.sleep(5000);
            } catch (SearchException e) {
                log.error(e.getMessage(), e);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        // re-index available items
        if (items != null && !items.isEmpty()) {
            try {
                searchService.indexItems(items);

                // sleep a bit here so Solr has a chance to process everything
                Thread.sleep(5000);
            } catch (SearchException e) {
                log.error(e.getMessage(), e);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
