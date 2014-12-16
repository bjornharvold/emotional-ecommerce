package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AttributeFormula;
import com.lela.data.domain.entity.AttributeFormulaDataOnDemand;
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


public class AttributeFormulaControllerTest extends AbstractControllerTest {

    private AttributeFormulaController attributeformulaController = new AttributeFormulaController();

    @Autowired
    private AttributeFormulaDataOnDemand attributeformulaDataOnDemand;

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

        AttributeFormula attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();

        String result = attributeformulaController.create(attributeformula, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/attributeformulas/"+attributeformula.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();

        result = attributeformulaController.create(attributeformula, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributeformulas/"+attributeformula.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();

        result = attributeformulaController.create(attributeformula, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributeformulas/"+attributeformula.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AttributeFormula attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();

        String result = attributeformulaController.create(attributeformula, results, model, request);
        assertEquals("The form was not returned", "crud/attributeformulas/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = attributeformulaController.createForm(model);
        assertEquals("The form was not returned", "crud/attributeformulas/create", result);
    }

    @Test
    public void show() {
        AttributeFormula attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();
        Long id = attributeformula.getId();
        Model model = getModel();
        String result = attributeformulaController.show(id, model);
        assertEquals("The show view was not returned", "crud/attributeformulas/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributeformulaController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributeformulas/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributeformulaController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributeformulas/list", result);

        page = 1;
        size = null;
        attributeformulaController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/attributeformulas/list", result);

        page = null;
        size = 10;
        attributeformulaController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/attributeformulas/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AttributeFormula attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributeformulaController.update(attributeformula, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/attributeformulas/"+attributeformula.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AttributeFormula attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributeformulaController.update(attributeformula, results, model, request);
        assertEquals("The update view was not returned", "crud/attributeformulas/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AttributeFormula attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();
        Long id = attributeformula.getId();
        Model model = getModel();
        String result = attributeformulaController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/attributeformulas/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AttributeFormula attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();
        Long id = attributeformula.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributeformulaController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributeformulas", result);

        attributeformula = AttributeFormula.findAttributeFormula(id);
        assertNull("AttributeFormula was not deleted", attributeformula);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AttributeFormula attributeformula = attributeformulaDataOnDemand.getRandomAttributeFormula();
        Long id = attributeformula.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributeformulaController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributeformulas", result);

        attributeformula = AttributeFormula.findAttributeFormula(id);
        assertNull("AttributeFormula was not deleted", attributeformula);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = attributeformulaController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributeformulas/list", result);
    }
}

