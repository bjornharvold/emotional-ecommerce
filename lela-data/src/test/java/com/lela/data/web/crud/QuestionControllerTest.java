package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Question;
import com.lela.data.domain.entity.QuestionDataOnDemand;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class QuestionControllerTest extends AbstractControllerTest {

    private QuestionController questionController = new QuestionController();

    @Autowired
    private QuestionDataOnDemand questionDataOnDemand;

    @Before
    public void setUp()
    {
        SpringSecurityHelper.secureChannel();
    }

    @After
    public void tearDown()
    {
        SpringSecurityHelper.unsecureChannel();
    }

    @Test
    @Transactional
    public void create() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResult();
        Model model = getModel();

        Question question = questionDataOnDemand.getRandomQuestion();

        String result = questionController.create(question, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/questions/"+question.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        question = questionDataOnDemand.getRandomQuestion();

        result = questionController.create(question, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/questions/"+question.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        question = questionDataOnDemand.getRandomQuestion();

        result = questionController.create(question, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/questions/"+question.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Question question = questionDataOnDemand.getRandomQuestion();

        String result = questionController.create(question, results, model, request);
        assertEquals("The form was not returned", "crud/questions/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = questionController.createForm(model);
        assertEquals("The form was not returned", "crud/questions/create", result);
    }

    @Test
    public void show() {
        Question question = questionDataOnDemand.getRandomQuestion();
        Long id = question.getId();
        Model model = getModel();
        String result = questionController.show(id, model);
        assertEquals("The show view was not returned", "crud/questions/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = questionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/questions/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = questionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/questions/list", result);

        page = 1;
        size = null;
        questionController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/questions/list", result);

        page = null;
        size = 10;
        questionController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/questions/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Question question = questionDataOnDemand.getRandomQuestion();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = questionController.update(question, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/questions/"+question.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Question question = questionDataOnDemand.getRandomQuestion();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = questionController.update(question, results, model, request);
        assertEquals("The update view was not returned", "crud/questions/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Question question = questionDataOnDemand.getRandomQuestion();
        Long id = question.getId();
        Model model = getModel();
        String result = questionController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/questions/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Question question = questionDataOnDemand.getRandomQuestion();
        Long id = question.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = questionController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/questions", result);

        question = Question.findQuestion(id);
        assertNull("Question was not deleted", question);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Question question = questionDataOnDemand.getRandomQuestion();
        Long id = question.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = questionController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/questions", result);

        question = Question.findQuestion(id);
        assertNull("Question was not deleted", question);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = questionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/questions/list", result);
    }
}

