package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AttributeTypeMotivator;
import com.lela.data.domain.entity.AttributeTypeMotivatorDataOnDemand;
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


public class AttributeTypeMotivatorControllerTest extends AbstractControllerTest {

    private AttributeTypeMotivatorController attributetypemotivatorController = new AttributeTypeMotivatorController();

    @Autowired
    private AttributeTypeMotivatorDataOnDemand attributetypemotivatorDataOnDemand;

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

        AttributeTypeMotivator attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();

        String result = attributetypemotivatorController.create(attributetypemotivator, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/attributetypemotivators/"+attributetypemotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();

        result = attributetypemotivatorController.create(attributetypemotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypemotivators/"+attributetypemotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();

        result = attributetypemotivatorController.create(attributetypemotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypemotivators/"+attributetypemotivator.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AttributeTypeMotivator attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();

        String result = attributetypemotivatorController.create(attributetypemotivator, results, model, request);
        assertEquals("The form was not returned", "crud/attributetypemotivators/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = attributetypemotivatorController.createForm(model);
        assertEquals("The form was not returned", "crud/attributetypemotivators/create", result);
    }

    @Test
    public void show() {
        AttributeTypeMotivator attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();
        Long id = attributetypemotivator.getId();
        Model model = getModel();
        String result = attributetypemotivatorController.show(id, model);
        assertEquals("The show view was not returned", "crud/attributetypemotivators/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypemotivators/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypemotivators/list", result);

        page = 1;
        size = null;
        attributetypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/attributetypemotivators/list", result);

        page = null;
        size = 10;
        attributetypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/attributetypemotivators/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AttributeTypeMotivator attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypemotivatorController.update(attributetypemotivator, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/attributetypemotivators/"+attributetypemotivator.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AttributeTypeMotivator attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypemotivatorController.update(attributetypemotivator, results, model, request);
        assertEquals("The update view was not returned", "crud/attributetypemotivators/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AttributeTypeMotivator attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();
        Long id = attributetypemotivator.getId();
        Model model = getModel();
        String result = attributetypemotivatorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/attributetypemotivators/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AttributeTypeMotivator attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();
        Long id = attributetypemotivator.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypemotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypemotivators", result);

        attributetypemotivator = AttributeTypeMotivator.findAttributeTypeMotivator(id);
        assertNull("AttributeTypeMotivator was not deleted", attributetypemotivator);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AttributeTypeMotivator attributetypemotivator = attributetypemotivatorDataOnDemand.getRandomAttributeTypeMotivator();
        Long id = attributetypemotivator.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypemotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypemotivators", result);

        attributetypemotivator = AttributeTypeMotivator.findAttributeTypeMotivator(id);
        assertNull("AttributeTypeMotivator was not deleted", attributetypemotivator);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = attributetypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypemotivators/list", result);
    }
}

