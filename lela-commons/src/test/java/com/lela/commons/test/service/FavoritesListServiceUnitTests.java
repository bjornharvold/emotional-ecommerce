/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.event.AddItemToListEvent;
import com.lela.commons.event.EventHelper;
import com.lela.commons.event.RemoveItemFromListEvent;
import com.lela.commons.service.BranchService;
import com.lela.commons.service.HTMLUtilityService;
import com.lela.commons.service.ImageUtilityService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.MerchantService;
import com.lela.commons.service.OwnerService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.StoreService;
import com.lela.commons.service.UserService;
import com.lela.commons.service.impl.FavoritesListServiceImpl;
import com.lela.commons.service.impl.MailServiceImpl;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.Alert;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Category;
import com.lela.domain.document.Comment;
import com.lela.domain.document.Item;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.ListCardBoard;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Note;
import com.lela.domain.document.Owner;
import com.lela.domain.document.Picture;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.Review;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.dto.dom.DOMElement;
import com.lela.domain.dto.dom.DOMElements;
import com.lela.domain.dto.list.ExternalImageRequest;
import com.lela.domain.dto.list.ExternalListCard;
import com.lela.domain.dto.list.ExternalListCardPicture;
import com.lela.domain.dto.list.ListCardPicture;
import com.lela.domain.dto.list.ListEntry;
import com.lela.domain.dto.list.ListPosition;
import com.lela.domain.dto.list.RenameListCard;
import com.lela.domain.dto.list.UserList;
import com.lela.domain.dto.list.UserListQuery;
import com.lela.domain.dto.report.UserUserSupplementEntry;
import com.lela.domain.enums.MailParameter;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.StoreType;
import com.lela.domain.enums.list.ListCardType;
import com.lela.domain.enums.list.ListContentType;
import com.lela.domain.enums.list.ListSortType;
import com.lela.util.UtilityException;
import com.lela.util.utilities.storage.dto.ImageDigest;
import junit.framework.Assert;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventHelper.class)
public class FavoritesListServiceUnitTests {
    private final static Logger log = LoggerFactory.getLogger(FavoritesListServiceUnitTests.class);
    private static final String USER_CODE = "someuser";
    private static final String ITEM_URL_NAME = "someitemurlname";
    private static final String ITEM_URL_NAME_2 = "someitemurlname2";
    private static final String ITEM_URL_NAME_3 = "someitemurlname3";
    private static final String STORE_NAME = "storename";
    private static final String STORE_URL_NAME = "storeurlname";
    private static final String CATEGORY_URL_NAME = "somecategory";
    private static final String CATEGORY_URL_NAME_2 = "somecategory2";
    private static final String OWNER_URL_NAME = "someowner";
    private static final String BRANCH_URL_NAME = "somebranch";
    private static final String PICTURE_CODE = "somepicturecode";
    private static final String NOTE_CODE = "somenotecode";
    private static final String REVIEW_CODE = "somereviewcode";
    private static final String ALERT_CODE = "somealertcode";
    private static final String COMMENT_CODE = "somecommentcode";

    private static final String EMAIL = "somestupidemail@home.com";
    private static final String PRICE_ALERT_TEST_ITEM_RLNM = "priceAlertTestItemRlnm";
    private static final String CUSTOM_BOARD_NAME = "somefreakyname";

    @Mock
    private UserService userService;

    @Mock
    private StoreService storeService;

    @Mock
    private OwnerService ownerService;

    @Mock
    private ItemService itemService;

    @Mock
    private BranchService branchService;

    @Mock
    private ProductEngineService productEngineService;

    @Mock
    private ImageUtilityService imageUtilityService;

    @Mock
    private HTMLUtilityService htmlUtilityService;

    @Mock
    private MailServiceImpl mailService;

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private FavoritesListServiceImpl favoritesListService;

    private ObjectId userId;
    private User user;
    private UserSupplement us;
    private Item item;
    private Item item2;
    private Item item3;
    private RelevantItem relevantItem;
    private Map<String, Integer> relevancyMap;
    private Store store;
    private Category category;
    private Owner owner;
    private Branch branch;
    private Category category2;

    @Before
    public void beforeEach() {
        userId = new ObjectId();
        user = new User();
        user.setId(userId);
        user.setMl(EMAIL);
        user.setCd(USER_CODE);
        Map<String, String> imageMap = new HashMap<String, String>(1);
        imageMap.put("50", "some url");

        us = new UserSupplement(USER_CODE);
        us.setMg(imageMap);
        us.setFnm("Hans");
        us.setLnm("Gruber");

        store = new Store(STORE_NAME, STORE_URL_NAME, StoreType.ONLINE);

        category = new Category();
        category.setRlnm(CATEGORY_URL_NAME);
        category.setRdr(1);

        category2 = new Category();
        category2.setRlnm(CATEGORY_URL_NAME_2);
        category2.setRdr(2);

        owner = new Owner();
        owner.setRlnm(OWNER_URL_NAME);

        item = new Item();
        item.setRlnm(ITEM_URL_NAME);
        item.setCtgry(category);
        item.setWnr(owner);
        List<Attribute> attributes = new ArrayList<Attribute>();
        Attribute attribute = new Attribute("LowestPrice", (double) 10);
        attributes.add(attribute);
        item.setAttrs(attributes);

        List<AvailableInStore> stores = new ArrayList<AvailableInStore>(1);
        stores.add(new AvailableInStore(STORE_NAME, STORE_URL_NAME, StoreType.ONLINE));
        item.setStrs(stores);

        item2 = new Item();
        item2.setRlnm(ITEM_URL_NAME_2);
        item2.setCtgry(category2);
        item2.setWnr(owner);
        item2.setStrs(stores);
        attributes = new ArrayList<Attribute>();
        attribute = new Attribute("LowestPrice", (double) 30);
        attributes.add(attribute);
        item2.setAttrs(attributes);

        item3 = new Item();
        item3.setRlnm(ITEM_URL_NAME_2);
        item3.setCtgry(category2);
        item3.setWnr(owner);
        item3.setStrs(stores);
        attributes = new ArrayList<Attribute>();
        attribute = new Attribute("LowestPrice", (double) 20);
        attributes.add(attribute);
        item3.setAttrs(attributes);

        relevancyMap = new HashMap<String, Integer>();
        relevancyMap.put("A", 9);
        relevancyMap.put("B", 2);
        relevancyMap.put("C", 9);
        relevancyMap.put("D", 2);
        relevancyMap.put("E", 9);
        relevancyMap.put("F", 2);
        relevancyMap.put("G", 9);
        relevancyMap.put("H", 2);
        relevantItem = new RelevantItem(item, 324, 99, relevancyMap);

        branch = new Branch();
        branch.setRlnm(BRANCH_URL_NAME);
    }

