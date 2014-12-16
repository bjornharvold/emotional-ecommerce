package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.DesignStyleMotivator;
import com.lela.data.domain.entity.DesignStyleMotivatorDataOnDemand;
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


public class DesignStyleMotivatorControllerTest extends AbstractControllerTest {

    private DesignStyleMotivatorController designstylemotivatorController = new DesignStyleMotivatorController();

    @Autowired
    private DesignStyleMotivatorDataOnDemand designstylemotivatorDataOnDemand;

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

        DesignStyleMotivator designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();

        String result = designstylemotivatorController.create(designstylemotivator, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/designstylemotivators/"+designstylemotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();

        result = designstylemotivatorController.create(designstylemotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/designstylemotivators/"+designstylemotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();

        result = designstylemotivatorController.create(designstylemotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/designstylemotivators/"+designstylemotivator.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        DesignStyleMotivator designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();

        String result = designstylemotivatorController.create(designstylemotivator, results, model, request);
        assertEquals("The form was not returned", "crud/designstylemotivators/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = designstylemotivatorController.createForm(model);
        assertEquals("The form was not returned", "crud/designstylemotivators/create", result);
    }

    @Test
    public void show() {
        DesignStyleMotivator designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();
        Long id = designstylemotivator.getId();
        Model model = getModel();
        String result = designstylemotivatorController.show(id, model);
        assertEquals("The show view was not returned", "crud/designstylemotivators/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = designstylemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/designstylemotivators/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = designstylemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/designstylemotivators/list", result);

        page = 1;
        size = null;
        designstylemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/designstylemotivators/list", result);

        page = null;
        size = 10;
        designstylemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/designstylemotivators/list", result);

    }

    @Test
    @Transactional
    public void update() {
        DesignStyleMotivator designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = designstylemotivatorController.update(designstylemotivator, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/designstylemotivators/"+designstylemotivator.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        DesignStyleMotivator designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = designstylemotivatorController.update(designstylemotivator, results, model, request);
        assertEquals("The update view was not returned", "crud/designstylemotivators/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        DesignStyleMotivator designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();
        Long id = designstylemotivator.getId();
        Model model = getModel();
        String result = designstylemotivatorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/designstylemotivators/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        DesignStyleMotivator designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();
        Long id = designstylemotivator.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = designstylemotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/designstylemotivators", result);

        designstylemotivator = DesignStyleMotivator.findDesignStyleMotivator(id);
        assertNull("DesignStyleMotivator was not deleted", designstylemotivator);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        DesignStyleMotivator designstylemotivator = designstylemotivatorDataOnDemand.getRandomDesignStyleMotivator();
        Long id = designstylemotivator.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = designstylemotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/designstylemotivators", result);

        designstylemotivator = DesignStyleMotivator.findDesignStyleMotivator(id);
        assertNull("DesignStyleMotivator was not deleted", designstylemotivator);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = designstylemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/designstylemotivators/list", result);
    }
}

