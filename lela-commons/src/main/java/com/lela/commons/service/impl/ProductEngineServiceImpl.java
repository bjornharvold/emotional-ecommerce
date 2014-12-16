/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

//~--- non-JDK imports --------------------------------------------------------

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.lela.commons.comparator.ComputedItemComparator;
import com.lela.commons.comparator.FunctionalFilterComparator;
import com.lela.commons.comparator.FunctionalFilterOptionComparator;
import com.lela.commons.comparator.ItemLowestPriceHighLowComparator;
import com.lela.commons.comparator.ItemLowestPriceLowHighComparator;
import com.lela.commons.comparator.ItemPopularityLowestPriceComparator;
import com.lela.commons.comparator.ItemRelevancyComparator;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.QuizAnswersEvent;
import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ComputedCategoryService;
import com.lela.commons.service.FunctionalFilterService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.QuizService;
import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.commons.service.UserService;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.Answer;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.ComputedCategory;
import com.lela.domain.document.ComputedItem;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.FunctionalFilterOption;
import com.lela.domain.document.FunctionalFilterPreset;
import com.lela.domain.document.FunctionalFilterPresetOption;
import com.lela.domain.document.Item;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Question;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.UserAnswer;
import com.lela.domain.document.UserQuestion;
import com.lela.domain.dto.AvailableFilters;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.dto.RelevantItemSearchQuery;
import com.lela.domain.dto.RelevantItemsForOwnerSearchQuery;
import com.lela.domain.dto.RelevantItemsInStoreSearchQuery;
import com.lela.domain.dto.RelevantOwnerItemsQuery;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.search.AbstractSearchResult;
import com.lela.domain.dto.search.CategoryCount;
import com.lela.domain.dto.search.GroupedSearchResult;
import com.lela.domain.dto.search.ItemSearchQuery;
import com.lela.domain.dto.search.ItemsForOwnerSearchQuery;
import com.lela.domain.dto.search.ItemsInStoreSearchQuery;
import com.lela.domain.dto.search.SearchResult;
import com.lela.domain.enums.FunctionalFilterType;
import com.lela.domain.enums.FunctionalSortType;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.QuestionType;
import com.lela.util.utilities.number.NumberUtils;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/14/11
 * Time: 10:14 PM
 * Responsibility: Implements the ProductEngineService interface
 */
@Service("productEngineService")
public class ProductEngineServiceImpl implements ProductEngineService {

    /**
     * Logger
     */
    private final static Logger log = LoggerFactory.getLogger(ProductEngineServiceImpl.class);

    //~--- fields -------------------------------------------------------------

    /**
     * User Service bean
     */
    private final UserService userService;

    private final SearchService searchService;

    private final CategoryService categoryService;

    private final ItemService itemService;

    private final QuizService quizService;

    private final FunctionalFilterService functionalFilterService;

    private final ComputedCategoryService computedCategoryService;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     * @param userService             userService
     * @param searchService           searchService
     * @param categoryService         categoryService
     * @param itemService             itemService
     * @param quizService             quizService
     * @param functionalFilterService functionalFilterService
     * @param computedCategoryService computedCategoryService
     */
    @Autowired
    public ProductEngineServiceImpl(UserService userService,
                                    SearchService searchService, CategoryService categoryService,
                                    ItemService itemService, QuizService quizService,
                                    FunctionalFilterService functionalFilterService,
                                    ComputedCategoryService computedCategoryService) {
        this.userService = userService;
        this.searchService = searchService;
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.quizService = quizService;
        this.functionalFilterService = functionalFilterService;
        this.computedCategoryService = computedCategoryService;
    }


    //~--- methods ------------------------------------------------------------
    
    public void resetFunctionalFiltersForCategoryGroupAndUser(String categoryGroupUrl, String userCode){
    	boolean updateNeeded = false;
    	
    	//Get all the categories in this categoryGroup
    	List<Category> categoryList = categoryService.findCategoriesByCategoryGroupUrlName(categoryGroupUrl);
    	List<FunctionalFilterPreset> presetList = userService.findFunctionalFilterPresets(userCode);
    	if (categoryList != null){
	    	for (Category category : categoryList) {
	            List<FunctionalFilter> functionalFilters = functionalFilterService.findFunctionalFiltersByUrlName(category.getRlnm());
	            
	            if ((functionalFilters != null) && !functionalFilters.isEmpty()) {
	            	for (FunctionalFilter functionalFilter : functionalFilters) {
	            		FunctionalFilterPreset existingPreset = getFunctionalFilterPreset(presetList, category.getRlnm(), functionalFilter.getKy());
	            		if (existingPreset != null){         		
		            		existingPreset.setRlnm(category.getRlnm());
		            		existingPreset.setKy(functionalFilter.getKy());

		            		List<FunctionalFilterPresetOption> presetOptionList = new ArrayList<FunctionalFilterPresetOption>();
		            		
		            		//Set the correct price range for DYNAMIC_RANGE filters
		            		if (functionalFilter.getTp().equals(FunctionalFilterType.DYNAMIC_RANGE)){
	                            for (FunctionalFilterOption option : functionalFilter.getPtns()) {
	                                	presetOptionList.add(new FunctionalFilterPresetOption(option.getKy(), option.getVl()));
	                        	}
                            }
		            		existingPreset.setPtns(presetOptionList);
		            		updateNeeded = true;
	            		}
					}
	            }
			}
            if (updateNeeded){         	
            	userService.saveFunctionalFilterPresets(userCode, presetList);
            }
    	}
    }

    /**
     * Method description
     *
     * @param query query
     * @return Return value
     */
    @Override
    public ItemPage<Item> findItemsByCategory(CategoryItemsQuery query) {
        ItemPage<Item> result = null;
        FunctionalSortType sortBy = null;

        // retrieve category from cache
        Category category = categoryService.findCategoryByUrlName(query.getCategoryUrlName());

        // retrieve all items from cache for this category
        List<Item> items = itemService.findItemsByCategoryUrlName(query.getCategoryUrlName());

        if (category != null) {

            // verify that we have items to work with
            if (items != null) {
                List<FunctionalFilter> functionalFilters = processFiltersAndFilterOutItemsByPresets(category, items, query);

                AvailableFilters availableFilters = new AvailableFilters();
                if (!items.isEmpty()) {

                    // Check remaining available filters to eliminate ones that will give zero results
                    calculateAvailableFilterOptions(functionalFilters, items, availableFilters);

                    // sort
                    if (query.getSort()) {
                        sortBy = sortItems(items, query.getSortBy());
                    }

                    // paginate
                    // paginate if required
                    if ((query.getPage() != null) && (query.getSize() != null)) {
                        int beg = query.getPage() * query.getSize();
                        int end = query.getPage() * query.getSize() + query.getSize();
                        List<Item> subList;

                        if ((beg < items.size()) && (end < items.size())) {
                            subList = items.subList(beg, end);
                            result = new ItemPage<Item>(subList, new PageRequest(query.getPage(), query.getSize()), items.size(),
                                    functionalFilters, sortBy, availableFilters);
                        } else if ((beg < items.size()) && (end >= items.size())) {

                            // in case the max results exceeds the list size
                            subList = items.subList(beg, items.size());
                            result = new ItemPage<Item>(subList, new PageRequest(query.getPage(), query.getSize()), items.size(),
                                    functionalFilters, sortBy, availableFilters);
                        }
                    } else {
                        result = new ItemPage<Item>(items, null, items.size(), functionalFilters, sortBy, availableFilters);
                    }

                } else {
                    // we just return the functional filters
                    if (query.getPage() != null && query.getSize() != null) {
                        result = new ItemPage<Item>(functionalFilters, sortBy, query.getPage(), query.getSize(), availableFilters);
                    } else {
                        result = new ItemPage<Item>(functionalFilters, sortBy, availableFilters);
                    }
                }

            } else {
                log.warn("Category with urlName: " + query.getCategoryUrlName() + " does not have any items");
            }
        } else {
            log.warn("Could not find a category with urlName: " + query.getCategoryUrlName());
        }

        return result;
    }

