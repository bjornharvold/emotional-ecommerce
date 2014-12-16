/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ComputedCategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.LocalCacheEvictionService;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.service.ProfileService;
import com.lela.commons.service.QuizService;
import com.lela.commons.service.UserService;
import com.lela.commons.spring.mobile.MockDevice;
import com.lela.commons.spring.mobile.WurflDevice;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.*;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.Principal;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.enums.QuestionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Bjorn Harvold
 * Date: 3/3/12
 * Time: 1:30 AM
 * Responsibility: This will run a complete test of the computing of user / product
 * matches and how they are persisted, updated and removed
 */
public class ComputedCategoriesFunctionalTest extends AbstractFunctionalTest {
    private final static Logger log = LoggerFactory.getLogger(ComputedCategoriesFunctionalTest.class);
    private static final String QUESTION_URL_NAME = "computedcategorytestquestion";
    private static final String CATEGORY_URL_NAME = "computedcategorytestcategory";
    private static final String ITEM_URL_NAME = "computedcategorytestitem";

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ComputedCategoryService computedCategoryService;

    @Autowired
    private LocalCacheEvictionService localCacheEvictionService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private ProductEngineService productEngineService;

    private User user;
    private Question question;
    private Category category;
    private Item item;
    private ComputedCategory cc;

    @Before
    public void setUp() {
        try {
            SpringSecurityHelper.secureChannel();

            log.info("Creating test user...");
            user = createRandomUser(true);
            user = profileService.registerTestUser(user);

            assertNotNull("User did not get saved correctly", user.getId());
            log.info("Test user created successfully");

            log.info("Creating a test question...");
            question = new Question();
            question.setNm(QUESTION_URL_NAME);
            question.setRlnm(QUESTION_URL_NAME);
            question.setTp(QuestionType.IMAGE_MULTIPLE_CHOICE_SINGLE_ANSWER);
            List<Answer> answers = new ArrayList<Answer>();
            Answer answer = new Answer();
            answer.setKy("A");
            answer.setNm("WTF");
            Map<String, Integer> answerMotivators = new HashMap<String, Integer>();
            answerMotivators.put("A", 5);
            answerMotivators.put("B", 7);
            answerMotivators.put("C", 2);
            answerMotivators.put("D", 1);
            answerMotivators.put("E", 3);
            answerMotivators.put("F", 3);
            answerMotivators.put("G", 3);
            answerMotivators.put("H", 7);
            answerMotivators.put("I", 6);
            answerMotivators.put("J", 9);
            answerMotivators.put("K", 6);
            answerMotivators.put("L", 2);
            answer.setMtvtrs(answerMotivators);
            answers.add(answer);
            question.setNswrs(answers);

            question = quizService.saveQuestion(question);

            log.info("Let's also create a test category with a test product in it");
            category = new Category();
            category.setRlnm(CATEGORY_URL_NAME);
            category.setSrlnm(CATEGORY_URL_NAME);
            category.setNm(CATEGORY_URL_NAME);

            category = categoryService.saveCategory(category);
            assertNotNull("Category did not get saved correctly", category.getId());

            item = new Item();
            item.setNm(ITEM_URL_NAME);
            item.setRlnm(ITEM_URL_NAME);
            item.setCtgry(category);
            item.setMtvtrs(answerMotivators);
            item = itemService.saveItem(item);
            assertNotNull("Item did not get saved correctly", item.getId());

            SpringSecurityHelper.unsecureChannel();

            assertNotNull("Question did not get saved correctly", question.getId());

            log.info("Set this newly created user in the security context");
            Principal principal = new Principal(user);
            SpringSecurityHelper.secureChannel(principal);
            log.info("Test question created successfully");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }

    @After
    public void tearDown() {
        log.info("Unsecuring channel from test user and securing with admin user with more privileges");
        SpringSecurityHelper.unsecureChannel();

        log.info("Removing test user");
        SpringSecurityHelper.secureChannel();

        if (question != null) {
            quizService.removeQuestion(question.getRlnm());
            localCacheEvictionService.evictQuestion(question.getRlnm());

            question = quizService.findQuestionByUrlName(question.getRlnm());
            assertNull("Question did not get deleted from the db", question);
        }

        if (category != null) {
            categoryService.removeCategory(category.getRlnm());
            localCacheEvictionService.evictCategory(category.getRlnm());

            category = categoryService.findCategoryByUrlName(category.getRlnm());
            assertNull("Category did not get deleted from the db", category);
        }

        if (cc != null) {
            computedCategoryService.removeComputedCategory(user.getCd(), CATEGORY_URL_NAME);
            localCacheEvictionService.evictComputedCategory(user.getCd() + "|" + CATEGORY_URL_NAME);

            cc = computedCategoryService.findComputedCategory(user.getCd(), CATEGORY_URL_NAME);
            assertNull("ComputedCategory did not get deleted from the db", cc);
        }

        if (item != null) {
            itemService.removeItem(item.getRlnm());
            localCacheEvictionService.evictItem(item.getRlnm() + "|" + item.getCtgry().getRlnm());

            item = itemService.findItemByUrlName(item.getRlnm());
            assertNull("Item did not get deleted from the db", item);
        }

        if (user != null) {
            userService.removeUser(user);

            user = userService.findUser(user.getId());
            assertNull("User did not get deleted from the db", user);
        }

        SpringSecurityHelper.unsecureChannel();

        log.info("Test user was removed successfully");
    }

    @Test
    public void testComputedCategories() {
        MockHttpSession session = new MockHttpSession();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        WebRequest webRequest = new DispatcherServletWebRequest(request, response);
        Model model = new BindingAwareModelMap();
        log.info("Testing how computed categories work from end to end...");
        log.info("We currently are testing for successes only here. We are not trying to produce any errors.");
        log.info("A newly created user will have no pre-computed categories");
        cc = computedCategoryService.findComputedCategory(user.getCd(), CATEGORY_URL_NAME);
        assertNull("User has pre-computed categories", cc);

        log.info("A newly created user also doesn't have any motivator values on it");
        Motivator motivator = userService.findBestMotivator(user.getCd());
        assertNull("Motivators isn't null", motivator);

        log.info("User will now answer a question and have motivator values created for this user");

        QuizAnswers answers = new QuizAnswers();
        List<QuizAnswer> quizAnswers = new ArrayList<QuizAnswer>(1);
        QuizAnswer answer = new QuizAnswer();
        answer.setAnswerKey("A");
        answer.setQuestionUrlName(QUESTION_URL_NAME);
        quizAnswers.add(answer);
        answers.setList(quizAnswers);

        try {
            productEngineService.answerQuestions(user.getCd(), answers);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        motivator = userService.findBestMotivator(user.getCd());
        assertNotNull("User is missing motivators even after having answered a question", motivator);

        log.info("Let's put these motivators to good use and have the user view the product details page for category: " + CATEGORY_URL_NAME);

        CategoryItemsQuery ciq = new CategoryItemsQuery();
        ciq.setCategoryUrlName(CATEGORY_URL_NAME);
        ciq.setPage(0);
        ciq.setSize(1);
        ciq.setUserCode(user.getCd());

        ItemPage<RelevantItem> relevantItems = productEngineService.findRelevantItemsByCategory(ciq);

        log.info("Successfully loaded the category page for our desired category");

        log.info("At this point the user should have a computed category entry for the category he just viewed");

        cc = computedCategoryService.findComputedCategory(user.getCd(), CATEGORY_URL_NAME);
        assertNotNull("There should be a computed category for category: " + CATEGORY_URL_NAME, cc);
        assertFalse("The computed category should not be dirty at this point", cc.getDrty());

        log.info("The computed categories are good");

        log.info("Now the user answers another question...");
        try {
            productEngineService.answerQuestions(user.getCd(), answers);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        cc = computedCategoryService.findComputedCategory(user.getCd(), CATEGORY_URL_NAME);
        assertNotNull("There should be a computed category for category: " + CATEGORY_URL_NAME, cc);
        assertTrue("The computed category should now be dirty because the user answered a new question", cc.getDrty());

        log.info("The computed category was indeed dirty. It will get updated again the next time the user views the product grid.");

        log.info("Loading the same category for the second time to re-compute motivators");
        relevantItems = productEngineService.findRelevantItemsByCategory(ciq);

        log.info("Successfully loaded the category page for our desired category");

        log.info("At this point the user should have a computed category entry for the category he just viewed");

        cc = computedCategoryService.findComputedCategory(user.getCd(), CATEGORY_URL_NAME);
        assertNotNull("There should be a computed category for category: " + CATEGORY_URL_NAME, cc);
        assertFalse("The computed category should not be dirty at this point", cc.getDrty());
        Date date1 = cc.getDt();

        log.info("The computed categories are good");

        log.info("Now the ingestion service updates the item, which should again set the dirty flag to true");
        log.info("We can't test that fully, so instead we will just use the method calls that makes that happen");

        // set user db values to dirty
        computedCategoryService.setComputedCategoryToDirty(user.getCd(), CATEGORY_URL_NAME);

        // refresh the category
        categoryService.refreshCategory(CATEGORY_URL_NAME);

        log.info("Loading the same category for the third time to re-compute motivators");
        relevantItems = productEngineService.findRelevantItemsByCategory(ciq);

        cc = computedCategoryService.findComputedCategory(user.getCd(), CATEGORY_URL_NAME);
        assertNotNull("There should be a computed category for category: " + CATEGORY_URL_NAME, cc);
        assertFalse("The computed category should now be dirty because the user answered a new question", cc.getDrty());
        Date date2 = cc.getDt();
        assertTrue("The second date should be after the first date", date2.after(date1));

        log.info("The computed category was indeed dirty");

        log.info("Tested how computed categories work from end to end SUCCESSFULLY");
    }
}
