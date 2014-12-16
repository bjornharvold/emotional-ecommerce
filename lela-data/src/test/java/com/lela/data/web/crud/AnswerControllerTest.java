package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Answer;
import com.lela.data.domain.entity.AnswerDataOnDemand;
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


public class AnswerControllerTest extends AbstractControllerTest {

    private AnswerController answerController = new AnswerController();

    @Autowired
    private AnswerDataOnDemand answerDataOnDemand;

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

        Answer answer = answerDataOnDemand.getRandomAnswer();

        String result = answerController.create(answer, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/answers/"+answer.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        answer = answerDataOnDemand.getRandomAnswer();

        result = answerController.create(answer, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/answers/"+answer.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        answer = answerDataOnDemand.getRandomAnswer();

        result = answerController.create(answer, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/answers/"+answer.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Answer answer = answerDataOnDemand.getRandomAnswer();

        String result = answerController.create(answer, results, model, request);
        assertEquals("The form was not returned", "crud/answers/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = answerController.createForm(model);
        assertEquals("The form was not returned", "crud/answers/create", result);
    }

    @Test
    public void show() {
        Answer answer = answerDataOnDemand.getRandomAnswer();
        Long id = answer.getId();
        Model model = getModel();
        String result = answerController.show(id, model);
        assertEquals("The show view was not returned", "crud/answers/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = answerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/answers/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = answerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/answers/list", result);

        page = 1;
        size = null;
        answerController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/answers/list", result);

        page = null;
        size = 10;
        answerController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/answers/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Answer answer = answerDataOnDemand.getRandomAnswer();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = answerController.update(answer, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/answers/"+answer.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Answer answer = answerDataOnDemand.getRandomAnswer();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = answerController.update(answer, results, model, request);
        assertEquals("The update view was not returned", "crud/answers/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Answer answer = answerDataOnDemand.getRandomAnswer();
        Long id = answer.getId();
        Model model = getModel();
        String result = answerController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/answers/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Answer answer = answerDataOnDemand.getRandomAnswer();
        Long id = answer.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = answerController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/answers", result);

        answer = Answer.findAnswer(id);
        assertNull("Answer was not deleted", answer);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Answer answer = answerDataOnDemand.getRandomAnswer();
        Long id = answer.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = answerController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/answers", result);

        answer = Answer.findAnswer(id);
        assertNull("Answer was not deleted", answer);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = answerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/answers/list", result);
    }
}