    private List<FunctionalFilter> processFiltersAndFilterOutItemsByPresets(Category category, List<Item> items, CategoryItemsQuery query) {
        // retrieve functional filters for category
        List<FunctionalFilter> functionalFilters = functionalFilterService.findFunctionalFiltersByUrlName(category.getRlnm());

        if ((functionalFilters != null) && !functionalFilters.isEmpty()) {
            updateFunctionalPresets(category, query.getUserCode(), functionalFilters, query.getFilters(), query.getUpdate());

            List<FunctionalFilterPreset> presets = userService.findFunctionalFilterPresets(query.getUserCode());

            // handle functional filters templates if available - this needs to be added BEFORE any sort of filtering occurs so
            // we don't loose any potential items
            preProcessFunctionalFilters(functionalFilters, presets);

            // filter items based on our user defined filters
            for (FunctionalFilter ff : functionalFilters) {
                switch (ff.getTp()) {
                    case DYNAMIC_RANGE:
                        filterOnDynamicRange(ff, presets, items);

                        break;

                    case MULTIPLE_CHOICE_SINGLE_ANSWER:
                    case MULTIPLE_CHOICE_MULTIPLE_ANSWER_AND:
                        filterOnMultipleChoiceMultipleAnswerAND(ff, presets, items);

                        break;

                    case MULTIPLE_CHOICE_MULTIPLE_ANSWER_OR:
                        filterOnMultipleChoiceMultipleAnswerOR(ff, presets, items);

                        break;
                    case BRAND:
                        filterOnBrand(ff, presets, items);

                        break;
                    case STORE:
                        filterOnStore(ff, presets, items);

                        break;
                    case CATEGORY:
                        filterOnCategory(ff, presets, items);

                        break;

                    default:
                        if (log.isWarnEnabled()) {
                            log.warn("No support for filtering on: " + ff.getTp() + " yet");
                        }
                }
            }
        } else {
            log.warn("Category with urlName: " + query.getCategoryUrlName()
                    + " does not have any functional filters");
        }
        return functionalFilters;
    }

    /**
     * This is the same as the method directly above. The difference is the search is run externally with Solr.
     *
     * @param query query
     * @return Return value
     */
    @Override
    public SearchResult searchForItemsUsingSolr(ItemSearchQuery query) throws SearchException {
        SearchResult solrResult = null;

        // first we query the search service for results
        solrResult = searchService.searchForItems(query);

        processSolrResult(solrResult);

        return solrResult;
    }

    /**
     * Search for store items. Group by category.
     *
     * @param query query
     * @return Return value
     */
    @Override
    public GroupedSearchResult searchForItemsInStoreUsingSolr(ItemsInStoreSearchQuery query) throws SearchException {
        // first we query the search service for results
        GroupedSearchResult solrResult = searchService.searchForItemsInStore(query);

        processGroupedSolrResult(solrResult);

        return solrResult;
    }

    @Override
    public SearchResult searchForCategoryItemsInStoreUsingSolr(ItemsInStoreSearchQuery query) throws SearchException {
        // first we query the search service for results
        SearchResult solrResult = searchService.searchForCategoryItemsInStore(query);

        processSolrResult(solrResult);

        return solrResult;
    }

    /**
     * Processes a solr search result that has a result list and one facet on ctgry
     *
     * @param solrResult solrResult
     */
    private void processSolrResult(SearchResult solrResult) {

        if (solrResult != null) {

            // first we get all the actual results
            SolrDocumentList documents = solrResult.getQueryResponse().getResults();

            if (documents != null && !documents.isEmpty()) {
                List<String> itemUrlNames = new ArrayList<String>(documents.size());

                for (SolrDocument document : documents) {
                    itemUrlNames.add((String) document.getFieldValue("rlnm"));
                }

                // retrieve actual items from the db
                List<Item> items = itemService.findItemsByUrlName(itemUrlNames);

                // create an item page based on the result
                solrResult.setItems(new ItemPage<Item>(items, new PageRequest(solrResult.getPage(), solrResult.getSize()), documents.getNumFound()));
            }

            addCategoryCounts(solrResult);
        }

    }

    private void addCategoryCounts(AbstractSearchResult solrResult) {
        FacetField facetField = solrResult.getQueryResponse().getFacetField("ctgry");

        if (facetField != null && facetField.getValues() != null && !facetField.getValues().isEmpty()) {
            List<CategoryCount> categories = new ArrayList<CategoryCount>(facetField.getValueCount());

            // then we get the categories that were part of this result
            for (FacetField.Count count : facetField.getValues()) {
                Category category = categoryService.findCategoryByUrlName(count.getName());

                categories.add(new CategoryCount(category, count.getCount()));
            }

            solrResult.setCategories(categories);
        }
    }

    private void processGroupedSolrResult(GroupedSearchResult solrResult) {
        Map<String, ItemPage<Item>> result = new HashMap<String, ItemPage<Item>>();

        if (solrResult != null) {
            GroupResponse response = solrResult.getQueryResponse().getGroupResponse();
            FacetField facetField = solrResult.getQueryResponse().getFacetField("ctgry");

            if (response != null && response.getValues() != null) {
                List<GroupCommand> groupCommands = response.getValues();

                for (GroupCommand gc : groupCommands) {
                    String groupName = gc.getName();
                    Integer totalMatches = gc.getMatches();
                    Integer nGroups = gc.getNGroups();
                    List<Group> groups = gc.getValues();

                    if (groups != null && !groups.isEmpty()) {
                        for (Group group : groups) {
                            String category = group.getGroupValue();
                            long matches = -1;

                            if (group.getResult() != null && !group.getResult().isEmpty()) {
                                List<String> itemUrlNames = new ArrayList<String>(group.getResult().size());

                                for (SolrDocument document : group.getResult()) {
                                    itemUrlNames.add((String) document.getFieldValue("rlnm"));
                                }

                                // locate the total matches from the facet
                                for (FacetField.Count count : facetField.getValues()) {
                                    if (StringUtils.equals(count.getName(), category)) {
                                        matches = count.getCount();
                                    }
                                }

                                List<Item> items = itemService.findItemsByUrlName(itemUrlNames);

                                if (items != null && !items.isEmpty()) {
                                    result.put(category, new ItemPage<Item>(items, new PageRequest(0, items.size()), matches));
                                }
                            }
                        }
                    }
                }

                addCategoryCounts(solrResult);
            }

            solrResult.setItems(result);
        }
    }

    @Override
    public GroupedSearchResult searchForItemsForOwnerUsingSolr(ItemsForOwnerSearchQuery query) throws SearchException {
        // first we query the search service for results
        GroupedSearchResult solrResult = searchService.searchForItemsForOwner(query);

        processGroupedSolrResult(solrResult);

        return solrResult;
    }

    @Override
    public SearchResult searchForCategoryItemsForOwnerUsingSolr(ItemsForOwnerSearchQuery query) throws SearchException {
        // first we query the search service for results
        SearchResult solrResult = searchService.searchForCategoryItemsForOwner(query);

        processSolrResult(solrResult);

        return solrResult;
    }

    @Override
    public void answerQuestions(String userCode, QuizAnswers answers) {
        List<UserAnswer> list = null;

        UserAnswer genderAnswer = null;
        if (answers != null && answers.getList() != null && !answers.getList().isEmpty()) {
            list = new ArrayList<UserAnswer>(answers.getList().size());

            for (QuizAnswer qa : answers.getList()) {
                UserAnswer ua = answerQuestion(userCode, answers.getAffiliateId(), answers.getApplicationId(), answers.getQuizUrlName(), qa.getQuestionUrlName(), qa.getAnswerKey());

                if (ua != null) {
                    list.add(ua);
                    if (QuestionType.GENDER.equals(ua.getQstn().getTp())) {
                        genderAnswer = ua;
                    }
                }
            }
        }

        // save answer on logged in user
        userService.saveUserAnswers(userCode, list);
        EventHelper.post(new QuizAnswersEvent(userService.findUserByCode(userCode), answers));

        // re-compute weighted motivators
        recomputeMotivatorsBasedOnQuizAnswers(userCode);

        // dirty computed calculated matches for all categories
        computedCategoryService.setComputedCategoriesToDirty(userCode);

        // recompute this user's relationship with lela
        userService.recomputeFriendshipLevel(userCode);

        // IF there was a gender question, save the gender
        if (genderAnswer != null && genderAnswer.getNm() != null) {
            if ("male".equals(genderAnswer.getNm().toLowerCase())) {
                userService.saveGender(userCode, Gender.MALE);
            } else if ("female".equals(genderAnswer.getNm().toLowerCase())) {
                userService.saveGender(userCode, Gender.FEMALE);
            }
        }
    }

    /**
     * Creates and appends a UserAnswer to the User + computes the weighted motivators straight on the user.
     *
     *
     *
     * @param userCode        userCode
     * @param questionUrlName questionUrlName
     * @param answerKey       answerKey
     */
    private UserAnswer answerQuestion(String userCode, String affiliateId, String applicationId, String quizUrlName, String questionUrlName, String answerKey) {
        UserAnswer ua = null;
        Question question = quizService.findQuestionByUrlName(questionUrlName);

        if (question == null) {
            throw new IllegalStateException("Question with url name: " + questionUrlName + " does not exist!");
        }

        UserQuestion uq = new UserQuestion(affiliateId, applicationId, quizUrlName, question.getRlnm(), question.getNm(), question.getTp());
        Answer a = null;

        for (Answer a1 : question.getNswrs()) {
            if (StringUtils.equals(a1.getKy(), answerKey)) {
                a = a1;
            }
        }

        if (a != null) {
            ua = new UserAnswer();

            ua.setMtvtrs(a.getMtvtrs());
            ua.setQstn(uq);
            ua.setKy(a.getKy());
            ua.setNm(a.getNm());
            ua.setMg(a.getMg());
            ua.setDt(new Date());
        } else {
            log.error("Could not find answer with ID: " + answerKey + " on question with ID: " + question.getId());
        }

        return ua;
    }