    @Test
    public void testFindListCard() {
        log.info("Testing finding a list card...");

        // set up test
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(userService.findUserByCode(USER_CODE)).thenReturn(user);

        // execute service method
        ListCard lc = favoritesListService.findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME);

        // verify
        assertNotNull("ListCard is null", lc);
        verify(userService, times(1)).findUserSupplement(USER_CODE);

        // execute service method
        ListCardBoard lcb = favoritesListService.findListCardBoard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);

        // verify
        assertNotNull("ListCard is null", lcb);
        assertEquals("ListCard size incorrect", 1, lcb.getCrds().size(), 0);
        assertEquals("ListCard object incorrect", lc, lcb.getCrds().get(0));
        verify(userService, times(2)).findUserSupplement(USER_CODE);

        // execute service method
        List<ListCard> list = favoritesListService.findAllListCards(USER_CODE, ListCardType.ITEM);

        // verify
        assertNotNull("ListCard is null", lcb);
        assertEquals("ListCard size incorrect", 1, list.size(), 0);
        assertEquals("ListCard object incorrect", lc, list.get(0));
        verify(userService, times(3)).findUserSupplement(USER_CODE);

        log.info("Testing finding a list card complete");
    }

    @Test
    public void testAddNoteToListCard() {
        log.info("Testing adding a note to a list card...");

        // set up test
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        Note note = new Note();
        note.setTxt("This is a note");

        // execute service method
        favoritesListService.addNoteToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, note);

        // verify
        assertNotNull("Note is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getNote(note.getCd()));
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a note to a list card complete");
    }

    @Test
    public void testAddAlertToListCard() {
        log.info("Testing adding a alert to a list card...");

        // set up test
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        Alert alert = new Alert();

        // execute service method
        favoritesListService.addAlertToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, alert);

        // verify
        assertNotNull("Alert is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getLrt());
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a alert to a list card complete");
    }

    @Test
    public void testDeleteAlertFromListCard() {
        log.info("Testing adding a alert to a list card...");

        // set up test
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);
        Alert alert = new Alert();
        alert.setCd(ALERT_CODE);
        lc.setLrt(alert);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        // execute service method
        favoritesListService.deleteAlertFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME);

        // verify
        assertNull("Alert is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getLrt());
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a alert to a list card complete");
    }

    @Test
    public void testDeleteNoteFromListCard() {
        log.info("Testing adding a note to a list card...");

        // set up test
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);
        Note note = new Note();
        note.setCd(NOTE_CODE);
        List<Note> notes = new ArrayList<Note>();
        notes.add(note);
        lc.setNts(notes);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        // execute service method
        favoritesListService.deleteNoteFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, NOTE_CODE);

        // verify
        assertNull("Note is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getNote(NOTE_CODE));
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a note to a list card complete");
    }

    @Test
    public void testDeleteReviewFromListCard() {
        log.info("Testing adding a review to a list card...");

        // set up test
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);
        Review review = new Review();
        review.setCd(REVIEW_CODE);
        List<Review> reviews = new ArrayList<Review>();
        reviews.add(review);
        lc.setRvws(reviews);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        // execute service method
        favoritesListService.deleteReviewFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, REVIEW_CODE);

        // verify
        assertNull("Review is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getReview(REVIEW_CODE));
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a review to a list card complete");
    }

    @Test
    public void testDeleteCommentFromListCard() {
        log.info("Testing adding a comment to a list card...");

        // set up test
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);
        Comment comment = new Comment();
        comment.setCd(COMMENT_CODE);
        List<Comment> comments = new ArrayList<Comment>();
        comments.add(comment);
        lc.setCmmnts(comments);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        // execute service method
        favoritesListService.deleteCommentFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, COMMENT_CODE);

        // verify
        assertNull("Comment is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getComment(COMMENT_CODE));
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a comment to a list card complete");
    }

    @Test
    public void testDeletePictureFromListCard() {
        log.info("Testing adding a picture to a list card...");

        // set up test
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);
        Picture picture = new Picture();
        picture.setCd(PICTURE_CODE);
        List<Picture> pictures = new ArrayList<Picture>();
        pictures.add(picture);
        lc.setPctrs(pictures);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        // execute service method
        favoritesListService.deletePictureFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, PICTURE_CODE);

        // verify
        assertNull("Picture is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getPicture(PICTURE_CODE));
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a picture to a list card complete");
    }

    @Test
    public void testAddPictureToListCard() {
        log.info("Testing adding a picture to a list card...");

        try {
            // set up test
            us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

            ListCardPicture picture = new ListCardPicture();
            String name = "Test";
            MockMultipartFile file = new MockMultipartFile(name, name.getBytes());
            picture.setMultipartFile(file);

            ImageDigest id = new ImageDigest();
            Map<Integer, String> imageUrls = new HashMap<Integer, String>();
            imageUrls.put(50, "someurl.jpg");
            id.setImageUrls(imageUrls);

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(imageUtilityService.ingestImage(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(InputStream.class), any(Integer[].class), any(Integer.class))).thenReturn(id);

            // execute service method
            favoritesListService.addPictureToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, picture);

            // verify
            assertNotNull("Picture is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getPctrs());
            verify(userService, times(1)).findUserSupplement(USER_CODE);
            verify(userService, times(1)).saveUserSupplement(us);
            verify(imageUtilityService, times(1)).ingestImage(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(InputStream.class), any(Integer[].class), any(Integer.class));
        } catch (UtilityException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing adding a picture to a list card complete");
    }

    @Test
    public void testAddExternalPictureToListCard() {
        log.info("Testing adding a picture to a list card...");

        // set up test
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        ExternalListCardPicture picture = new ExternalListCardPicture();
        picture.setUrl("someurl");
        picture.setWidth("200");

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        // execute service method
        favoritesListService.addPictureToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, picture);

        // verify
        assertNotNull("Picture is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getPctrs());
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a picture to a list card complete");
    }

    @Test
    public void testAddReviewToListCard() {
        log.info("Testing adding a review to a list card...");

        // set up test
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        Review review = new Review();
        review.setTxt("This is a review");

        // execute service method
        favoritesListService.addReviewToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, review);

        // verify
        assertNotNull("Review is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getReview(review.getCd()));
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a review to a list card complete");
    }

    @Test
    public void testAddCommentToListCard() {
        log.info("Testing adding a comment to a list card...");

        // set up test
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        Comment comment = new Comment();
        comment.setTxt("This is a comment");

        // execute service method
        User commentingUser = new User();
        favoritesListService.addCommentToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, commentingUser, comment);

        // verify
        assertNotNull("Comment is null", us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME).getComment(comment.getCd()));
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a comment to a list card complete");
    }

    @Test
    public void testAddItemToList() {
        log.info("Testing adding an item to user's list...");

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);

        ListEntry li = new ListEntry(ITEM_URL_NAME);
        li.setUserCode(USER_CODE);

        ArgumentCaptor< Object > trackEvent = (ArgumentCaptor) ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute service method
        ListCard si = favoritesListService.addItemToList(li);

        // verify
        assertNotNull("Saved item is null", si);
        assertEquals("Incorrect saved item", ITEM_URL_NAME, si.getRlnm());
        assertEquals("Incorrect item order", 1, si.getRdr(), 0);
        assertEquals("Incorrect saved item list size", 1, us.getBrds().get(0).getCrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(itemService, times(1)).findItemByUrlName(ITEM_URL_NAME);
        verify(userService, times(1)).saveUserSupplement(us);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        Assert.assertTrue("AddItemToListEvent not posted", trackEvent.getAllValues().get(0) instanceof AddItemToListEvent);
        AddItemToListEvent addItemToListEvent =  (AddItemToListEvent)trackEvent.getAllValues().get(0);
        Assert.assertEquals("UserCode blank", USER_CODE, addItemToListEvent.getUserCode());
        Assert.assertEquals("ItemUrlName blank", ITEM_URL_NAME, addItemToListEvent.getItemUrlName());

        log.info("Testing adding an item to user's list complete");
    }

    @Test
    public void testAddItemToNewListWithSpecifiedName() {
        log.info("Testing adding an item to user list created at runtime...");

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);

        ListEntry li = new ListEntry(ITEM_URL_NAME);
        li.setUserCode(USER_CODE);
        li.setBoardName(CUSTOM_BOARD_NAME);
        li.setRlnm(ITEM_URL_NAME);

        ArgumentCaptor< Object > trackEvent = (ArgumentCaptor) ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute service method
        ListCard si = favoritesListService.addItemToList(li);

        // verify
        assertNotNull("Saved item is null", si);
        assertEquals("Incorrect item", ITEM_URL_NAME, si.getRlnm());
        assertEquals("Incorrect board name", CUSTOM_BOARD_NAME, us.getBrds().get(0).getNm());
        assertFalse("Incorrect board code", "Default".equals(us.getBrds().get(0).getCd()));
        assertEquals("Incorrect item order", 1, si.getRdr(), 0);
        assertEquals("Incorrect saved item list size", 1, us.getBrds().get(0).getCrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(itemService, times(1)).findItemByUrlName(ITEM_URL_NAME);
        verify(userService, times(1)).saveUserSupplement(us);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        Assert.assertTrue("AddItemToListEvent not posted", trackEvent.getAllValues().get(0) instanceof AddItemToListEvent);
        AddItemToListEvent addItemToListEvent =  (AddItemToListEvent)trackEvent.getAllValues().get(0);
        Assert.assertEquals("UserCode blank", USER_CODE, addItemToListEvent.getUserCode());
        Assert.assertEquals("ItemUrlName blank", ITEM_URL_NAME, addItemToListEvent.getItemUrlName());

        log.info("Testing adding an item to user's list complete");
    }

    @Test
    public void testDeleteSavedItemFromList() {
        log.info("Testing deleting an item from user's list...");

        // first we add an item to user supplement to then remove
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        assertEquals("Incorrect saved item list size", 1, us.getBrds().size(), 0);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        ListEntry li = new ListEntry(ITEM_URL_NAME);
        li.setUserCode(USER_CODE);

        ArgumentCaptor < Object > trackEvent = (ArgumentCaptor) ArgumentCaptor.forClass(Object.class);
        mockStatic(EventHelper.class);

        // execute service method
        favoritesListService.deleteListCard(li);

        // verify
        assertEquals("Incorrect saved item list size", 0, us.getBrds().get(0).getCrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        verifyStatic(times(1));
        EventHelper.post(trackEvent.capture());

        Assert.assertTrue("PurchaseEvent not posted", trackEvent.getAllValues().get(0) instanceof RemoveItemFromListEvent);
        RemoveItemFromListEvent removeItemFromListEvent =  (RemoveItemFromListEvent)trackEvent.getAllValues().get(0);
        Assert.assertEquals("User Code was blank", USER_CODE, removeItemFromListEvent.getUserCode());
        Assert.assertEquals("Item Url Name was null", ITEM_URL_NAME, removeItemFromListEvent.getItemUrlName());

        log.info("Testing deleting an item from user's list complete");
    }

    @Test
    public void testAddStoreToList() {
        log.info("Testing adding a store to user's list...");

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(storeService.findStoreByUrlName(STORE_URL_NAME)).thenReturn(store);

        ListEntry li = new ListEntry(STORE_URL_NAME);
        li.setUserCode(USER_CODE);

        // execute service method
        ListCard si = favoritesListService.addStoreToList(li);

        // verify
        assertNotNull("Saved store is null", si);
        assertEquals("Incorrect saved store", STORE_URL_NAME, si.getRlnm());
        assertEquals("Incorrect store order", 1, si.getRdr(), 0);
        assertEquals("Incorrect saved store list size", 1, us.getBrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(storeService, times(1)).findStoreByUrlName(STORE_URL_NAME);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a store to user's list complete");
    }

    @Test
    public void testDeleteSavedStoreFromList() {
        log.info("Testing deleting a store from user's list...");

        // first we add an store to user supplement to then remove
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.STORE, STORE_URL_NAME, 1));

        assertEquals("Incorrect saved store list size", 1, us.getBrds().size(), 0);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        ListEntry li = new ListEntry(STORE_URL_NAME);
        li.setUserCode(USER_CODE);

        // execute service method
        favoritesListService.deleteListCard(li);

        // verify
        assertEquals("Incorrect saved item list size", 0, us.getBrds().get(0).getCrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing deleting a store from user's list complete");
    }

    @Test
    public void testAddBranchToList() {
        log.info("Testing adding a branch to user's list...");

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(branchService.findBranchByUrlName(BRANCH_URL_NAME)).thenReturn(branch);

        ListEntry li = new ListEntry(BRANCH_URL_NAME);
        li.setUserCode(USER_CODE);

        // execute service method
        ListCard si = favoritesListService.addBranchToList(li);

        // verify
        assertNotNull("Saved branch is null", si);
        assertEquals("Incorrect saved branch", BRANCH_URL_NAME, si.getRlnm());
        assertEquals("Incorrect branch order", 1, si.getRdr(), 0);
        assertEquals("Incorrect saved branch list size", 1, us.getBrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(branchService, times(1)).findBranchByUrlName(BRANCH_URL_NAME);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a branch to user's list complete");
    }

    @Test
    public void testDeleteSavedBranchFromList() {
        log.info("Testing deleting a branch from user's list...");

        // first we add an branch to user supplement to then remove
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.BRANCH, BRANCH_URL_NAME, 1));

        assertEquals("Incorrect saved branch list size", 1, us.getBrds().size(), 0);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        ListEntry li = new ListEntry(BRANCH_URL_NAME);
        li.setUserCode(USER_CODE);

        // execute service method
        favoritesListService.deleteListCard(li);

        // verify
        assertEquals("Incorrect saved item list size", 0, us.getBrds().get(0).getCrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing deleting a branch from user's list complete");
    }

    @Test
    public void testAddOwnerToList() {
        log.info("Testing adding a owner to user's list...");

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(ownerService.findOwnerByUrlName(OWNER_URL_NAME)).thenReturn(owner);

        ListEntry li = new ListEntry(OWNER_URL_NAME);
        li.setUserCode(USER_CODE);

        // execute service method
        ListCard si = favoritesListService.addOwnerToList(li);

        // verify
        assertNotNull("Saved owner is null", si);
        assertEquals("Incorrect saved owner", OWNER_URL_NAME, si.getRlnm());
        assertEquals("Incorrect owner order", 1, si.getRdr(), 0);
        assertEquals("Incorrect saved owner list size", 1, us.getBrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(ownerService, times(1)).findOwnerByUrlName(OWNER_URL_NAME);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing adding a owner to user's list complete");
    }

    @Test
    public void testDeleteSavedOwnerFromList() {
        log.info("Testing deleting a owner from user's list...");

        // first we add an owner to user supplement to then remove
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.OWNER, OWNER_URL_NAME, 1));

        assertEquals("Incorrect saved owner list size", 1, us.getBrds().size(), 0);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        ListEntry li = new ListEntry(OWNER_URL_NAME);
        li.setUserCode(USER_CODE);

        // execute service method
        favoritesListService.deleteListCard(li);

        // verify
        assertEquals("Incorrect saved item list size", 0, us.getBrds().get(0).getCrds().size(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(userService, times(1)).saveUserSupplement(us);

        log.info("Testing deleting a owner from user's list complete");
    }

    @Test
    public void testFetchUserList() {
        log.info("Testing fetching user's list...");

        log.info("Setting up our UserSupplement object with values");
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.STORE, STORE_URL_NAME, 2));

        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.OWNER, OWNER_URL_NAME, 3));

        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.BRANCH, BRANCH_URL_NAME, 4));

        UserListQuery query = new UserListQuery(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);
        query.setContentType(ListContentType.ITEMS);

        // configure mocks
        when(userService.findUserByCode(USER_CODE)).thenReturn(user);
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);
        when(ownerService.findOwnerByUrlName(OWNER_URL_NAME)).thenReturn(owner);
        when(storeService.findStoreByUrlName(STORE_URL_NAME)).thenReturn(store);
        when(branchService.findBranchByUrlName(BRANCH_URL_NAME)).thenReturn(branch);

        // execute service method
        UserList ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 1, ulr.getCards().size(), 0);
        assertEquals("Incorrect list card type", ListCardType.ITEM, ulr.getCards().get(0).getTp());
        assertNotNull("Incorrect list card object", ulr.getCards().get(0).getItem());
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(itemService, times(1)).findItemByUrlName(ITEM_URL_NAME);

        // because they already existed with embedded on the item
        verify(ownerService, times(1)).findOwnerByUrlName(OWNER_URL_NAME);
        verify(storeService, times(1)).findStoreByUrlName(STORE_URL_NAME);

        log.info("Let's try again and this time with a motivator attached");
        us.addMotivator(new Motivator(MotivatorSource.QUIZ, relevancyMap));

        // configure mocks
        when(productEngineService.findRelevantItem(any(RelevantItemQuery.class))).thenReturn(relevantItem);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 1, ulr.getCards().size(), 0);
        assertEquals("Incorrect list card type", ListCardType.ITEM, ulr.getCards().get(0).getTp());
        assertNotNull("Incorrect list card object", ulr.getCards().get(0).getRelevantItem());
        verify(productEngineService, times(1)).findRelevantItem(any(RelevantItemQuery.class));

        log.info("Now we try it again and this time with all content types...");
        query.setContentType(ListContentType.ALL);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 4, ulr.getCards().size(), 0);

        log.info("Now we try it again and this time with store content types...");

        query.setContentType(ListContentType.STORES);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 1, ulr.getCards().size(), 0);

        log.info("Now we try it again and this time with owner content types...");

        query.setContentType(ListContentType.OWNERS);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 1, ulr.getCards().size(), 0);

        log.info("Now we try it again and this time with branch content types...");

        query.setContentType(ListContentType.BRANCHES);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 1, ulr.getCards().size(), 0);

        log.info("Now we try it again and this time with a store and its items content types...");

        query.setContentType(ListContentType.STORE_WITH_ITEMS);
        query.setStoreUrlName(STORE_URL_NAME);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 2, ulr.getCards().size(), 0);

        log.info("Now we try it again and this time with a store and its items content types...");

        query.setContentType(ListContentType.OWNER_WITH_ITEMS);
        query.setOwnerUrlName(OWNER_URL_NAME);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 2, ulr.getCards().size(), 0);

        log.info("Now we try it again and this time with different content types complete");

        log.info("Testing fetching user's list complete");
    }

    @Test
    public void testFetchUserLists() {
        log.info("Testing fetchUserLists()...");

        log.info("Setting up our UserSupplement object with values");

        final String one = "one";
        final String two = "two";
        final String three = "three";
        final String four = "four";

        us.addListCardBoard(new ListCardBoard(one));
        us.addListCardBoard(new ListCardBoard(two));
        us.addListCardBoard(new ListCardBoard(three));
        us.addListCardBoard(new ListCardBoard(four));

        us.addListCard(one, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        us.addListCard(two, new ListCard(ListCardType.STORE, STORE_URL_NAME, 1));

        us.addListCard(three, new ListCard(ListCardType.OWNER, OWNER_URL_NAME, 1));

        us.addListCard(four, new ListCard(ListCardType.BRANCH, BRANCH_URL_NAME, 1));

        // configure mocks
        when(userService.findUserByCode(USER_CODE)).thenReturn(user);
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);
        when(ownerService.findOwnerByUrlName(OWNER_URL_NAME)).thenReturn(owner);
        when(storeService.findStoreByUrlName(STORE_URL_NAME)).thenReturn(store);
        when(branchService.findBranchByUrlName(BRANCH_URL_NAME)).thenReturn(branch);

        // execute service method
        List<UserList> ulr = favoritesListService.fetchUserLists(USER_CODE);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of lists", 5, ulr.size(), 0);
        verify(userService, times(6)).findUserSupplement(USER_CODE);

        log.info("Testing fetchUserLists() complete");
    }

    @Test
    public void testFetchUserListWithFilter() {
        log.info("Testing fetching user's list with filtering...");

        log.info("Setting up our UserSupplement object with values");
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME_2, 2));

        UserListQuery query = new UserListQuery(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);
        query.setContentType(ListContentType.ITEMS);

        // configure mocks
        when(userService.findUserByCode(USER_CODE)).thenReturn(user);
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);
        when(itemService.findItemByUrlName(ITEM_URL_NAME_2)).thenReturn(item2);
        when(ownerService.findOwnerByUrlName(OWNER_URL_NAME)).thenReturn(owner);
        when(storeService.findStoreByUrlName(STORE_URL_NAME)).thenReturn(store);
        when(branchService.findBranchByUrlName(BRANCH_URL_NAME)).thenReturn(branch);

        // execute service method
        UserList ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 2, ulr.getCards().size(), 0);
        assertEquals("Incorrect list card type", ListCardType.ITEM, ulr.getCards().get(0).getTp());
        assertNotNull("Incorrect list card object 1", ulr.getCards().get(0).getItem());
        assertNotNull("Incorrect list card object 2", ulr.getCards().get(1).getItem());
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(itemService, times(1)).findItemByUrlName(ITEM_URL_NAME);
        verify(itemService, times(1)).findItemByUrlName(ITEM_URL_NAME_2);

        log.info("That was without filtering. Now we filter on category");
        query.setCategoryUrlName(CATEGORY_URL_NAME);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 1, ulr.getCards().size(), 0);
        assertEquals("Incorrect list card type", ListCardType.ITEM, ulr.getCards().get(0).getTp());
        assertNotNull("Incorrect list card object 1", ulr.getCards().get(0).getItem());

        log.info("Testing fetching user's list with filtering complete");
    }

    @Test
    public void testFetchUserListWithSort() {
        log.info("Testing fetching user's list with sorting...");

        log.info("Setting up our UserSupplement object with values");
        ListCard lc1 = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);
        lc1.setWn(true);
        List<Review> reviews = new ArrayList<Review>();
        Review review = new Review();
        review.setDt(new Date());
        reviews.add(review);
        lc1.setRvws(reviews);
        Alert alert = new Alert();
        alert.setDt(new Date());
        lc1.setLrt(alert);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc1);
        try {
            // sleep for a second so we know the dates are different
            Thread.sleep(250l);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        ListCard lc2 = new ListCard(ListCardType.ITEM, ITEM_URL_NAME_2, 2);
        reviews = new ArrayList<Review>();
        review = new Review();
        review.setDt(new Date());
        reviews.add(review);
        lc2.setRvws(reviews);
        alert = new Alert();
        alert.setDt(new Date());
        lc2.setLrt(alert);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc2);
        try {
            // sleep for a second so we know the dates are different
            Thread.sleep(250l);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        ListCard lc3 = new ListCard(ListCardType.ITEM, ITEM_URL_NAME_3, 3);
        lc3.setWn(true);
        reviews = new ArrayList<Review>();
        review = new Review();
        review.setDt(new Date());
        reviews.add(review);
        lc3.setRvws(reviews);
        alert = new Alert();
        alert.setDt(new Date());
        lc3.setLrt(alert);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc3);

        UserListQuery query = new UserListQuery(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);
        query.setContentType(ListContentType.ITEMS);
        query.setSortType(ListSortType.CREATED_DATE);

        // configure mocks
        when(userService.findUserByCode(USER_CODE)).thenReturn(user);
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(ITEM_URL_NAME)).thenReturn(item);
        when(itemService.findItemByUrlName(ITEM_URL_NAME_2)).thenReturn(item2);
        when(itemService.findItemByUrlName(ITEM_URL_NAME_3)).thenReturn(item3);
        when(ownerService.findOwnerByUrlName(OWNER_URL_NAME)).thenReturn(owner);
        when(storeService.findStoreByUrlName(STORE_URL_NAME)).thenReturn(store);
        when(branchService.findBranchByUrlName(BRANCH_URL_NAME)).thenReturn(branch);

        log.info("Sorting on created date");

        // execute service method
        UserList ulr = favoritesListService.fetchUserList(query);

        // verify
        assertNotNull("UserList is null", ulr);
        assertEquals("Incorrect number of list cards", 3, ulr.getCards().size(), 0);
        assertEquals("Incorrect list card type", ListCardType.ITEM, ulr.getCards().get(0).getTp());
        assertNotNull("Incorrect list card object 1", ulr.getCards().get(0).getItem());
        assertNotNull("Incorrect list card object 2", ulr.getCards().get(1).getItem());
        assertNotNull("Incorrect list card object 3", ulr.getCards().get(2).getItem());
        assertEquals("Incorrect sort", ITEM_URL_NAME_3, ulr.getCards().get(0).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_2, ulr.getCards().get(1).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME, ulr.getCards().get(2).getRlnm());
        verify(userService, times(1)).findUserSupplement(USER_CODE);
        verify(itemService, times(1)).findItemByUrlName(ITEM_URL_NAME);
        verify(itemService, times(1)).findItemByUrlName(ITEM_URL_NAME_2);
        verify(itemService, times(1)).findItemByUrlName(ITEM_URL_NAME_3);

        log.info("Sorting on order");
        query.setSortType(ListSortType.CUSTOM_ORDER);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertEquals("Incorrect sort", ITEM_URL_NAME_3, ulr.getCards().get(0).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_2, ulr.getCards().get(1).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME, ulr.getCards().get(2).getRlnm());

        log.info("Sorting on ownership");
        query.setSortType(ListSortType.OWNERSHIP);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertEquals("Incorrect sort", ITEM_URL_NAME_3, ulr.getCards().get(0).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_2, ulr.getCards().get(2).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME, ulr.getCards().get(1).getRlnm());

        log.info("Sorting on ownership");
        query.setSortType(ListSortType.REVIEW_DATE);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertEquals("Incorrect sort", ITEM_URL_NAME_3, ulr.getCards().get(0).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_2, ulr.getCards().get(1).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME, ulr.getCards().get(2).getRlnm());

        log.info("Sorting on ownership");
        query.setSortType(ListSortType.ALERT_DATE);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertEquals("Incorrect sort", ITEM_URL_NAME_3, ulr.getCards().get(2).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_2, ulr.getCards().get(1).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME, ulr.getCards().get(0).getRlnm());

        log.info("Sorting on ownership");
        query.setSortType(ListSortType.PRICE_HIGH);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertEquals("Incorrect sort", ITEM_URL_NAME, ulr.getCards().get(2).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_2, ulr.getCards().get(0).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_3, ulr.getCards().get(1).getRlnm());

        log.info("Sorting on ownership");
        query.setSortType(ListSortType.PRICE_LOW);

        // execute service method
        ulr = favoritesListService.fetchUserList(query);

        // verify
        assertEquals("Incorrect sort", ITEM_URL_NAME, ulr.getCards().get(0).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_2, ulr.getCards().get(2).getRlnm());
        assertEquals("Incorrect sort", ITEM_URL_NAME_3, ulr.getCards().get(1).getRlnm());


        log.info("Testing fetching user's list with sorting complete");
    }

    @Test
    public void testReorderUserList() {
        log.info("Testing reorderUserList()...");

        log.info("Setting up our UserSupplement object with values");
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_5", 5));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_6", 6));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_23", 23));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_1", 1));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_57", 57));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_100", 100));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_3", 3));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_17", 17));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_31", 31));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_45", 45));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_77", 77));
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME + "_32", 32));

        ListPosition lp = new ListPosition();
        lp.setUrlName(ITEM_URL_NAME + "_32");
        lp.setOrder(7);
        lp.setUserCode(USER_CODE);

        // configure mocks
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

        // execute service method
        favoritesListService.reorderUserList(lp);

        // verify
        assertNotNull("Response is null", lp.getMessage());
        assertEquals("Incorrect order of item", 1, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_1").getRdr(), 0);
        assertEquals("Incorrect order of item", 2, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_3").getRdr(), 0);
        assertEquals("Incorrect order of item", 3, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_5").getRdr(), 0);
        assertEquals("Incorrect order of item", 4, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_6").getRdr(), 0);
        assertEquals("Incorrect order of item", 5, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_17").getRdr(), 0);
        assertEquals("Incorrect order of item", 6, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_32").getRdr(), 0);
        assertEquals("Incorrect order of item", 7, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_23").getRdr(), 0);
        assertEquals("Incorrect order of item", 8, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_31").getRdr(), 0);
        assertEquals("Incorrect order of item", 9, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_45").getRdr(), 0);
        assertEquals("Incorrect order of item", 10, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_57").getRdr(), 0);
        assertEquals("Incorrect order of item", 11, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_77").getRdr(), 0);
        assertEquals("Incorrect order of item", 12, us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME + "_100").getRdr(), 0);
        verify(userService, times(1)).findUserSupplement(USER_CODE);

        log.info("Testing reorderUserList() complete");
    }

    @Test
    public void testFetchExternalImages() {
        log.info("Testing fetchExternalImages()...");

        log.info("Setting up our UserSupplement object with values");

        String url = "someurl";
        ExternalImageRequest request = new ExternalImageRequest();
        request.setUrl(url);
        DOMElement element = new DOMElement();
        element.setText("someelement");
        List<DOMElement> list = new ArrayList<DOMElement>(1);
        list.add(element);

        // configure mocks
        when(htmlUtilityService.fetchImagesFromUrl(any(String.class), any(Integer.class))).thenReturn(list);

        // execute service method
        DOMElements elements = favoritesListService.fetchExternalImages(request);

        // verify
        assertNotNull("DOMElements is null", elements);
        assertEquals("DomElements list is incorrect", list.size(), elements.getList().size(), 0);
        verify(htmlUtilityService, times(1)).fetchImagesFromUrl(any(String.class), any(Integer.class));

        log.info("Testing fetchExternalImages() complete");
    }

    @Test
    public void testSaveExternalListCard() {
        log.info("Testing addExternalCardToList()...");

        try {
            String url = "someurl";
            String name = "somename";
            String name2 = "somename2";
            ImageDigest id = new ImageDigest();
            Map<Integer, String> imageUrls = new HashMap<Integer, String>();
            imageUrls.put(50, "someurl.jpg");
            id.setImageUrls(imageUrls);

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(imageUtilityService.ingestImage(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(InputStream.class), any(Integer[].class), any(Integer.class))).thenReturn(id);

            // execute service method
            ExternalListCard elc = new ExternalListCard();
            elc.setExternalUrl(url);
            elc.setName(name);
            elc.setImageUrl(url);
            elc.setWidth("200");
            elc.setUserCode(USER_CODE);
            ListCard listCard1 = favoritesListService.addExternalCardToList(elc);

            // verify
            assertNotNull("ListCard is null", listCard1);
            assertNotNull("ListCard is not on UserSupplement", us.findListCard(elc.getBoardCode(), listCard1.getRlnm()));
            verify(userService, times(1)).findUserSupplement(USER_CODE);
            verify(userService, times(1)).saveUserSupplement(us);

            // execute service method
            elc = new ExternalListCard();
            elc.setExternalUrl(url);
            elc.setName(name2);
            MockMultipartFile file = new MockMultipartFile(name2, name2.getBytes());
            elc.setMultipartFile(file);
            elc.setUserCode(USER_CODE);
            ListCard listCard2 = favoritesListService.addExternalCardToList(elc);

            // verify
            assertNotNull("ListCard is null", listCard2);
            assertNotNull("ListCard is not on UserSupplement", us.findListCard(elc.getBoardCode(), listCard2.getRlnm()));
            verify(userService, times(2)).findUserSupplement(USER_CODE);
            verify(userService, times(2)).saveUserSupplement(us);
            verify(imageUtilityService, times(1)).ingestImage(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class), any(InputStream.class), any(Integer[].class), any(Integer.class));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        log.info("Testing addExternalCardToList() complete");
    }

    @Test
    public void testRenameListCard() {
        log.info("Testing renameListCard()...");

        final String newname = "newname";
        try {
            String url = "someurl";
            RenameListCard rlc = new RenameListCard();
            rlc.setName(newname);

            ListCard lc = new ListCard(ListCardType.EXTERNAL, url, 1);
            lc.setNm("oldname");
            us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, lc);

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

            // execute service method
            ListCard listCard1 = favoritesListService.renameListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, url, rlc);

            // verify
            assertNotNull("ListCard is null", listCard1);
            ListCard lc2 = us.findListCard(ApplicationConstants.DEFAULT_BOARD_NAME, listCard1.getRlnm());
            assertNotNull("ListCard is not on UserSupplement", lc2);
            assertEquals("ListCard name is incorrect", lc2.getNm(), newname);
            verify(userService, times(1)).findUserSupplement(USER_CODE);
            verify(userService, times(1)).saveUserSupplement(us);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        log.info("Testing renameListCard() complete");
    }

    @Test
    public void testDeleteListCardBoard() {
        log.info("Testing deleteListCardBoard()...");

        final String newname = "newname";

        try {
            String url = "someurl";
            RenameListCard rlc = new RenameListCard();
            rlc.setName(newname);

            ListCard lc = new ListCard(ListCardType.EXTERNAL, url, 1);
            lc.setNm("oldname");
            ListCardBoard board = new ListCardBoard(ApplicationConstants.DEFAULT_BOARD_NAME);
            String cd = board.getCd();
            us.addListCardBoard(board);

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);

            // execute service method
            favoritesListService.deleteListCardBoard(USER_CODE, cd);

            // verify
            assertTrue("Board is not null", us.getBrds().isEmpty());
            assertNull("Board is not null", us.findListCardBoard(cd));
            verify(userService, times(1)).findUserSupplement(USER_CODE);
            verify(userService, times(1)).saveUserSupplement(us);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
        log.info("Testing deleteListCardBoard() complete");
    }

    @Test
    public void testEmailPriceAlert() {
        log.info("Test email price alert");

        // Set up the mock data
        TestData testData = new TestData().invoke(true, 10.00, 8.00);
        List<UserUserSupplementEntry> entries = testData.getEntries();
        Item item = testData.getItem();
        List<AvailableInStore> availableInStores = testData.getAvailableInStores();
        RelevantItem ri = testData.getRi();
        Map<MailParameter, Object> params = testData.getParams();
        UserSupplement us = testData.getUs();

        // Set up mock expectations
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(PRICE_ALERT_TEST_ITEM_RLNM)).thenReturn(item);
        when(merchantService.findOnlineStores(item)).thenReturn(availableInStores);
        when(productEngineService.findRelevantItem(any(RelevantItemQuery.class))).thenReturn(ri);

        // execute the service method
        try {
            favoritesListService.sendValidPriceAlerts(USER_CODE);

            // verify
            verify(itemService, times(1)).findItemByUrlName(PRICE_ALERT_TEST_ITEM_RLNM);
            verify(productEngineService, times(1)).findRelevantItem(any(RelevantItemQuery.class));
            verify(mailService, times(1)).sendPriceAlert(EMAIL, params, Locale.US);
            verify(userService, times(1)).saveUserSupplement(us);

            // Make sure that alert won't get sent twice
            reset(userService, itemService, mailService);
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(itemService.findItemByUrlName(PRICE_ALERT_TEST_ITEM_RLNM)).thenReturn(item);

            favoritesListService.sendValidPriceAlerts(USER_CODE);

            // verify
            verify(itemService, times(0)).findItemByUrlName(PRICE_ALERT_TEST_ITEM_RLNM);
            verify(mailService, times(0)).sendPriceAlert(EMAIL, params, Locale.US);
            verify(userService, times(0)).saveUserSupplement(us);

            log.info("Test price alert email successful");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testNoPriceAlertWithHigherPrice() {
        log.info("Test no price alert for higher price");

        // Set up the mock data
        TestData testData = new TestData().invoke(true, 10.00, 11.00);
        List<UserUserSupplementEntry> entries = testData.getEntries();
        Item item = testData.getItem();
        Map<MailParameter, Object> params = testData.getParams();
        UserSupplement us = testData.getUs();

        // Set up mock expectations
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(PRICE_ALERT_TEST_ITEM_RLNM)).thenReturn(item);

        // execute the service method
        try {
            favoritesListService.sendValidPriceAlerts(USER_CODE);

            // verify
            verify(itemService, times(1)).findItemByUrlName(PRICE_ALERT_TEST_ITEM_RLNM);
            verify(mailService, times(0)).sendPriceAlert(EMAIL, params, Locale.US);
            verify(userService, times(0)).saveUserSupplement(us);

            log.info("Test price alert email successful");
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testNoPriceAlertWithDisabledAlert() {
        log.info("Test no price alert for diasbled alert");

        // Set up the mock data
        TestData testData = new TestData().invoke(false, 10.00, 8.00);
        List<UserUserSupplementEntry> entries = testData.getEntries();
        Item item = testData.getItem();
        RelevantItem ri = testData.getRi();
        Map<MailParameter, Object> params = testData.getParams();
        UserSupplement us = testData.getUs();

        // Set up mock expectations
        when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
        when(itemService.findItemByUrlName(PRICE_ALERT_TEST_ITEM_RLNM)).thenReturn(item);

        // execute the service method
        try {
            favoritesListService.sendValidPriceAlerts(USER_CODE);

            // verify
            verify(itemService, times(0)).findItemByUrlName(PRICE_ALERT_TEST_ITEM_RLNM);
            verify(mailService, times(0)).sendPriceAlert(EMAIL, params, Locale.US);
            verify(userService, times(0)).saveUserSupplement(us);

            log.info("Test price alert email successful");
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    private class TestData {
        private UserSupplement us;
        private List<UserUserSupplementEntry> entries;
        private Item item;
        private Map<MailParameter, Object> params;
        private RelevantItem ri;
        List<AvailableInStore> availableInStores;

        public List<AvailableInStore> getAvailableInStores() {
            return availableInStores;
        }

        public void setAvailableInStores(List<AvailableInStore> availableInStores) {
            this.availableInStores = availableInStores;
        }

        public RelevantItem getRi() {
            return ri;
        }

        public UserSupplement getUs() {
            return us;
        }

        public List<UserUserSupplementEntry> getEntries() {
            return entries;
        }

        public Item getItem() {
            return item;
        }

        public Map<MailParameter, Object> getParams() {
            return params;
        }

        public TestData invoke(boolean alertEnabled, double alertPrice, double itemPrice) {
            ListCard lc = new ListCard();
            lc.setTp(ListCardType.ITEM);
            lc.setRlnm(PRICE_ALERT_TEST_ITEM_RLNM);

            ListCardBoard lcb = new ListCardBoard();
            lcb.addListCard(lc);

            Alert alert = new Alert();
            alert.setPrc(alertPrice);
            alert.setMl(EMAIL);
            alert.setPrclrt(alertEnabled);
            lcb.addAlert(PRICE_ALERT_TEST_ITEM_RLNM, alert);

            us = new UserSupplement();
            us.setLcl(Locale.US);
            us.setCd(USER_CODE);

            List<ListCardBoard> boards = new ArrayList<ListCardBoard>();
            boards.add(lcb);
            us.setBrds(boards);

            entries = new ArrayList<UserUserSupplementEntry>();
            UserUserSupplementEntry entry = new UserUserSupplementEntry(user, us);
            entries.add(entry);

            createItem(itemPrice);

            availableInStores = new ArrayList<AvailableInStore>(1);
            AvailableInStore ais = new AvailableInStore(item.getNm(), item.getRlnm(), StoreType.ONLINE);
            availableInStores.add(ais);

            params = new HashMap<MailParameter, Object>();
            params.put(MailParameter.ITEM, item);
            params.put(MailParameter.PRICE_ALERT, alert);
            params.put(MailParameter.USER_SUPPLEMENT, us);
            params.put(MailParameter.ITEM_RELEVANCY, 99);
            params.put(MailParameter.STORES, availableInStores);

            ri = new RelevantItem(item, 430, 99, null);

            return this;
        }

        private void createItem(double itemPrice) {
            item = new Item();

            Attribute attr = new Attribute("LowestPrice", itemPrice);
            List<Attribute> sbttrs = new ArrayList<Attribute>();
            sbttrs.add(attr);
            item.setSbttrs(sbttrs);
        }
    }
}
