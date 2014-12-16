/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.test.controller;

import com.lela.commons.service.FavoritesListService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.utilities.UserSessionTrackingHelper;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.Alert;
import com.lela.domain.document.Comment;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.ListCardBoard;
import com.lela.domain.document.Note;
import com.lela.domain.document.Picture;
import com.lela.domain.document.Review;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.dto.dom.DOMElement;
import com.lela.domain.dto.dom.DOMElements;
import com.lela.domain.dto.list.ExternalImageRequest;
import com.lela.domain.dto.list.ExternalListCard;
import com.lela.domain.dto.list.ExternalListCardPicture;
import com.lela.domain.dto.list.ListCardBoardName;
import com.lela.domain.dto.list.ListCardPicture;
import com.lela.domain.dto.list.ListEntry;
import com.lela.domain.dto.list.ListPosition;
import com.lela.domain.dto.list.RenameListCard;
import com.lela.domain.dto.list.UserList;
import com.lela.domain.dto.list.UserListQuery;
import com.lela.domain.enums.list.ListCardType;
import com.lela.web.web.controller.FavoritesListController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Bjorn Harvold
 * Date: 8/3/11
 * Time: 10:05 AM
 * Responsibility:
 */
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class FavoritesListControllerUnitTest {
    private static final Logger log = LoggerFactory.getLogger(FavoritesListControllerUnitTest.class);
    private static final String USER_CODE = "someusercode";
    private static final String STORE_URL_NAME = "someteststore";
    private static final String ITEM_URL_NAME = "someitem";
    private static final String BRANCH_URL_NAME = "somebranch";
    private static final String OWNER_URL_NAME = "someowner";
    private static final String PICTURE_CODE = "somepicturecode";
    private static final String NOTE_CODE = "somenotecode";
    private static final String REVIEW_CODE = "somereviewcode";
    private static final String COMMENT_CODE = "somecommentcode";

    @Mock
    private FavoritesListService favoritesListService;

    @Mock
    private UserService userService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserSessionTrackingHelper userSessionTrackingHelper;

    @InjectMocks
    private FavoritesListController favoritesListController;

    private User user;

    private MockDevice mockDevice = new MockDevice(MockDevice.DEVICE_TYPE.NORMAL);
    private Device device = new WurflDevice(mockDevice);

    @Before
    public void beforeEach() {
        favoritesListController.setUserSessionTrackingHelper(userSessionTrackingHelper);
        favoritesListController.setMobileEnabledInEnvironment(false);
        user = new User();
        user.setCd(USER_CODE);
    }

    @Test
    public void testShowUserList() {
        log.info("Testing showUserList()...");

        MockHttpSession session = new MockHttpSession();
        String view;
        Model model = new BindingAwareModelMap();
        UserListQuery query = new UserListQuery(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            view = favoritesListController.showUserList(ApplicationConstants.DEFAULT_BOARD_NAME, device, session, model);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list", view);
            assertNull("Model should be empty", model.asMap().get(WebConstants.USER_LIST));
            verify(favoritesListService, times(1)).fetchUserList(any(UserListQuery.class));

            log.info("Then we configure the mocks to return a result object in the model");

            // configure mocks
            UserList pl = new UserList(query);
            when(favoritesListService.fetchUserList(any(UserListQuery.class))).thenReturn(pl);

            // execute controller method
            view = favoritesListController.showUserList(ApplicationConstants.DEFAULT_BOARD_NAME, device, session, model);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list", view);
            assertNotNull("Model should not be empty", model.asMap().get(WebConstants.USER_LIST));
            assertEquals("Objects don't match", pl, model.asMap().get(WebConstants.USER_LIST));
            verify(favoritesListService, times(2)).fetchUserList(any(UserListQuery.class));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showUserList() complete");
    }

    @Test
    public void testShowPublicUserList() {
        log.info("Testing showPublicUserList()...");

        MockHttpSession session = new MockHttpSession();
        String view;
        Model model = new BindingAwareModelMap();
        UserListQuery query = new UserListQuery(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            view = favoritesListController.showPublicUserList(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, model);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list", view);
            assertNull("Model should be empty", model.asMap().get(WebConstants.USER_LIST));
            verify(favoritesListService, times(1)).fetchUserList(any(UserListQuery.class));

            log.info("Then we configure the mocks to return a result object in the model");

            // configure mocks
            UserList pl = new UserList(query);
            when(favoritesListService.fetchUserList(any(UserListQuery.class))).thenReturn(pl);

            // execute controller method
            view = favoritesListController.showPublicUserList(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, model);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list", view);
            assertNotNull("Model should not be empty", model.asMap().get(WebConstants.USER_LIST));
            assertEquals("Objects don't match", pl, model.asMap().get(WebConstants.USER_LIST));
            verify(favoritesListService, times(2)).fetchUserList(any(UserListQuery.class));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showPublicUserList() complete");
    }

    @Test
    public void testReorderUserList() {
        log.info("Testing reorderUserList()...");

        MockHttpSession session = new MockHttpSession();
        ListPosition lp = new ListPosition();
        lp.setUserCode(USER_CODE);
        lp.setBoardName(ApplicationConstants.DEFAULT_BOARD_NAME);

        try {
            log.info("Configuring the mocks");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            lp = favoritesListController.reorderUserList(lp, session);

            // verify
            assertNotNull("Object is null", lp);
            verify(favoritesListService, times(1)).reorderUserList(lp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing reorderUserList() complete");
    }

    @Test
    public void testShowUserListWithOptions() {
        log.info("Testing showUserListWithOptions()...");

        MockHttpSession session = new MockHttpSession();
        String view;
        Model model = new BindingAwareModelMap();
        UserListQuery query = new UserListQuery(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            view = favoritesListController.showUserListWithOptions(query, session, model);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list.data", view);
            assertNull("Model should be empty", model.asMap().get(WebConstants.USER_LIST));
            verify(favoritesListService, times(1)).fetchUserList(any(UserListQuery.class));

            log.info("Then we configure the mocks to return a result object in the model");

            // configure mocks
            UserList pl = new UserList(query);
            when(favoritesListService.fetchUserList(any(UserListQuery.class))).thenReturn(pl);

            // execute controller method
            view = favoritesListController.showUserListWithOptions(query, session, model);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list.data", view);
            assertNotNull("Model should not be empty", model.asMap().get(WebConstants.USER_LIST));
            assertEquals("Objects don't match", pl, model.asMap().get(WebConstants.USER_LIST));
            verify(favoritesListService, times(2)).fetchUserList(any(UserListQuery.class));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showUserListWithOptions() complete");
    }

    @Test
    public void testShowPublicUserListWithOptions() {
        log.info("Testing showPublicUserListWithOptions()...");

        MockHttpSession session = new MockHttpSession();
        String view;
        Model model = new BindingAwareModelMap();
        UserListQuery query = new UserListQuery(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            view = favoritesListController.showUserListWithOptions(query, session, model);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list.data", view);
            assertNull("Model should be empty", model.asMap().get(WebConstants.USER_LIST));
            verify(favoritesListService, times(1)).fetchUserList(any(UserListQuery.class));

            log.info("Then we configure the mocks to return a result object in the model");

            // configure mocks
            UserList pl = new UserList(query);
            when(favoritesListService.fetchUserList(any(UserListQuery.class))).thenReturn(pl);

            // execute controller method
            view = favoritesListController.showPublicUserListWithOptions(USER_CODE, query, model);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list.data", view);
            assertNotNull("Model should not be empty", model.asMap().get(WebConstants.USER_LIST));
            assertEquals("Objects don't match", pl, model.asMap().get(WebConstants.USER_LIST));
            verify(favoritesListService, times(2)).fetchUserList(any(UserListQuery.class));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showPublicUserListWithOptions() complete");
    }

    @Test
    public void testShowUserListCard() {
        log.info("Testing showUserListCard()...");

        MockHttpSession session = new MockHttpSession();
        String view;
        Model model = new BindingAwareModelMap();
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);

        try {
            // configure mocks
            when(favoritesListService.findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME)).thenReturn(lc);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            view = favoritesListController.showUserListCard(ApplicationConstants.DEFAULT_BOARD_NAME, null, ITEM_URL_NAME, session, model, Locale.US);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list.card", view);
            assertNotNull("Model should not be empty", model.asMap().get(WebConstants.LIST_CARD));
            verify(favoritesListService, times(1)).findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showUserListCard() complete");
    }

    @Test
    public void testShowNoteDetails() {
        log.info("Testing showNoteDetails()...");

        MockHttpSession session = new MockHttpSession();
        String view;
        Model model = new BindingAwareModelMap();
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);

        try {
            // configure mocks
            when(favoritesListService.findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME)).thenReturn(lc);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            view = favoritesListController.showNoteDetails(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, session, model, Locale.US);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list.card.note", view);
            assertNotNull("Model should not be empty", model.asMap().get(WebConstants.LIST_CARD));
            verify(favoritesListService, times(1)).findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showNoteDetails() complete");
    }

    @Test
    public void testShowReviewDetails() {
        log.info("Testing showReviewDetails()...");

        MockHttpSession session = new MockHttpSession();
        String view;
        Model model = new BindingAwareModelMap();
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);

        try {
            // configure mocks
            when(favoritesListService.findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME)).thenReturn(lc);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            view = favoritesListController.showReviewDetails(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, session, model, Locale.US);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list.card.review", view);
            assertNotNull("Model should not be empty", model.asMap().get(WebConstants.LIST_CARD));
            verify(favoritesListService, times(1)).findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showReviewDetails() complete");
    }

    @Test
    public void testShowAlertDetails() {
        log.info("Testing showAlertDetails()...");

        MockHttpSession session = new MockHttpSession();
        String view;
        Model model = new BindingAwareModelMap();
        ListCard lc = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);

        try {
            // configure mocks
            when(favoritesListService.findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME)).thenReturn(lc);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            view = favoritesListController.showAlertDetails(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, session, model, Locale.US);

            // verify
            assertNotNull("View is null", view);
            assertEquals("Tile view is incorrect", "user.list.card.alert", view);
            assertNotNull("Model should not be empty", model.asMap().get(WebConstants.LIST_CARD));
            verify(favoritesListService, times(1)).findListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showAlertDetails() complete");
    }

    @Test
    public void testSaveNoteToListCard() {
        log.info("Testing saveNoteToListCard()...");

        MockHttpSession session = new MockHttpSession();
        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));
        Note note = new Note();
        note.setTxt("somenote");

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            note = favoritesListController.saveNoteToListCard(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, note, session);

            // verify
            assertNotNull("Note is null", note);
            verify(favoritesListService, times(1)).addNoteToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, note);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing saveNoteToListCard() complete");
    }

    @Test
    public void testSaveListCardBoard() {
        log.info("Testing saveListCardBoard()...");

        MockHttpSession session = new MockHttpSession();
        UserSupplement us = new UserSupplement(USER_CODE);
        ListCardBoard lcb = new ListCardBoard("someboard");
        BindingResult errors = new BindException(lcb, "listCardBoard");
        Model model = new BindingAwareModelMap();
        SessionStatus status = new SimpleSessionStatus();

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);
            when(favoritesListService.addListCardBoard(USER_CODE, lcb)).thenReturn(lcb);

            // execute controller method
            String view = favoritesListController.saveListCardBoard(lcb, errors, session, Locale.US, model, status);

            // verify
            assertNotNull("View is null", view);
            assertNotNull("Object is null", model.containsAttribute(WebConstants.LIST_CARD_BOARD));
            verify(favoritesListService, times(1)).addListCardBoard(USER_CODE, lcb);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing saveListCardBoard() complete");
    }

    @Test
    public void testSaveAlertToListCard() {
        log.info("Testing saveAlertToListCard()...");

        MockHttpSession session = new MockHttpSession();

        Model model = new BindingAwareModelMap();
        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));
        Alert alert = new Alert();
        BindingResult errors = new BindException(alert, "alert");
        SessionStatus status = new SimpleSessionStatus();

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String view = favoritesListController.saveAlertToListCard(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, alert, errors, session, Locale.US, model, status);

            // verify
            assertEquals("View is incorrect", "user.list.card.alert", view);
            verify(favoritesListService, times(1)).addAlertToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, alert);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing saveAlertToListCard() complete");
    }

    @Test
    public void testSaveReviewToListCard() {
        log.info("Testing saveReviewToListCard()...");

        MockHttpSession session = new MockHttpSession();
        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));
        Review review = new Review();
        review.setTxt("somereview");

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            review = favoritesListController.saveReviewToListCard(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, review, session);

            // verify
            assertNotNull("Review is null", review);
            verify(favoritesListService, times(1)).addReviewToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, review);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing saveReviewToListCard() complete");
    }

    @Test
    public void testSaveCommentToListCard() {
        log.info("Testing saveCommentToListCard()...");

        MockHttpSession session = new MockHttpSession();
        Model model = new BindingAwareModelMap();
        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        Comment comment = new Comment();
        comment.setTxt("somecomment");
        comment.setWcd(USER_CODE);

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String view = favoritesListController.saveCommentToListCard(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, comment, session, model);

            // verify
            assertEquals("View is incorrect", "user.list.card.comment", view);
            verify(favoritesListService, times(1)).addCommentToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, user, comment);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing saveCommentToListCard() complete");
    }

    @Test
    public void testSavePictureToListCard() {
        log.info("Testing savePictureToListCard()...");

        MockHttpSession session = new MockHttpSession();

        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));
        ListCardPicture picture = new ListCardPicture();
        String fileString = "somefile";
        MockMultipartFile file = new MockMultipartFile(fileString, fileString + ".jpg", "utf-8", fileString.getBytes());
        picture.setMultipartFile(file);
        BindingResult errors = new BindException(picture, "picture");
        Model model = new BindingAwareModelMap();
        Picture pic = new Picture();
        SessionStatus status = new SimpleSessionStatus();

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(favoritesListService.addPictureToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, picture)).thenReturn(pic);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String view = favoritesListController.savePictureToListCard(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, picture, errors, session, model, status, Locale.US);

            // verify
            assertNotNull("view is null", view);
            verify(favoritesListService, times(1)).addPictureToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, picture);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing savePictureToListCard() complete");
    }


    @Test
    public void testSaveExternalPictureToListCard() {
        log.info("Testing saveExternalPictureToListCard()...");

        MockHttpSession session = new MockHttpSession();

        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));
        ExternalListCardPicture picture = new ExternalListCardPicture();
        picture.setUrl("someurl");
        picture.setWidth("200");
        BindingResult errors = new BindException(picture, "picture");
        Model model = new BindingAwareModelMap();
        Picture pic = new Picture();
        SessionStatus status = new SimpleSessionStatus();

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(favoritesListService.addPictureToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, picture)).thenReturn(pic);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String view = favoritesListController.saveExternalPictureToListCard(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, picture, errors, session, model, status, Locale.US);

            // verify
            assertNotNull("view is null", view);
            verify(favoritesListService, times(1)).addPictureToListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, picture);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing saveExternalPictureToListCard() complete");
    }

    @Test
    public void testDeletePictureFromListCard() {
        log.info("Testing deletePictureFromListCard()...");

        MockHttpSession session = new MockHttpSession();
        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String result = favoritesListController.deletePictureFromListCard(ITEM_URL_NAME, PICTURE_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, session);

            // verify
            assertEquals("Result is incorrect", WebConstants.SUCCESS, result);
            verify(favoritesListService, times(1)).deletePictureFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, PICTURE_CODE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing deletePictureFromListCard() complete");
    }

    @Test
    public void testDeleteCommentFromListCard() {
        log.info("Testing deleteCommentFromListCard()...");

        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String result = favoritesListController.deleteCommentFromListCard(USER_CODE, ITEM_URL_NAME, COMMENT_CODE, ApplicationConstants.DEFAULT_BOARD_NAME);

            // verify
            assertEquals("Result is incorrect", WebConstants.SUCCESS, result);
            verify(favoritesListService, times(1)).deleteCommentFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, COMMENT_CODE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing deleteCommentFromListCard() complete");
    }

    @Test
    public void testDeleteNoteFromListCard() {
        log.info("Testing deleteNoteFromListCard()...");

        MockHttpSession session = new MockHttpSession();
        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String result = favoritesListController.deleteNoteFromListCard(ITEM_URL_NAME, NOTE_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, session);

            // verify
            assertEquals("Result is incorrect", WebConstants.SUCCESS, result);
            verify(favoritesListService, times(1)).deleteNoteFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, NOTE_CODE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing deleteNoteFromListCard() complete");
    }

    @Test
    public void testDeleteReviewFromListCard() {
        log.info("Testing deleteReviewFromListCard()...");

        MockHttpSession session = new MockHttpSession();
        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String result = favoritesListController.deleteReviewFromListCard(ITEM_URL_NAME, REVIEW_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, session);

            // verify
            assertEquals("Result is incorrect", WebConstants.SUCCESS, result);
            verify(favoritesListService, times(1)).deleteReviewFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME, REVIEW_CODE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing deleteReviewFromListCard() complete");
    }

    @Test
    public void testDeleteAlertFromListCard() {
        log.info("Testing deleteAlertFromListCard()...");

        MockHttpSession session = new MockHttpSession();
        UserSupplement us = new UserSupplement(USER_CODE);
        us.addListCard(ApplicationConstants.DEFAULT_BOARD_NAME, new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1));

        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userService.findUserSupplement(USER_CODE)).thenReturn(us);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String result = favoritesListController.deleteAlertFromListCard(ITEM_URL_NAME, ApplicationConstants.DEFAULT_BOARD_NAME, session);

            // verify
            assertEquals("Result is incorrect", WebConstants.SUCCESS, result);
            verify(favoritesListService, times(1)).deleteAlertFromListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, ITEM_URL_NAME);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing deleteAlertFromListCard() complete");
    }

    @Test
    public void testAddItemToList() {
        log.info("Testing addItemToList()...");

        MockHttpSession session = new MockHttpSession();
        ListEntry li = new ListEntry(ITEM_URL_NAME);
        ListCard si = new ListCard(ListCardType.ITEM, ITEM_URL_NAME, 1);
        
        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            li = favoritesListController.addItemToList(li, session);

            // verify
            assertEquals("This should not have worked", WebConstants.FAILURE, li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(1)).addItemToList(li);

            log.info("Then we configure the mocks to return a result object in the model");

            // configure mocks
            when(favoritesListService.addItemToList(li)).thenReturn(si);

            // execute controller method
            li = favoritesListController.addItemToList(li, session);

            // verify
            assertEquals("This should have worked", WebConstants.SUCCESS, li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(2)).addItemToList(li);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing addItemToList()...");
    }

    @Test
    public void testDeleteListCard() {
        log.info("Testing deleteListCard()...");

        MockHttpSession session = new MockHttpSession();
        ListEntry li = new ListEntry(ITEM_URL_NAME);
        
        try {
            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            li = favoritesListController.deleteListCard(li, session);

            // verify
            assertNull("This should not have been populated as we are working with mocks", li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(1)).deleteListCard(li);
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing deleteListCard()...");
    }

    @Test
    public void testAddStoreToList() {
        log.info("Testing addStoreToList()...");

        MockHttpSession session = new MockHttpSession();
        ListEntry li = new ListEntry(STORE_URL_NAME);
        ListCard si = new ListCard(ListCardType.STORE, STORE_URL_NAME, 1);
        
        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            li = favoritesListController.addStoreToList(li, session);

            // verify
            assertEquals("This should not have worked", WebConstants.FAILURE, li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(1)).addStoreToList(li);

            log.info("Then we configure the mocks to return a result object in the model");

            // configure mocks
            when(favoritesListService.addStoreToList(li)).thenReturn(si);

            // execute controller method
            li = favoritesListController.addStoreToList(li, session);

            // verify
            assertEquals("This should have worked", WebConstants.SUCCESS, li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(2)).addStoreToList(li);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing addStoreToList()...");
    }

    @Test
    public void testAddOwnerToList() {
        log.info("Testing addOwnerToList()...");

        MockHttpSession session = new MockHttpSession();
        ListEntry li = new ListEntry(OWNER_URL_NAME);
        ListCard si = new ListCard(ListCardType.OWNER, OWNER_URL_NAME, 1);
        
        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            li = favoritesListController.addOwnerToList(li, session);

            // verify
            assertEquals("This should not have worked", WebConstants.FAILURE, li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(1)).addOwnerToList(li);

            log.info("Then we configure the mocks to return a result object in the model");

            // configure mocks
            when(favoritesListService.addOwnerToList(li)).thenReturn(si);

            // execute controller method
            li = favoritesListController.addOwnerToList(li, session);

            // verify
            assertEquals("This should have worked", WebConstants.SUCCESS, li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(2)).addOwnerToList(li);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing addOwnerToList()...");
    }

    @Test
    public void testAddBranchToList() {
        log.info("Testing addBranchToList()...");

        MockHttpSession session = new MockHttpSession();
        ListEntry li = new ListEntry(BRANCH_URL_NAME);
        ListCard si = new ListCard(ListCardType.BRANCH, BRANCH_URL_NAME, 1);
        
        try {
            log.info("First we test the controller without any mock configuration");

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            li = favoritesListController.addBranchToList(li, session);

            // verify
            assertEquals("This should not have worked", WebConstants.FAILURE, li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(1)).addBranchToList(li);

            log.info("Then we configure the mocks to return a result object in the model");

            // configure mocks
            when(favoritesListService.addBranchToList(li)).thenReturn(si);

            // execute controller method
            li = favoritesListController.addBranchToList(li, session);

            // verify
            assertEquals("This should have worked", WebConstants.SUCCESS, li.getMessage());
            assertNotNull("User code should have been populated", li.getUserCode());
            verify(favoritesListService, times(2)).addBranchToList(li);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing addBranchToList()...");
    }

    @Test
    public void testFetchExternalImages() {
        log.info("Testing fetchExternalImages()...");

        String url = "someexternalurl";
        ExternalImageRequest request = new ExternalImageRequest();
        request.setUrl(url);
        DOMElement element = new DOMElement();
        element.setText("someelement");
        List<DOMElement> list = new ArrayList<DOMElement>(1);
        list.add(element);
        DOMElements returnObject = new DOMElements(list);
        Model model = new BindingAwareModelMap();

        try {
            // configure mocks
            when(favoritesListService.fetchExternalImages(request)).thenReturn(returnObject);

            // execute controller method
            String view = favoritesListController.fetchExternalImages(ApplicationConstants.DEFAULT_BOARD_NAME, request, model);

            // verify
            assertNotNull("view is null", view);
            assertNotNull("DOMElements is null", model.asMap().get(WebConstants.EXTERNAL_IMAGES));
            verify(favoritesListService, times(1)).fetchExternalImages(request);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing fetchExternalImages() complete");
    }

    @Test
    public void testShowNewListCardForm() {
        log.info("Testing showNewListCardForm()...");

        Model model = new BindingAwareModelMap();

        try {

            // execute controller method
            String view = favoritesListController.showNewListCardForm(ApplicationConstants.DEFAULT_BOARD_NAME, model);

            // verify
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "user.list.external.form", view);
            assertNotNull("binding object is null", model.asMap().get(WebConstants.EXTERNAL_LIST_CARD));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing showNewListCardForm() complete");
    }

    @Test
    public void testSaveExternalListCard() {
        log.info("Testing saveExternalListCard()...");

        Model model = new BindingAwareModelMap();

        try {
            ListCard lc = new ListCard();
            ExternalListCard elc = new ExternalListCard();
            BindingResult errors = new BindException(elc, WebConstants.EXTERNAL_LIST_CARD);
            SessionStatus status = new SimpleSessionStatus();
            MockHttpSession session = new MockHttpSession();

            // configure mocks
            when(favoritesListService.addExternalCardToList(elc)).thenReturn(lc);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String view = favoritesListController.saveExternalListCard(ApplicationConstants.DEFAULT_BOARD_NAME, elc, errors, status, model, session, Locale.US);

            // verify
            assertNotNull("view is null", view);
            assertEquals("view is incorrect", "user.list.external.form", view);
            assertNotNull("binding object is null", model.asMap().get(WebConstants.EXTERNAL_LIST_CARD));
            verify(favoritesListService, times(1)).addExternalCardToList(elc);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        SpringSecurityHelper.unsecureChannel();
        log.info("Testing saveExternalListCard() complete");
    }

    @Test
    public void testShowListCardBoardNames() {
        log.info("Testing showListCardBoardNames()...");

        try {
            List<ListCardBoardName> names = new ArrayList<ListCardBoardName>(1);
            ListCardBoardName name = new ListCardBoardName("name", "name");
            names.add(name);

            SessionStatus status = new SimpleSessionStatus();
            MockHttpSession session = new MockHttpSession();
            Model model = new BindingAwareModelMap();

            // configure mocks
            when(favoritesListService.findListCardBoardNames(USER_CODE)).thenReturn(names);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String view = favoritesListController.showListCardBoardNames(session, model, Locale.US);

            // verify
            assertNotNull("view is null", view);
            assertTrue("Names don't exist", model.containsAttribute(WebConstants.LIST_CARD_BOARD_NAMES));
            List<ListCardBoardName> list = (List<ListCardBoardName>) model.asMap().get(WebConstants.LIST_CARD_BOARD_NAMES);
            assertEquals("names is incorrect", 1, list.size(), 0);
            verify(favoritesListService, times(1)).findListCardBoardNames(USER_CODE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing showListCardBoardNames() complete");
    }

    @Test
    public void testDeleteListCardBoard() {
        log.info("Testing deleteListCardBoard()...");

        try {
            HttpSession session = new MockHttpSession();

            // configure mocks
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String result = favoritesListController.deleteListCardBoard(ApplicationConstants.DEFAULT_BOARD_NAME, session);

            // verify
            assertNotNull("result is null", result);
            assertEquals("result is incorrect", WebConstants.SUCCESS, result);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing deleteListCardBoard() complete");
    }

    @Test
    public void testRenameListCard() {
        log.info("Testing renameListCard()...");

        final String newname = "newname";
        try {
            ListCard lc = new ListCard(ListCardType.EXTERNAL, newname, 1);

            RenameListCard rlc = new RenameListCard();
            rlc.setName(newname);

            SessionStatus status = new SimpleSessionStatus();
            MockHttpSession session = new MockHttpSession();
            BindingResult errors = new BindException(rlc, "renameListCard");
            Model model = new BindingAwareModelMap();

            // configure mocks
            when(favoritesListService.renameListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, newname, rlc)).thenReturn(lc);
            when(userSessionTrackingHelper.retrieveUserFromPrincipalOrSession(any(MockHttpSession.class))).thenReturn(user);

            // execute controller method
            String view = favoritesListController.renameListCard(newname, ApplicationConstants.DEFAULT_BOARD_NAME, rlc, errors, session, model, status, Locale.US);

            // verify
            verify(favoritesListService, times(1)).renameListCard(USER_CODE, ApplicationConstants.DEFAULT_BOARD_NAME, newname, rlc);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail("Method threw unexpected exception: " + e.getMessage());
        }

        log.info("Testing renameListCard() complete");
    }
}
