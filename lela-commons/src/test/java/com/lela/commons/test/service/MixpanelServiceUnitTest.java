package com.lela.commons.test.service;

import com.google.common.collect.Lists;
import com.lela.commons.event.*;
import com.lela.commons.service.*;
import com.lela.commons.service.impl.MixpanelServiceImpl;
import com.lela.domain.document.*;
import com.lela.domain.dto.AffiliateIdentifiers;
import com.lela.domain.dto.RelevantItemQuery;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.dto.user.Address;
import com.lela.domain.enums.*;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.mixpanel.java.mpmetrics.MPConfig;
import com.mixpanel.java.mpmetrics.MPMetrics;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/5/12
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MPMetrics.class)
public class MixpanelServiceUnitTest {


    MixpanelService mixpanelService = new MixpanelServiceImpl();

    private User user;
    private UserSupplement userSupplement;
    private AffiliateIdentifiers affiliateIdentifiers;

    @Mock MPMetrics mpMetrics;

    @Mock UserService userService;

    @Mock UserTrackerService userTrackerService;

    @Mock
    private ProductEngineService productEngineService;

    @Mock
    private ItemService itemService;

    @Mock
    private CategoryService categoryService;

    @Mock
    LookupService lookupService;

    @Before
    public void setUp() {
        String remoteAddress = new String("127.0.0.1");
        Location location = new Location();
        location.countryCode = "us";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(remoteAddress);
        request.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        ReflectionTestUtils.setField(mixpanelService, "lookupService", lookupService);

        when(lookupService.getLocation(remoteAddress)).thenReturn(location);


        user = new User();
        user.setCd("user-code");

        userSupplement = new UserSupplement();
        userSupplement.setFnm("Jimmy");
        userSupplement.setLnm("Page");
        userSupplement.setMl("jimmy@ledzeppelin.edu");
        userSupplement.setGndr(Gender.MALE);


        affiliateIdentifiers = new AffiliateIdentifiers();
        affiliateIdentifiers.setFfltccntrlnm("affiliateUrlName");
        affiliateIdentifiers.setCmpgnrlnm("campaignUrlName");
        affiliateIdentifiers.setRfrrlnm("referrerUrlName");

        PowerMockito.mockStatic(MPMetrics.class);

        MPConfig mpConfig = new MPConfig(null, null, null);

        Mockito.when(MPMetrics.getInstance(mpConfig)).thenReturn(mpMetrics);
        Mockito.when(MPMetrics.getInstance(eq(mpConfig), anyBoolean())).thenReturn(mpMetrics);

        ReflectionTestUtils.setField(mixpanelService, "enabled", Boolean.TRUE);
        ReflectionTestUtils.setField(mixpanelService, "userService", userService);
        ReflectionTestUtils.setField(mixpanelService, "userTrackerService", userTrackerService);
        ReflectionTestUtils.setField(mixpanelService, "productEngineService", productEngineService);
        ReflectionTestUtils.setField(mixpanelService, "itemService", itemService);
        ReflectionTestUtils.setField(mixpanelService, "categoryService", categoryService);
        ReflectionTestUtils.setField(mixpanelService, "mpConfig", mpConfig);
    }

    @Test
    public void registerUser()
    {
        RegistrationEvent event = new RegistrationEvent(user, userSupplement, RegistrationType.WEBSITE);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.registerUser(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.SIGNUP.getValue()), trackArgument.capture());
        verify(mpMetrics).set(setArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);

