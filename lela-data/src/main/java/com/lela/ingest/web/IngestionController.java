/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */



package com.lela.ingest.web;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.service.*;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.*;
import com.lela.domain.dto.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/28/11
 * Time: 10:40 AM
 * Responsibility: This controller is responsible for ingesting content that is coming from Talend feeds
 */
@Controller
@RequestMapping("/ingestjson/*")
public class IngestionController {

    /** Field description */
    private static final String CONTENTPRODUCER_ROLE = "contentproducer";

    /**
     * Field description
     */
    private static final String CONTENT_INGESTION_USER_EMAIL = "john@lelaknows.com";

    /**
     * Field description
     */
    private static final String CONTENT_INGESTION_USER_PASSWORD = "pn3M9-546m|bsUe)0l8j";

    /**
     * Field description
     */
    private static final String CREDENTIALS_ARE_OK_SECURING_CHANNEL = "Credentials are ok. Securing channel...";

    /**
     * Field description
     */
    private static final String NO_USER_WITH_THAT_USERNAME = "No user with that email: %s";

    /**
     * Field description
     */
    private static final String PROCESSING_D_ENTRIES = "Processing %d entries";

    /**
     * Field description
     */
    private static final String UNSECURING_CHANNEL = "Unsecuring channel";

    /**
     * Field description
     */
    private static final String YOUR_CREDENTIALS_ARE_INCORRECT = "Your credentials are incorrect";

    /**
     * Field description
     */
    private static final String YOU_HAVE_PASSED_AN_EMPTY_DATA_SET = "You have passed an empty data set.";

    /**
     * Field description
     */
    private final static Logger log = LoggerFactory.getLogger(IngestionController.class);

    //~--- fields -------------------------------------------------------------

    /**
     * Field description
     */
    private final MetricService metricService;

    private final StoreService storeService;

    private final OwnerService ownerService;

    private final BranchService branchService;

    private final PostalCodeService postalCodeService;

    private final UserService userService;

    private final ItemService itemService;

    private final CategoryService categoryService;

    private final NavigationBarService navigationBarService;

    private final CampaignService campaignService;

    private final AffiliateService affiliateService;

    private final QuizService quizService;

    private final FunctionalFilterService functionalFilterService;

    private final RoleService roleService;

    private final OfferService offerService;

    private final ComputedCategoryService computedCategoryService;

    private final DealService dealService;

