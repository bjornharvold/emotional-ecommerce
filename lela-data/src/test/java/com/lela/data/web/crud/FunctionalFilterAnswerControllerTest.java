package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.FunctionalFilterAnswer;
import com.lela.data.domain.entity.FunctionalFilterAnswerDataOnDemand;
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


public class FunctionalFilterAnswerControllerTest extends AbstractControllerTest {

    private FunctionalFilterAnswerController functionalfilteranswerController = new FunctionalFilterAnswerController();

    @Autowired
    private FunctionalFilterAnswerDataOnDemand functionalfilteranswerDataOnDemand;

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

        FunctionalFilterAnswer functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();

        String result = functionalfilteranswerController.create(functionalfilteranswer, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/functionalfilteranswers/"+functionalfilteranswer.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();

        result = functionalfilteranswerController.create(functionalfilteranswer, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/functionalfilteranswers/"+functionalfilteranswer.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();

        result = functionalfilteranswerController.create(functionalfilteranswer, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/functionalfilteranswers/"+functionalfilteranswer.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        FunctionalFilterAnswer functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();

        String result = functionalfilteranswerController.create(functionalfilteranswer, results, model, request);
        assertEquals("The form was not returned", "crud/functionalfilteranswers/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = functionalfilteranswerController.createForm(model);
        assertEquals("The form was not returned", "crud/functionalfilteranswers/create", result);
    }

    @Test
    public void show() {
        FunctionalFilterAnswer functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();
        Long id = functionalfilteranswer.getId();
        Model model = getModel();
        String result = functionalfilteranswerController.show(id, model);
        assertEquals("The show view was not returned", "crud/functionalfilteranswers/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = functionalfilteranswerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilteranswers/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = functionalfilteranswerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilteranswers/list", result);

        page = 1;
        size = null;
        functionalfilteranswerController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/functionalfilteranswers/list", result);

        page = null;
        size = 10;
        functionalfilteranswerController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/functionalfilteranswers/list", result);

    }

    @Test
    @Transactional
    public void update() {
        FunctionalFilterAnswer functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = functionalfilteranswerController.update(functionalfilteranswer, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/functionalfilteranswers/"+functionalfilteranswer.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        FunctionalFilterAnswer functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = functionalfilteranswerController.update(functionalfilteranswer, results, model, request);
        assertEquals("The update view was not returned", "crud/functionalfilteranswers/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        FunctionalFilterAnswer functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();
        Long id = functionalfilteranswer.getId();
        Model model = getModel();
        String result = functionalfilteranswerController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/functionalfilteranswers/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        FunctionalFilterAnswer functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();
        Long id = functionalfilteranswer.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = functionalfilteranswerController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/functionalfilteranswers", result);

        functionalfilteranswer = FunctionalFilterAnswer.findFunctionalFilterAnswer(id);
        assertNull("FunctionalFilterAnswer was not deleted", functionalfilteranswer);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        FunctionalFilterAnswer functionalfilteranswer = functionalfilteranswerDataOnDemand.getRandomFunctionalFilterAnswer();
        Long id = functionalfilteranswer.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = functionalfilteranswerController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/functionalfilteranswers", result);

        functionalfilteranswer = FunctionalFilterAnswer.findFunctionalFilterAnswer(id);
        assertNull("FunctionalFilterAnswer was not deleted", functionalfilteranswer);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = functionalfilteranswerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilteranswers/list", result);
    }
}

