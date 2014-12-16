/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.LelaException;
import com.lela.commons.comparator.AbstractNoteLastUpdateComparator;
import com.lela.commons.comparator.ListCardSortOnOrderAscendingComparator;
import com.lela.commons.event.AddItemToListEvent;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.RemoveItemFromListEvent;
import com.lela.commons.mail.MailServiceException;
import com.lela.commons.service.HTMLUtilityService;
import com.lela.commons.service.ImageUtilityService;
import com.lela.commons.service.MailService;
import com.lela.commons.service.MerchantService;
import com.lela.commons.service.NavigationBarService;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.comparator.CategoryOrderComparator;
import com.lela.commons.comparator.ListCardItemSortOnHighestPriceComparator;
import com.lela.commons.comparator.ListCardSortOnAlertDateComparator;
import com.lela.commons.comparator.ListCardSortOnDateComparator;
import com.lela.commons.comparator.ListCardSortOnOwnedComparator;
import com.lela.commons.comparator.ListCardItemSortOnLowestPriceComparator;
import com.lela.commons.comparator.ListCardSortOnReviewDateComparator;
import com.lela.commons.comparator.OwnerByNameComparator;
import com.lela.commons.comparator.StoreByNameComparator;
import com.lela.commons.service.BranchService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.FavoritesListService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.StoreService;
import com.lela.commons.service.UserService;
import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.Alert;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Category;
import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.Comment;
import com.lela.domain.document.Item;
import com.lela.domain.document.ListCardBoard;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.NavigationBar;
import com.lela.domain.document.Note;
import com.lela.domain.document.Owner;
import com.lela.domain.document.Picture;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.Review;
import com.lela.domain.document.Social;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.dom.DOMElement;
import com.lela.domain.dto.dom.DOMElements;
import com.lela.domain.dto.list.ExternalImageRequest;
import com.lela.domain.dto.list.ExternalListCard;
import com.lela.domain.dto.list.ExternalListCardPicture;
import com.lela.domain.dto.list.ListCardBoardName;
import com.lela.domain.dto.list.ListCardPicture;
import com.lela.domain.document.ListCardProfile;
import com.lela.domain.dto.list.ListEntry;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.document.ListCard;
import com.lela.domain.dto.list.ListPosition;
import com.lela.domain.dto.list.RenameListCard;
import com.lela.domain.dto.list.UserList;
import com.lela.domain.dto.list.UserListQuery;
import com.lela.domain.enums.MailParameter;
import com.lela.domain.enums.list.ListCardType;
import com.lela.util.UtilityException;
import com.lela.util.utilities.number.NumberUtils;
import com.lela.util.utilities.storage.dto.ImageDigest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 9/9/11
 * Time: 11:38 AM
 * Responsibility:
 */
@Service("favoritesListService")
public class FavoritesListServiceImpl implements FavoritesListService {
    private final Logger log = LoggerFactory.getLogger(FavoritesListServiceImpl.class);

    @Value("${amazon.access.key}")
    private String accessKey;

    @Value("${amazon.secret.key}")
    private String secretKey;

    @Value("${card.images.amazon.bucket}")
    private String cardImagesBucketName;

    @Value("${card.images.min.width}")
    private Integer cardImagesMinWidth;

    private final UserService userService;
    private final ProductEngineService productEngineService;
    private final ItemService itemService;
    private final StoreService storeService;
    private final OwnerService ownerService;
    private final BranchService branchService;
    private final MerchantService merchantService;
    private final ImageUtilityService imageUtilityService;
    private final HTMLUtilityService htmlUtilityService;
    private final MailService mailService;
    private final NavigationBarService navigationBarService;

    /*
    Image sizes are placed in a map, therefore no two images can be the same width
     */
    private static final int small = 44;
    private static final int medium = 155;
    private static final int large = 500;
    private static final int list = 250;

    private static Map<Integer, String> imageSizes = new HashMap();

    static {
        imageSizes.put(small, "small");
        imageSizes.put(medium, "medium");
        imageSizes.put(large, "large");
        imageSizes.put(list, "list");
    }


    @Autowired
    public FavoritesListServiceImpl(UserService userService,
                                    ProductEngineService productEngineService,
                                    ItemService itemService,
                                    StoreService storeService,
                                    OwnerService ownerService,
                                    BranchService branchService,
                                    MerchantService merchantService,
                                    ImageUtilityService imageUtilityService,
                                    HTMLUtilityService htmlUtilityService,
                                    MailService mailService,
                                    NavigationBarService navigationBarService) {
        this.userService = userService;
        this.productEngineService = productEngineService;
        this.itemService = itemService;
        this.storeService = storeService;
        this.ownerService = ownerService;
        this.branchService = branchService;
        this.merchantService = merchantService;
        this.imageUtilityService = imageUtilityService;
        this.htmlUtilityService = htmlUtilityService;
        this.mailService = mailService;
        this.navigationBarService = navigationBarService;
    }

    /**
     * Adds an existing item from our site to the user's list
     *
     * @param li li
     * @return ListCard
     */
    @Override
    public ListCard addItemToList(ListEntry li) {
        ListCard result = null;

        // do some preliminary checks
        if (li != null && StringUtils.isNotBlank(li.getUserCode()) && StringUtils.isNotBlank(li.getRlnm())) {
            String userCode = li.getUserCode();
            String urlName = li.getRlnm();
            String boardCode = li.getBoardCode();
            String boardName = li.getBoardName();
            Boolean own = li.getOwn();

            // if the item was already added - we ignore it
            UserSupplement us = userService.findUserSupplement(userCode);

            // check if board code is empty and board name not
            if (StringUtils.isNotBlank(boardName)) {
                // create a new board on the fly
                ListCardBoard lcb = new ListCardBoard(boardName);
                us.addListCardBoard(lcb);

                // save the new board code
                boardCode = lcb.getCd();

                // this is just so the caller gets the new board code
                li.setBoardCode(boardCode);
            }

            if (!us.containsListCard(boardCode, urlName)) {
                // retrieve item from db or cache
                Item item = itemService.findItemByUrlName(urlName);

                if (item != null) {
                    Integer maxOrder = us.findListCardMaxOrder(boardCode);
                    maxOrder += 1;
                    result = new ListCard(ListCardType.ITEM, item.getRlnm(), maxOrder, own);
                    us.addListCard(boardCode, result);

                    userService.saveUserSupplement(us);

                    EventHelper.post(new AddItemToListEvent(userCode, urlName));
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Item: " + urlName + " could not be found.");
                    }
                }
            }
        }

