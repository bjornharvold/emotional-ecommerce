/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.service;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lela.commons.event.*;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.Application;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Sale;
import com.lela.domain.document.User;
import com.lela.domain.document.UserEvent;
import com.lela.domain.document.UserSupplement;
import com.lela.domain.document.UserTracker;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.user.Address;
import com.lela.domain.enums.AuthenticationType;
import com.lela.domain.enums.Gender;
import com.lela.domain.enums.RegistrationType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Chris Tallent
 * Date: 10/22/12
 * Time: 9:32 AM
 */
public interface MixpanelService {
    boolean isEnabled(HttpServletRequest request);
    String getMixpanelKey();
    void registerUser(RegistrationEvent event);
    void deleteUser(DeleteUserEvent event);
    void setGender(SetGenderEvent event);
    void setAge(SetAgeEvent event);
    void setAddress(SetAddressEvent event);
    void subscribe(SubscribeEvent event);
    void unsubscribe(UnsubscribeEvent event);
    void trackVisit(UserVisitEvent event);
    void trackPageView(PageViewEvent event);
    void viewedCategory(ViewedCategoryEvent event);
    void filteredCategoryResults(FunctionalFilterEvent event);
    void sortedCategory(SortEvent event);
    void viewedDepartment(ViewedDepartmentEvent event);
    void viewedItem(ViewedItemEvent event);
    void compareItems(CompareItemsEvent event);
    void loginUser(LoginEvent event);
    void motivators(MotivatorEvent event);
    void quizStarted(QuizStartEvent event);
    void quizFinished(QuizFinishEvent event);
    void quizStepFinished(QuizStepFinishEvent event);
    void quizAnswers(QuizAnswersEvent event);
    void purchase(PurchaseEvent event);
    void eventParticipant(EventParticipantEvent event);
    void addedItemToList(AddItemToListEvent event);
    void removeItemFromList(RemoveItemFromListEvent event);
//    void saveAddress(String userCode, Address address);
//    void saveAge(String userCode, Integer age);
//    void trackSale(Sale sale, UserTracker userTracker);
}