    /**
     * Method description
     *
     * @param userMotivators userMotivators
     * @param itemMotivators itemMotivators
     * @return Return value
     */
    public Map<String, Integer> computeMotivatorRelevancy(Map<String, Integer> userMotivators,
                                                          Map<String, Integer> itemMotivators) {
        Map<String, Integer> result = new HashMap<String, Integer>();

        // compute motivators into relevancy
        for (String key : userMotivators.keySet()) {
            if (itemMotivators.containsKey(key)) {
                Integer resultMotivatorValue = 0;

                // e.g. 6
                Integer userMotivatorValue = userMotivators.get(key);

                // if it is not greater than 0, there is no relevancy at all
                if (userMotivatorValue > 0) {

                    // e.g. 9
                    Integer itemMotivatorValue = itemMotivators.get(key);

                    // e.g. 3
                    Integer itemDistance = itemMotivatorValue - userMotivatorValue;
                    StringBuilder uniqueIdentifier = new StringBuilder(userMotivatorValue.toString());

                    if (itemDistance > 0) {

                        // product motivator is greater than user motivator we need to change the
                        // item motivator value to be less
                        // than user motivator but with the same distance away from the user motivator
                        // e.g. user motivator is 6
                        // e.g. item motivator is 9
                        // e.g. distance is 3
                        // e.g. so we need to subtract 3 from 6 to end up on the opposite side of the 6 spectrum
                        // e.g. result is 63
                        if (userMotivatorValue - itemDistance < 0) {
                            uniqueIdentifier.append("0");
                        } else {
                            uniqueIdentifier.append((userMotivatorValue - itemDistance));
                        }
                    } else {

                        // e.g. "69"
                        uniqueIdentifier.append(itemMotivatorValue.toString());
                    }

                    // e.g. 69
                    Integer uniqueIdentifierValue = Integer.parseInt(uniqueIdentifier.toString());

                    // e.g. 69 % 60 = 9
                    Integer subtractFromUniqueIdentifierValue = uniqueIdentifierValue % (userMotivatorValue * 10);

                    // if modulus 10 is 0 here it means the last number was a 0
                    // and that there is no relevancy here worth thinking about
                    if (uniqueIdentifierValue % 10 == 0) {
                        resultMotivatorValue = 0;
                    } else {

                        // e.g. 6 * (69 - 60) = 54
                        resultMotivatorValue = userMotivatorValue
                                * (uniqueIdentifierValue
                                - (uniqueIdentifierValue - subtractFromUniqueIdentifierValue));
                    }

                    /*
                     * System.out.println("key: " + key);
                     * System.out.println("user motivator value: " + userMotivatorValue);
                     * System.out.println("item motivator value: " + itemMotivatorValue);
                     * System.out.println("item distance: " + itemDistance);
                     * System.out.println("unique identifer: " + uniqueIdentifier.toString());
                     * System.out.println("unique identifer value: " + uniqueIdentifierValue);
                     * System.out.println("motivator relevancy: " + resultMotivatorValue);
                     */

                    // e.g. 6 * (69 - (69 - 9)) = 36
                    result.put(key, resultMotivatorValue);
                }
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param motivatorRelevancy motivatorRelevancy
     * @return Return value
     */
    @Override
    public Integer computeTotalRelevancy(Map<String, Integer> motivatorRelevancy) {
        Integer totalRelevancy = 0;

        for (Integer relevancy : motivatorRelevancy.values()) {
            totalRelevancy += relevancy;
        }

        return totalRelevancy;
    }

    /**
     * Method description
     *
     * @param categories categories
     * @param userCode       userCode
     * @param page       page
     * @param size       size
     * @return Return value
     */
    @Override
    public Map<String, List<RelevantItem>> findHighestMatchingItemPerCategory(List<Category> categories, String userCode,
                                                                              Integer page, Integer size) {
        Map<String, List<RelevantItem>> result = null;

        if (StringUtils.isNotBlank(userCode) && (categories != null) && categories.iterator().hasNext()) {
            result = new HashMap<String, List<RelevantItem>>();

            // loop through categories and find the highest matching item for each category
            for (Category category : categories) {

                // we should only have to do this once per user for a very long time
                // if the user has no pre-computed values, we need to load up all items for the category
                // and compute and store the matches so we then can retrieve at a later date
                // it's our way of saving user - item matches
                validateAndComputeCategoryMatches(category.getRlnm(), userCode);

                // try to load up from the user object first
                ComputedCategory cc = computedCategoryService.findComputedCategory(userCode, category.getRlnm());

                if (cc != null) {
                    List<ComputedItem> computedItems = cc.getTms();

                    if ((computedItems != null) && !computedItems.isEmpty()) {

                        // first we sort to get the highest products to bubble up
                        Collections.sort(computedItems, new ComputedItemComparator());

                        // now we can retrieve the highest matching items we want
                        List<ComputedItem> sublist = null;

                        if (size > computedItems.size()) {
                            sublist = computedItems;
                        } else {
                            sublist = computedItems.subList(0, size);
                        }

                        if (sublist != null) {
                            List<RelevantItem> relevantItems = new ArrayList<RelevantItem>(sublist.size());

                            for (ComputedItem ci : sublist) {

                                // retrieve item from cache
                                Item item = itemService.findItemByUrlName(ci.getRlnm());
                                RelevantItem ri = new RelevantItem(item, ci.getTtlrlvncy(), ci.getTtlrlvncynmbr(),
                                        ci.getMtvtrrlvncy());

                                relevantItems.add(ri);
                            }

                            result.put(cc.getRlnm(), relevantItems);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param query query
     * @return Return value
     */
    @Override
    public RelevantItem findRelevantItem(RelevantItemQuery query) {
        RelevantItem result = null;
        Item item = itemService.findItemByUrlName(query.getUrlName());

        if (item != null) {
            List<Item> items = new ArrayList<Item>(1);
            items.add(item);

            // we should only have to do this once per user for a very long time
            // if the user has no pre-computed values, we need to load up all items for the category
            // and compute and store the matches so we then can retrieve at a later date
            // it's our way of saving user - item matches
            validateAndComputeCategoryMatches(item.getCtgry().getRlnm(), query.getUserCode());

            if (!items.isEmpty()) {

                // run business domain computations where we match users with items
                List<RelevantItem> list = retrievePreComputedUserMatches(items, query.getUserCode());

                result = list.get(0);
            }
        }
        return result;
    }

    /**
     * Returns a list of relevant items matching a map of motivators. It's sorts the relevant items from high to low
     * and cuts off results that are below the specified percentage point
     *
     * @param query query
     * @return Return value
     */
    @Override
    public ItemPage<RelevantItem> findRelevantItemsByCategory(CategoryItemsQuery query) {
        ItemPage<RelevantItem> result = null;
        FunctionalSortType sortBy = null;

        // we should only have to do this once per user for a very long time
        // if the user has no pre-computed values, we need to load up all items for the category
        // and compute and store the matches so we then can retrieve at a later date
        // it's our way of saving user - item matches
        validateAndComputeCategoryMatches(query.getCategoryUrlName(), query.getUserCode());

        // retrieve category from cache
        Category category = categoryService.findCategoryByUrlName(query.getCategoryUrlName());

        // retrieve all items from cache for this category
        List<Item> items = itemService.findItemsByCategoryUrlName(query.getCategoryUrlName());

        if (category != null) {

            // verify that we have items to work with
            if (items != null) {
                List<FunctionalFilter> functionalFilters = processFiltersAndFilterOutItemsByPresets(category, items, query);

                AvailableFilters availableFilters = new AvailableFilters();

                if (!items.isEmpty()) {

                    // Check remaining available filters to eliminate ones that will give zero results
                    calculateAvailableFilterOptions(functionalFilters, items, availableFilters);

                    // run business domain computations where we match users with items
                    List<RelevantItem> list = retrievePreComputedUserMatches(items, query.getUserCode());

                    // sort
                    if (query.getSort()) {
                        sortBy = sortRelevantItems(list, query.getSortBy());
                    }

                    // paginate if required
                    if ((query.getPage() != null) && (query.getSize() != null)) {
                        int beg = query.getPage() * query.getSize();
                        int end = query.getPage() * query.getSize() + query.getSize();
                        List<RelevantItem> subList;

                        if ((beg < list.size()) && (end < list.size())) {
                            subList = list.subList(beg, end);
                            result = new ItemPage<RelevantItem>(subList, new PageRequest(query.getPage(), query.getSize()), list.size(),
                                    functionalFilters, sortBy, availableFilters);
                        } else if ((beg < list.size()) && (end >= list.size())) {

                            // in case the max results exceeds the list size
                            subList = list.subList(beg, list.size());
                            result = new ItemPage<RelevantItem>(subList, new PageRequest(query.getPage(), query.getSize()), list.size(),
                                    functionalFilters, sortBy, availableFilters);
                        }
                    } else {
                        result = new ItemPage<RelevantItem>(list, functionalFilters, sortBy);
                    }

                } else {
                    result = new ItemPage<RelevantItem>(functionalFilters, sortBy, query.getPage(), query.getSize(), availableFilters);
                }

            } else {
                log.warn("Category with urlName: " + query.getCategoryUrlName() + " does not have any items");
            }
        } else {
            log.warn("Could not find a category with urlName: " + query.getCategoryUrlName());
        }

        return result;
    }

    /**
     * Method description
     *
     * @param query query
     * @return Return value
     */
    @Override
    public List<RelevantItem> findRelevantItemsByOwner(RelevantOwnerItemsQuery query) {
        List<Item> list = itemService.findItemsByOwnerName(query.getOwnerName());

        // run business domain computations where we match users with items
        return computeWithMatchingEngine(list, query.getUserCode(), query.getMotivator());
    }

    /**
     * @param query query
     * @return Map
     */
    @Override
    public SearchResult searchForRelevantItemsUsingSolr(RelevantItemSearchQuery query) throws SearchException {
        SearchResult result = searchForItemsUsingSolr(query);

        if (result.getCategories() != null && !result.getCategories().isEmpty()) {
            List<String> categoryUrlNames = new ArrayList<String>(result.getCategories().size());
            for (CategoryCount count : result.getCategories()) {
                categoryUrlNames.add(count.getCategory().getRlnm());
            }

            result.setRelevantItems(processRelevancyComputations(query.getUserCode(), categoryUrlNames, result.getItems()));
        }

        return result;
    }

    /**
     * @param query query
     * @return Map
     */
    @Override
    public GroupedSearchResult searchForRelevantItemsInStoreUsingSolr(RelevantItemsInStoreSearchQuery query) throws SearchException {
        GroupedSearchResult result = searchForItemsInStoreUsingSolr(query);

        result.setRelevantItems(processRelevancyComputationsForGroups(query.getUserCode(), result.getItems()));

        return result;
    }

    /**
     * @param query query
     * @return Map
     */
    @Override
    public SearchResult searchForRelevantCategoryItemsInStoreUsingSolr(RelevantItemsInStoreSearchQuery query) throws SearchException {
        SearchResult result = searchForCategoryItemsInStoreUsingSolr(query);

        if (result.getCategories() != null && !result.getCategories().isEmpty()) {
            List<String> categoryUrlNames = new ArrayList<String>(result.getCategories().size());
            for (CategoryCount count : result.getCategories()) {
                categoryUrlNames.add(count.getCategory().getRlnm());
            }

            result.setRelevantItems(processRelevancyComputations(query.getUserCode(), categoryUrlNames, result.getItems()));
        }

        return result;
    }

    /**
     * @param query query
     * @return Map
     */
    @Override
    public GroupedSearchResult searchForRelevantItemsForOwnerUsingSolr(RelevantItemsForOwnerSearchQuery query) throws SearchException {
        GroupedSearchResult result = searchForItemsForOwnerUsingSolr(query);

        if (result != null && result.getItems() !=null && !result.getItems().isEmpty()) {
            result.setRelevantItems(processRelevancyComputationsForGroups(query.getUserCode(), result.getItems()));
        }

        return result;
    }

    /**
     * @param query query
     * @return Map
     */
    @Override
    public SearchResult searchForRelevantCategoryItemsForOwnerUsingSolr(RelevantItemsForOwnerSearchQuery query) throws SearchException {
        SearchResult result = searchForCategoryItemsForOwnerUsingSolr(query);

        if (result != null && result.getCategories() != null && !result.getCategories().isEmpty()) {
            List<String> categoryUrlNames = new ArrayList<String>(result.getCategories().size());
            for (CategoryCount count : result.getCategories()) {
                categoryUrlNames.add(count.getCategory().getRlnm());
            }

            result.setRelevantItems(processRelevancyComputations(query.getUserCode(), categoryUrlNames, result.getItems()));
        }

        return result;
    }

    /**
     * Method looks at user's motivators and filters to determine the products that best match the specified item
     *
     * @param userCode userCode
     * @param item item
     * @return List
     */
    @Override
    public List<AbstractItem> findSupplementaryItemsByItem(String userCode, AbstractItem item, Integer size) {
        List<AbstractItem> result = null;

        Category category = item.getCtgry();

        if (userService.shouldUserSeeRecommendedProducts(userCode)) {
            // TODO this will be a little intensive but we're hoping to offload this at a later date to an external computation service
            // TODO the good part is that this should only happen once per category per user
            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(category.getRlnm());
            query.setPage(0);
            query.setSize(size);
            query.setSort(true);
            query.setSortBy(FunctionalSortType.RECOMMENDED);
            query.setUpdate(false);
            query.setUserCode(userCode);
            ItemPage<RelevantItem> page = findRelevantItemsByCategory(query);

            // convert it to the result list
            if (page != null && page.hasContent()) {
                result = new ArrayList<AbstractItem>(page.getContent());
            }
        } else {
            // TODO this will be a little intensive but we're hoping to offload this at a later date to an external computation service
            // TODO the good part is that this should only happen once per category per user
            CategoryItemsQuery query = new CategoryItemsQuery();
            query.setCategoryUrlName(category.getRlnm());
            query.setPage(0);
            query.setSize(size);
            query.setSort(true);
            query.setSortBy(FunctionalSortType.POPULARITY);
            query.setUpdate(false);
            query.setUserCode(userCode);
            ItemPage<Item> page = findItemsByCategory(query);

            // convert it to the result list
            if (page != null && page.hasContent()) {
                result = new ArrayList<AbstractItem>(page.getContent());
            }
        }

        // we want to avoid returning the item we are already displaying
        if (result != null && !result.isEmpty()) {
            AbstractItem dupe = null;

            for (AbstractItem ai : result) {
                if (StringUtils.equals(ai.getRlnm(), item.getRlnm())) {
                    dupe = ai;
                }
            }

            if (dupe != null) {
                result.remove(dupe);
            }
        }

        return result;
    }

    /**
     * Computes relevancy results on items that have been grouped by category
     *
     * @param userCode  userCode
     * @param pages pages
     * @return Returns a map of RelevantItem pages
     */
    private Map<String, ItemPage<RelevantItem>> processRelevancyComputationsForGroups(String userCode, Map<String, ItemPage<Item>> pages) {
        Map<String, ItemPage<RelevantItem>> result = null;

        if (pages != null && !pages.isEmpty()) {
            result = new HashMap<String, ItemPage<RelevantItem>>();

            // loop through our categories to get the relevant items
            for (String categoryUrlName : pages.keySet()) {

                // we should only have to do this once per user for a very long time
                // if the user has no pre-computed values, we need to load up all items for the category
                // and compute and store the matches so we then can retrieve at a later date
                // it's our way of saving user - item matches
                validateAndComputeCategoryMatches(categoryUrlName, userCode);

                ItemPage<Item> page = pages.get(categoryUrlName);
                if (page != null && page.hasContent()) {
                    // run business domain computations where we match users with items
                    List<RelevantItem> list = retrievePreComputedUserMatches(page.getContent(), userCode);

                    // now we sort by total relevancy
                    FunctionalSortType sortBy = sortRelevantItems(list, null);

                    // convert to new ItemPage
                    ItemPage<RelevantItem> relevantItemPage = new ItemPage<RelevantItem>(list,
                            new PageRequest(0, list.size()), page.getTotalElements(), sortBy);

                    result.put(categoryUrlName, relevantItemPage);
                }
            }
        }
        return result;
    }

    /**
     * Computes relevancy results on items that are not grouped by category
     *
     * @param userCode             userCode
     * @param categoryUrlNames categoryUrlNames
     * @param page             page
     * @return Returns a list of relevant items
     */
    private ItemPage<RelevantItem> processRelevancyComputations(String userCode, List<String> categoryUrlNames, ItemPage<Item> page) {
        ItemPage<RelevantItem> result = null;

        if (categoryUrlNames != null && !categoryUrlNames.isEmpty() && page != null && page.hasContent()) {

            // loop through our categories to compute relevancy matches
            for (String urlName : categoryUrlNames) {

                // we should only have to do this once per user for a very long time
                // if the user has no pre-computed values, we need to load up all items for the category
                // and compute and store the matches so we then can retrieve at a later date
                // it's our way of saving user - item matches
                validateAndComputeCategoryMatches(urlName, userCode);
            }

            // run business domain computations where we match users with items
            List<RelevantItem> list = retrievePreComputedUserMatches(page.getContent(), userCode);

            // convert to new ItemPage
            result = new ItemPage<RelevantItem>(list, new PageRequest(page.getNumber(), page.getSize()), page.getTotalElements());
        }

        return result;
    }

    /**
     * Method description
     *
     * @param items     items
     * @param userCode  userCode
     * @param motivator motivator
     * @return List of relevant items matching the user
     */
    private List<RelevantItem> computeWithMatchingEngine(List<Item> items, String userCode, Motivator motivator) {
        List<RelevantItem> result = null;

        // add weight and relevancy
        if (items != null && StringUtils.isNotBlank(userCode) && motivator != null) {
            result = new ArrayList<RelevantItem>();

            for (Item item : items) {
                RelevantItem ri = null;
                Map<String, Integer> motivatorRelevancy = computeMotivatorRelevancy(motivator.getMtvtrs(), item.getMtvtrs());

                // compute total relevancy
                Integer totalRelevancy = computeTotalRelevancy(motivatorRelevancy);

                ri = new RelevantItem(item, totalRelevancy, motivatorRelevancy);

                result.add(ri);
            }

            if (!result.isEmpty()) {
                // sort by relevancy
                Collections.sort(result, new ItemRelevancyComparator());

                // see if computed category exists - it shouldn't yet
                ComputedCategory cc = computedCategoryService.findComputedCategory(userCode, result.get(0).getCtgry().getRlnm());

                // add the new computed category to the user
                if (cc == null) {
                    // add the category if it doesn't exist
                    cc = new ComputedCategory(userCode, result.get(0).getCtgry().getRlnm());
                }

                populateComputedCategoryMaxScore(cc, result.get(0));

                for (RelevantItem ri : result) {
                    float percentageRelevancy = ri.getTtlrlvncy().floatValue() / cc.getMxscr().floatValue();

                    // generates a percentage
                    // e.g. 0.7 * 100 ==> 70
                    Float segment = percentageRelevancy * 100f;

                    // update only if not yet populated or if the value differs
                    ri.setTtlrlvncynmbr(segment.intValue());

                    // add the computed item
                    cc.getTms().add(new ComputedItem(ri.getRlnm(), ri.getMtvtrrlvncy(), ri.getTtlrlvncy(),
                            ri.getTtlrlvncynmbr()));
                }

                // save all these computations for next time to avoid valuable cpu cycles
                computedCategoryService.saveComputedCategory(cc);
            }
        }

        return result;
    }

    /**
     * This method will first validate if it is necessary to compute matches
     * or if the computation has already taken place
     *
     * @param categoryUrlName categoryUrlName
     * @param userCode            userCode
     */
    private void validateAndComputeCategoryMatches(String categoryUrlName, String userCode) {
        boolean needsRefresh = false;
        ComputedCategory cc = computedCategoryService.findComputedCategory(userCode, categoryUrlName);
        Category category = categoryService.findCategoryByUrlName(categoryUrlName);

        if (cc == null) {
            // first we check if we have computed this category before
            needsRefresh = true;
        } else if (cc.getDrty() != null && cc.getDrty()) {
            // we might have computed it before but the values might have been set to dirty
            needsRefresh = true;
        } else if (cc.getDt() == null || category.getLdt().after(cc.getDt())) {
            // if this category was updated after these computed values were created, we have to refresh
            needsRefresh = true;
        } else {
            // we might still need a refresh if the item count for that category has changed
            // this should really not happen as the dirty flag and the date check should take care of all of that
            Long count = itemService.findItemCountByCategoryUrlName(categoryUrlName);
            Integer computedCount = cc.getTms().size();

            if (computedCount.longValue() != count) {
                needsRefresh = true;
            }
        }

        if (needsRefresh) {
            // clear out previous computations
            computedCategoryService.removeComputedCategory(userCode, categoryUrlName);

            // load up all items for that category
            List<Item> items = itemService.findItemsByCategoryUrlName(categoryUrlName);

            if ((items != null) && !items.isEmpty()) {
                Motivator motivator = userService.findBestMotivator(userCode);

                // run business domain computations where we match users with items
                computeWithMatchingEngine(items, userCode, motivator);

                // all calculations have been saved
            }
        }
    }

    /**
     * we need a max score to compare all less items with
     * that max score comes from either the top matching item or the user's previous top score
     * for that category
     * if the max score from the top matching item is greater than the user's current score
     * we also have to update the user's current score
     *
     * @param cc        cc
     * @param topScorer topScorer
     */
    private void populateComputedCategoryMaxScore(ComputedCategory cc, RelevantItem topScorer) {

        // first set max score to be the highest result set value
        Category category = topScorer.getCtgry();

        if (category != null) {
            String categoryUrlName = category.getRlnm();

            if (cc != null) {
                // we need to update the user's value here
                cc.setMxscr(topScorer.getTtlrlvncy());
                cc.setMxscrtmrlnm(topScorer.getRlnm());
            } else {
                log.warn("User doesn't contain a computed category for: " + categoryUrlName);
            }
        } else {
            log.warn("Item: " + topScorer.getRlnm() + " is missing its category");
        }
    }

    /**
     * Method description
     *
     * @param userCode userCode
     */
    private void recomputeMotivatorsBasedOnQuizAnswers(String userCode) {
        if (log.isDebugEnabled()) {
            log.debug("Recomputing weighted user motivators...");
        }

        List<UserAnswer> answers = userService.findUserAnswers(userCode);

        if (answers != null && !answers.isEmpty()) {
            // we just added a new answer for this user
            Map<String, List<Integer>> motivators = new HashMap<String, List<Integer>>();

            // We're going to loop through all answers and compute the average based on each motivator value.
            for (UserAnswer ua : answers) {
                if (ua.getMtvtrs() != null && !ua.getMtvtrs().isEmpty()) {
                    for (String motivatorKey : ua.getMtvtrs().keySet()) {
                        if (!motivators.containsKey(motivatorKey)) {
                            motivators.put(motivatorKey, new ArrayList<Integer>());
                        }

                        // we don't want to add a motivator with 0. 0 means ignore
                        if (ua.getMtvtrs().get(motivatorKey) > 0) {
                            motivators.get(motivatorKey).add(ua.getMtvtrs().get(motivatorKey));
                        }
                    }
                }
            }

            Map<String, Integer> newMotivators = new HashMap<String, Integer>();

            // time to average motivator scores and override user's existing motivators
            for (String motivatorKey : motivators.keySet()) {
                List<Integer> motivatorValues = motivators.get(motivatorKey);
                float sum = 0f;
                float average = 0f;
                int roundedAverage = 0;

                // sum
                for (Integer value : motivatorValues) {
                    sum += value;
                }

                // average
                average = sum / motivatorValues.size();

                // round
                roundedAverage = Math.round(average);

                // add / update user's motivator key
                if (roundedAverage > 0) {
                    newMotivators.put(motivatorKey, roundedAverage);
                }
            }

            userService.saveMotivator(userCode, new Motivator(MotivatorSource.QUIZ, newMotivators));
        }

        if (log.isDebugEnabled()) {
            log.debug("Recomputing weighted user motivators complete");
        }
    }

    /**
     * Method description
     *
     * @param items items
     * @param userCode  userCode
     * @return Return value
     */
    @Override
    public List<RelevantItem> retrievePreComputedUserMatches(List<Item> items, String userCode) {
        List<RelevantItem> result = null;

        // add weight and relevancy
        if (items != null) {
            result = new ArrayList<RelevantItem>();

            for (Item item : items) {
                result.add(retrievePreComputedUserMatch(item, userCode));
            }
        }

        return result;
    }

    @Override
    public RelevantItem retrievePreComputedUserMatch(Item item, String userCode) {
        RelevantItem ri = null;

        // first check to see if this motivator match has already been calculated
        ComputedCategory cc = computedCategoryService.findComputedCategory(userCode, item.getCtgry().getRlnm());

        if (cc != null) {
            ComputedItem ci = cc.getComputedItem(item.getRlnm());

            if (ci != null) {
                ri = new RelevantItem(item, ci.getTtlrlvncy(), ci.getTtlrlvncynmbr(), ci.getMtvtrrlvncy());
            }
        }

        return ri;
    }

    /**
     * Method description
     *
     * @param items items
     * @return Return value
     */
    private FunctionalSortType sortRelevantItems(List<RelevantItem> items, FunctionalSortType sortBy) {
        FunctionalSortType result = null;

        if ((items != null) && !items.isEmpty()) {
            if (sortBy != null) {
                result = sortBy;

                switch (sortBy) {
                    case POPULARITY:
                        Collections.sort(items, new ItemPopularityLowestPriceComparator());
                        result = FunctionalSortType.POPULARITY;

                        break;

                    case RECOMMENDED:
                        Collections.sort(items, new ItemRelevancyComparator());
                        result = FunctionalSortType.RECOMMENDED;

                        break;

                    case PRICE_HIGH_LOW:
                        Collections.sort(items, new ItemLowestPriceHighLowComparator());
                        result = FunctionalSortType.PRICE_HIGH_LOW;

                        break;

                    case PRICE_LOW_HIGH:
                        Collections.sort(items, new ItemLowestPriceLowHighComparator());
                        result = FunctionalSortType.PRICE_LOW_HIGH;

                        break;
                    default:
                        if (log.isWarnEnabled()) {
                            log.warn("Not supported: " + sortBy.name() + ". Defaulting to recommended");
                        }
                        Collections.sort(items, new ItemRelevancyComparator());
                        result = FunctionalSortType.RECOMMENDED;
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Sort by not specified. Defaulting to recommended");
                }
                Collections.sort(items, new ItemRelevancyComparator());
                result = FunctionalSortType.RECOMMENDED;
            }
        }

        return result;
    }

    /**
     * Method description
     *
     * @param category            category
     * @param userCode            userCode
     * @param functionalFilters   functionalFilters
     * @param incomingUserFilters incomingUserFilters
     * @param update              update
     */
    private void updateFunctionalFilterPresets(Category category, String userCode, List<FunctionalFilter> functionalFilters,
                                                  Map<String, Map<String, String>> incomingUserFilters, Boolean update) {
        boolean userNeedsUpdate = false;
        List<FunctionalFilterPreset> filterPresets;
        List<FunctionalFilterPresetOption> presetOptions;


        // start creating the new filters
        // if the incoming user filters doesn't contain a particular filter, we use the default filter
        if ((functionalFilters != null) && !functionalFilters.isEmpty()) {
            filterPresets = userService.findFunctionalFilterPresets(userCode);

            // user might not have any presets yet, in which case we create a new list to populate and the persist
            if (filterPresets == null) {
                filterPresets = new ArrayList<FunctionalFilterPreset>();
            }

            if ((incomingUserFilters != null) && !incomingUserFilters.isEmpty()) {

                // only update if the presets have actually changed
                if (update) {
                    for (FunctionalFilter ff : functionalFilters) {

                        // check if we have a preset for this filter already
                        FunctionalFilterPreset existingPreset = getFunctionalFilterPreset(filterPresets, category.getRlnm(), ff.getKy());

                        if (existingPreset != null) {
                            // remove existing preset
                            filterPresets.remove(existingPreset);
                        }

                        presetOptions = new ArrayList<FunctionalFilterPresetOption>();

                        // check if preset is part of incoming filters
                        if (incomingUserFilters.containsKey(ff.getKy())) {
                            Map<String, String> filterOptions = incomingUserFilters.get(ff.getKy());

                            if ((filterOptions != null) && !filterOptions.isEmpty()) {
                                for (String filterOptionKey : filterOptions.keySet()) {
                                    String filterOptionValue = filterOptions.get(filterOptionKey);

                                    if (StringUtils.isNotBlank(filterOptionKey) && StringUtils.isNotBlank(filterOptionValue)) {
                                        presetOptions.add(new FunctionalFilterPresetOption(filterOptionKey, filterOptionValue));
                                    }
                                }
                            }

                            if (!presetOptions.isEmpty()) {
                                filterPresets.add(new FunctionalFilterPreset(category.getRlnm(), ff.getKy(), presetOptions));
                                userNeedsUpdate = true;
                            }
                        } else if (ff.hasSelected()) {

                            // if the incoming user filters does not contain the specified key, we're going to check if there
                            // is a default key with the filter itself
                            for (FunctionalFilterOption option : ff.getPtns()) {
                                if (option.getSlctd()) {
                                    if (StringUtils.isNotBlank(option.getKy()) && option.getVl() != null && StringUtils.isNotBlank(option.getVl().toString())) {
                                        presetOptions.add(new FunctionalFilterPresetOption(option.getKy(), option.getVl()));
                                    }
                                }
                            }

                            if (!presetOptions.isEmpty()) {
                                filterPresets.add(new FunctionalFilterPreset(category.getRlnm(), ff.getKy(), presetOptions));
                                userNeedsUpdate = true;
                            }
                        }
                    }
                }
            } else {

                // if user filters are null - we just create presets based on filters that have defaults
                for (FunctionalFilter ff : functionalFilters) {

                    // check if we have a preset for this filter already
                    FunctionalFilterPreset existingPreset = getFunctionalFilterPreset(filterPresets, category.getRlnm(), ff.getKy());

                    if ((existingPreset == null) && ff.hasSelected()) {
                        presetOptions = new ArrayList<FunctionalFilterPresetOption>();

                        for (FunctionalFilterOption option : ff.getPtns()) {
                            if (option.getSlctd()) {
                                if (StringUtils.isNotBlank(option.getKy()) && option.getVl() != null && StringUtils.isNotBlank(option.getVl().toString())) {
                                    presetOptions.add(new FunctionalFilterPresetOption(option.getKy(), option.getVl()));
                                }
                            }
                        }

                        if (!presetOptions.isEmpty()) {
                            filterPresets.add(new FunctionalFilterPreset(category.getRlnm(), ff.getKy(), presetOptions));
                            userNeedsUpdate = true;
                        }
                    }
                }
            }

            if (userNeedsUpdate) {
                userService.saveFunctionalFilterPresets(userCode, filterPresets);
            }
        }
    }

    /**
     * Method description
     *
     * @param categoryUrlName categoryUrlName
     * @param key             key
     * @return Return value
     */
    private FunctionalFilterPreset getFunctionalFilterPreset(List<FunctionalFilterPreset> filterPresets, String categoryUrlName, String key) {
        if ((filterPresets != null) && !filterPresets.isEmpty()) {
            for (FunctionalFilterPreset ffp : filterPresets) {
                if (StringUtils.equals(ffp.getKy(), key) && StringUtils.equals(ffp.getRlnm(), categoryUrlName)) {
                    return ffp;
                }
            }
        }

        return null;
    }

    /**
     * Method description
     *
     * @param categoryUrlName categoryUrlName
     * @return Return value
     */
    private List<FunctionalFilterPreset> getFunctionalFilterPresetsByCategoryUrlName(List<FunctionalFilterPreset> filterPresets, String categoryUrlName) {
        List<FunctionalFilterPreset> result = null;

        if ((filterPresets != null) && !filterPresets.isEmpty()) {
            for (FunctionalFilterPreset preset : filterPresets) {
                if (StringUtils.equals(categoryUrlName, preset.getRlnm())) {
                    if (result == null) {
                        result = new ArrayList<FunctionalFilterPreset>();
                    }

                    result.add(preset);
                }
            }
        }

        return result;
    }

    /**
     * If the filters map is not empty, it means the user selected some new filters OR is
     * paginating over the existing filters. This method makes sure the persisted presets are in sync with
     * the latest user selected filters
     *
     * @param category            category
     * @param userCode                userCode
     * @param functionalFilters   functionalFilters
     * @param incomingUserFilters filters
     * @param update              update
     */
    private void updateFunctionalPresets(Category category, String userCode, List<FunctionalFilter> functionalFilters,
                                         Map<String, Map<String, String>> incomingUserFilters, Boolean update) {
            updateFunctionalFilterPresets(category, userCode, functionalFilters, incomingUserFilters, update);
    }

    /**
     * This methods creates functional filters for the specific category.
     * <p/>
     * E.g. A template tuner question of type "range" filters items on price. It assumes all items
     * have a list price associated with them so it can create the range of prices it needs.
     *
     * @param filters filters
     * @param presets presets
     */
    private void preProcessFunctionalFilters(List<FunctionalFilter> filters, List<FunctionalFilterPreset> presets) {

        for (FunctionalFilter filter : filters) {

            // loop through tuner questions and fill in runtime data
            switch (filter.getTp()) {
                case DYNAMIC_RANGE:

                    // we have a saved value here we want to use
                    handlePreProcessDynamicRangeFilter(filter, presets);

                    break;
                case BRAND:

                    // here we have to populate brands so the user can filter on brands
                    handlePreProcessBrandFilter(filter, findPreset(filter, presets));

                    break;
                case STORE:

                    // here we have to populate stores so the user can filter on stores
                    handlePreProcessStoreFilter(filter, findPreset(filter, presets));

                    break;
                default:
                    handlePreProcessFunctionalFilter(filter, findPreset(filter, presets));
            }

            // sort the answers
            Collections.sort(filter.getPtns(), new FunctionalFilterOptionComparator());
        }

        // sort the questions
        Collections.sort(filters, new FunctionalFilterComparator());

    }

    /**
     * The brand filter needs to be populated with user presets (if available)
     *
     * @param filter filter
     * @param preset preset
     */
    private void handlePreProcessBrandFilter(FunctionalFilter filter, FunctionalFilterPreset preset) {

        if (filter != null && filter.getPtns() != null && !filter.getPtns().isEmpty()) {

            if (preset != null && preset.getPtns() != null && !preset.getPtns().isEmpty()) {
                // loop through filter options
                for (FunctionalFilterOption ffo : filter.getPtns()) {
                    if (filter.getPtns() == null) {
                        filter.setPtns(new ArrayList<FunctionalFilterOption>());
                    }

                    boolean found = false;

                    // loop through user presets looking for a match
                    for (FunctionalFilterPresetOption ffpo : preset.getPtns()) {
                        // if the preset option matches the owner url name we have ourselves a preset
                        // otherwise do the same as below
                        if (StringUtils.equals(ffpo.getKy(), ffo.getKy())) {
                            found = true;
                        }
                    }

                    if (found) {
                        ffo.setSlctd(true);
                    } else {
                        ffo.setSlctd(false);
                    }
                }
            }

            // last thing we want to do is sort the filter options
            Collections.sort(filter.getPtns(), new FunctionalFilterOptionComparator());
        }

    }

    /**
     * The store filter needs to be populated with user presets (if available)
     *
     * @param filter filter
     * @param preset preset
     */
    private void handlePreProcessStoreFilter(FunctionalFilter filter, FunctionalFilterPreset preset) {

        if (filter != null && filter.getPtns() != null && !filter.getPtns().isEmpty()) {

            if (preset != null && preset.getPtns() != null && !preset.getPtns().isEmpty()) {

                // loop through filter options
                for (FunctionalFilterOption ffo : filter.getPtns()) {
                    boolean found = false;

                    // loop through preset options looking for a match
                    for (FunctionalFilterPresetOption ffpo : preset.getPtns()) {
                        // if the preset option matches the owner url name we have ourselves a preset
                        // otherwise do the same as below
                        if (StringUtils.equals(ffpo.getKy(), ffo.getKy())) {
                            found = true;
                        }
                    }

                    if (found) {
                        ffo.setSlctd(true);
                    } else {
                        ffo.setSlctd(false);
                    }
                }
            }

            // last thing we want to do is sort the filter options
            Collections.sort(filter.getPtns(), new FunctionalFilterOptionComparator());
        }
    }

    /**
     * Method description
     *
     * @param filter  filter
     * @param presets presets
     * @param items   items
     */
    private void filterOnDynamicRange(FunctionalFilter filter, List<FunctionalFilterPreset> presets, List<Item> items) {
        String attributeKey = filter.getDtky();
        List<Item> toRemove = null;
        Double high = null;
        Double low = null;

        // first we check the preset - if the preset contains values we use those
        FunctionalFilterPreset preset = findPreset(filter, presets);

        if (preset != null) {
            for (FunctionalFilterPresetOption presetOption : preset.getPtns()) {

                // these values might be strings if generated by the user and doubles if generated by the back-end
                if (StringUtils.equals(presetOption.getKy(), ApplicationConstants.DYNAMIC_RANGE_HIGH)) {
                    high = NumberUtils.safeDouble(presetOption.getVl());
                } else if (StringUtils.equals(presetOption.getKy(), ApplicationConstants.DYNAMIC_RANGE_LOW)) {
                    low = NumberUtils.safeDouble(presetOption.getVl());
                }
            }

            if ((high != null) && (low != null)) {

                // loop through items and filter out the items that don't fit this description
                for (Item item : items) {
                	Double attributeValue = null;
                	if (item.getAttributes() != null){
                		attributeValue = NumberUtils.safeDouble(item.getAttributes().get(attributeKey));
                	}
                    if (attributeValue != null) {
                        // verify that the value is in between the high and low ranges
                        if ((attributeValue.compareTo(low) < 0) || (attributeValue.compareTo(high) > 0)) {

                            // didn't pass the test so we want remove it
                            if (toRemove == null) {
                                toRemove = new ArrayList<Item>();
                            }

                            toRemove.add(item);
                        }
                    } else {
                        // didn't pass the test so we want remove it
                        if (toRemove == null) {
                            toRemove = new ArrayList<Item>();
                        }
                        toRemove.add(item);
                    }
                }

                // remove unwanted items from original list
                if (toRemove != null) {
                    items.removeAll(toRemove);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Could not filter on dynamic range as range(s) are missing. High: " + high + ", low: "
                            + low);
                }
            }
        }
    }

    /**
     * Method description
     *
     * @param filter  filter
     * @param presets presets
     * @param items   items
     */
    private void filterOnMultipleChoiceMultipleAnswerAND(FunctionalFilter filter, List<FunctionalFilterPreset> presets,
                                                         List<Item> items) {

        // multiple choice filter is just about comparing item attribute keys and their values
        List<Item> toRemove = null;

        // first we check the preset - if the preset contains values we use those
        FunctionalFilterPreset preset = findPreset(filter, presets);

        if (preset != null) {
            for (Item item : items) {
                Boolean match = true;

                for (FunctionalFilterPresetOption option : preset.getPtns()) {
                    String itemAttributeValue = (String) item.getAttributes().get(option.getKy());
                    String attributeValue = (String) option.getVl();

                    if (!StringUtils.equals(attributeValue, itemAttributeValue)) {
                        match = false;

                        break;
                    }
                }

                if (!match) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<Item>();
                    }

                    toRemove.add(item);
                }
            }

            // remove unwanted items from original list
            if (toRemove != null) {
                items.removeAll(toRemove);
            }

        }
    }

    /**
     * Method description
     *
     * @param filter  filter
     * @param presets presets
     * @param items   items
     */
    private void filterOnMultipleChoiceMultipleAnswerOR(FunctionalFilter filter, List<FunctionalFilterPreset> presets,
                                                        List<Item> items) {

        // multiple choice filter is just about comparing item attribute keys and their values
        List<Item> toRemove = null;

        // first we check the preset - if the preset contains values we use those
        FunctionalFilterPreset preset = findPreset(filter, presets);

        if (preset != null) {
            for (Item item : items) {
                Boolean match = false;

                // this is an OR statement - if either of these properties are found we won't remove the item from the list
                if (preset.getPtns().isEmpty()) {
                    match = true;
                } else {
                    for (FunctionalFilterPresetOption option : preset.getPtns()) {
                        String itemAttributeValue = (String) item.getAttributes().get(option.getKy());
                        String attributeValue = (String) option.getVl();

                        if (StringUtils.equals(attributeValue, itemAttributeValue)) {
                            match = true;

                            break;
                        }
                    }
                }

                if (!match) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<Item>();
                    }

                    toRemove.add(item);
                }
            }

            // remove unwanted items from original list
            if (toRemove != null) {
                items.removeAll(toRemove);
            }
        }
    }

    /**
     * Filters list on item category
     * @param filter filter
     * @param presets presets
     * @param items items
     */
    private void filterOnCategory(FunctionalFilter filter, List<FunctionalFilterPreset> presets,
                                                        List<Item> items) {

        // category filter is just about comparing item attribute keys and their values
        List<Item> toRemove = null;

        // first we check the preset - if the preset contains values we use those
        FunctionalFilterPreset preset = findPreset(filter, presets);

        if (preset != null) {
            for (Item item : items) {
                Boolean match = false;

                // start looping through the presets in hope that it includes the category
                // this item is in
                if (preset.getPtns() != null && !preset.getPtns().isEmpty()) {
                    for (FunctionalFilterPresetOption option : preset.getPtns()) {
                        String itemCategory = item.getCtgry().getRlnm();
                        String optionValue = (String) option.getVl();

                        if (StringUtils.equals(itemCategory, optionValue)) {
                            match = true;

                            break;
                        }
                    }
                }

                if (!match) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<Item>();
                    }

                    toRemove.add(item);
                }
            }

            // remove unwanted items from original list
            if (toRemove != null) {
                items.removeAll(toRemove);
            }
        }
    }

    /**
     * This filters the item list on items belonging to a specific brand(s)
     *
     * @param filter  filter
     * @param presets presets
     * @param items   items
     */
    private void filterOnBrand(FunctionalFilter filter, List<FunctionalFilterPreset> presets, List<Item> items) {

        // multiple choice filter is just about comparing item attribute keys and their values
        List<Item> toRemove = null;

        // first we check the preset - if the preset contains values we use those
        FunctionalFilterPreset preset = findPreset(filter, presets);

        if (preset != null) {
            for (Item item : items) {
                Boolean match = false;

                if (preset.getPtns().isEmpty()) {
                    match = true;
                } else {
                    for (FunctionalFilterPresetOption option : preset.getPtns()) {

                        // here we match on brand url name
                        if (StringUtils.equals(option.getKy(), item.getWnr().getRlnm())) {
                            match = true;

                            break;
                        }

                    }
                }

                if (!match) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<Item>();
                    }

                    toRemove.add(item);
                }
            }

            // remove unwanted items from original list
            if (toRemove != null) {
                items.removeAll(toRemove);
            }
        }
    }

