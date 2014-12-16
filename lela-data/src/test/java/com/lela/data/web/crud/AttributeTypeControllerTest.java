package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AttributeType;
import com.lela.data.domain.entity.AttributeTypeDataOnDemand;
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


public class AttributeTypeControllerTest extends AbstractControllerTest {

    private AttributeTypeController attributetypeController = new AttributeTypeController();

    @Autowired
    private AttributeTypeDataOnDemand attributetypeDataOnDemand;

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

        AttributeType attributetype = attributetypeDataOnDemand.getRandomAttributeType();

        String result = attributetypeController.create(attributetype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/attributetypes/"+attributetype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetype = attributetypeDataOnDemand.getRandomAttributeType();

        result = attributetypeController.create(attributetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypes/"+attributetype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetype = attributetypeDataOnDemand.getRandomAttributeType();

        result = attributetypeController.create(attributetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypes/"+attributetype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AttributeType attributetype = attributetypeDataOnDemand.getRandomAttributeType();

        String result = attributetypeController.create(attributetype, results, model, request);
        assertEquals("The form was not returned", "crud/attributetypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = attributetypeController.createForm(model);
        assertEquals("The form was not returned", "crud/attributetypes/create", result);
    }

    @Test
    public void show() {
        AttributeType attributetype = attributetypeDataOnDemand.getRandomAttributeType();
        Long id = attributetype.getId();
        Model model = getModel();
        String result = attributetypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/attributetypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypes/list", result);

        page = 1;
        size = null;
        attributetypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/attributetypes/list", result);

        page = null;
        size = 10;
        attributetypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/attributetypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AttributeType attributetype = attributetypeDataOnDemand.getRandomAttributeType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypeController.update(attributetype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/attributetypes/"+attributetype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AttributeType attributetype = attributetypeDataOnDemand.getRandomAttributeType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypeController.update(attributetype, results, model, request);
        assertEquals("The update view was not returned", "crud/attributetypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AttributeType attributetype = attributetypeDataOnDemand.getRandomAttributeType();
        Long id = attributetype.getId();
        Model model = getModel();
        String result = attributetypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/attributetypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AttributeType attributetype = attributetypeDataOnDemand.getRandomAttributeType();
        Long id = attributetype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypes", result);

        attributetype = AttributeType.findAttributeType(id);
        assertNull("AttributeType was not deleted", attributetype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AttributeType attributetype = attributetypeDataOnDemand.getRandomAttributeType();
        Long id = attributetype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypes", result);

        attributetype = AttributeType.findAttributeType(id);
        assertNull("AttributeType was not deleted", attributetype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = attributetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypes/list", result);
    }
}

