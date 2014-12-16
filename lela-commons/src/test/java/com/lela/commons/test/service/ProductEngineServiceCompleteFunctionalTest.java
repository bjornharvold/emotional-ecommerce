/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.service;

import com.lela.commons.service.CategoryService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.QuizService;
import com.lela.domain.document.Answer;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.document.Motivator;
import com.lela.domain.document.Question;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.document.User;
import com.lela.domain.dto.CustomPage;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.dto.CategoryItemsQuery;
import com.lela.domain.dto.quiz.QuizAnswer;
import com.lela.domain.dto.quiz.QuizAnswers;
import com.lela.domain.enums.MotivatorSource;
import com.lela.domain.enums.QuestionType;
import com.lela.commons.service.ProductEngineService;
import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.commons.test.AbstractFunctionalTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 6/23/11
 * Time: 11:16 AM
 * Responsibility: Fully tests the ProductEngineService from front to back.
 */
public class ProductEngineServiceCompleteFunctionalTest extends AbstractFunctionalTest {
    private static final Logger log = LoggerFactory.getLogger(ProductEngineServiceCompleteFunctionalTest.class);
    private static final String TEST_CATEGORY = "TestCategory";
    private static final String USER_CODE = "ProductEngineServiceCompleteFunctionalTestUserCode";
    private Question question = null;
    private Category category = null;
    private Item matchingItem = null;
    private Item nonMatchingItem = null;

    @Autowired
    private ProductEngineService eres;