    /**
     * This filters the item list on items available in a specific brand(s)
     *
     * @param filter  filter
     * @param presets presets
     * @param items   items
     */
    private void filterOnStore(FunctionalFilter filter, List<FunctionalFilterPreset> presets, List<Item> items) {

        // multiple choice filter is just about comparing item attribute keys and their values
        List<Item> toRemove = null;

        // first we check the preset - if the preset contains values we use those
        FunctionalFilterPreset preset = findPreset(filter, presets);

        if (preset != null) {
            for (Item item : items) {
                Boolean match = false;

                if (item.getStrs() != null && !item.getStrs().isEmpty()) {
                    for (AvailableInStore store : item.getStrs()) {

                        if (preset.getPtns().isEmpty()) {
                            match = true;
                        } else {
                            for (FunctionalFilterPresetOption option : preset.getPtns()) {

                                // here we match on store url name
                                if (StringUtils.equals(option.getKy(), store.getRlnm())) {
                                    match = true;

                                    break;
                                }

                            }
                        }
                    }
                }

                if (!match) {
                    if (toRemove == null) {
                        toRemove = new ArrayList<Item>();
                    }

                    toRemove.add(item);
                }
            }

            // remove unwanted items from original list
            if (toRemove != null) {
                items.removeAll(toRemove);
            }
        }
    }