    private final ProfileService profileService;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     * @param metricService metricService
     * @param storeService storeService
     * @param ownerService ownerService
     * @param branchService branchService
     * @param postalCodeService postalCodeService
     * @param userService          userService
     * @param itemService itemService
     * @param categoryService categoryService
     * @param navigationBarService navigationBarService
     * @param campaignService campaignService
     * @param affiliateService affiliateService
     * @param quizService quizService
     * @param functionalFilterService functionalFilterService
     * @param roleService roleService
     * @param offerService offerService
     * @param computedCategoryService
     * @param dealService
     * @param profileService
     */
    @Autowired
    public IngestionController(MetricService metricService,
                               StoreService storeService,
                               OwnerService ownerService,
                               BranchService branchService,
                               PostalCodeService postalCodeService,
                               UserService userService,
                               ItemService itemService,
                               CategoryService categoryService,
                               NavigationBarService navigationBarService,
                               CampaignService campaignService,
                               AffiliateService affiliateService,
                               QuizService quizService,
                               FunctionalFilterService functionalFilterService,
                               RoleService roleService,
                               OfferService offerService,
                               ComputedCategoryService computedCategoryService,
                               DealService dealService, ProfileService profileService) {
        this.metricService = metricService;
        this.storeService = storeService;
        this.ownerService = ownerService;
        this.branchService = branchService;
        this.postalCodeService = postalCodeService;
        this.userService          = userService;
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.navigationBarService = navigationBarService;
        this.campaignService = campaignService;
        this.affiliateService = affiliateService;
        this.quizService = quizService;
        this.functionalFilterService = functionalFilterService;
        this.roleService = roleService;
        this.offerService = offerService;
        this.computedCategoryService = computedCategoryService;
        this.dealService = dealService;
        this.profileService = profileService;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     * @param affiliateAccounts affiliateAccounts
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "affiliateaccount/remove", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public StringList deleteAffiliateAccounts(@RequestBody StringList affiliateAccounts,
                                   @RequestHeader(value = "iu", required = true) String email,
                                   @RequestHeader(value = "ip",
                                           required = true) String password) {
        StringList result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((affiliateAccounts != null) && (affiliateAccounts.getList() != null) && !affiliateAccounts.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, affiliateAccounts.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String affiliateAccount : affiliateAccounts.getList()) {
                        affiliateService.removeAffiliateAccount(affiliateAccount);
                    }

                    result.setList(affiliateAccounts.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param branches branches
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "branch/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public StringList deleteBranches(@RequestBody StringList branches,
                                   @RequestHeader(value = "iu", required = true) String email,
                                   @RequestHeader(value = "ip",
            required = true) String password) {
        StringList result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((branches != null) && (branches.getList() != null) && !branches.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, branches.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String branch : branches.getList()) {
                        branchService.removeBranch(branch);
                    }

                    result.setList(branches.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param campaigns campaigns
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "campaign/remove", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public StringList deleteCampaigns(@RequestBody StringList campaigns,
                                   @RequestHeader(value = "iu", required = true) String email,
                                   @RequestHeader(value = "ip",
                                           required = true) String password) {
        StringList  result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((campaigns != null) && (campaigns.getList() != null) && !campaigns.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, campaigns.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String campaign : campaigns.getList()) {
                        campaignService.removeCampaign(campaign);
                    }

                    result.setList(campaigns.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Deletes a list of categories
     *
     * @param categories categories
     * @param email      email
     * @param password   password
     * @return Return value
     */
    @RequestMapping(value = "category/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public StringList deleteCategories(@RequestBody StringList categories,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
            required = true) String password) {
        StringList result = new StringList();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((categories != null) && (categories.getList() != null) && !categories.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, categories.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String category : categories.getList()) {
                        categoryService.removeCategory(category);
                    }

                    result.setList(categories.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param filters filters
     * @param email email
     * @param password password
     *
     * @return Return value
     */
    @RequestMapping(value = "functionalfilter/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public ObjectIds deleteFunctionalFilters(@RequestBody ObjectIds filters,
            @RequestHeader(value = "iu",
                           required = true) String email, @RequestHeader(value = "ip",
                           required = true) String password) {
        ObjectIds result = new ObjectIds();
        Principal         u      = null;
        User              user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((filters != null) && (filters.getList() != null) && !filters.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, filters.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (ObjectId filter : filters.getList()) {
                        functionalFilterService.removeFunctionalFilter(filter);
                    }

                    result.setList(filters.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param items    items
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "item/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public StringList deleteItems(@RequestBody StringList items, @RequestHeader(value = "iu", required = true) String email,
                             @RequestHeader(value = "ip",
            required = true) String password) {
        StringList result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if (items != null && items.getList() != null && !items.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, items.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    // first we need to retrieve one item so we know the category
                    Item item = itemService.findItemByUrlName(items.getList().get(0));

                    for (String rlnm : items.getList()) {
                        itemService.removeItem(rlnm);
                    }

                    // update category last update
                    // refresh category for this list of items
                    categoryService.refreshCategory(item.getCtgry().getRlnm());

                    // clear ingested category for all users
                    computedCategoryService.setAllComputedCategoryToDirty(item.getCtgry().getRlnm());

                    result.setList(items.getList());

                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param metrics  metrics
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "metric/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public ObjectIds deleteMetrics(@RequestBody ObjectIds metrics,
                                 @RequestHeader(value = "iu", required = true) String email,
                                 @RequestHeader(value = "ip",
            required = true) String password) {
        ObjectIds   result = new ObjectIds();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((metrics != null) && (metrics.getList() != null) && !metrics.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, metrics.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (ObjectId metric : metrics.getList()) {
                        metricService.removeMetric(metric);
                    }

                    result.setList(metrics.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param offers   offers
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "offer/remove", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public StringList deleteOffers(@RequestBody StringList offers, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
                                       required = true) String password) {
        StringList result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((offers != null) && (offers.getList() != null) && !offers.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, offers.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String offer: offers.getList()) {
                        offerService.removeOffer(offer);
                    }

                    result.setList(offers.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param owners   owners
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "owner/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public StringList deleteOwners(@RequestBody StringList owners, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
            required = true) String password) {
        StringList result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((owners != null) && (owners.getList() != null) && !owners.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, owners.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String rlnm : owners.getList()) {
                        ownerService.removeOwner(rlnm);
                    }

                    result.setList(owners.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param postalCodes postalCodes
     * @param email email
     * @param password password
     *
     * @return Return value
     */
    @RequestMapping(value = "postalcode/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public StringList deletePostalCodes(@RequestBody StringList postalCodes, @RequestHeader(value = "iu",
            required = true) String email, @RequestHeader(value = "ip", required = true) String password) {
        StringList result = new StringList();
        Principal   u      = null;
        User        user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((postalCodes != null) && (postalCodes.getList() != null) && !postalCodes.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, postalCodes.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String code : postalCodes.getList()) {
                        postalCodeService.removePostalCode(code);
                    }

                    result.setList(postalCodes.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param questions questions
     * @param email     email
     * @param password  password
     * @return Return value
     */
    @RequestMapping(value = "question/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public StringList deleteQuestions(@RequestBody StringList questions,
                                     @RequestHeader(value = "iu", required = true) String email,
                                     @RequestHeader(value = "ip",
            required = true) String password) {
        StringList result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((questions != null) && (questions.getList() != null) && !questions.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, questions.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String question : questions.getList()) {
                        quizService.removeQuestion(question);
                    }

                    result.setList(questions.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param roles    roles
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "role/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public StringList deleteRoles(@RequestBody StringList roles, @RequestHeader(value = "iu", required = true) String email,
                             @RequestHeader(value = "ip",
            required = true) String password) {
        StringList result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((roles != null) && (roles.getList() != null) && !roles.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, roles.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String role : roles.getList()) {
                        roleService.removeRole(role);
                    }

                    result.setList(roles.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param stores   stores
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "store/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public StringList deleteStores(@RequestBody StringList stores, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
            required = true) String password) {
        StringList result = new StringList();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((stores != null) && (stores.getList() != null) && !stores.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, stores.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String store : stores.getList()) {
                        storeService.removeStore(store);
                    }

                    result.setList(stores.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param users    users
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "user/remove", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Users deleteUsers(@RequestBody Users users, @RequestHeader(value = "iu", required = true) String email,
                             @RequestHeader(value = "ip",
            required = true) String password) {
        Users     result = new Users();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((users != null) && (users.getList() != null) && !users.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, users.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (User u1 : users.getList()) {
                        userService.removeUser(u1);
                    }

                    result.setList(users.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param affiliateAccounts affiliateAccounts
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "affiliateaccount", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public AffiliateAccounts ingestAffiliateAccounts(@RequestBody AffiliateAccounts affiliateAccounts,
                                                   @RequestHeader(value = "iu", required = true) String email,
                                                   @RequestHeader(value = "ip",
                                                           required = true) String password) {
        AffiliateAccounts  result = new AffiliateAccounts();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((affiliateAccounts != null) && (affiliateAccounts.getList() != null) && !affiliateAccounts.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, affiliateAccounts.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(affiliateService.saveAffiliateAccounts(affiliateAccounts.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param branches branches
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "branch", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Branches ingestBranches(@RequestBody Branches branches,
                                   @RequestHeader(value = "iu", required = true) String email,
                                   @RequestHeader(value = "ip",
            required = true) String password) {
        Branches  result = new Branches();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((branches != null) && (branches.getList() != null) && !branches.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, branches.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(branchService.saveBranches(branches.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param campaigns campaigns
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "campaign", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public Campaigns ingestCampaigns(@RequestBody Campaigns campaigns,
                                   @RequestHeader(value = "iu", required = true) String email,
                                   @RequestHeader(value = "ip",
                                           required = true) String password) {
        Campaigns  result = new Campaigns();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((campaigns != null) && (campaigns.getList() != null) && !campaigns.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, campaigns.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(campaignService.saveCampaigns(campaigns.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param categories roles
     * @param email      email
     * @param password   password
     * @return Return value
     */
    @RequestMapping(value = "category", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Categories ingestCategories(@RequestBody Categories categories,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
            required = true) String password) {
        Categories result = new Categories();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((categories != null) && (categories.getList() != null) && !categories.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, categories.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(categoryService.saveCategories(categories.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param filters filters
     * @param email email
     * @param password password
     *
     * @return Return value
     */
    @RequestMapping(value = "functionalfilter", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public FunctionalFilters ingestFunctionalFilters(@RequestBody FunctionalFilters filters,
            @RequestHeader(value = "iu",
                           required = true) String email, @RequestHeader(value = "ip",
                           required = true) String password) {
        FunctionalFilters result = new FunctionalFilters();
        Principal         u      = null;
        User              user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((filters != null) && (filters.getList() != null) && !filters.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, filters.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(functionalFilterService.saveFunctionalFilters(filters.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param items    items
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "item", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Items ingestItems(@RequestBody Items items, @RequestHeader(value = "iu", required = true) String email,
                             @RequestHeader(value = "ip",
            required = true) String password) {
        Items     result = new Items();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((items != null) && (items.getList() != null) && !items.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, items.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(itemService.saveItems(items.getList()));

                    // update category last update
                    // refresh category for this list of items
                    categoryService.refreshCategory(items.getList().get(0).getCtgry().getRlnm());

                    // clear ingested category for all users
                    computedCategoryService.setAllComputedCategoryToDirty(items.getList().get(0).getCtgry().getRlnm());

                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param metrics  metrics
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "metric", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Metrics ingestMetrics(@RequestBody Metrics metrics,
                                 @RequestHeader(value = "iu", required = true) String email,
                                 @RequestHeader(value = "ip",
            required = true) String password) {
        Metrics   result = new Metrics();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((metrics != null) && (metrics.getList() != null) && !metrics.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, metrics.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(metricService.saveMetrics(metrics.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param offers   offers
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "offer", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public Offers ingestOffers(@RequestBody Offers offers, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
                                       required = true) String password) {
        Offers    result = new Offers();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((offers != null) && (offers.getList() != null) && !offers.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, offers.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(offerService.saveOffers(offers.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param owners   owners
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "owner", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Owners ingestOwners(@RequestBody Owners owners, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
            required = true) String password) {
        Owners    result = new Owners();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((owners != null) && (owners.getList() != null) && !owners.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, owners.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(ownerService.saveOwners(owners.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param postalCodes postalCodes
     * @param email email
     * @param password password
     *
     * @return Return value
     */
    @RequestMapping(value = "postalcode", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public PostalCodes ingestPostalCodes(@RequestBody PostalCodes postalCodes, @RequestHeader(value = "iu",
            required = true) String email, @RequestHeader(value = "ip", required = true) String password) {
        PostalCodes result = new PostalCodes();
        Principal   u      = null;
        User        user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((postalCodes != null) && (postalCodes.getList() != null) && !postalCodes.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, postalCodes.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    result.setList(postalCodeService.savePostalCodes(postalCodes.getList()));

                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param questions questions
     * @param email     email
     * @param password  password
     * @return Return value
     */
    @RequestMapping(value = "question", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Questions ingestQuestions(@RequestBody Questions questions,
                                     @RequestHeader(value = "iu", required = true) String email,
                                     @RequestHeader(value = "ip",
            required = true) String password) {
        Questions result = new Questions();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((questions != null) && (questions.getList() != null) && !questions.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, questions.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(quizService.saveQuestions(questions.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param roles    roles
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "role", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Roles ingestRoles(@RequestBody Roles roles, @RequestHeader(value = "iu", required = true) String email,
                             @RequestHeader(value = "ip",
            required = true) String password) {
        Roles     result = new Roles();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((roles != null) && (roles.getList() != null) && !roles.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, roles.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(roleService.saveRoles(roles.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param stores   stores
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "store", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Stores ingestStores(@RequestBody Stores stores, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
            required = true) String password) {
        Stores    result = new Stores();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((stores != null) && (stores.getList() != null) && !stores.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, stores.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(storeService.saveStores(stores.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param users    users
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "user", method = RequestMethod.POST, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Users ingestUsers(@RequestBody Users users, @RequestHeader(value = "iu", required = true) String email,
                             @RequestHeader(value = "ip",
            required = true) String password) {
        Users     result = new Users();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((users != null) && (users.getList() != null) && !users.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, users.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(userService.saveUsers(users.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * This method creates a content ingestion user that can ingest all the other end points on this controller
     *
     */
    @RequestMapping(value = "initialize", method = RequestMethod.GET, produces = { "application/json" })
    @ResponseBody
    public void initiateContentIngestion() {
        Users result = new Users();
        User  user   = userService.findUserByEmail(CONTENT_INGESTION_USER_EMAIL);

        if (user != null) {
            log.warn("Content ingestion user already exists");
            result.setMessage("Content Producer User already exists");
        } else {
            Role role = roleService.findRoleByUrlName(CONTENTPRODUCER_ROLE);

            if (role == null) {
                role = new Role();
                role.setRlnm(CONTENTPRODUCER_ROLE);
                role.setNm("Content Producer");

                List<String> rights = new ArrayList<String>(1);

                rights.add("RIGHT_CONTENT_INGEST");
                role.setRghts(rights);
                SpringSecurityHelper.secureChannel();
                role = roleService.saveRole(role);
                SpringSecurityHelper.unsecureChannel();
            }

            user = new User();
            user.setMl(CONTENT_INGESTION_USER_EMAIL);
            user.setPsswrd(CONTENT_INGESTION_USER_PASSWORD);

            List<String> roles = new ArrayList<String>(1);

            roles.add(role.getRlnm());
            user.setRrlnms(roles);
            user = profileService.registerContentIngestionUser(user);
            result.setList(new ArrayList<User>());
            result.getList().add(user);
            result.setMessage(WebConstants.SUCCESS);

            if (log.isInfoEnabled()) {
                log.info("Content producer created successfully");
            }
        }
    }

    /**
     * Method description
     *
     * @param affiliateAccounts affiliateAccounts
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "affiliateaccount", method = RequestMethod.PUT, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public AffiliateAccounts updateAffiliateAccounts(@RequestBody AffiliateAccounts affiliateAccounts,
                                   @RequestHeader(value = "iu", required = true) String email,
                                   @RequestHeader(value = "ip",
                                           required = true) String password) {
        AffiliateAccounts  result = new AffiliateAccounts();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((affiliateAccounts != null) && (affiliateAccounts.getList() != null) && !affiliateAccounts.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, affiliateAccounts.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (AffiliateAccount affiliateAccount : affiliateAccounts.getList()) {
                        affiliateService.saveAffiliateAccount(affiliateAccount);
                    }

                    result.setList(affiliateAccounts.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param branches branches
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "branch", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Branches updateBranches(@RequestBody Branches branches,
                                   @RequestHeader(value = "iu", required = true) String email,
                                   @RequestHeader(value = "ip",
            required = true) String password) {
        Branches  result = new Branches();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((branches != null) && (branches.getList() != null) && !branches.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, branches.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Branch branch : branches.getList()) {
                        branchService.saveBranch(branch);
                    }

                    result.setList(branches.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param campaigns branches
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "campaign", method = RequestMethod.PUT, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public Campaigns updateCampaigns(@RequestBody Campaigns campaigns,
                                   @RequestHeader(value = "iu", required = true) String email,
                                   @RequestHeader(value = "ip",
                                           required = true) String password) {
        Campaigns  result = new Campaigns();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((campaigns != null) && (campaigns.getList() != null) && !campaigns.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, campaigns.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Campaign campaign : campaigns.getList()) {
                        campaignService.saveCampaign(campaign);
                    }

                    result.setList(campaigns.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * This differs from the ingestCategories method in that it saves one category at the time. This should be used
     * for incremental updates of an existing category as it doesn't affect the cache so badly
     *
     * @param categories roles
     * @param email      email
     * @param password   password
     * @return
     */
    @RequestMapping(value = "category", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Categories updateCategories(@RequestBody Categories categories,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
            required = true) String password) {
        Categories result = new Categories();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((categories != null) && (categories.getList() != null) && !categories.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, categories.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Category category : categories.getList()) {
                        categoryService.saveCategory(category);
                    }

                    result.setList(categories.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param filters filters
     * @param email email
     * @param password password
     *
     * @return Return value
     */
    @RequestMapping(value = "functionalfilter", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public FunctionalFilters updateFunctionalFilters(@RequestBody FunctionalFilters filters,
            @RequestHeader(value = "iu",
                           required = true) String email, @RequestHeader(value = "ip",
                           required = true) String password) {
        FunctionalFilters result = new FunctionalFilters();
        Principal         u      = null;
        User              user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((filters != null) && (filters.getList() != null) && !filters.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, filters.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (FunctionalFilter filter : filters.getList()) {
                        functionalFilterService.saveFunctionalFilter(filter);
                    }

                    result.setList(filters.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * This differs from the ingestItems method in that it saves one category at the time. This should be used
     * for incremental updates of an existing item as it doesn't affect the cache so badly
     *
     * @param items    items
     * @param email    email
     * @param password password
     * @return
     */
    @RequestMapping(value = "item", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Items updateItems(@RequestBody Items items, @RequestHeader(value = "iu", required = true) String email,
                             @RequestHeader(value = "ip",
            required = true) String password) {
        Items     result = new Items();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((items != null) && (items.getList() != null) && !items.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, items.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Item item : items.getList()) {
                        itemService.saveItem(item);
                    }

                    // update category last update
                    // refresh category for this list of items
                    categoryService.refreshCategory(items.getList().get(0).getCtgry().getRlnm());

                    // clear ingested category for all users
                    computedCategoryService.setAllComputedCategoryToDirty(items.getList().get(0).getCtgry().getRlnm());

                    result.setList(items.getList());

                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param metrics  metrics
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "metric", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Metrics updateMetrics(@RequestBody Metrics metrics,
                                 @RequestHeader(value = "iu", required = true) String email,
                                 @RequestHeader(value = "ip",
            required = true) String password) {
        Metrics   result = new Metrics();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((metrics != null) && (metrics.getList() != null) && !metrics.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, metrics.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Metric metric : metrics.getList()) {
                        metricService.saveMetric(metric);
                    }

                    result.setList(metrics.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * This differs from the ingestOffers method in that it saves one category at the time. This should be used
     * for incremental updates of an existing owner as it doesn't affect the cache so badly
     *
     * @param offers   offers
     * @param email    email
     * @param password password
     * @return
     */
    @RequestMapping(value = "offer", method = RequestMethod.PUT, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public Offers updateOffers(@RequestBody Offers offers, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
                                       required = true) String password) {
        Offers    result = new Offers();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((offers != null) && (offers.getList() != null) && !offers.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, offers.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Offer offer : offers.getList()) {
                        offerService.saveOffer(offer);
                    }

                    result.setList(offers.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * This differs from the ingestOwners method in that it saves one category at the time. This should be used
     * for incremental updates of an existing owner as it doesn't affect the cache so badly
     *
     * @param owners   owners
     * @param email    email
     * @param password password
     * @return
     */
    @RequestMapping(value = "owner", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Owners updateOwners(@RequestBody Owners owners, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
            required = true) String password) {
        Owners    result = new Owners();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((owners != null) && (owners.getList() != null) && !owners.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, owners.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Owner owner : owners.getList()) {
                        ownerService.saveOwner(owner);
                    }

                    result.setList(owners.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     *
     * @param postalCodes postalCodes
     * @param email email
     * @param password password
     *
     * @return Return value
     */
    @RequestMapping(value = "postalcode", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public PostalCodes updatePostalCodes(@RequestBody PostalCodes postalCodes, @RequestHeader(value = "iu",
            required = true) String email, @RequestHeader(value = "ip", required = true) String password) {
        PostalCodes result = new PostalCodes();
        Principal   u      = null;
        User        user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((postalCodes != null) && (postalCodes.getList() != null) && !postalCodes.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, postalCodes.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (PostalCode pc : postalCodes.getList()) {
                        postalCodeService.savePostalCode(pc);
                    }

                    result.setList(postalCodes.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param questions questions
     * @param email     email
     * @param password  password
     * @return Return value
     */
    @RequestMapping(value = "question", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Questions updateQuestions(@RequestBody Questions questions,
                                     @RequestHeader(value = "iu", required = true) String email,
                                     @RequestHeader(value = "ip",
            required = true) String password) {
        Questions result = new Questions();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((questions != null) && (questions.getList() != null) && !questions.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, questions.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Question question : questions.getList()) {
                        quizService.saveQuestion(question);
                    }

                    result.setList(questions.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param roles    roles
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "role", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Roles updateRoles(@RequestBody Roles roles, @RequestHeader(value = "iu", required = true) String email,
                             @RequestHeader(value = "ip",
            required = true) String password) {
        Roles     result = new Roles();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((roles != null) && (roles.getList() != null) && !roles.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, roles.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Role role : roles.getList()) {
                        roleService.saveRole(role);
                    }

                    result.setList(roles.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Method description
     *
     * @param stores   stores
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "store", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Stores updateStores(@RequestBody Stores stores, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
            required = true) String password) {
        Stores    result = new Stores();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((stores != null) && (stores.getList() != null) && !stores.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, stores.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Store store : stores.getList()) {
                        storeService.saveStore(store);
                    }

                    result.setList(stores.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Updates the available stores where this item is available
     *
     * @param items items
     * @param email    email
     * @param password password
     * @return Return value
     */
    @RequestMapping(value = "availableinstore", method = RequestMethod.PUT, consumes = { "application/json" },
                    produces = { "application/json" })
    @ResponseBody
    public Items updateAvailableInStoreOnItem(@RequestBody Items items, @RequestHeader(value = "iu", required = true) String email,
                               @RequestHeader(value = "ip",
            required = true) String password) {
        Items    result = new Items();
        Principal u      = null;
        User      user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((items != null) && (items.getList() != null) && !items.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, items.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Item item : items.getList()) {
                        itemService.updateAvailableStoresOnItem(item);
                    }

                    result.setList(items.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Persists a list of navigation bars
     * @param navigationBars navigationBars
     * @param email email
     * @param password password
     * @return Persisted navigation bars
     */
    @RequestMapping(value = "navigationbar", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public NavigationBars ingestNavigationBars(@RequestBody NavigationBars navigationBars,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
                                               required = true) String password) {
        NavigationBars result = new NavigationBars();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((navigationBars != null) && (navigationBars.getList() != null) && !navigationBars.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, navigationBars.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(navigationBarService.saveNavigationBars(navigationBars.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Updates a list of deals
     * @param deals deals
     * @param email email
     * @param password password
     * @return Updated nav bars
     */
    @RequestMapping(value = "deal", method = RequestMethod.PUT, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public Deals updateDeals(@RequestBody Deals deals,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
                                               required = true) String password) {
        Deals result = new Deals();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((deals != null) && (deals.getList() != null) && !deals.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, deals.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (Deal deal : deals.getList()) {
                        dealService.removeAndSave(deal);
                    }

                    result.setList(deals.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Deletes a list of categories
     *
     * @param deals deals
     * @param email      email
     * @param password   password
     * @return Return value
     */
    @RequestMapping(value = "deal/remove", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public StringList deleteDeals(@RequestBody StringList deals,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
                                               required = true) String password) {
        StringList result = new StringList();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((deals != null) && (deals.getList() != null) && !deals.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, deals.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String storeUrlName : deals.getList()) {
                        dealService.removeDealsForStore(storeUrlName);
                    }

                    result.setList(deals.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Persists a list of deals
     * @param deals deals
     * @param email email
     * @param password password
     * @return Persisted navigation bars
     */
    @RequestMapping(value = "deal", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public Deals ingestDeals(@RequestBody Deals deals,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
                                               required = true) String password) {
        Deals result = new Deals();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((deals != null) && (deals.getList() != null) && !deals.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, deals.getList().size()));
                    SpringSecurityHelper.secureChannel(u);
                    result.setList(dealService.removeAndSave(deals.getList()));
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Updates a list of navigation bars
     * @param navigationBars navigationBars
     * @param email email
     * @param password password
     * @return Updated nav bars
     */
    @RequestMapping(value = "navigationbar", method = RequestMethod.PUT, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public NavigationBars updateNavigationBars(@RequestBody NavigationBars navigationBars,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
                                               required = true) String password) {
        NavigationBars result = new NavigationBars();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((navigationBars != null) && (navigationBars.getList() != null) && !navigationBars.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, navigationBars.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (NavigationBar nb : navigationBars.getList()) {
                        navigationBarService.saveNavigationBar(nb);
                    }

                    result.setList(navigationBars.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }

    /**
     * Deletes a list of categories
     *
     * @param navigationBars navigationBars
     * @param email      email
     * @param password   password
     * @return Return value
     */
    @RequestMapping(value = "navigationbar/remove", method = RequestMethod.POST, consumes = { "application/json" },
            produces = { "application/json" })
    @ResponseBody
    public StringList deleteNavigationBars(@RequestBody StringList navigationBars,
                                       @RequestHeader(value = "iu", required = true) String email,
                                       @RequestHeader(value = "ip",
                                               required = true) String password) {
        StringList result = new StringList();
        Principal  u      = null;
        User       user   = userService.findUserByEmail(email);

        if (user != null) {
            u = new Principal(user);
        }

        try {
            if (u == null) {
                log.info(NO_USER_WITH_THAT_USERNAME);
                result.setMessage(String.format(NO_USER_WITH_THAT_USERNAME, email));
            } else if (userService.isValid(u.getPassword(), password)) {
                if ((navigationBars != null) && (navigationBars.getList() != null) && !navigationBars.getList().isEmpty()) {
                    log.info(CREDENTIALS_ARE_OK_SECURING_CHANNEL);
                    log.info(String.format(PROCESSING_D_ENTRIES, navigationBars.getList().size()));
                    SpringSecurityHelper.secureChannel(u);

                    for (String navigationBarUrlName : navigationBars.getList()) {
                        navigationBarService.removeNavigationBar(navigationBarUrlName);
                    }

                    result.setList(navigationBars.getList());
                    result.setMessage(WebConstants.SUCCESS);
                } else {
                    log.info(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                    result.setMessage(YOU_HAVE_PASSED_AN_EMPTY_DATA_SET);
                }
            } else {
                log.info(YOUR_CREDENTIALS_ARE_INCORRECT);
                result.setMessage(YOUR_CREDENTIALS_ARE_INCORRECT);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setMessage(ex.getMessage());
        } finally {
            log.info(UNSECURING_CHANNEL);
            SpringSecurityHelper.unsecureChannel();
        }

        return result;
    }
}
