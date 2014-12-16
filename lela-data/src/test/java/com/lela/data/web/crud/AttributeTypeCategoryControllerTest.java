package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AttributeTypeCategory;
import com.lela.data.domain.entity.AttributeTypeCategoryDataOnDemand;
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


public class AttributeTypeCategoryControllerTest extends AbstractControllerTest {

    private AttributeTypeCategoryController attributetypecategoryController = new AttributeTypeCategoryController();

    @Autowired
    private AttributeTypeCategoryDataOnDemand attributetypecategoryDataOnDemand;

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

        AttributeTypeCategory attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();

        String result = attributetypecategoryController.create(attributetypecategory, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/attributetypecategorys/"+attributetypecategory.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();

        result = attributetypecategoryController.create(attributetypecategory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypecategorys/"+attributetypecategory.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();

        result = attributetypecategoryController.create(attributetypecategory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypecategorys/"+attributetypecategory.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AttributeTypeCategory attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();

        String result = attributetypecategoryController.create(attributetypecategory, results, model, request);
        assertEquals("The form was not returned", "crud/attributetypecategorys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = attributetypecategoryController.createForm(model);
        assertEquals("The form was not returned", "crud/attributetypecategorys/create", result);
    }

    @Test
    public void show() {
        AttributeTypeCategory attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();
        Long id = attributetypecategory.getId();
        Model model = getModel();
        String result = attributetypecategoryController.show(id, model);
        assertEquals("The show view was not returned", "crud/attributetypecategorys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypecategoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypecategorys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypecategoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypecategorys/list", result);

        page = 1;
        size = null;
        attributetypecategoryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/attributetypecategorys/list", result);

        page = null;
        size = 10;
        attributetypecategoryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/attributetypecategorys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AttributeTypeCategory attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypecategoryController.update(attributetypecategory, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/attributetypecategorys/"+attributetypecategory.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AttributeTypeCategory attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypecategoryController.update(attributetypecategory, results, model, request);
        assertEquals("The update view was not returned", "crud/attributetypecategorys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AttributeTypeCategory attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();
        Long id = attributetypecategory.getId();
        Model model = getModel();
        String result = attributetypecategoryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/attributetypecategorys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AttributeTypeCategory attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();
        Long id = attributetypecategory.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypecategoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypecategorys", result);

        attributetypecategory = AttributeTypeCategory.findAttributeTypeCategory(id);
        assertNull("AttributeTypeCategory was not deleted", attributetypecategory);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AttributeTypeCategory attributetypecategory = attributetypecategoryDataOnDemand.getRandomAttributeTypeCategory();
        Long id = attributetypecategory.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypecategoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypecategorys", result);

        attributetypecategory = AttributeTypeCategory.findAttributeTypeCategory(id);
        assertNull("AttributeTypeCategory was not deleted", attributetypecategory);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = attributetypecategoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypecategorys/list", result);
    }
}