    /**
     * Method description
     *
     * @param filter  filter
     * @param presets presets
     */
    private void handlePreProcessDynamicRangeFilter(FunctionalFilter filter, List<FunctionalFilterPreset> presets) {
        FunctionalFilterOption high = null;
        FunctionalFilterOption low = null;
        FunctionalFilterOption currentHigh = null;
        FunctionalFilterOption currentLow = null;
        FunctionalFilterPreset preset = null;

        // first we find high and low for the range
        for (FunctionalFilterOption option : filter.getPtns()) {
            if (StringUtils.equals(option.getKy(), ApplicationConstants.DYNAMIC_RANGE_HIGH)) {
                high = option;
            } else if (StringUtils.equals(option.getKy(), ApplicationConstants.DYNAMIC_RANGE_LOW)) {
                low = option;
            }
        }

        if ((high != null) && (low != null)) {

            // first we check the preset - if the preset contains values we use those
            preset = findPreset(filter, presets);

            if (preset != null) {
                FunctionalFilterPresetOption optionHigh = preset.getOption(ApplicationConstants.DYNAMIC_RANGE_HIGH);
                FunctionalFilterPresetOption optionLow = preset.getOption(ApplicationConstants.DYNAMIC_RANGE_LOW);

                if ((optionHigh != null) && (optionLow != null) && (optionHigh.getVl() != null)
                        && (optionLow.getVl() != null)) {
                    currentHigh = new FunctionalFilterOption(ApplicationConstants.CURRENT_DYNAMIC_RANGE_KEY_HIGH, optionHigh.getVl(), 4, null);
                    currentLow = new FunctionalFilterOption(ApplicationConstants.CURRENT_DYNAMIC_RANGE_KEY_LOW, optionLow.getVl(), 2, null);

                    // add preset values as new options on the filter
                    filter.getPtns().add(currentHigh);
                    filter.getPtns().add(currentLow);
                }
            } else {
                // means there is no preset and we just add the max and min as current max and min
                currentHigh = new FunctionalFilterOption(ApplicationConstants.CURRENT_DYNAMIC_RANGE_KEY_HIGH, high.getVl(), 4, null);
                currentLow = new FunctionalFilterOption(ApplicationConstants.CURRENT_DYNAMIC_RANGE_KEY_LOW, low.getVl(), 2, null);

                // add preset values as new options on the filter
                filter.getPtns().add(currentHigh);
                filter.getPtns().add(currentLow);
            }

            // sort filter options
            Collections.sort(filter.getPtns(), new FunctionalFilterOptionComparator());
        }
    }

