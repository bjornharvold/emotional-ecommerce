package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AttributeTypeNormalization;
import com.lela.data.domain.entity.AttributeTypeNormalizationDataOnDemand;
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


public class AttributeTypeNormalizationControllerTest extends AbstractControllerTest {

    private AttributeTypeNormalizationController attributetypenormalizationController = new AttributeTypeNormalizationController();

    @Autowired
    private AttributeTypeNormalizationDataOnDemand attributetypenormalizationDataOnDemand;

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

        AttributeTypeNormalization attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();

        String result = attributetypenormalizationController.create(attributetypenormalization, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/attributetypenormalizations/"+attributetypenormalization.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();

        result = attributetypenormalizationController.create(attributetypenormalization, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypenormalizations/"+attributetypenormalization.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();

        result = attributetypenormalizationController.create(attributetypenormalization, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypenormalizations/"+attributetypenormalization.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AttributeTypeNormalization attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();

        String result = attributetypenormalizationController.create(attributetypenormalization, results, model, request);
        assertEquals("The form was not returned", "crud/attributetypenormalizations/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = attributetypenormalizationController.createForm(model);
        assertEquals("The form was not returned", "crud/attributetypenormalizations/create", result);
    }

    @Test
    public void show() {
        AttributeTypeNormalization attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();
        Long id = attributetypenormalization.getId();
        Model model = getModel();
        String result = attributetypenormalizationController.show(id, model);
        assertEquals("The show view was not returned", "crud/attributetypenormalizations/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypenormalizationController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypenormalizations/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypenormalizationController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypenormalizations/list", result);

        page = 1;
        size = null;
        attributetypenormalizationController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/attributetypenormalizations/list", result);

        page = null;
        size = 10;
        attributetypenormalizationController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/attributetypenormalizations/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AttributeTypeNormalization attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypenormalizationController.update(attributetypenormalization, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/attributetypenormalizations/"+attributetypenormalization.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AttributeTypeNormalization attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypenormalizationController.update(attributetypenormalization, results, model, request);
        assertEquals("The update view was not returned", "crud/attributetypenormalizations/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AttributeTypeNormalization attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();
        Long id = attributetypenormalization.getId();
        Model model = getModel();
        String result = attributetypenormalizationController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/attributetypenormalizations/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AttributeTypeNormalization attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();
        Long id = attributetypenormalization.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypenormalizationController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypenormalizations", result);

        attributetypenormalization = AttributeTypeNormalization.findAttributeTypeNormalization(id);
        assertNull("AttributeTypeNormalization was not deleted", attributetypenormalization);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AttributeTypeNormalization attributetypenormalization = attributetypenormalizationDataOnDemand.getRandomAttributeTypeNormalization();
        Long id = attributetypenormalization.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypenormalizationController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypenormalizations", result);

        attributetypenormalization = AttributeTypeNormalization.findAttributeTypeNormalization(id);
        assertNull("AttributeTypeNormalization was not deleted", attributetypenormalization);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = attributetypenormalizationController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypenormalizations/list", result);
    }
}

