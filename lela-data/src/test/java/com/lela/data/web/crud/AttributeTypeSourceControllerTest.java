package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AttributeTypeSource;
import com.lela.data.domain.entity.AttributeTypeSourceDataOnDemand;
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


public class AttributeTypeSourceControllerTest extends AbstractControllerTest {

    private AttributeTypeSourceController attributetypesourceController = new AttributeTypeSourceController();

    @Autowired
    private AttributeTypeSourceDataOnDemand attributetypesourceDataOnDemand;

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

        AttributeTypeSource attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();

        String result = attributetypesourceController.create(attributetypesource, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/attributetypesources/"+attributetypesource.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();

        result = attributetypesourceController.create(attributetypesource, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypesources/"+attributetypesource.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();

        result = attributetypesourceController.create(attributetypesource, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypesources/"+attributetypesource.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AttributeTypeSource attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();

        String result = attributetypesourceController.create(attributetypesource, results, model, request);
        assertEquals("The form was not returned", "crud/attributetypesources/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = attributetypesourceController.createForm(model);
        assertEquals("The form was not returned", "crud/attributetypesources/create", result);
    }

    @Test
    public void show() {
        AttributeTypeSource attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();
        Long id = attributetypesource.getId();
        Model model = getModel();
        String result = attributetypesourceController.show(id, model);
        assertEquals("The show view was not returned", "crud/attributetypesources/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypesourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypesources/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypesourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypesources/list", result);

        page = 1;
        size = null;
        attributetypesourceController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/attributetypesources/list", result);

        page = null;
        size = 10;
        attributetypesourceController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/attributetypesources/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AttributeTypeSource attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypesourceController.update(attributetypesource, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/attributetypesources/"+attributetypesource.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AttributeTypeSource attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypesourceController.update(attributetypesource, results, model, request);
        assertEquals("The update view was not returned", "crud/attributetypesources/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AttributeTypeSource attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();
        Long id = attributetypesource.getId();
        Model model = getModel();
        String result = attributetypesourceController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/attributetypesources/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AttributeTypeSource attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();
        Long id = attributetypesource.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypesourceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypesources", result);

        attributetypesource = AttributeTypeSource.findAttributeTypeSource(id);
        assertNull("AttributeTypeSource was not deleted", attributetypesource);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AttributeTypeSource attributetypesource = attributetypesourceDataOnDemand.getRandomAttributeTypeSource();
        Long id = attributetypesource.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypesourceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypesources", result);

        attributetypesource = AttributeTypeSource.findAttributeTypeSource(id);
        assertNull("AttributeTypeSource was not deleted", attributetypesource);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = attributetypesourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypesources/list", result);
    }
}