        return result;
    }

    @Override
    public ListCard addOwnerToList(ListEntry li) {
        ListCard result = null;

        // do some preliminary checks
        if (li != null && StringUtils.isNotBlank(li.getUserCode()) && StringUtils.isNotBlank(li.getRlnm())) {
            String userCode = li.getUserCode();
            String urlName = li.getRlnm();
            String boardCode = li.getBoardCode();
            String boardName = li.getBoardName();

            // if the branch was already added - we ignore it
            UserSupplement us = userService.findUserSupplement(userCode);

            // check if board code is empty and board name not
            if (StringUtils.isNotBlank(boardName)) {
                // create a new board on the fly
                ListCardBoard lcb = new ListCardBoard(boardName);
                us.addListCardBoard(lcb);

                // save the new board code
                boardCode = lcb.getCd();
            }

            if (!us.containsListCard(boardCode, urlName)) {
                // retrieve item from db or cache
                Owner owner = ownerService.findOwnerByUrlName(urlName);

                if (owner != null) {
                    Integer maxOrder = us.findListCardMaxOrder(boardCode);
                    result = new ListCard(ListCardType.OWNER, owner.getRlnm(), maxOrder + 1);

                    us.addListCard(boardCode, result);

                    userService.saveUserSupplement(us);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Owner: " + urlName + " could not be found.");
                    }
                }
            }
        }

        return result;
    }

    @Override
    public ListCard addBranchToList(ListEntry li) {
        ListCard result = null;

        // do some preliminary checks
        if (li != null && StringUtils.isNotBlank(li.getUserCode()) && StringUtils.isNotBlank(li.getRlnm())) {
            String userCode = li.getUserCode();
            String urlName = li.getRlnm();
            String boardCode = li.getBoardCode();
            String boardName = li.getBoardName();

            // if the branch was already added - we ignore it
            UserSupplement us = userService.findUserSupplement(userCode);

            // check if board code is empty and board name not
            if (StringUtils.isNotBlank(boardName)) {
                // create a new board on the fly
                ListCardBoard lcb = new ListCardBoard(boardName);
                us.addListCardBoard(lcb);

                // save the new board code
                boardCode = lcb.getCd();
            }

            if (!us.containsListCard(boardCode, urlName)) {
                // retrieve item from db or cache
                Branch branch = branchService.findBranchByUrlName(urlName);

                if (branch != null) {
                    Integer maxOrder = us.findListCardMaxOrder(boardCode);

                    result = new ListCard(ListCardType.BRANCH, branch.getRlnm(), maxOrder + 1);

                    us.addListCard(boardCode, result);

                    userService.saveUserSupplement(us);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Branch: " + urlName + " could not be found.");
                    }
                }
            }

        }

        return result;
    }

    @Override
    public ListCard addStoreToList(ListEntry li) {
        ListCard result = null;

        // do some preliminary checks
        if (li != null && StringUtils.isNotBlank(li.getUserCode()) && StringUtils.isNotBlank(li.getRlnm())) {
            String userCode = li.getUserCode();
            String urlName = li.getRlnm();
            String boardCode = li.getBoardCode();
            String boardName = li.getBoardName();

            UserSupplement us = userService.findUserSupplement(userCode);

            // check if board code is empty and board name not
            if (StringUtils.isNotBlank(boardName)) {
                // create a new board on the fly
                ListCardBoard lcb = new ListCardBoard(boardName);
                us.addListCardBoard(lcb);

                // save the new board code
                boardCode = lcb.getCd();
            }

            // only add store if store doesn't already exist
            if (!us.containsListCard(boardCode, urlName)) {
                Integer maxOrder = us.findListCardMaxOrder(boardCode);
                Store store = storeService.findStoreByUrlName(urlName);

                if (store != null) {
                    result = new ListCard(ListCardType.STORE, store.getRlnm(), maxOrder + 1);

                    us.addListCard(boardCode, result);

                    userService.saveUserSupplement(us);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Store: " + urlName + " could not be found.");
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void deleteListCard(ListEntry li) {
        // do some preliminary checks
        if (li != null && StringUtils.isNotBlank(li.getUserCode()) && StringUtils.isNotBlank(li.getRlnm())) {
            String userCode = li.getUserCode();
            String urlName = li.getRlnm();
            String boardCode = li.getBoardCode();

            UserSupplement us = userService.findUserSupplement(userCode);

            if (us.containsListCard(boardCode, urlName)) {
                // remove from db
                us.removeListCard(boardCode, urlName);

                userService.saveUserSupplement(us);

                EventHelper.post(new RemoveItemFromListEvent(userCode, urlName));

                li.setMessage(ApplicationConstants.SUCCESS);
            } else {
                li.setMessage(ApplicationConstants.FAILURE);
            }

        }
    }

    /**
     * Method creates and populates the UserList object that contains all the data needed to display the user's list
     *
     * @param query query
     * @return UserList
     */
    @Override
    public UserList fetchUserList(UserListQuery query) {
        UserList result = new UserList(query);

        // retrieve User Supplement from cache
        UserSupplement us = userService.findUserSupplement(query.getUserCode());

        ListCardBoard lcb = us.findListCardBoard(query.getBoardCode());

        if (lcb != null) {
            result.setNm(lcb.getNm());
            result.setCd(lcb.getCd());

            // enrich the list card board
            enrichListCardBoard(result, lcb, us);

            // derive categories from the items on our list board
            populateCategoryReferenceData(result, lcb);

            // populate owner drop downs
            populateOwnerReferenceData(result, lcb);

            // populate store drop downs
            populateStoreReferenceData(result, lcb);

            // now we have all the cards loaded up full
            // time to create reference data and filter the list
            populateUserList(result, lcb, query);
        }

        return result;
    }

    private void enrichListCardBoard(UserList ul, ListCardBoard lcb, UserSupplement us) {

        if ((lcb.getCrds() != null) && !lcb.getCrds().isEmpty()) {
            // set the bard's owner profile on the UserList
            if (ul.getPrfl() == null) {
                ul.setPrfl(createUserProfile(us));
            }
            for (ListCard lc : lcb.getCrds()) {
                enrichListCard(us, lc);
            }
        }

    }

    private void enrichListCard(UserSupplement us, ListCard lc) {
        switch (lc.getTp()) {
            case BRANCH:
                loadBranchOnCard(lc);
                break;
            case ITEM:
                loadItemOnCard(us.getMotivator(), us.getCd(), lc);
                break;
            case OWNER:
                loadOwnerOnCard(lc);
                break;
            case STORE:
                loadStoreOnCard(lc);
                break;
        }

        // sort nested lists
        // need to load user profiles on each of the comments
        if (lc.getCmmnts() != null && !lc.getCmmnts().isEmpty()) {
            for (Comment comment : lc.getCmmnts()) {
                loadUserProfileOnComment(comment.getPrfl().getCd(), comment);
            }

            Collections.sort(lc.getCmmnts(), new AbstractNoteLastUpdateComparator());
        }

        if (lc.getNts() != null && !lc.getNts().isEmpty()) {
            Collections.sort(lc.getNts(), new AbstractNoteLastUpdateComparator());
        }

        if (lc.getRvws() != null && !lc.getRvws().isEmpty()) {
            Collections.sort(lc.getRvws(), new AbstractNoteLastUpdateComparator());
        }

        // add user information
        loadUserProfile(us, lc);
    }

    /**
     * Adds user specific information to the card
     *
     * @param lc lc
     */
    private void loadUserProfile(UserSupplement us, ListCard lc) {
        if (lc.getPrfl() == null) {
            lc.setPrfl(createUserProfile(us));
        }
    }

    private ListCardProfile createUserProfile(UserSupplement us) {
        ListCardProfile profile = new ListCardProfile();

        // add the commenting user profile code
        String imageUrl = null;
        String facebookId = null;

        if (us != null) {
            profile.setCd(us.getCd());
            profile.setFnm(us.getFnm());
            profile.setLnm(us.getLnm());

            Social social = us.getSocial(ApplicationConstants.FACEBOOK);

            if (social != null) {
                imageUrl = social.getImageUrl();
                facebookId = social.getProviderUserId();
            }

            if (us.getMg() != null && !us.getMg().isEmpty()) {
                // valid px size is 50 / 200
                imageUrl = us.getMg().get(ApplicationConstants.PROFILE_IMAGE_SMALL);
            }
        }

        profile.setMg(imageUrl);
        profile.setFbd(facebookId);

        return profile;
    }

    private void loadUserProfileOnComment(User commentingUser, Comment comment) {
        if (comment.isProfileEmpty()) {
            UserSupplement commentingUS = userService.findUserSupplement(commentingUser.getCd());

            ListCardProfile profile = createUserProfile(commentingUS);

            comment.setPrfl(profile);
        }
    }

    private void loadUserProfileOnComment(String commentingUserCode, Comment comment) {
        if (comment.isProfileEmpty()) {
            User commentingUser = userService.findUserByCode(commentingUserCode);

            loadUserProfileOnComment(commentingUser, comment);
        }
    }

    @Override
    public ListCardBoard findListCardBoard(String userCode, String boardCode) {
        // retrieve User Supplement from cache
        UserSupplement us = userService.findUserSupplement(userCode);

        return us.findListCardBoard(boardCode);
    }

    @Override
    public List<UserList> fetchUserLists(String userCode) {
        List<UserList> result = null;

        // retrieve User Supplement from cache
        UserSupplement us = userService.findUserSupplement(userCode);

        if (us.getBrds() != null && !us.getBrds().isEmpty()) {
            result = new ArrayList<UserList>(us.getBrds().size());

            for (ListCardBoard lcb : us.getBrds()) {
                UserListQuery query = new UserListQuery(userCode, lcb.getCd());

                // TODO do we really need all the data fetchUserList creates
                result.add(fetchUserList(query));
            }

        }

        return result;
    }

    @Override
    public ListCardBoard addListCardBoard(String userCode, ListCardBoard lcb) {
        // retrieve User Supplement from cache
        UserSupplement us = userService.findUserSupplement(userCode);

        // we only take the name here for now
        lcb = us.addListCardBoard(lcb);

        userService.saveUserSupplement(us);

        return lcb;
    }

    @Override
    public List<ListCardBoardName> findListCardBoardNames(String userCode) {
        UserSupplement us = userService.findUserSupplement(userCode);

        return us.findListCardBoardNames();
    }

    @Override
    public ListCard renameListCard(String userCode, String boardCode, String urlName, RenameListCard rename) {
        UserSupplement us = userService.findUserSupplement(userCode);

        ListCard lc = us.findListCard(boardCode, urlName);

        // TODO we might have to check the name here for moderation

        lc.setNm(rename.getName());

        userService.saveUserSupplement(us);

        return lc;
    }

    @Override
    public void deleteListCardBoard(String userCode, String boardCode) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.removeListCardBoard(boardCode);

        userService.saveUserSupplement(us);
    }

    @Override
    public List<CategoryGroup> findTopLevelDepartments(Locale locale) {
        NavigationBar nb = navigationBarService.findDefaultNavigationBar(locale);

        // if we don't support this locale - default to US
        if (nb == null) {
            nb = navigationBarService.findDefaultNavigationBar(Locale.US);
        }

        return nb.getGrps();
    }

    @Override
    public ListCard findListCard(String userCode, String boardCode, String urlName) {
        // retrieve User Supplement from cache
        UserSupplement us = userService.findUserSupplement(userCode);

        ListCard lc = us.findListCard(boardCode, urlName);

        if (lc != null) {
            enrichListCard(us, lc);
        }

        return lc;
    }

    @Override
    public List<ListCard> findAllListCards(String userCode, ListCardType type) {
        List<ListCard> result = null;
        UserSupplement us = userService.findUserSupplement(userCode);

        if (us.getBrds() != null && !us.getBrds().isEmpty()) {
            for (ListCardBoard lcb : us.getBrds()) {
                List<ListCard> list = lcb.findListCardsByType(type);

                if (list != null && !list.isEmpty()) {
                    if (result == null) {
                        result = new ArrayList<ListCard>();
                    }
                    result.addAll(list);
                }
            }
        }

        return result;
    }

    /**
     * Based on the saved items located on UserSupplement, we create ListCard for every item
     *
     * @param ulr   ulr
     * @param lcb   lcb
     * @param query query
     */
    private void populateUserList(UserList ulr, ListCardBoard lcb, UserListQuery query) {

        switch (query.getContentType()) {
            case ALL:
                populateUserListWithAll(ulr, lcb, query);
                break;
            case ITEMS:
                populateUserListWithItems(ulr, lcb, query);
                break;
            case STORES:
                populateUserListWithStores(ulr, lcb, query);
                break;
            case OWNERS:
                populateUserListWithOwners(ulr, lcb, query);
                break;
            case BRANCHES:
                populateUserListWithBranches(ulr, lcb, query);
                break;
            case OWNER_WITH_ITEMS:
                populateUserListWithItemsGroupedByOwners(ulr, lcb, query);
                break;
            case STORE_WITH_ITEMS:
                populateUserListWithItemsGroupedByStores(ulr, lcb, query);
                break;
        }

        // then we sort the list
        switch (query.getContentType()) {
            case OWNER_WITH_ITEMS:
                // todo implement
                break;
            case STORE_WITH_ITEMS:
                // todo implement
                break;
            case ITEMS:
                switch (query.getSortType()) {
                    case PRICE_HIGH:
                        Collections.sort(ulr.getCards(), new ListCardItemSortOnHighestPriceComparator());
                        break;
                    case PRICE_LOW:
                        Collections.sort(ulr.getCards(), new ListCardItemSortOnLowestPriceComparator());
                }

                // items have 2 unique sorting types - we're not breaking here so if the sorting type above
                // don't match, we should flow down to default
            default:
                switch (query.getSortType()) {
                    case CUSTOM_ORDER:
                        Collections.sort(ulr.getCards(), new ListCardSortOnOrderAscendingComparator());
                        break;
                    case ALERT_DATE:
                        Collections.sort(ulr.getCards(), new ListCardSortOnAlertDateComparator());
                        break;
                    case OWNERSHIP:
                        Collections.sort(ulr.getCards(), new ListCardSortOnOwnedComparator());
                        break;
                    case REVIEW_DATE:
                        Collections.sort(ulr.getCards(), new ListCardSortOnReviewDateComparator());
                        break;
                    case CREATED_DATE:
                        Collections.sort(ulr.getCards(), new ListCardSortOnDateComparator());
                }
                break;
        }

        // TODO pagination not working yet
//        paginateUserList(ulr, query);
    }

    /**
     * This method will grab all the items saved from user supplement and create ListCards out of them and then order them by desired sort routine
     *
     * @param ulr   ulr
     * @param lcb   lcb
     * @param query query
     */
    private void populateUserListWithItems(UserList ulr, ListCardBoard lcb, UserListQuery query) {

        List<ListCard> cards = lcb.findListCardsByType(ListCardType.ITEM);
        List<ListCard> filteredCards = null;

        if (cards != null && !cards.isEmpty()) {
            for (ListCard lc : cards) {
                // check to see if we have to filter out the item
                if (isCardWithinFilterBoundaries(query, lc)) {
                    if (filteredCards == null) {
                        filteredCards = new ArrayList<ListCard>();
                    }
                    filteredCards.add(lc);
                }
            }
        }

        ulr.addCards(filteredCards);

    }

    /**
     * This method will grab all the external cards saved from user supplement and create ListCards out of them and then order them by desired sort routine
     *
     * @param ulr   ulr
     * @param lcb   lcb
     * @param query query
     */
    private void populateUserListWithExternals(UserList ulr, ListCardBoard lcb, UserListQuery query) {

        List<ListCard> cards = lcb.findListCardsByType(ListCardType.EXTERNAL);

        if (cards != null && !cards.isEmpty()) {
            ulr.addCards(cards);
        }

    }

    private boolean isCardWithinFilterBoundaries(UserListQuery query, ListCard lc) {
        boolean result = false;

        if (lc.getRelevantItem() != null) {
            result = includeItem(query, lc.getRelevantItem());
        } else if (lc.getItem() != null) {
            result = includeItem(query, lc.getItem());
        }

        return result;
    }

    /**
     * This method will grab all the stores saved from user supplement and create ListCards out of them and then order them by desired sort routine
     *
     * @param ulr ulr
     * @param lcb lcb
     */
    private void populateUserListWithStores(UserList ulr, ListCardBoard lcb, UserListQuery query) {

        List<ListCard> cards;

        if (StringUtils.isBlank(query.getStoreUrlName())) {
            cards = lcb.findListCardsByType(ListCardType.STORE);
        } else {
            cards = lcb.findListCardsByType(ListCardType.STORE, query.getStoreUrlName());
        }

        if (cards != null && !cards.isEmpty()) {
            // add it to the filtered result list
            ulr.addCards(cards);
        }

    }

    /**
     * This method will grab all the owners saved from user supplement and create ListCards out of them and then order them by desired sort routine
     *
     * @param ulr ulr
     * @param lcb lcb
     */
    private void populateUserListWithOwners(UserList ulr, ListCardBoard lcb, UserListQuery query) {

        List<ListCard> cards;

        if (StringUtils.isBlank(query.getOwnerUrlName())) {
            cards = lcb.findListCardsByType(ListCardType.OWNER);
        } else {
            cards = lcb.findListCardsByType(ListCardType.OWNER, query.getOwnerUrlName());
        }

        if (cards != null && !cards.isEmpty()) {
            ulr.addCards(cards);
        }

    }

    /**
     * This method will grab all the branches saved from user supplement and create ListCards out of them and then order them by desired sort routine
     *
     * @param ulr ulr
     * @param lcb lcb
     */
    private void populateUserListWithBranches(UserList ulr, ListCardBoard lcb, UserListQuery query) {
        List<ListCard> cards;

        if (StringUtils.isBlank(query.getBranchUrlName())) {
            cards = lcb.findListCardsByType(ListCardType.BRANCH);
        } else {
            cards = lcb.findListCardsByType(ListCardType.BRANCH, query.getBranchUrlName());
        }

        if (cards != null && !cards.isEmpty()) {
            ulr.addCards(cards);
        }
    }

    /**
     * This method will grab all the items saved from user supplement and create ListCards out of them and then order them by desired sort routine
     *
     * @param ulr ulr
     * @param lcb lcb
     */
    private void populateUserListWithAll(UserList ulr, ListCardBoard lcb, UserListQuery query) {

        populateUserListWithItems(ulr, lcb, query);
        populateUserListWithStores(ulr, lcb, query);
        populateUserListWithOwners(ulr, lcb, query);
        populateUserListWithBranches(ulr, lcb, query);
        populateUserListWithExternals(ulr, lcb, query);

    }

    /**
     * Loads the transient property which pertains to the card type. In this case an item.
     *
     * @param userCode  userCode
     * @param motivator motivator
     * @param lc        lc
     */
    private void loadItemOnCard(Motivator motivator, String userCode, ListCard lc) {
        if (motivator != null) {
            if (lc.getRelevantItem() == null) {
                RelevantItem item = productEngineService.findRelevantItem(new RelevantItemQuery(lc.getRlnm(), userCode));

                if (item != null) {
                    lc.setRelevantItem(item);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Item: " + lc.getRlnm() + " is not available but it should be");
                    }
                }
            }
        } else {
            if (lc.getItem() == null) {
                Item item = itemService.findItemByUrlName(lc.getRlnm());

                if (item != null) {
                    lc.setItem(item);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Item: " + lc.getRlnm() + " is not available but it should be");
                    }
                }
            }
        }
    }

    private boolean includeItem(UserListQuery query, AbstractItem item) {
        boolean include = true;

        if (query != null) {
            if (StringUtils.isNotBlank(query.getCategoryUrlName()) &&
                    !StringUtils.equals(query.getCategoryUrlName(), item.getCtgry().getRlnm())) {
                // filter on category
                include = false;
            } else if (StringUtils.isNotBlank(query.getOwnerUrlName()) &&
                    !StringUtils.equals(query.getOwnerUrlName(), item.getWnr().getRlnm())) {
                // filter on owner
                include = false;
            } else if (StringUtils.isNotBlank(query.getStoreUrlName()) &&
                    !item.isAvailableInStore(query.getStoreUrlName())) {
                // filter on store
                include = false;
            }
        }

        return include;
    }

    /**
     * Loads the transient property which pertains to the card type. In this case a branch.
     *
     * @param lc lc
     */
    private void loadBranchOnCard(ListCard lc) {

        if (lc.getBranch() == null) {
            Branch branch = branchService.findBranchByUrlName(lc.getRlnm());

            if (branch != null) {
                lc.setBranch(branch);
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Branch: " + lc.getRlnm() + " is not available but it should be");
                }
            }
        }
    }

    /**
     * Loads the transient property which pertains to the card type. In this case a store.
     *
     * @param lc lc
     */
    private void loadStoreOnCard(ListCard lc) {

        if (lc.getStore() == null) {
            Store store = storeService.findStoreByUrlName(lc.getRlnm());

            if (store != null) {
                lc.setStore(store);
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Store: " + lc.getRlnm() + " is not available but it should be");
                }
            }
        }
    }

    /**
     * Loads the transient property which pertains to the card type. In this case a owner.
     *
     * @param lc lc
     */
    private void loadOwnerOnCard(ListCard lc) {

        Owner owner = ownerService.findOwnerByUrlName(lc.getRlnm());

        if (owner != null) {
            lc.setOwner(owner);
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Owner: " + lc.getRlnm() + " is not available but it should be");
            }
        }

    }

    /**
     * This method will grab all the saved stores and the items the user has saved that are available in those stores
     * and create ListCards out of them. It will group items by the stores that they are in
     *
     * @param ulr   ulr
     * @param lcb   lcb
     * @param query query
     */
    private void populateUserListWithItemsGroupedByStores(UserList ulr, ListCardBoard lcb, UserListQuery query) {
        // make sure the store exists
        if (StringUtils.isNotBlank(query.getStoreUrlName())) {
            // check if the store can be found as a listcard
            ListCard storeCard = lcb.findListCard(query.getStoreUrlName());

            if (storeCard != null) {
                // ok - add store card to result and then add all items belonging to this store after
                ulr.addCard(storeCard);

                populateUserListWithItems(ulr, lcb, query);
            }
        }
    }

    /**
     * This method will grab all the saved owners and the items the user has saved that are available in those owners
     * and create ListCards out of them. It will group items by the owners that they are in
     *
     * @param ulr   ulr
     * @param lcb   lcb
     * @param query query
     */
    private void populateUserListWithItemsGroupedByOwners(UserList ulr, ListCardBoard lcb, UserListQuery query) {
        // make sure the owner exists
        if (StringUtils.isNotBlank(query.getOwnerUrlName())) {
            // check if the owner can be found as a listcard
            ListCard ownerCard = lcb.findListCard(query.getOwnerUrlName());

            if (ownerCard != null) {
                // ok - add owner card to result and then add all items belonging to this owner after
                ulr.addCard(ownerCard);

                populateUserListWithItems(ulr, lcb, query);
            }
        }

    }

    /**
     * Paginates the result
     * TODO pagination is not in use yet
     *
     * @param ulr   ulr
     * @param query query
     */
    private void paginateUserList(UserList ulr, UserListQuery query) {
        // finally we paginate
        if (query.getPage() != null && query.getMaxResults() != null) {
            Integer page = query.getPage();
            Integer maxResults = query.getMaxResults();

            // check for some bad values
            if (page < 0) {
                page = 0;
            }

            // check so we don't go out of bounds on the list
            if (page * maxResults + maxResults > ulr.getCards().size()) {

            }

            ulr.addCards(ulr.getCards().subList(query.getPage() * query.getMaxResults(), query.getPage() * query.getMaxResults() + query.getMaxResults()));
        }
    }

    /**
     * Method reorders any sort of saved list (items, branch, store, owner)
     *
     * @param dto dto
     */
    @Override
    public void reorderUserList(ListPosition dto) {
        if (dto != null && StringUtils.isNotBlank(dto.getUrlName()) && dto.getOrder() != null) {
            UserSupplement us = userService.findUserSupplement(dto.getUserCode());

            String boardCode = dto.getBoardName();

            ListCardBoard lcb = us.findListCardBoard(boardCode);

            if (lcb != null) {

                if (lcb.getCrds() != null && !lcb.getCrds().isEmpty()) {
                    List<ListCard> cards = lcb.getCrds();
                    int size = cards.size();
                    ListCard itemToReposition = null;

                    // first we clean up all order numbers so it's nice a sequential

                    // sort by order first
                    Collections.sort(cards, new ListCardSortOnOrderAscendingComparator());

                    // reverse as the comparator wants to show highest order
                    // first but we want to sort on numbers starting with lowest first
                    Collections.reverse(cards);

                    // update order numbers sequentially
                    // so the numbers could've been 10, 13, 15, 18, 54
                    // now they are 1, 2, 3, 4, 5
                    for (int i = 0; i < size; i++) {
                        ListCard lc = cards.get(i);
                        lc.setRdr(i + 1);

                        if (StringUtils.equals(lc.getRlnm(), dto.getUrlName())) {
                            itemToReposition = lc;
                        }
                    }

                    if (itemToReposition != null) {
                        // and now we are ready to do the actual reordering
                        Integer currentPosition = itemToReposition.getRdr();
                        int newPosition = dto.getOrder();

                        // the new position will be inverse
                        // e.g. if the newPosition = 1 and the list size is 5
                        // => newPosition should be 5
                        newPosition = Math.abs(newPosition - size - 1);

                        if (newPosition >= 1 && newPosition <= size) {
                            // e.g. (1), 2, 3, 4, >newPosition<, 5, 6
                            // 2--, 3--, 4--, (1), 5, 6
                            // e.g. 1, >newPosition<, 2, 3, 4, 5, (6)
                            // 1, (6), ++2, ++3, ++4, ++5
                            // e.g. 1, 2, >newPosition<, 3, (4), 5, 6
                            // 1, 2, (4), ++3, 5, 6
                            // e.g. 1, 2, (3), 4, >newPosition<, 5, 6
                            // 1, 2, 4--, (3), 5, 6
                            List<ListCard> newListCards = new ArrayList<ListCard>();

                            for (ListCard item : cards) {
                                // we don't need to do anything with the item we
                                // are repositioning as we already have the object
                                if (!item.equals(itemToReposition)) {

                                    if (item.getRdr() > currentPosition && item.getRdr() <= newPosition) {
                                        item.setRdr(item.getRdr() - 1);
                                    } else if (item.getRdr() < currentPosition && item.getRdr() >= newPosition) {
                                        item.setRdr(item.getRdr() + 1);
                                    }

                                    newListCards.add(item);
                                } else {
                                    itemToReposition.setRdr(newPosition);
                                    newListCards.add(itemToReposition);
                                }
                            }

                            // sort one more time
                            Collections.sort(newListCards, new ListCardSortOnOrderAscendingComparator());

                            // overwrite the newly sorted saved items list
                            lcb.setCrds(newListCards);

                            // persist new list order to db
                            userService.saveUserSupplement(us);

                            // set success message
                            dto.setMessage(ApplicationConstants.SUCCESS);
                        } else {
                            dto.setMessage(ApplicationConstants.FAILURE);
                            if (log.isWarnEnabled()) {
                                log.warn("The requested position was not valid: " + newPosition);
                            }
                        }
                    } else {
                        dto.setMessage(ApplicationConstants.FAILURE);
                        if (log.isWarnEnabled()) {
                            log.warn("The item to reposition was not valid: " + dto.getUrlName());
                        }
                    }
                } else {
                    dto.setMessage(ApplicationConstants.FAILURE);
                    if (log.isWarnEnabled()) {
                        log.warn("There are no items to reposition");
                    }
                }

            }

        }
    }

    @Override
    public Note addNoteToListCard(String userCode, String boardCode, String urlName, Note note) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.addNote(boardCode, urlName, note);

        userService.saveUserSupplement(us);

        return note;
    }

    @Override
    public void deleteNoteFromListCard(String userCode, String boardCode, String urlName, String noteCode) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.removeNote(boardCode, urlName, noteCode);

        userService.saveUserSupplement(us);
    }

    @Override
    public Review addReviewToListCard(String userCode, String boardCode, String urlName, Review review) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.addReview(boardCode, urlName, review);

        userService.saveUserSupplement(us);

        return review;
    }

    @Override
    public Comment addCommentToListCard(String userCode, String boardCode, String urlName, User commentingUser, Comment comment) {
        UserSupplement us = userService.findUserSupplement(userCode);

        // add a profile to the comment
        loadUserProfileOnComment(commentingUser, comment);

        // add the comment to the user supplement
        us.addComment(boardCode, urlName, comment);

        // save the user supplement
        userService.saveUserSupplement(us);

        return comment;
    }

    @Override
    public void deleteReviewFromListCard(String userCode, String boardCode, String urlName, String reviewCode) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.removeReview(boardCode, urlName, reviewCode);

        userService.saveUserSupplement(us);
    }

    @Override
    public void deleteCommentFromListCard(String userCode, String boardCode, String urlName, String commentCode) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.removeComment(boardCode, urlName, commentCode);

        userService.saveUserSupplement(us);
    }

    @Override
    public DOMElements fetchExternalImages(ExternalImageRequest request) {
        DOMElements result = null;

        if (StringUtils.isNotBlank(request.getUrl())) {
            List<DOMElement> images = htmlUtilityService.fetchImagesFromUrl(request.getUrl(), cardImagesMinWidth);

            if (images != null && !images.isEmpty()) {
                result = new DOMElements(images);
            }
        }

        return result;
    }

    @Override
    public ListCard addExternalCardToList(ExternalListCard externalListCard) {
        ListCard result = null;

        try {
            // do some preliminary checks
            if (externalListCard != null && StringUtils.isNotBlank(externalListCard.getUserCode())) {
                String userCode = externalListCard.getUserCode();
                String boardCode = externalListCard.getBoardCode();
                String name = externalListCard.getName().trim();
                String urlName = externalListCard.getCd();

                UserSupplement us = userService.findUserSupplement(userCode);
                Integer maxOrder = us.findListCardMaxOrder(boardCode);
                maxOrder += 1;

                // start populating the list card
                result = new ListCard();
                result.setTp(ListCardType.EXTERNAL);
                result.setRdr(maxOrder);
                result.setNm(name);
                result.setRlnm(urlName);

                if (StringUtils.isNotBlank(externalListCard.getNote())) {
                    Note note = new Note();
                    note.setDt(new Date());
                    note.setTxt(externalListCard.getNote());

                    List<Note> notes = new ArrayList<Note>(1);
                    notes.add(note);
                    result.setNts(notes);
                }

                Picture picture = null;
                if (StringUtils.isNotBlank(externalListCard.getImageUrl()) && StringUtils.isNotBlank(externalListCard.getWidth())) {
                    picture = new Picture(externalListCard.getImageUrl(), externalListCard.getWidth());
                    result.setRl(externalListCard.getExternalUrl());
                } else if (externalListCard.getMultipartFile() != null && !externalListCard.getMultipartFile().isEmpty()) {
                    picture = new Picture(new HashMap<String, String>(imageSizes.size()));

                    MultipartFile file = externalListCard.getMultipartFile();
                    ImageDigest id = imageUtilityService.ingestImage(accessKey, secretKey, cardImagesBucketName, userCode, picture.getCd(), file.getInputStream(), new Integer[]{small, medium, large}, list);

                    // we have to convert the integer keys to strings because JSPs cann't access Integer keys because they treat it as a long
                    if (id.getImageUrls() != null && !id.getImageUrls().isEmpty()) {

                        for (Map.Entry<Integer, String> entry : id.getImageUrls().entrySet()) {
                            picture.getMg().put(imageSizes.get(entry.getKey()), entry.getValue());
                        }

                    }
                }

                if (picture != null) {
                    List<Picture> pictures = new ArrayList<Picture>(1);
                    pictures.add(picture);
                    result.setPctrs(pictures);
                }

                us.addListCard(boardCode, result);

                // front-end is requesting the user profile to be appended at this time
                loadUserProfile(us, result);

                userService.saveUserSupplement(us);
            }
        } catch (UtilityException e) {
            throw new LelaException(e.getMessage(), e);
        } catch (IOException e) {
            throw new LelaException(e.getMessage(), e);
        }

        return result;
    }

    @Override
    public void deleteAlertFromListCard(String userCode, String boardCode, String urlName) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.removeAlert(boardCode, urlName);

        userService.saveUserSupplement(us);
    }

    @Override
    public void deletePictureFromListCard(String userCode, String boardCode, String urlName, String pictureCode) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.removePicture(boardCode, urlName, pictureCode);

        userService.saveUserSupplement(us);
    }

    private void populateCategoryReferenceData(UserList ulr, ListCardBoard lcb) {

        if (lcb.getCrds() != null && !lcb.getCrds().isEmpty()) {
            Map<String, Category> categories = new HashMap<String, Category>();

            for (ListCard lc : lcb.getCrds()) {
                if (lc.getItem() != null) {
                    categories.put(lc.getItem().getCtgry().getRlnm(), lc.getItem().getCtgry());
                } else if (lc.getRelevantItem() != null) {
                    categories.put(lc.getRelevantItem().getCtgry().getRlnm(), lc.getRelevantItem().getCtgry());
                }
            }

            // Sort the categories as well
            if (!categories.isEmpty()) {
                List<Category> categoryList = new ArrayList<Category>(categories.values());

                Collections.sort(categoryList, new CategoryOrderComparator());
                ulr.addCategories(categoryList);
            }
        }

    }

    private void populateOwnerReferenceData(UserList ulr, ListCardBoard lcb) {

        if (lcb.getCrds() != null && !lcb.getCrds().isEmpty()) {

            for (ListCard lc : lcb.getCrds()) {

                switch (lc.getTp()) {
                    case OWNER:
                        ulr.addOwner(lc.getOwner());
                }
            }
        }

        // sort owners
        if (ulr.getOwners() != null && !ulr.getOwners().isEmpty()) {
            Collections.sort(ulr.getOwners(), new OwnerByNameComparator());
        }
    }

    private void populateStoreReferenceData(UserList ulr, ListCardBoard lcb) {
        // we also need to grab stores from the items themselves
        if (lcb.getCrds() != null && !lcb.getCrds().isEmpty()) {

            for (ListCard lc : lcb.getCrds()) {
                switch (lc.getTp()) {
                    case STORE:
                        ulr.addStore(lc.getStore());
                }
            }
        }

        // sort stores
        if (ulr.getStores() != null && !ulr.getStores().isEmpty()) {
            Collections.sort(ulr.getStores(), new StoreByNameComparator());
        }
    }

    @Override
    public Alert addAlertToListCard(String userCode, String boardCode, String urlName, Alert alert) {
        UserSupplement us = userService.findUserSupplement(userCode);

        us.addAlert(boardCode, urlName, alert);

        userService.saveUserSupplement(us);

        return alert;
    }

    @Override
    public Picture addPictureToListCard(String userCode, String boardCode, String urlName, ListCardPicture picture) {
        Picture result = null;

        try {
            UserSupplement us = userService.findUserSupplement(userCode);
            ListCardBoard lcb = us.findListCardBoard(boardCode);

            if (lcb != null) {
                if (picture.getMultipartFile() != null && !picture.getMultipartFile().isEmpty()) {
                    result = new Picture(new HashMap<String, String>(imageSizes.size()));

                    MultipartFile file = picture.getMultipartFile();
                    ImageDigest id = imageUtilityService.ingestImage(accessKey, secretKey, cardImagesBucketName, userCode, result.getCd(), file.getInputStream(), new Integer[]{small, medium, large}, list);

                    // we have to convert the integer keys to strings because JSPs cann't access Integer keys because they treat it as a long
                    if (id.getImageUrls() != null && !id.getImageUrls().isEmpty()) {

                        for (Map.Entry<Integer, String> entry : id.getImageUrls().entrySet()) {
                            result.getMg().put(imageSizes.get(entry.getKey()), entry.getValue());
                        }

                        lcb.addPicture(urlName, result);

                        userService.saveUserSupplement(us);
                    }
                }
            }
        } catch (Exception e) {
            throw new LelaException(e.getMessage(), e);
        }

        return result;
    }

    /**
     * Adds an external image url instead of a MultipartFile like above
     *
     * @param userCode  userCode
     * @param boardCode boardCode
     * @param urlName   urlName
     * @param picture   picture
     * @return Picture object
     */
    @Override
    public Picture addPictureToListCard(String userCode, String boardCode, String urlName, ExternalListCardPicture picture) {
        Picture result = null;

        try {
            UserSupplement us = userService.findUserSupplement(userCode);
            ListCardBoard lcb = us.findListCardBoard(boardCode);

            if (lcb != null) {
                result = new Picture(picture.getUrl(), picture.getWidth());

                lcb.addPicture(urlName, result);

                userService.saveUserSupplement(us);

            }
        } catch (Exception e) {
            throw new LelaException(e.getMessage(), e);
        }

        return result;
    }

    /**
     * Sends price alert emails for any price alert that is active and meets its condition.
     * Returns a list of the price alerts sent.
     *
     * @param userCode
     * @return
     */
    @Override
    public List<Alert> sendValidPriceAlerts(String userCode) {
        List<Alert> result = new ArrayList<Alert>();

        UserSupplement us = userService.findUserSupplement(userCode);
        boolean dirty = false;
        try {
            if (us != null && us.getBrds() != null) {
                for (ListCardBoard lcb : us.getBrds()) {
                    if (lcb.getCrds() != null && !lcb.getCrds().isEmpty()) {
                        for (ListCard lc : lcb.getCrds()) {
                            // only supporting alerts on type: ITEM as of this time
                            if (lc.getTp().equals(ListCardType.ITEM)) {

                                if (lc.getLrt() != null) {
                                    Alert alert = lc.getLrt();

                                    if (alert.getPrc() != null && alert.getPrclrt()) {
                                        // Get the Item and check the price
                                        Item item = itemService.findItemByUrlName(lc.getRlnm());
                                        if (item != null) {
                                            Double listPrice = NumberUtils.safeDouble(item.getSubAttributes().get("LowestPrice"));
                                            if (listPrice != null) {
                                                if (listPrice <= alert.getPrc()) {

                                                    Map<MailParameter, Object> params = new HashMap<MailParameter, Object>();
                                                    params.put(MailParameter.ITEM, item);
                                                    params.put(MailParameter.PRICE_ALERT, alert);
                                                    params.put(MailParameter.USER_SUPPLEMENT, us);

                                                    RelevantItemQuery query = new RelevantItemQuery(item.getRlnm(), us.getCd());
                                                    RelevantItem ri = productEngineService.findRelevantItem(query);
                                                    if (ri != null) {
                                                        params.put(MailParameter.ITEM_RELEVANCY, ri.getTtlrlvncynmbr());
                                                    }

                                                    List<AvailableInStore> stores = merchantService.findOnlineStores(item);
                                                    params.put(MailParameter.STORES, stores);

                                                    // Send the alert email now
                                                    log.debug(String.format("Sending price alert to %s for item %s", us.getMl(), lc.getRlnm()));
                                                    mailService.sendPriceAlert(alert.getMl(), params, us.getLocale());

                                                    // Delete the alert
                                                    alert.setPrclrt(false);
                                                    alert.setPrc(null);
                                                    dirty = true;

                                                    result.add(alert);
                                                }
                                            }
                                        } else {
                                            log.warn("Price alert - No item found for rlnm: " + lc.getRlnm());
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        } catch (MailServiceException e) {
            log.error("Error processing price alerts for user: " + us.getMl(), e);
        } finally {
            if (dirty) {
                userService.saveUserSupplement(us);
            }
        }

        return result;
    }
}
