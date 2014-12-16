/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Alert;
import com.lela.domain.document.CategoryGroup;
import com.lela.domain.document.Comment;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.ListCardBoard;
import com.lela.domain.document.Note;
import com.lela.domain.document.Picture;
import com.lela.domain.document.Review;
import com.lela.domain.document.User;
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

import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 9/9/11
 * Time: 11:17 AM
 * Responsibility:
 */
public interface FavoritesListService {
    ListCard addItemToList(ListEntry li);
    ListCard addStoreToList(ListEntry li);
    ListCard addOwnerToList(ListEntry li);
    ListCard addBranchToList(ListEntry li);
    void deleteListCard(ListEntry li);
    UserList fetchUserList(UserListQuery query);
    ListCardBoard findListCardBoard(String userCode, String boardName);
    List<ListCard> findAllListCards(String userCode, ListCardType type);
    ListCard findListCard(String userCode, String boardName, String urlName);
    void reorderUserList(ListPosition dto);
    Note addNoteToListCard(String userCode, String boardName, String urlName, Note note);
    Review addReviewToListCard(String userCode, String boardName, String urlName, Review review);
    Alert addAlertToListCard(String userCode, String boardName, String urlName, Alert alert);
    Picture addPictureToListCard(String userCode, String boardName, String urlName, ListCardPicture picture);
    Picture addPictureToListCard(String userCode, String boardName, String urlName, ExternalListCardPicture picture);

    void deleteNoteFromListCard(String userCode, String boardName, String urlName, String noteCode);

    void deleteReviewFromListCard(String userCode, String boardName, String urlName, String reviewCode);

    void deleteAlertFromListCard(String userCode, String boardName, String urlName);

    void deletePictureFromListCard(String userCode, String boardName, String urlName, String pictureCode);

    Comment addCommentToListCard(String userCode, String boardName, String urlName, User commentingUser, Comment comment);

    void deleteCommentFromListCard(String userCode, String boardName, String urlName, String commentCode);

    DOMElements fetchExternalImages(ExternalImageRequest request);

    ListCard addExternalCardToList(ExternalListCard externalListCard);

    List<Alert> sendValidPriceAlerts(String userCode);

    List<UserList> fetchUserLists(String userCode);

    ListCardBoard addListCardBoard(String userCode, ListCardBoard lcb);

    List<ListCardBoardName> findListCardBoardNames(String userCode);

    ListCard renameListCard(String userCode, String boardCode, String urlName, RenameListCard rename);

    void deleteListCardBoard(String userCode, String boardCode);

    List<CategoryGroup> findTopLevelDepartments(Locale locale);
}
