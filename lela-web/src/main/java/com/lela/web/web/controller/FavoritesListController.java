/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */


package com.lela.web.web.controller;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.commons.service.UserService;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.service.FavoritesListService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.document.Alert;
import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.Comment;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.ListCardBoard;
import com.lela.domain.document.Note;
import com.lela.domain.document.Review;
import com.lela.domain.document.User;
import com.lela.domain.document.UserSupplement;
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
import com.lela.web.web.validator.ListCardAlertValidator;
import com.lela.web.web.validator.ListCardBoardValidator;
import com.lela.web.web.validator.ListCardImageValidator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * <p>Title: FavoritesListController</p>
 * <p>Description: User profile.</p>
 *
 * @author Bjorn Harvold
 */
@Controller("favoritesListController")
@SessionAttributes(types = {ListCardPicture.class, ListCardBoard.class})
public class FavoritesListController extends AbstractController {
    private final static Logger log = LoggerFactory.getLogger(FavoritesListController.class);

    private final FavoritesListService favoritesListService;
    private final UserService userService;
    private final MessageSource messageSource;

    /**
     * Constructs ...
     *
     * @param favoritesListService favoritesListService
     * @param userService          userService
     * @param messageSource        messageSource
     */
    @Autowired
    public FavoritesListController(FavoritesListService favoritesListService,
                                   UserService userService,
                                   MessageSource messageSource) {
        this.favoritesListService = favoritesListService;
        this.userService = userService;
        this.messageSource = messageSource;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Retrieves a specific ListCard object
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/list/card/{urlName}", method = RequestMethod.GET)
    public String showUserListCard(@RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                   @RequestParam(value = "cd", required = false) String userCode,
                                   @PathVariable("urlName") String urlName,
                                   HttpSession session, Model model, Locale locale) throws Exception {

        model.addAttribute(WebConstants.PUBLIC_LIST, true);
        if (StringUtils.isBlank(userCode)) {
            model.addAttribute(WebConstants.PUBLIC_LIST, false);
            User user = retrieveUserFromPrincipalOrSession(session);
            userCode = user.getCd();
        }

        ListCard lc = favoritesListService.findListCard(userCode, boardCode, urlName);
        List<CategoryGroup> topLevelDepartments = favoritesListService.findTopLevelDepartments(locale);
        List<ListCardBoardName> names = favoritesListService.findListCardBoardNames(userCode);

        model.addAttribute(WebConstants.LIST_CARD, lc);
        model.addAttribute(WebConstants.BOARD_CODE, boardCode);
        model.addAttribute(WebConstants.DEPARTMENTS, topLevelDepartments);
        model.addAttribute(WebConstants.LIST_CARD_BOARD_NAMES, names);

        return "user.list.card";
    }

    /**
     * Retrieves all products, brands and stores the user has saved explicitly or implicitly
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String showUserList(@RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                               Device device, HttpSession session, Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        UserListQuery query = new UserListQuery(user.getCd(), boardCode);
        UserList ul = favoritesListService.fetchUserList(query);
        model.addAttribute(WebConstants.USER_LIST, ul);
        model.addAttribute(WebConstants.PUBLIC_LIST, false);
        model.addAttribute(WebConstants.FILTERED, false);
        model.addAttribute(WebConstants.BOARD_CODE, boardCode);

        // get the user
        String view = "user.list";
        return returnMobileViewIfNecessary(model, device, session, view);
    }

    /**
     * Retrieves all user lists
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/lists", method = RequestMethod.GET)
    public String showUserLists(Device device, HttpSession session, Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        List<UserList> lists = favoritesListService.fetchUserLists(user.getCd());
        model.addAttribute(WebConstants.USER_LISTS, lists);

        // get the user
        String view = "user.board";
        return returnMobileViewIfNecessary(model, device, session, view);
    }

    /**
     * Retrieves all products, brands and stores the user has saved explicitly or implicitly
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/list/{userCode}", method = RequestMethod.GET)
    public String showPublicUserList(@PathVariable("userCode") String userCode,
                                     @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                     Model model) throws Exception {

        UserListQuery query = new UserListQuery(userCode, boardCode);
        UserList ul = favoritesListService.fetchUserList(query);
        List<ListCardBoardName> names = favoritesListService.findListCardBoardNames(userCode);
        
        model.addAttribute(WebConstants.USER_LIST, ul);
        model.addAttribute(WebConstants.PUBLIC_LIST, true);
        model.addAttribute(WebConstants.FILTERED, false);
        model.addAttribute(WebConstants.BOARD_CODE, boardCode);
        model.addAttribute(WebConstants.LIST_CARD_BOARD_NAMES, names);

        return "user.list";
    }

    /**
     * Retrieves all products, brands and stores the user has saved explicitly or implicitly
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.POST)
    public String showUserListWithOptions(@RequestBody UserListQuery query,
                                          HttpSession session, Model model) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        query.setUserCode(user.getCd());
        UserList ul = favoritesListService.fetchUserList(query);
        model.addAttribute(WebConstants.USER_LIST, ul);
        model.addAttribute(WebConstants.PUBLIC_LIST, false);
        model.addAttribute(WebConstants.FILTERED, true);
        model.addAttribute(WebConstants.BOARD_CODE, query.getBoardCode());

        return "user.list.data";
    }

    /**
     * Retrieves all products, brands and stores the user has saved explicitly or implicitly
     *
     * @param model model
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/list/{userCode}", method = RequestMethod.POST)
    public String showPublicUserListWithOptions(@PathVariable("userCode") String userCode,
                                                @RequestBody UserListQuery query,
                                                Model model) throws Exception {
        query.setUserCode(userCode);
        UserList ul = favoritesListService.fetchUserList(query);
        model.addAttribute(WebConstants.USER_LIST, ul);
        model.addAttribute(WebConstants.PUBLIC_LIST, true);
        model.addAttribute(WebConstants.FILTERED, true);

        return "user.list.data";
    }

    @RequestMapping(value = "/user/list/reorder", method = RequestMethod.POST)
    @ResponseBody
    public ListPosition reorderUserList(@RequestBody ListPosition dto,
                                        HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        dto.setUserCode(user.getCd());
        favoritesListService.reorderUserList(dto);

        return dto;
    }

    /**
     * Method description
     *
     * @param li item
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/list/item", method = RequestMethod.PUT, consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public ListEntry addItemToList(@RequestBody ListEntry li, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        li.setUserCode(user.getCd());

        ListCard si = favoritesListService.addItemToList(li);

        if (si != null) {
            li.setMessage(WebConstants.SUCCESS);
        } else {
            li.setMessage(WebConstants.FAILURE);
        }

        return li;
    }

    /**
     * Method description
     *
     * @param li li
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/list/store", method = RequestMethod.PUT, consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public ListEntry addStoreToList(@RequestBody ListEntry li, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        li.setUserCode(user.getCd());

        ListCard si = favoritesListService.addStoreToList(li);

        if (si != null) {
            li.setMessage(WebConstants.SUCCESS);
        } else {
            li.setMessage(WebConstants.FAILURE);
        }

        return li;
    }

    /**
     * Method description
     *
     * @param li li
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/list/owner", method = RequestMethod.PUT, consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public ListEntry addOwnerToList(@RequestBody ListEntry li, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        li.setUserCode(user.getCd());

        ListCard si = favoritesListService.addOwnerToList(li);

        if (si != null) {
            li.setMessage(WebConstants.SUCCESS);
        } else {
            li.setMessage(WebConstants.FAILURE);
        }

        return li;
    }

    /**
     * Method description
     *
     * @param li li
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/list/branch", method = RequestMethod.PUT, consumes = "application/json",
            produces = "application/json")
    @ResponseBody
    public ListEntry addBranchToList(@RequestBody ListEntry li, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        li.setUserCode(user.getCd());

        ListCard si = favoritesListService.addBranchToList(li);

        if (si != null) {
            li.setMessage(WebConstants.SUCCESS);
        } else {
            li.setMessage(WebConstants.FAILURE);
        }

        return li;
    }

    /**
     * Generic ListCard deletion method
     *
     * @param li li
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/card/delete", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ListEntry deleteListCard(@RequestBody ListEntry li, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        li.setUserCode(user.getCd());
        favoritesListService.deleteListCard(li);

        return li;
    }

    /**
     * Adds / Edits a note on a ListCard
     *
     * @param note note
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/note", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Note saveNoteToListCard(@PathVariable("urlName") String urlName,
                                   @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                   @RequestBody Note note, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        favoritesListService.addNoteToListCard(user.getCd(), boardCode, urlName, note);

        return note;
    }

    /**
     * Removes a note on a ListCard
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/note/{noteCode}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteNoteFromListCard(@PathVariable("urlName") String urlName,
                                         @PathVariable("noteCode") String noteCode,
                                         @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                         HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        favoritesListService.deleteNoteFromListCard(user.getCd(), boardCode, urlName, noteCode);

        // this is not an accurate representation of whether it was removed or not
        return WebConstants.SUCCESS;
    }

    /**
     * Shows the note tile
     *
     * @param boardCode boardCode
     * @param urlName   urlName
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/note", method = RequestMethod.GET)
    public String showNoteDetails(@PathVariable("urlName") String urlName,
                                  @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                  HttpSession session, Model model, Locale locale) throws Exception {
        showUserListCard(boardCode, null, urlName, session, model, locale);

        return "user.list.card.note";
    }

    /**
     * Adds / Edits a comment on a ListCard
     *
     * @param comment comment
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/comment", method = RequestMethod.POST)
    public String saveCommentToListCard(@PathVariable("urlName") String urlName,
                                        @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                        @Valid Comment comment, HttpSession session, Model model) throws Exception {
        User commentingUser = retrieveUserFromPrincipalOrSession(session);

        favoritesListService.addCommentToListCard(comment.getWcd(), boardCode, urlName, commentingUser, comment);

        model.addAttribute(WebConstants.COMMENT, comment);
        model.addAttribute(WebConstants.BOARD_CODE, boardCode);

        return "user.list.card.comment";
    }

    /**
     * Removes a comment on a ListCard
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{userCode}/{urlName}/comment/{commentCode}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteCommentFromListCard(@PathVariable("userCode") String userCode,
                                            @PathVariable("urlName") String urlName,
                                            @PathVariable("commentCode") String commentCode,
                                            @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode) throws Exception {

        favoritesListService.deleteCommentFromListCard(userCode, boardCode, urlName, commentCode);

        // this is not an accurate representation of whether it was removed or not
        return WebConstants.SUCCESS;
    }

    /**
     * Adds / Edits a review on a ListCard
     *
     * @param review review
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/review", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Review saveReviewToListCard(@PathVariable("urlName") String urlName,
                                       @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                       @RequestBody Review review, HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        favoritesListService.addReviewToListCard(user.getCd(), boardCode, urlName, review);

        return review;
    }

    /**
     * Removes a review on a ListCard
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/review/{reviewCode}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteReviewFromListCard(@PathVariable("urlName") String urlName,
                                           @PathVariable("reviewCode") String reviewCode,
                                           @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                           HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        favoritesListService.deleteReviewFromListCard(user.getCd(), boardCode, urlName, reviewCode);

        // this is not an accurate representation of whether it was removed or not
        return WebConstants.SUCCESS;
    }

    /**
     * Shows the note tile
     *
     * @param boardCode boardCode
     * @param urlName   urlName
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/review", method = RequestMethod.GET)
    public String showReviewDetails(@PathVariable("urlName") String urlName,
                                    @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                    HttpSession session, Model model, Locale locale) throws Exception {
        showUserListCard(boardCode, null, urlName, session, model, locale);

        return "user.list.card.review";
    }

    @RequestMapping(value = "/user/list/{urlName}/alert", method = RequestMethod.POST)
    public String saveAlertToListCard(@PathVariable("urlName") String urlName,
                                      @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                      @Valid Alert alert, BindingResult errors, HttpSession session,
                                      Locale locale, Model model, SessionStatus status) throws Exception {

        new ListCardAlertValidator().validate(alert, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.ALERT, alert);
            showAlertDetails(urlName, boardCode, session, model, locale);
        } else {
            User user = retrieveUserFromPrincipalOrSession(session);

            // add the alert to the user object
            alert = favoritesListService.addAlertToListCard(user.getCd(), boardCode, urlName, alert);
            model.addAttribute(WebConstants.ALERT, alert);
            String message = messageSource.getMessage("task.alert.saved", null, locale);
            model.addAttribute(WebConstants.MESSAGE, message);

            // load up list card
            showUserListCard(boardCode, user.getCd(), urlName, session, model, locale);
            status.setComplete();
        }

        model.addAttribute(WebConstants.BOARD_CODE, boardCode);

        return "user.list.card.alert";
    }

    @RequestMapping(value = "/user/list/create", method = RequestMethod.GET)
    public String showListCardBoardForm(@RequestParam(value = WebConstants.BOARD_CODE, required = false) String boardCode,
                                        HttpSession session, Model model, Locale locale) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        if (StringUtils.isBlank(boardCode)) {
            model.addAttribute(WebConstants.LIST_CARD_BOARD, new ListCardBoard());
        } else {
            ListCardBoard lcb = favoritesListService.findListCardBoard(user.getCd(), boardCode);
            if (StringUtils.equals(lcb.getNm(), ApplicationConstants.DEFAULT_BOARD_NAME)) {
                // we want to rename this to a family friendly list
                lcb.setNm(messageSource.getMessage(ApplicationConstants.DEFAULT_BOARD_NAME, null, locale));
            }
            model.addAttribute(WebConstants.LIST_CARD_BOARD, lcb);
        }

        model.addAttribute(WebConstants.BOARD_CODE, boardCode);

        return "user.list.create";
    }

    @RequestMapping(value = "/user/list/create", method = RequestMethod.POST)
    public String saveListCardBoard(@Valid ListCardBoard lcb, BindingResult errors, HttpSession session,
                                    Locale locale, Model model, SessionStatus status) throws Exception {

        new ListCardBoardValidator().validate(lcb, errors);

        if (errors.hasErrors()) {
            model.addAttribute(WebConstants.LIST_CARD_BOARD, lcb);
        } else {
            User user = retrieveUserFromPrincipalOrSession(session);

            // add the new board to the UserSupplement object
            lcb = favoritesListService.addListCardBoard(user.getCd(), lcb);
            model.addAttribute(WebConstants.LIST_CARD_BOARD, lcb);
            String message = messageSource.getMessage("task.board.saved", null, locale);
            model.addAttribute(WebConstants.MESSAGE, message);

            // finish process
            status.setComplete();
        }

        return "user.list.create";
    }

    /**
     * Removes a alert on a ListCard
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/alert", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deleteAlertFromListCard(@PathVariable("urlName") String urlName,
                                          @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                          HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        favoritesListService.deleteAlertFromListCard(user.getCd(), boardCode, urlName);

        // this is not an accurate representation of whether it was removed or not
        return WebConstants.SUCCESS;
    }

    /**
     * Shows the note tile
     *
     * @param boardCode boardCode
     * @param urlName   urlName
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/alert", method = RequestMethod.GET)
    public String showAlertDetails(@PathVariable("urlName") String urlName,
                                   @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                   HttpSession session, Model model, Locale locale) throws Exception {
        showUserListCard(boardCode, null, urlName, session, model, locale);

        if (model.asMap().containsKey(WebConstants.LIST_CARD)) {

            // if the model already contains an alert it means it's coming from the saveAlertToListCard method
            if (!model.asMap().containsKey(WebConstants.ALERT)) {
                ListCard lc = (ListCard) model.asMap().get(WebConstants.LIST_CARD);
                if (lc.getLrt() != null) {
                    model.addAttribute(WebConstants.ALERT, lc.getLrt());
                } else {
                    User user = retrieveUserFromPrincipalOrSession(session);
                    UserSupplement us = userService.findUserSupplement(user.getCd());
                    Alert alert = new Alert();

                    if (us != null) {
                        alert.setPhn(us.getPhn());
                        alert.setMl(user.getMl());
                    }
                    model.addAttribute(WebConstants.ALERT, alert);
                }
            }

        }
        model.addAttribute(WebConstants.BOARD_CODE, boardCode);
        return "user.list.card.alert";
    }

    @RequestMapping(value = "/user/list/{urlName}/picture", method = RequestMethod.POST)
    public String savePictureToListCard(@PathVariable("urlName") String urlName,
                                        @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                        ListCardPicture picture, BindingResult errors, HttpSession session, Model model, SessionStatus status, Locale locale) throws Exception {

        User user = retrieveUserFromPrincipalOrSession(session);

        // validate picture
        new ListCardImageValidator().validate(picture, errors);
        model.addAttribute(WebConstants.ERROR, true);

        if (!errors.hasErrors()) {
            // add the picture to the user object
            model.addAttribute(WebConstants.ERROR, false);
            favoritesListService.addPictureToListCard(user.getCd(), boardCode, urlName, picture);
            status.setComplete();
        }

        return showPictureUploadDetails(urlName, boardCode, session, model, locale);
    }

    /**
     * Removes a picture on a ListCard
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/picture/{pictureCode}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public String deletePictureFromListCard(@PathVariable("urlName") String urlName,
                                            @PathVariable("pictureCode") String pictureCode,
                                            @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                            HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        favoritesListService.deletePictureFromListCard(user.getCd(), boardCode, urlName, pictureCode);

        // this is not an accurate representation of whether it was removed or not
        return WebConstants.SUCCESS;
    }

    /**
     * Shows the picture tile
     *
     * @param boardCode boardCode
     * @param urlName   urlName
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/picture", method = RequestMethod.GET)
    public String showPictureUploadDetails(@PathVariable("urlName") String urlName,
                                           @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                           HttpSession session, Model model, Locale locale) throws Exception {
        if (!model.containsAttribute(WebConstants.LIST_CARD_PICTURE)) {
            model.addAttribute(WebConstants.LIST_CARD_PICTURE, new ListCardPicture());
        }

        showUserListCard(boardCode, null, urlName, session, model, locale);

        return "user.list.card.picture";
    }

    /**
     * Fetches all images from a given url
     *
     * @return Return value
     * @throws Exception Exception
     */
    @RequestMapping(value = "/user/list/picture/ext", method = RequestMethod.POST)
    public String fetchExternalImages(@RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                      @RequestBody ExternalImageRequest request, Model model) throws Exception {

        model.addAttribute(WebConstants.EXTERNAL_IMAGES, favoritesListService.fetchExternalImages(request));
        model.addAttribute(WebConstants.BOARD_CODE, boardCode);
        return "user.list.external.images";
    }

    /**
     * Assigns an external image url
     *
     * @param urlName   urlName
     * @param boardCode boardCode
     * @param picture   picture
     * @param errors    errors
     * @param session   session
     * @param model     model
     * @param status    status
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/user/list/{urlName}/picture/ext", method = RequestMethod.POST)
    public String saveExternalPictureToListCard(@PathVariable("urlName") String urlName,
                                                @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                                @Valid ExternalListCardPicture picture, BindingResult errors, HttpSession session, Model model, SessionStatus status, Locale locale) throws Exception {

        User user = retrieveUserFromPrincipalOrSession(session);

        if (!errors.hasErrors()) {
            // add the picture to the user object
            favoritesListService.addPictureToListCard(user.getCd(), boardCode, urlName, picture);
            status.setComplete();
        }

        return showPictureUploadDetails(urlName, boardCode, session, model, locale);
    }

    @RequestMapping(value = "/user/list/{urlName}/rename", method = RequestMethod.POST)
    public String renameListCard(@PathVariable("urlName") String urlName,
                                 @RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                 @Valid RenameListCard rename, BindingResult errors,
                                 HttpSession session, Model model,
                                 SessionStatus status, Locale locale) throws Exception {

        User user = retrieveUserFromPrincipalOrSession(session);

        if (!errors.hasErrors()) {
            // add the picture to the user object
            favoritesListService.renameListCard(user.getCd(), boardCode, urlName, rename);
            status.setComplete();
        }

        return showPictureUploadDetails(urlName, boardCode, session, model, locale);
    }

    /**
     * Shows the form to create a new external list card
     *
     * @param boardCode boardCode
     * @param model     model
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/user/list/external", method = RequestMethod.GET)
    public String showNewListCardForm(@RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                      Model model) throws Exception {
        model.addAttribute(WebConstants.EXTERNAL_LIST_CARD, new ExternalListCard());
        model.addAttribute(WebConstants.BOARD_CODE, boardCode);
        return "user.list.external.form";
    }

    /**
     * Saves an external list card to user's list
     *
     * @param boardCode        boardCode
     * @param externalListCard externalListCard
     * @param errors           errors
     * @param status           status
     * @param model            model
     * @param locale           locale
     * @return Tile def
     * @throws Exception
     */
    @RequestMapping(value = "/user/list/external", method = RequestMethod.POST)
    public String saveExternalListCard(@RequestParam(value = WebConstants.BOARD_CODE, required = false, defaultValue = ApplicationConstants.DEFAULT_BOARD_NAME) String boardCode,
                                       @Valid ExternalListCard externalListCard, BindingResult errors,
                                       SessionStatus status, Model model, HttpSession session,
                                       Locale locale) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        externalListCard.setUserCode(user.getCd());
        ListCard lc = favoritesListService.addExternalCardToList(externalListCard);

        if (lc != null) {
            // put the newly created list card in the request
            model.addAttribute(WebConstants.LIST_CARD, lc);
            externalListCard.setMessage(WebConstants.SUCCESS);

            // add success message
            String message = messageSource.getMessage("list.card.external.saved", new String[]{externalListCard.getName()}, locale);
            model.addAttribute(WebConstants.MESSAGE, message);

            // clear the session
            status.setComplete();

            // bind a new form object
            showNewListCardForm(boardCode, model);
        } else {
            externalListCard.setMessage(WebConstants.FAILURE);
        }

        model.addAttribute(WebConstants.BOARD_CODE, boardCode);
        return "user.list.external.form";
    }

    @RequestMapping(value = "/user/list/names", method = RequestMethod.GET)
    public String showListCardBoardNames(HttpSession session, Model model, Locale locale) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        List<ListCardBoardName> names = favoritesListService.findListCardBoardNames(user.getCd());

        if (names != null && !names.isEmpty()) {
            for (ListCardBoardName name : names) {
                if (StringUtils.equals(name.getNm(), ApplicationConstants.DEFAULT_BOARD_NAME)) {
                    name.setNm(messageSource.getMessage(ApplicationConstants.DEFAULT_BOARD_NAME, null, locale));
                }
            }
        }
        model.addAttribute(WebConstants.LIST_CARD_BOARD_NAMES, names);

        return "user.list.names";
    }

    @RequestMapping(value = "/user/list/{boardCode}", method = RequestMethod.DELETE,
            produces = "application/json")
    @ResponseBody
    public String deleteListCardBoard(@PathVariable(WebConstants.BOARD_CODE) String boardCode,
                                                       HttpSession session) throws Exception {
        User user = retrieveUserFromPrincipalOrSession(session);

        favoritesListService.deleteListCardBoard(user.getCd(), boardCode);

        return WebConstants.SUCCESS;
    }
}