    /**
     * This method is responsible for setting the "selected" flag to false or true on the filter options
     *
     * @param filter filter
     * @param preset preset
     */
    private void handlePreProcessFunctionalFilter(FunctionalFilter filter, FunctionalFilterPreset preset) {
        if (preset != null) {
            if ((filter.getPtns() != null) && (preset.getPtns() != null)) {
                for (FunctionalFilterOption ffo : filter.getPtns()) {
                    FunctionalFilterPresetOption ffpo = preset.getOption(ffo.getKy());

                    // the existence of the option means it has been specifically set and we can "check" the option
                    // the non-existence of the option means it was not selected by the user but the preset has been created
                    // which means we can set it to false as the user "un-checked" it
                    if (ffpo != null) {
                        ffo.setSlctd(true);
                    } else {
                        ffo.setSlctd(false);
                    }
                }
            }
        }
    }

    /**
     * Method description
     *
     * @param filter  filter
     * @param presets presets
     * @return Return value
     */
    private FunctionalFilterPreset findPreset(FunctionalFilter filter, List<FunctionalFilterPreset> presets) {
        FunctionalFilterPreset preset = null;

        if ((presets != null) && !presets.isEmpty()) {
            for (FunctionalFilterPreset ffp : presets) {
                if (StringUtils.equals(filter.getRlnm(), ffp.getRlnm()) && StringUtils.equals(filter.getKy(), ffp.getKy())) {
                    preset = ffp;

                    break;
                }
            }
        }

        return preset;
    }