    @Autowired
    private QuizService quizService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Before
    public void setUp() {
        SpringSecurityHelper.unsecureChannel();

        log.info("Testing a completely controlled, front-to-back, scenario of matching");

        log.info("Securing channel...");
        SpringSecurityHelper.secureChannel();
        log.info("Channel secured");

        log.info("Now we want to create a pertinent question");
        question = new Question();
        question.setNm("One + One");
        question.setRlnm("oneone");
        question.setTp(QuestionType.SLIDER);

        List<Answer> answers = new ArrayList<Answer>();
        Answer a1 = new Answer();
        a1.setKy("a");
        a1.setNm("One");
        Map<String, Integer> motivators = new HashMap<String, Integer>();
        motivators.put("A", 9);
        motivators.put("B", 8);
        motivators.put("C", 9);
        motivators.put("D", 8);
        motivators.put("E", 9);
        motivators.put("F", 8);
        motivators.put("G", 9);
        a1.setMtvtrs(motivators);
        answers.add(a1);

        question.setNswrs(answers);

        question = quizService.saveQuestion(question);
        assertNotNull("Question is null", question);
        assertNotNull("Question ID is null", question.getId());
        assertNotNull("Question UrlName is null", question.getRlnm());

        QuizAnswers quizAnswers = new QuizAnswers();
        quizAnswers.setAffiliateId("nonexistantaffiliateid");
        quizAnswers.setApplicationId("nonexistantapplicationid");
        quizAnswers.setQuizUrlName("nonexistantquizurlname");
        List<QuizAnswer> list = new ArrayList<QuizAnswer>(1);
        QuizAnswer answer = new QuizAnswer();
        answer.setAnswerKey(question.getNswrs().get(0).getKy());
        answer.setQuestionUrlName(question.getRlnm());
        list.add(answer);
        quizAnswers.setList(list);

        log.info("Now we need the user to answer the question with key a");
        eres.answerQuestions(USER_CODE, quizAnswers);

        Motivator motivator = userService.findMotivator(USER_CODE, MotivatorSource.QUIZ);
        assertNotNull("Quiz motivator is null", motivator);

        assertNotNull("Motivator is missing motivators", motivator.getMtvtrs());
        log.info("Motivators should match the answer 1-to-1");
        assertEquals("Motivator A is wrong", 9, motivator.getMtvtrs().get("A"), 0);
        assertEquals("Motivator B is wrong", 8, motivator.getMtvtrs().get("B"), 0);
        assertEquals("Motivator C is wrong", 9, motivator.getMtvtrs().get("C"), 0);
        assertEquals("Motivator D is wrong", 8, motivator.getMtvtrs().get("D"), 0);
        assertEquals("Motivator E is wrong", 9, motivator.getMtvtrs().get("E"), 0);
        assertEquals("Motivator F is wrong", 8, motivator.getMtvtrs().get("F"), 0);
        assertEquals("Motivator G is wrong", 9, motivator.getMtvtrs().get("G"), 0);

        log.info("Time to create a product that matches the user");
        category = new Category();
        category.setNm(TEST_CATEGORY);
        category.setRlnm(TEST_CATEGORY.toLowerCase());
        category = categoryService.saveCategory(category);
        assertNotNull("Category is null", category);
        assertNotNull("Category ID is null", category.getId());

        matchingItem = new Item();
        matchingItem.setCtgry(category);
        matchingItem.setNm("An amazing product");
        matchingItem.setRlnm("anamazingproduct");
        Map<String, Integer> itemMotivators = new HashMap<String, Integer>();
        itemMotivators.put("A", 9);
        itemMotivators.put("B", 0);
        itemMotivators.put("C", 9);
        itemMotivators.put("D", 0);
        itemMotivators.put("E", 9);
        itemMotivators.put("F", 0);
        itemMotivators.put("G", 9);
        matchingItem.setMtvtrs(itemMotivators);

        matchingItem = itemService.saveItem(matchingItem);
        assertNotNull("Item is null", matchingItem);
        assertNotNull("Item ID is null", matchingItem.getId());

        nonMatchingItem = new Item();
        nonMatchingItem.setCtgry(category);
        nonMatchingItem.setNm("A sucky product");
        nonMatchingItem.setRlnm("asuckyproduct");
        itemMotivators = new HashMap<String, Integer>();
        itemMotivators.put("A", 0);
        itemMotivators.put("B", 9);
        itemMotivators.put("C", 0);
        itemMotivators.put("D", 9);
        itemMotivators.put("E", 0);
        itemMotivators.put("F", 9);
        itemMotivators.put("G", 0);
        nonMatchingItem.setMtvtrs(itemMotivators);

        nonMatchingItem = itemService.saveItem(nonMatchingItem);
        assertNotNull("Item is null", nonMatchingItem);
        assertNotNull("Item ID is null", nonMatchingItem.getId());

        SpringSecurityHelper.unsecureChannel();
    }

    @After
    public void tearDown() {
        SpringSecurityHelper.secureChannel();
        log.info("Test ran fine. Time to delete all our test creations");
        itemService.removeItem(matchingItem.getRlnm());
        itemService.removeItem(nonMatchingItem.getRlnm());
        categoryService.removeCategory(category.getRlnm());
        quizService.removeQuestion(question.getRlnm());
        userService.removeUser(USER_CODE);
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    public void testCompleteFunctionality() {

        log.info("Test is all set up. Now let's run a query to find the top 20% relevant products for our new user");

        log.info("===========================================================");
        log.info("Matching user code: " + USER_CODE + " with category: " + category.getNm());
        CategoryItemsQuery query = new CategoryItemsQuery();
        query.setCategoryUrlName(category.getRlnm());
        query.setPage(0);
        query.setSize(1);
        query.setUserCode(USER_CODE);
        ItemPage<RelevantItem> page = eres.findRelevantItemsByCategory(query);

        if (page != null && page.getHasContent()) {

            for (RelevantItem ri : page.getContent()) {
                log.info(ri.getNm() + ". Relevancy: " + ri.getTtlrlvncy() + ". Motivators: " + ri.getMtvtrs().toString());
                assertEquals("Relevancy not matching", 324, ri.getTtlrlvncy(), 0);
            }

        }

        log.info("===========================================================");

    }

}
