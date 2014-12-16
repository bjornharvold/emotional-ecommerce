/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain;

/**
 * Created by Bjorn Harvold
 * Date: 10/19/11
 * Time: 2:22 PM
 * Responsibility:
 */
public class ApplicationConstants {

    public static final String LELA_AFFILIATE_ID = "lela";

    public static final Float[] RADIUS_STEPS = {10f, 25f, 50f};

    /**
     * Service success value
     */
    public static final String SUCCESS = "1";

    /**
     * Service failure value
     */
    public static final String FAILURE = "0";

    /**
     * Field description
     */
    public static final String CURRENT_DYNAMIC_RANGE_KEY_HIGH = "currentHigh";

    /**
     * Field description
     */
    public static final String CURRENT_DYNAMIC_RANGE_KEY_LOW = "currentLow";

    /**
     * Field description
     */
    public static final String DYNAMIC_RANGE_HIGH = "high";

    /**
     * Field description
     */
    public static final String DYNAMIC_RANGE_LOW = "low";
    
    /**
     * Field description
     */
    public static final String BRANCH_CACHE = "branch";

    /**
     * Field description
     */
    public static final String CATEGORIES_KEY = "categories";

    /**
     * Field description
     */
    public final static String CATEGORY_CACHE = "category";

    /**
     * Field description
     */
    public static final String ITEM_CATEGORY_CACHE = "categoryitems";

    /**
     * Field description
     */
    public static final String CLIENT_DETAILS_CACHE = "clientdetails";

    /**
     * Field description
     */
    public static final String FUNCTIONAL_FILTER_CACHE = "functionalfilter";

    /**
     * Field description
     */
    public static final String ITEM_CACHE = "item";

    /**
     * Field description
     */
    public final static String OFFER_CACHE = "offer";

    /**
     * Field description
     */
    public final static String OWNER_CACHE = "owner";

    /**
     * Field description
     */
    public static final String POSTAL_CODE_CACHE = "postalcode";

    /**
     * Field description
     */
    public static final String QUESTION_CACHE = "question";

    public static final String CSS_STYLES_KEY = "cssStyles";
    public static final String CSS_STYLE_CACHE = "cssStyle";

    /**
     * Field description
     */
    public final static String ROLE_CACHE = "role";

    /** User attributes key on User Supplement object */
    public static final String USER_ATTRIBUTES = "userAttributes";
    public static final String USER_ATTRIBUTE_ADDRESS = "address";
    public static final String USER_ATTRIBUTE_ADDRESS_2 = "address2";
    public static final String USER_ATTRIBUTE_CITY = "city";
    public static final String USER_ATTRIBUTE_STATE = "state";
    public static final String USER_ATTRIBUTE_ZIPCODE = "zip";
    public static final String USER_ATTRIBUTE_DUE_DATE = "ddt";

    /** Default role for users */
    public static final String APPLICATION_USER_ROLE = "appuser";

    /** Cache */
    public static final String STORE_CACHE = "store";
    public static final String STATIC_CONTENT_CACHE = "staticcontent";
    public static final String EVENT_CACHE = "event";
    public static final String CAMPAIGN_CACHE = "campaign";
    public static final String AFFILIATE_ACCOUNT_CACHE = "affiliate_account";
    public static final String DOMAIN_CACHE = "domain";
    public static final String NAVIGATION_BAR_CACHE = "navigationbar";
    public static final String SEO_CACHE = "seo";
    public static final String STORE_SEO_CACHE = "store_seo_cache";
    public static final String STORE_SEO_CACHE_KEY = "store_seo_cache_key";
    public static final String OWNER_SEO_CACHE = "owner_seo_cache";
    public static final String OWNER_SEO_CACHE_KEY = "owner_seo_cache_key";
    public static final String FACEBOOK = "facebook";
    public static final String NETFLIX = "netflix";
    public static final String PRESS_CACHE = "press";
    public static final String QUIZ_CACHE = "quiz";
    public static final String DEFAULT_QUIZ = "default_quiz";
    public static final String APPLICATION_CACHE = "application";
    public static final String COMPUTED_CATEGORY_CACHE = "computed_category";
    public static final String USER_SUPPLEMENT_CACHE = "user_supplement";
    public static final String PRODUCT_GRID_CACHE = "product_grid";
    public static final String USER_CACHE = "user";
    public static final String TRIGGER_CACHE = "trigger";
    public static final String DEAL_CACHE = "deal";

    /** api */
    public static final String APPLICATION_NOT_AUTHENTICATED = "Not authenticated";
    public static final String APPLICATION_MISSING_DATA = "Missing data to process request";
    public static final String APPLICATION_MISSING_USER = "User doesn't exist";
    public static final String APPLICATION_EXISTING_USER = "User already exists";
    public static final String APPLICATION_CREATED_USER = "User created";
    public static final String DEFAULT_NAVBAR = "default_navbar";
    public static final String PRODUCT_REVIEW_CACHE = "productReviewCache";
    /** INGEST */
    public static final String JOB_PARAMETERS = "JOB_PARAMETERS";

    /** Lela List default board name */
    public static final String DEFAULT_BOARD_NAME = "Default";
    public static final String PROFILE_IMAGE_SMALL = "50";
    public static final String PROFILE_IMAGE_LARGE = "200";

    public static final String DATA_APP = "DATA_APP";
    
    public static final String REPORT_ID_PREFIX = "REPORT:";
    public static final String REPORT_TASK_PREFIX = "REPORT_TASK:";
}