    /**
     * Method description
     *
     * @param items items
     * @return Return value
     */
    private FunctionalSortType sortItems(List<Item> items, FunctionalSortType sortBy) {
        FunctionalSortType result = null;

        if ((items != null) && !items.isEmpty()) {
            // sort by always takes precedence
            if (sortBy != null) {

                switch (sortBy) {
                    case POPULARITY:
                        Collections.sort(items, new ItemPopularityLowestPriceComparator());
                        result = FunctionalSortType.POPULARITY;

                        break;

                    case PRICE_HIGH_LOW:
                        Collections.sort(items, new ItemLowestPriceHighLowComparator());
                        result = FunctionalSortType.PRICE_HIGH_LOW;

                        break;

                    case PRICE_LOW_HIGH:
                        Collections.sort(items, new ItemLowestPriceLowHighComparator());
                        result = FunctionalSortType.PRICE_LOW_HIGH;

                        break;
                    default:
                        if (log.isWarnEnabled()) {
                            log.warn("Not supported: " + sortBy.name() + ". Defaulting to popularity.");
                        }
                        Collections.sort(items, new ItemPopularityLowestPriceComparator());
                        result = FunctionalSortType.POPULARITY;
                }

            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No sort type specified. Defaulting to popularity.");
                }
                Collections.sort(items, new ItemPopularityLowestPriceComparator());
                result = FunctionalSortType.POPULARITY;
            }
        }

