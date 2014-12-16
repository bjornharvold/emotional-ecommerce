/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.ingest.test.web.controller.ingest;

import com.lela.commons.service.*;
import com.lela.domain.enums.FunctionalFilterDomainType;
import com.lela.domain.enums.QuestionType;
import com.lela.ingest.test.AbstractFunctionalTest;
import com.lela.domain.document.*;
import com.lela.domain.dto.*;
import com.lela.domain.enums.FunctionalFilterType;
import com.lela.domain.enums.MetricType;
import com.lela.domain.enums.PublishStatus;
import com.lela.ingest.web.IngestionController;
import org.apache.commons.lang.RandomStringUtils;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
public class IngestionControllerFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(IngestionControllerFunctionalTest.class);
    private static final String CONTENT_PRODUCER_EMAIL = "john@lelaknows.com";
    private static final String PASSWORD = "pn3M9-546m|bsUe)0l8j";
    private static final String CATEGORYINGESTIONTEST = "categoryingestiontest";
    private static final String OFFERINGESTIONTEST = "offeringestiontest";
    private static final String OWNERINGESTIONTEST = "owneringestiontest";
    private static final String ITEMINGESTIONTEST = "itemingestiontest";
    private static final String QUESTION_INGESTION_TEST = "questioningestiontest";
    private static final String STORE_TEST = "storetest";
    private static final String BRANCH_TEST = "branchtest";
    private static final String AFFILIATE_ACCOUNT_TEST = "affiliateaccounttest";
    private static final String CAMPAIGN_TEST = "campaigntest";
    private static final String NAVIGATION_BAR_INGESTION_TEST = "navigationbaringestiontest";

    @Autowired
    private MongoOperations mongo;

    @Autowired
    private IngestionController ingestionController;

    @Autowired
    private MetricService metricService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private UserService userService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private PostalCodeService postalCodeService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private NavigationBarService navigationBarService;

    @Autowired
    private AffiliateService affiliateService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private FunctionalFilterService functionalFilterService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private DealService dealService;

    @Before
    @After
    public void tearDown() {
        mongo.remove(query(where("rlnm").is(CATEGORYINGESTIONTEST)), Category.class);
        mongo.remove(query(where("rlnm").is(OFFERINGESTIONTEST)), Offer.class);
        mongo.remove(query(where("rlnm").is(OWNERINGESTIONTEST)), Owner.class);
        mongo.remove(query(where("rlnm").is(ITEMINGESTIONTEST)), Item.class);
        mongo.remove(query(where("rlnm").is(QUESTION_INGESTION_TEST)), Question.class);
        mongo.remove(query(where("rlnm").is(STORE_TEST)), Store.class);
        mongo.remove(query(where("rlnm").is(BRANCH_TEST)), Branch.class);
        mongo.remove(query(where("rlnm").is(AFFILIATE_ACCOUNT_TEST)), AffiliateAccount.class);
        mongo.remove(query(where("rlnm").is(CAMPAIGN_TEST)), Campaign.class);
        mongo.remove(query(where("rlnm").is(NAVIGATION_BAR_INGESTION_TEST)), NavigationBar.class);
    }

    @Test
    public void testIngestCategories() {
        log.info("Testing ingesting categories...");

        try {
            List<Category> list = new ArrayList<Category>();
            Category category = new Category();
            category.setNm(CATEGORYINGESTIONTEST);
            category.setRlnm(CATEGORYINGESTIONTEST);
            list.add(category);

            Categories categories = new Categories(list);

            // insert method
            categories = ingestionController.ingestCategories(categories, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Categories result is null", categories);
            assertNotNull("Saving categories returns null", categories.getList().get(0).getId());

            categories = ingestionController.updateCategories(categories, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Categories result is null", categories);
            assertNotNull("Saving categories returns null", categories.getList().get(0).getId());

            category = categoryService.findCategoryByUrlName(CATEGORYINGESTIONTEST);
            assertNotNull("Ingested category is null", category);

            StringList deletes = new StringList();
            deletes.getList().add(CATEGORYINGESTIONTEST);
            ingestionController.deleteCategories(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            category = categoryService.findCategoryByUrlName(CATEGORYINGESTIONTEST);
            assertNull("Category did not get removed", category);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing ingesting categories complete");
    }

    @Test
    public void testIngestOffers() {
        log.info("Testing ingesting offers...");

        try {
            List<Offer> list = new ArrayList<Offer>();
            Offer offer = new Offer();
            offer.setRlnm(OFFERINGESTIONTEST);
            list.add(offer);

            Offers offers = new Offers(list);
            offers = ingestionController.ingestOffers(offers, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Offers result is null", offers);
            assertNotNull("Saving offer returns null", offers.getList().get(0).getId());

            offers = ingestionController.updateOffers(offers, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Offers result is null", offers);
            assertNotNull("Saving offers returns null", offers.getList().get(0).getId());

            offer = offerService.findOfferByUrlName(OFFERINGESTIONTEST);
            assertNotNull("Ingested offer is null", offer);

            StringList delete = new StringList();
            delete.getList().add(OFFERINGESTIONTEST);
            ingestionController.deleteOffers(delete, CONTENT_PRODUCER_EMAIL, PASSWORD);

            offer = offerService.findOfferByUrlName(OFFERINGESTIONTEST);
            assertNull("Offer did not get removed", offer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting offers complete");
    }
    
    @Test
    public void testIngestOwners() {
        log.info("Testing ingesting owners...");

        try {
            List<Owner> list = new ArrayList<Owner>();
            Owner owner = new Owner();
            owner.setNm(OWNERINGESTIONTEST);
            owner.setRlnm(OWNERINGESTIONTEST);
            list.add(owner);

            Owners owners = new Owners(list);
            owners = ingestionController.ingestOwners(owners, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Owners result is null", owners);
            assertNotNull("Saving owners returns null", owners.getList().get(0).getId());

            owners = ingestionController.updateOwners(owners, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Owners result is null", owners);
            assertNotNull("Saving owners returns null", owners.getList().get(0).getId());

            owner = ownerService.findOwnerByUrlName(OWNERINGESTIONTEST);
            assertNotNull("Ingested owner is null", owner);

            StringList delete = new StringList();
            delete.getList().add(OWNERINGESTIONTEST);
            ingestionController.deleteOwners(delete, CONTENT_PRODUCER_EMAIL, PASSWORD);

            owner = ownerService.findOwnerByUrlName(OWNERINGESTIONTEST);
            assertNull("Owner did not get removed", owner);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting owners complete");
    }

    @Test
    public void testIngestItems() {
        log.info("Testing ingesting items...");

        try {
            
            StringList rlnms = new StringList();
            rlnms.setList(new ArrayList<String>());
            rlnms.getList().add(ITEMINGESTIONTEST);
            
            List<Item> list = new ArrayList<Item>();
            Item item = new Item();
            item.setNm(ITEMINGESTIONTEST);
            item.setRlnm(ITEMINGESTIONTEST);
            Category category = new Category();
            category.setRlnm(CATEGORYINGESTIONTEST);
            item.setCtgry(category);
            list.add(item);

            Items items = new Items(list);
            items = ingestionController.ingestItems(items, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Items result is null", items);
            assertNotNull("Saving items returns null", items.getList().get(0).getId());

            items = ingestionController.updateItems(items, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Items result is null", items);
            assertNotNull("Saving items returns null", items.getList().get(0).getId());

            item = itemService.findItemByUrlName(ITEMINGESTIONTEST);
            assertNotNull("Ingested item is null", item);
            assertNotNull("Saving items returns null", items.getList().get(0).getId());

            AvailableInStore store = new AvailableInStore();
            store.setNm("Store name");
            store.setRlnm("StoreUrlName");

            List<AvailableInStore> stores = new ArrayList<AvailableInStore>(1);
            stores.add(store);

            List<Deal> deals = new ArrayList<Deal>(1);
            Deal deal = new Deal();
            deal.setNm("Name");
            deal.setRl("Url");
            deals.add(deal);
            store.setDls(deals);

            List<Item> inStoreItems = new ArrayList<Item>(1);
            Item inStoreItem = new Item();
            inStoreItem.setNm("blah");
            inStoreItems.add(inStoreItem);
            store.setTms(inStoreItems);

            item.setStrs(stores);

            list = new ArrayList<Item>();
            list.add(item);
            items = new Items(list);
            items = ingestionController.updateAvailableInStoreOnItem(items, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Ingested item is null", items.getList().get(0));
            assertNotNull("Saving items returns null", items.getList().get(0).getId());
            assertNotNull("Saving items available in store returns null", items.getList().get(0).getId());

            item = itemService.findItemByUrlName(ITEMINGESTIONTEST);
            assertNotNull("Ingested item is null", item);
            assertNotNull("Saving items returns null", item.getId());
            assertNotNull("Saving items available in stores returns null", item.getStrs());
            assertFalse("Saving items available in stores returns null", item.getStrs().isEmpty());

            ingestionController.deleteItems(rlnms, CONTENT_PRODUCER_EMAIL, PASSWORD);

            item = itemService.findItemByUrlName(ITEMINGESTIONTEST);
            assertNull("Item did not get removed", item);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting items complete");
    }

    @Test
    public void testIngestQuestions() {
        log.info("Testing ingesting questions...");

        try {
            List<Question> list = new ArrayList<Question>();
            Question question = new Question();
            question.setNm(QUESTION_INGESTION_TEST);
            question.setRlnm(QUESTION_INGESTION_TEST);
            question.setTp(QuestionType.IMAGE_MULTIPLE_CHOICE_SINGLE_ANSWER);
            list.add(question);

            Questions questions = new Questions(list);
            questions = ingestionController.ingestQuestions(questions, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Questions result is null", questions);
            assertNotNull("Saving questions returns null", questions.getList().get(0).getId());

            questions = ingestionController.updateQuestions(questions, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Questions result is null", questions);
            assertNotNull("Saving questions returns null", questions.getList().get(0).getId());

            question = quizService.findQuestionByUrlName(QUESTION_INGESTION_TEST);
            assertNotNull("Ingested question is null", question);

            StringList deletes = new StringList();
            deletes.getList().add(QUESTION_INGESTION_TEST);
            ingestionController.deleteQuestions(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            question = quizService.findQuestionByUrlName(QUESTION_INGESTION_TEST);
            assertNull("Question did not get removed", question);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting questions complete");
    }

    @Test
    public void testIngestMetrics() {
        log.info("Testing ingesting questions...");

        try {
            List<Metric> list = new ArrayList<Metric>();
            Metric metric = new Metric();
            metric.setId(new ObjectId());
            metric.setTp(MetricType.TEST);

            List<MetricValue> mvs = new ArrayList<MetricValue>(2);
            MetricValue mv1 = new MetricValue("key1", 1);
            MetricValue mv2 = new MetricValue("key2", 2);
            mvs.add(mv1);
            mvs.add(mv2);
            metric.setVls(mvs);

            list.add(metric);

            Metrics metrics = new Metrics(list);
            metrics = ingestionController.ingestMetrics(metrics, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Metrics result is null", metrics);
            assertNotNull("Saving metrics returns null", metrics.getList().get(0).getId());

            metrics = ingestionController.updateMetrics(metrics, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Metrics result is null", metrics);
            assertNotNull("Saving metrics returns null", metrics.getList().get(0).getId());

            metric = metricService.findMetricByType(MetricType.TEST);
            assertNotNull("Ingested metric is null", metric);

            ObjectIds deletes = new ObjectIds();
            deletes.getList().add(metric.getId());
            ingestionController.deleteMetrics(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            metric = metricService.findMetricByType(MetricType.TEST);
            assertNull("Metric did not get removed", metric);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting metrics complete");
    }

    @Test
    public void testIngestStores() {
        log.info("Testing ingesting stores...");

        try {
            List<Store> list = new ArrayList<Store>();
            Store store = new Store();
            store.setNm(STORE_TEST);
            store.setRlnm(STORE_TEST);

            list.add(store);

            Stores stores = new Stores(list);
            stores = ingestionController.ingestStores(stores, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Stores result is null", stores);
            assertNotNull("Saving stores returns null", stores.getList().get(0).getId());

            stores = ingestionController.updateStores(stores, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Stores result is null", stores);
            assertNotNull("Saving stores returns null", stores.getList().get(0).getId());

            store = storeService.findStoreByUrlName(STORE_TEST);
            assertNotNull("Ingested store is null", store);

            StringList deletes = new StringList();
            deletes.getList().add(STORE_TEST);
            ingestionController.deleteStores(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            store = storeService.findStoreByUrlName(STORE_TEST);
            assertNull("Store did not get removed", store);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting stores complete");
    }

    @Test
    public void testIngestAffiliateAccounts() {
        log.info("Testing ingesting affiliate accounts...");

        try {
            List<AffiliateAccount> list = new ArrayList<AffiliateAccount>();
            AffiliateAccount affiliateAccount = new AffiliateAccount();
            affiliateAccount.setNm(AFFILIATE_ACCOUNT_TEST);
            affiliateAccount.setRlnm(AFFILIATE_ACCOUNT_TEST);

            list.add(affiliateAccount);

            AffiliateAccounts affiliateAccounts = new AffiliateAccounts(list);
            affiliateAccounts = ingestionController.ingestAffiliateAccounts(affiliateAccounts, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("AffiliateAccounts result is null", affiliateAccounts);
            assertNotNull("Saving AffiliateAccounts returns null", affiliateAccounts.getList().get(0).getId());

            affiliateAccounts = ingestionController.updateAffiliateAccounts(affiliateAccounts, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("AffiliateAccounts result is null", affiliateAccounts);
            assertNotNull("Saving AffiliateAccounts returns null", affiliateAccounts.getList().get(0).getId());

            affiliateAccount = affiliateService.findAffiliateAccountByUrlName(AFFILIATE_ACCOUNT_TEST);
            assertNotNull("Ingested AffiliateAccount is null", affiliateAccount);

            StringList delete = new StringList();
            delete.getList().add(AFFILIATE_ACCOUNT_TEST);
            ingestionController.deleteAffiliateAccounts(delete, CONTENT_PRODUCER_EMAIL, PASSWORD);

            localCacheEvictionService.evictAffiliateAccount(affiliateAccount.getRlnm());

            affiliateAccount = affiliateService.findAffiliateAccountByUrlName(AFFILIATE_ACCOUNT_TEST);
            assertNull("AffiliateAccount did not get removed", affiliateAccount);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing ingesting AffiliateAccounts complete");
    }

    @Test
    public void testIngestBranches() {
        log.info("Testing ingesting branches...");

        try {
            List<Branch> list = new ArrayList<Branch>();
            Branch branch = new Branch();
            branch.setNm(BRANCH_TEST);
            branch.setRlnm(BRANCH_TEST);

            list.add(branch);

            Branches branches = new Branches(list);
            branches = ingestionController.ingestBranches(branches, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Branches result is null", branches);
            assertNotNull("Saving branches returns null", branches.getList().get(0).getId());

            branches = ingestionController.updateBranches(branches, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Branches result is null", branches);
            assertNotNull("Saving branches returns null", branches.getList().get(0).getId());

            branch = branchService.findBranchByUrlName(BRANCH_TEST);
            assertNotNull("Ingested branch is null", branch);

            StringList deletes = new StringList();
            deletes.getList().add(BRANCH_TEST);
            ingestionController.deleteBranches(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            branch = branchService.findBranchByUrlName(BRANCH_TEST);
            assertNull("Branch did not get removed", branch);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting branches complete");
    }

    @Test
    public void testIngestCampaigns() {
        log.info("Testing ingesting Campaigns...");

        try {
            List<Campaign> list = new ArrayList<Campaign>();
            Campaign campaign = new Campaign();
            campaign.setId(new ObjectId());
            campaign.setNm(CAMPAIGN_TEST);
            campaign.setRlnm(CAMPAIGN_TEST);

            list.add(campaign);

            Campaigns campaigns = new Campaigns(list);
            campaigns = ingestionController.ingestCampaigns(campaigns, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Campaigns result is null", campaigns);
            assertNotNull("Saving Campaigns returns null", campaigns.getList().get(0).getId());

            campaigns = ingestionController.updateCampaigns(campaigns, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Campaigns result is null", campaigns);
            assertNotNull("Saving Campaigns returns null", campaigns.getList().get(0).getId());

            campaign = campaignService.findCampaignByUrlName(CAMPAIGN_TEST);
            assertNotNull("Ingested Campaign is null", campaign);

            StringList deletes = new StringList();
            deletes.getList().add(CAMPAIGN_TEST);
            ingestionController.deleteCampaigns(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            localCacheEvictionService.evictCampaign(campaign.getRlnm());

            campaign = campaignService.findCampaignByUrlName(CAMPAIGN_TEST);
            assertNull("Campaign did not get removed", campaign);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting Campaigns complete");
    }

    @Test
    public void testIngestUsers() {
        log.info("Testing ingesting users...");

        try {
            List<User> list = new ArrayList<User>();
            User user = new User();
            user.setMl(RandomStringUtils.randomAlphabetic(6) + "@lela.com");
            user.setPsswrd(RandomStringUtils.randomAlphabetic(6));

            list.add(user);

            Users users = new Users(list);
            users = ingestionController.ingestUsers(users, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Users result is null", users);
            assertNotNull("Saving users returns null", users.getList().get(0).getId());

            user = userService.findUserByEmail(user.getMl());
            assertNotNull("Ingested user is null", user);

            log.info("This user should be disabled as we didn't give him a password");
            assertEquals("User role missing", 1, user.getRrlnms().size(), 0);

            ingestionController.deleteUsers(users, CONTENT_PRODUCER_EMAIL, PASSWORD);

            user = userService.findUserByEmail(user.getMl());
            assertNull("User did not get removed", user);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting users complete");
    }

    @Test
    public void testIngestFunctionalFilters() {
        log.info("Testing ingesting functional filters...");

        try {
            List<FunctionalFilter> list = new ArrayList<FunctionalFilter>();
            FunctionalFilter ff = new FunctionalFilter();
            ff.setId(new ObjectId());
            ff.setTp(FunctionalFilterType.DYNAMIC_RANGE);
            ff.setDtp(FunctionalFilterDomainType.CATEGORY);
            ff.setRlnm("blah");
            ff.setKy("blah");
            ff.setRdr(1);
            ff.setLcl("en_US");
            List<FunctionalFilterOption> values = new ArrayList<FunctionalFilterOption>();
            values.add(new FunctionalFilterOption("high", 3, 1, false));
            values.add(new FunctionalFilterOption("medium", 2, 2, false));
            values.add(new FunctionalFilterOption("low", 1, 3, false));
            ff.setPtns(values);
            list.add(ff);

            FunctionalFilters filters = new FunctionalFilters(list);

            filters = ingestionController.ingestFunctionalFilters(filters, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Filters result is null", filters);
            assertNotNull("Saving filters returns null", filters.getList().get(0).getId());

            List<FunctionalFilter> filters2 = functionalFilterService.findFunctionalFiltersByUrlName(ff.getRlnm());
            assertNotNull("Ingested filters is null", filters2);

            ObjectIds ids = new ObjectIds();
            ids.getList().add(ff.getId());
            ingestionController.deleteFunctionalFilters(ids, CONTENT_PRODUCER_EMAIL, PASSWORD);

            filters2 = functionalFilterService.findFunctionalFiltersByUrlName(ff.getRlnm());
            assertNull("filters2 did not get removed", filters2);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting functional filters complete");
    }

    @Test
    public void testIngestRoles() {
        log.info("Testing ingesting roles...");

        try {
            List<Role> list = new ArrayList<Role>();
            Role ff = new Role();
            ff.setRlnm("blah");
            ff.setNm("blah");
            List<String> values = new ArrayList<String>();
            values.add("high");
            values.add("medium");
            values.add("low");
            ff.setRghts(values);
            list.add(ff);

            Roles roles = new Roles(list);

            roles = ingestionController.ingestRoles(roles, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Role result is null", roles);
            assertNotNull("Saving roles returns null", roles.getList().get(0).getId());

            Role role = roleService.findRoleByUrlName(ff.getRlnm());
            assertNotNull("Ingested role is null", role);

            StringList deletes = new StringList();
            deletes.getList().add("blah");
            ingestionController.deleteRoles(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            role = roleService.findRoleByUrlName(ff.getRlnm());
            assertNull("Ingested role is not null", role);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting roles complete");
    }

    @Test
    public void testIngestPostalCodes() {
        log.info("Testing ingesting postal codes...");

        try {
            List<PostalCode> list = new ArrayList<PostalCode>();
            PostalCode ff = new PostalCode();
            ff.setCd("1234567890");
            ff.setPpltn(11111);
            ff.setLc(new Float[]{12.0f, 12.0f});
            ff.setSt("KKJKJ");
            ff.setStnm("New York");
            ff.setCt("New York City");

            list.add(ff);

            PostalCodes postalCodes = new PostalCodes(list);

            postalCodes = ingestionController.ingestPostalCodes(postalCodes, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Postal codes result is null", postalCodes);
            assertNotNull("Saving postal code returns null", postalCodes.getList().get(0).getId());

            PostalCode pc = postalCodeService.findPostalCodeByCode(ff.getCd());
            assertNotNull("Ingested postal code is null", pc);

            StringList delete = new StringList();
            delete.getList().add(ff.getCd());
            ingestionController.deletePostalCodes(delete, CONTENT_PRODUCER_EMAIL, PASSWORD);

            pc = postalCodeService.findPostalCodeByCode(ff.getCd());
            assertNull("Ingested postal code is not null", pc);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting postal codes complete");
    }

    @Test
    public void testIngestContentProducerUsers() {
        log.info("Testing ingesting content producer users...");

        try {

            ingestionController.initiateContentIngestion();

            User user = userService.findUserByEmail(CONTENT_PRODUCER_EMAIL);
            assertNotNull("Ingested user is null", user);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }
        assertTrue(true);
        log.info("Testing ingesting content producer users complete");
    }

    @Test
    public void testIngestNavigationBars() {
        log.info("Testing ingesting navigation bars...");

        try {

            List<NavigationBar> list = new ArrayList<NavigationBar>();
            NavigationBar nb = new NavigationBar();
            nb.setDflt(true);
            nb.setLcl(Locale.US);
            nb.setRlnm(NAVIGATION_BAR_INGESTION_TEST);
            CategoryGroup cg = new CategoryGroup();
            cg.setNm("CategoryGroup");
            cg.setRlnm("CategoryGroup");
            cg.setRdr(1);
            cg.setStts(PublishStatus.UNPUBLISHED);
            List<CategoryGroup> groups = new ArrayList<CategoryGroup>();
            groups.add(cg);
            nb.setGrps(groups);
            
            list.add(nb);

            NavigationBars nbs = new NavigationBars(list);

            // insert method
            nbs = ingestionController.ingestNavigationBars(nbs, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("NavigationBars result is null", nbs);
            assertNotNull("Saving navigation bars returns null", nbs.getList().get(0).getId());

            nbs = ingestionController.updateNavigationBars(nbs, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("NavigationBars result is null", nbs);
            assertNotNull("Saving navigation bars returns null", nbs.getList().get(0).getId());

            nb = navigationBarService.findNavigationBarByUrlName(NAVIGATION_BAR_INGESTION_TEST);
            assertNotNull("Ingested navigation bar is null", nb);

            StringList deletes = new StringList();
            deletes.getList().add(NAVIGATION_BAR_INGESTION_TEST);
            ingestionController.deleteNavigationBars(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            nb = navigationBarService.findNavigationBarByUrlName(NAVIGATION_BAR_INGESTION_TEST);
            assertNull("Navigation bar did not get removed", nb);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing ingesting navigation bars complete");
    }

    @Test
    public void testIngestDeals() {
        log.info("Testing ingesting deals...");

        try {

            List<Deal> list = new ArrayList<Deal>();
            Deal deal = new Deal();
            deal.setRlnm(STORE_TEST);
            deal.setNm(STORE_TEST);
            list.add(deal);

            Deals deals = new Deals(list);

            // insert method
            deals = ingestionController.ingestDeals(deals, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Deals result is null", deals);
            assertNotNull("Saving deals returns null", deals.getList().get(0).getId());

            deals = ingestionController.updateDeals(deals, CONTENT_PRODUCER_EMAIL, PASSWORD);
            assertNotNull("Deals result is null", deals);
            assertNotNull("Saving deals returns null", deals.getList().get(0).getId());

            List<String> storeUrlNames = new ArrayList<String>();
            storeUrlNames.add(STORE_TEST);

            List<Deal> result = dealService.findDealsForStores(storeUrlNames);
            assertNotNull("Ingested deal is null", result);

            StringList deletes = new StringList();
            deletes.getList().add(STORE_TEST);
            ingestionController.deleteDeals(deletes, CONTENT_PRODUCER_EMAIL, PASSWORD);

            result = dealService.findDealsForStores(storeUrlNames);
            assertNull("Deals did not get removed", result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing ingesting navigation bars complete");
    }
}