        Map<String, Object> setArgumentMap = setArgument.getValue();
        assertEquals("$first_name not set", "Jimmy", setArgumentMap.get(MixpanelPeoplePropertyType.FIRST_NAME.getValue()));
        assertEquals("$last_name not set", "Page", setArgumentMap.get(MixpanelPeoplePropertyType.LAST_NAME.getValue()));
        assertEquals("$full_name not set", "Jimmy Page", setArgumentMap.get(MixpanelPeoplePropertyType.FULL_NAME.getValue()));
        assertEquals("Gender not set", Gender.MALE, setArgumentMap.get(MixpanelPeoplePropertyType.GENDER.getValue()));
        assertEquals("$email not set", "jimmy@ledzeppelin.edu", setArgumentMap.get(MixpanelPeoplePropertyType.EMAIL.getValue()));
        assertEquals("$username not set", "jimmy@ledzeppelin.edu", setArgumentMap.get(MixpanelPeoplePropertyType.USERNAME.getValue()));
        assertEquals("Email Opt In not set", false, setArgumentMap.get(MixpanelPeoplePropertyType.EMAIL_OPT_IN.getValue()));
    }

    @Test
    public void deleteUser()
    {
        DeleteUserEvent event = new DeleteUserEvent(user);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.deleteUser(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.DELETE_USER.getValue()), trackArgument.capture());
        verify(mpMetrics).set(setArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();
        assertGlobalProperties(trackArgumentMap);

        Map<String, Object> setArgumentMap = setArgument.getValue();
        assertTrue("Deleted User not set", Boolean.valueOf(setArgumentMap.get(MixpanelPeoplePropertyType.DELETED_USER.getValue()).toString()));
    }

    @Test
    public void subscribe()
    {
        String listId = "listId";
        SubscribeEvent event = new SubscribeEvent(user, listId);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.subscribe(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.SUBSCRIBE.getValue()), trackArgument.capture());
        verify(mpMetrics).set(setArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Email List Id not set", listId, trackArgumentMap.get(MixpanelEngagementPropertyType.EMAIL_LIST_ID.getValue()));

        Map<String, Object> setArgumentMap = setArgument.getValue();
        assertTrue("Email Opt In not set", Boolean.valueOf(setArgumentMap.get(MixpanelPeoplePropertyType.EMAIL_OPT_IN.getValue()).toString()));

    }

    @Test
    public void unsubscribe()
    {
        String listId = "listId";
        UnsubscribeEvent event = new UnsubscribeEvent(user, listId);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.unsubscribe(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.UNSUBSCRIBE.getValue()), trackArgument.capture());
        verify(mpMetrics).set(setArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Email List Id not set", listId, trackArgumentMap.get(MixpanelEngagementPropertyType.EMAIL_LIST_ID.getValue()));

        Map<String, Object> setArgumentMap = setArgument.getValue();
        assertFalse("Email Opt In not set", Boolean.valueOf(setArgumentMap.get(MixpanelPeoplePropertyType.EMAIL_OPT_IN.getValue()).toString()));

    }

    @Test
    public void trackVisit()
    {
        UserVisit userVisit = new UserVisit();

        UserVisitEvent event = new UserVisitEvent(user, userVisit);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.trackVisit(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.VISIT.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
    }

    @Test
    public void trackPageView()
    {
        PageViewEvent event = new PageViewEvent(user);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.trackPageView(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.PAGE_VIEW.getValue()), trackArgument.capture());
        verify(mpMetrics).increment(eq(MixpanelPeoplePropertyType.PAGE_VIEW_TOTAL.getValue()), eq(1l));

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        //assertGlobalProperties(trackArgumentMap);
        assertEquals("Country Code not set", "us", trackArgumentMap.get(MixpanelEngagementPropertyType.COUNTRY_CODE.getValue()));
        assertEquals("Distinct Id not set", "user-code", trackArgumentMap.get(MixpanelEngagementPropertyType.DISTINCT_ID.getValue()));
        assertEquals("Streams Name not set", "Jimmy Page", trackArgumentMap.get(MixpanelEngagementPropertyType.STREAMS_NAME.getValue()));

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void viewedCategory()
    {
        String categoryUrlName = "category";
        ViewedCategoryEvent event = new ViewedCategoryEvent(user, categoryUrlName);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.viewedCategory(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.VIEWED_CATEGORY.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
    }

    @Test
    public void viewedDepartment()
    {
        String departmentUrlName = "departmentUrlName";
        ViewedDepartmentEvent event = new ViewedDepartmentEvent(user, departmentUrlName);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.viewedDepartment(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.VIEWED_DEPARTMENT.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
    }

    @Test
    public void viewedItem()
    {
        String itemUrlName = "itemUrlName";
        ViewedItemEvent event = new ViewedItemEvent(user, itemUrlName, ViewedItemEvent.ItemViewType.DETAILS);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.viewedItem(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.VIEWED_ITEM.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Item Id not set", "itemUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.ITEM_ID.getValue()));
        assertEquals("Distinct Id not set", ViewedItemEvent.ItemViewType.DETAILS, trackArgumentMap.get(MixpanelEngagementPropertyType.ITEM_VIEW_TYPE.getValue()));
    }

    @Test
    public void compareItems()
    {
        String[] itemUrlNames = new String[]{"itemUrlName1", "itemUrlName2"};
        CompareItemsEvent event = new CompareItemsEvent(user, itemUrlNames);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.compareItems(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.COMPARED_ITEMS.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Country Code not set", itemUrlNames, trackArgumentMap.get(MixpanelEngagementPropertyType.COMPARED_ITEMS.getValue()));
    }

    @Test
    public void loginUser()
    {
        LoginEvent event = new LoginEvent(user, AuthenticationType.USERNAME_PASSWORD);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.loginUser(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.LOGIN.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Authentication Type not set", AuthenticationType.USERNAME_PASSWORD, trackArgumentMap.get(MixpanelEngagementPropertyType.AUTHENTICATION_TYPE.getValue()));
    }

    @Test
    public void motivators()
    {
        //MotivatorEvent event
        Map<String, Integer> motivators = new HashMap<String, Integer>();
        motivators.put("A",0);
        motivators.put("B",1);

        Motivator motivator = new Motivator();
        motivator.setMtvtrs(motivators);
        motivator.setTp(MotivatorSource.QUIZ);

        MotivatorEvent event = new MotivatorEvent(user, motivator);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.motivators(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.MOTIVATORS.getValue()), trackArgument.capture());
        verify(mpMetrics).set(setArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Motivator Source not set", MotivatorSource.QUIZ.name(), trackArgumentMap.get(MixpanelEngagementPropertyType.MOTIVATOR_SOURCE.getValue()));
        assertEquals("Motivator A not set", 0, trackArgumentMap.get("Motivator A"));
        assertEquals("Motivator B not set", 1, trackArgumentMap.get("Motivator B"));

        Map<String, Object> setArgumentMap = setArgument.getValue();
        assertEquals("Motivator Source not set", MotivatorSource.QUIZ.name(), setArgumentMap.get(MixpanelPeoplePropertyType.MOTIVATOR_SOURCE.getValue()));
        assertEquals("Motivator A not set", 0, setArgumentMap.get("Motivator A"));
        assertEquals("Motivator B not set", 1, setArgumentMap.get("Motivator B"));
    }

    @Test
    public void quizStarted()
    {
        //QuizStartEvent event
        AffiliateAccount affiliateAccount = new AffiliateAccount();
        affiliateAccount.setRlnm("affiliateUrlName");
        Application application = new Application();
        application.setRlnm("applicationUrlName");
        application.setTrlnm("testUrlName");
        QuizStartEvent event = new QuizStartEvent(user, affiliateAccount, application);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.quizStarted(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.QUIZ_START.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Affiliate ID not set", "affiliateUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_ID.getValue()));
        assertEquals("Application ID not set", "applicationUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.APPLICATION_ID.getValue()));
        assertEquals("Quiz ID not set", "testUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.QUIZ_ID.getValue()));


    }

    @Test
    public void quizFinished()
    {
        //QuizFinishEvent event
        AffiliateAccount affiliateAccount = new AffiliateAccount();
        affiliateAccount.setRlnm("affiliateUrlName");
        Application application = new Application();
        application.setRlnm("applicationUrlName");
        application.setTrlnm("testUrlName");
        QuizFinishEvent event = new QuizFinishEvent(user, affiliateAccount, application);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.quizFinished(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.QUIZ_FINISH.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Affiliate Id not set", "affiliateUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_ID.getValue()));
        assertEquals("Application Id not set", "applicationUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.APPLICATION_ID.getValue()));
        assertEquals("Quiz Id not set", "testUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.QUIZ_ID.getValue()));

    }

    @Test
    public void quizStepFinished()
    {

        AffiliateAccount affiliateAccount = new AffiliateAccount();
        affiliateAccount.setRlnm("affiliateUrlName");
        Application application = new Application();
        application.setRlnm("applicationUrlName");
        application.setTrlnm("testUrlName");
        String stepId = "stepId";
        QuizStepFinishEvent event = new QuizStepFinishEvent(user, affiliateAccount, application, stepId);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.quizStepFinished(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.QUIZ_STEP_FINISH.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Affiliate Id not set", "affiliateUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_ID.getValue()));
        assertEquals("Application Id not set", "applicationUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.APPLICATION_ID.getValue()));
        assertEquals("Quiz Id not set", "testUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.QUIZ_ID.getValue()));
        assertEquals("Quiz Step Id not set", "stepId", trackArgumentMap.get(MixpanelEngagementPropertyType.QUIZ_STEP_ID.getValue()));
    }

    @Test
    public void quizAnswers()
    {
        QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setAnswerKey("1");
        quizAnswer.setMessage("message1");
        quizAnswer.setQuestionUrlName("questionUrlName");

        List<QuizAnswer> listOfQuizAnswers = new ArrayList<QuizAnswer>();
        listOfQuizAnswers.add(quizAnswer);

        QuizAnswers quizAnswers = new QuizAnswers();
        quizAnswers.setList(listOfQuizAnswers);
        quizAnswers.setAffiliateId("affiliateUrlName");
        quizAnswers.setApplicationId("applicationUrlName");
        quizAnswers.setQuizUrlName("quizUrlName");

        QuizAnswersEvent event = new QuizAnswersEvent(user, quizAnswers);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.quizAnswers(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.QUIZ_ANSWERS.getValue()), trackArgument.capture());
        verify(mpMetrics).set(setArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);
        assertEquals("Affiliate Id not set", "affiliateUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_ID.getValue()));
        assertEquals("Application Id not set", "applicationUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.APPLICATION_ID.getValue()));
        assertEquals("Quiz Id Id not set", "quizUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.QUIZ_ID.getValue()));
        assertEquals("Question Inofrmation not set", "1", trackArgumentMap.get("Question questionUrlName"));

        Map<String, Object> setArgumentMap = setArgument.getValue();
        assertEquals("Quiz Affilicate Id not set", "affiliateUrlName", setArgumentMap.get(MixpanelPeoplePropertyType.QUIZ_AFFILIATE_ID.getValue()).toString());
        assertEquals("Quiz Application Id not set", "applicationUrlName", setArgumentMap.get(MixpanelPeoplePropertyType.QUIZ_APPLICATION_ID.getValue()).toString());
        assertEquals("Quiz Id not set", "quizUrlName", setArgumentMap.get(MixpanelPeoplePropertyType.QUIZ_ID.getValue()).toString());
        assertEquals("Question Info not set", "1", setArgumentMap.get("Question questionUrlName").toString());
    }

    @Test
    public void setAge()
    {
        // event

        SetAgeEvent event = new SetAgeEvent(user, 37);


        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.setAge(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).set(eq("Age"), eq(37));

    }

    @Test
    public void setGender()
    {
        // event

        SetGenderEvent event = new SetGenderEvent(user, Gender.MALE);


        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.setGender(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).set(eq("Gender"), eq(Gender.MALE));

    }

    @Test
    public void setAddress()
    {
        // event

        Address address = new Address(AddressType.HOME, "Line 1", "Line 2", "City", "State", "Zip");
        SetAddressEvent event = new SetAddressEvent(user, address);


        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.setAddress(event);

        verify(mpMetrics).identify(user.getCd());

        ArgumentCaptor<Map<String, Object>> setArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);
        verify(mpMetrics).set(setArgument.capture());

        Map<String, Object> setArgumentMap = setArgument.getValue();
        assertEquals("City was not set", "City", setArgumentMap.get("Address City"));
        assertEquals("State was not set", "State", setArgumentMap.get("Address State"));
        assertEquals("Zipcode was not set", "Zip", setArgumentMap.get("Address Zipcode"));

    }

    @Test
    public void testAddItemToList() {
        String itemUrlName = "itemUrlName";
        AddItemToListEvent event = new AddItemToListEvent(user.getCd(), itemUrlName);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        Category category = new Category();
        category.setRlnm("categoryUrlName");
        category.setGrps(Lists.newArrayList("group"));
        Item item = new Item();
        item.setRlnm(itemUrlName);
        item.setCtgry(category);
        RelevantItem relevantItem = new RelevantItem();
        relevantItem.setTtlrlvncynmbr(94);

        when(itemService.findItemByUrlName(itemUrlName)).thenReturn(item);
        when(categoryService.findCategoryByUrlName("categoryUrlName")).thenReturn(category);
        when(productEngineService.findRelevantItem(any(RelevantItemQuery.class))).thenReturn(relevantItem);
        when(userService.findUserByCode(user.getCd())).thenReturn(user);
        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.addedItemToList(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.LELA_LIST.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);

        assertEquals("Item Url should be set", itemUrlName, trackArgumentMap.get( MixpanelEngagementPropertyType.ITEM_ID.getValue()));
        assertEquals("Category id should be set", "categoryUrlName", trackArgumentMap.get( MixpanelEngagementPropertyType.CATEGORY_ID.getValue()));
        assertTrue("Department id should be set", ((String[])trackArgumentMap.get( MixpanelEngagementPropertyType.DEPARTMENT_ID.getValue()))[0].equals("group"));
        assertEquals("Compatibility score should be 94", 94, trackArgumentMap.get( MixpanelEngagementPropertyType.COMPATIBILITY_SCORE.getValue()));
        assertEquals("Action should be add", "Add", trackArgumentMap.get(MixpanelEngagementPropertyType.LELA_LIST_ACTION.getValue()));
    }

    @Test
    public void testRemoveItemFromList() {
        String itemUrlName = "itemUrlName";
        RemoveItemFromListEvent event = new RemoveItemFromListEvent(user.getCd(), itemUrlName);

        ArgumentCaptor<Map<String, Object>> trackArgument = (ArgumentCaptor)ArgumentCaptor.forClass(Map.class);

        Category category = new Category();
        category.setRlnm("categoryUrlName");
        category.setGrps(Lists.newArrayList("group"));
        Item item = new Item();
        item.setRlnm(itemUrlName);
        item.setCtgry(category);
        RelevantItem relevantItem = new RelevantItem();
        relevantItem.setTtlrlvncynmbr(94);

        when(itemService.findItemByUrlName(itemUrlName)).thenReturn(item);
        when(categoryService.findCategoryByUrlName("categoryUrlName")).thenReturn(category);
        when(productEngineService.findRelevantItem(any(RelevantItemQuery.class))).thenReturn(relevantItem);
        when(userService.findUserByCode(user.getCd())).thenReturn(user);
        when(userService.findUserSupplement(user.getCd())).thenReturn(userSupplement);
        when(userTrackerService.findAffiliateIdentifiers(user.getCd())).thenReturn(affiliateIdentifiers);

        mixpanelService.removeItemFromList(event);

        verify(mpMetrics).identify(user.getCd());
        verify(mpMetrics).track(eq(MixpanelEventType.LELA_LIST.getValue()), trackArgument.capture());

        Map<String, Object> trackArgumentMap = trackArgument.getValue();

        assertGlobalProperties(trackArgumentMap);

        assertEquals("Item Url should be set", "itemUrlName", trackArgumentMap.get( MixpanelEngagementPropertyType.ITEM_ID.getValue()));
        assertEquals("Category id should be set", "categoryUrlName", trackArgumentMap.get( MixpanelEngagementPropertyType.CATEGORY_ID.getValue()));
        assertTrue("Department id should be set", ((String[])trackArgumentMap.get( MixpanelEngagementPropertyType.DEPARTMENT_ID.getValue()))[0].equals("group"));
        assertEquals("Compatibility score should be 94", 94, trackArgumentMap.get( MixpanelEngagementPropertyType.COMPATIBILITY_SCORE.getValue()));
        assertEquals("Action should be remove", "Remove", trackArgumentMap.get(MixpanelEngagementPropertyType.LELA_LIST_ACTION.getValue()));
    }

    private void assertGlobalProperties(Map<String, Object> trackArgumentMap) {
        assertEquals("First Name not set", "Jimmy", trackArgumentMap.get(MixpanelEngagementPropertyType.FIRST_NAME.getValue()));
        assertEquals("Last Name not set", "Page", trackArgumentMap.get(MixpanelEngagementPropertyType.LAST_NAME.getValue()));
        assertEquals("Full Name not set", "Jimmy Page", trackArgumentMap.get(MixpanelEngagementPropertyType.FULL_NAME.getValue()));
        assertEquals("Gender not set", Gender.MALE, trackArgumentMap.get(MixpanelEngagementPropertyType.GENDER.getValue()));
        assertEquals("Email not set", "jimmy@ledzeppelin.edu", trackArgumentMap.get(MixpanelEngagementPropertyType.EMAIL.getValue()));

        assertEquals("Affiliate ID not set", "affiliateUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_ID.getValue()));
        assertEquals("Campaign ID not set", "campaignUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.CAMPAIGN_ID.getValue()));
        assertEquals("Affiliate Referrer Id not set", "referrerUrlName", trackArgumentMap.get(MixpanelEngagementPropertyType.AFFILIATE_REFERRER_ID.getValue()));
    }
}