        return result;
    }

    private void calculateAvailableFilterOptions(List<FunctionalFilter> functionalFilters, List<Item> items, AvailableFilters availableFilters) {
        // TODO - NON-OPTIMAL....
        // This could be re-worked by having a pre-built collection on the items that ONLY contain the
        // filters valid for that item.  Then instead of running through ALL filters x ALL items we
        // could run through ALL items x ONLY valid filters.
        // Alternately we could pre-calculate a bit field per item where each functional filter within a
        // category has a specific mask value for a bit.  Then we'd just OR the bit fields together and
        // then decode the results at the end.  This doesn't handle brands/stores as elegantly since
        // there might be large numbers of discrete values.  This is still probably the better option
        if (functionalFilters != null) {
            for (FunctionalFilter ff : functionalFilters) {

                if (FunctionalFilterType.MULTIPLE_CHOICE_SINGLE_ANSWER.equals(ff.getTp()) ||
                        FunctionalFilterType.MULTIPLE_CHOICE_MULTIPLE_ANSWER_AND.equals(ff.getTp()) ||
                        FunctionalFilterType.MULTIPLE_CHOICE_MULTIPLE_ANSWER_OR.equals(ff.getTp())) {

                    // Track this filter
                    availableFilters.addTrackedFilter(ff);

                    for (Item item : items) {
                        if (ff.getPtns() != null) {
                            for (FunctionalFilterOption option : ff.getPtns()) {
                            	if ( item.getAttributes() != null){
	                                String itemAttributeValue = (String) item.getAttributes().get(option.getKy());
	
	                                // If this option is true for this item, add it to the set
	                                if (Boolean.parseBoolean(itemAttributeValue)) {
	                                    availableFilters.addAvailableOption(ff, option.getKy());
	                                }
                            	}
                            }
                        }
                    }
                } else if (FunctionalFilterType.BRAND.equals(ff.getTp())) {
                    // Track this filter
                    availableFilters.addTrackedFilter(ff);

                    for (Item item : items) {
                        if (item.getWnr() != null) {
                            availableFilters.addAvailableOption(ff, item.getWnr().getRlnm());
                        }
                    }
                } else if (FunctionalFilterType.STORE.equals(ff.getTp())) {
                    // Track this filter
                    availableFilters.addTrackedFilter(ff);

                    for (Item item : items) {
                        if (item.getStrs() != null) {
                            for (AvailableInStore store : item.getStrs()) {
                                availableFilters.addAvailableOption(ff, store.getRlnm());
                            }
                        }
                    }
                }
            }
        }
    }
}
