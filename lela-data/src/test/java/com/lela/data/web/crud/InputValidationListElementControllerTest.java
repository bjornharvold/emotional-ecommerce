package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.InputValidationListElement;
import com.lela.data.domain.entity.InputValidationListElementDataOnDemand;
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


public class InputValidationListElementControllerTest extends AbstractControllerTest {

    private InputValidationListElementController inputvalidationlistelementController = new InputValidationListElementController();

    @Autowired
    private InputValidationListElementDataOnDemand inputvalidationlistelementDataOnDemand;

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

        InputValidationListElement inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();

        String result = inputvalidationlistelementController.create(inputvalidationlistelement, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/inputvalidationlistelements/"+inputvalidationlistelement.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();

        result = inputvalidationlistelementController.create(inputvalidationlistelement, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/inputvalidationlistelements/"+inputvalidationlistelement.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();

        result = inputvalidationlistelementController.create(inputvalidationlistelement, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/inputvalidationlistelements/"+inputvalidationlistelement.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        InputValidationListElement inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();

        String result = inputvalidationlistelementController.create(inputvalidationlistelement, results, model, request);
        assertEquals("The form was not returned", "crud/inputvalidationlistelements/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = inputvalidationlistelementController.createForm(model);
        assertEquals("The form was not returned", "crud/inputvalidationlistelements/create", result);
    }

    @Test
    public void show() {
        InputValidationListElement inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();
        Long id = inputvalidationlistelement.getId();
        Model model = getModel();
        String result = inputvalidationlistelementController.show(id, model);
        assertEquals("The show view was not returned", "crud/inputvalidationlistelements/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = inputvalidationlistelementController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputvalidationlistelements/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = inputvalidationlistelementController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputvalidationlistelements/list", result);

        page = 1;
        size = null;
        inputvalidationlistelementController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/inputvalidationlistelements/list", result);

        page = null;
        size = 10;
        inputvalidationlistelementController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/inputvalidationlistelements/list", result);

    }

    @Test
    @Transactional
    public void update() {
        InputValidationListElement inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = inputvalidationlistelementController.update(inputvalidationlistelement, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/inputvalidationlistelements/"+inputvalidationlistelement.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        InputValidationListElement inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = inputvalidationlistelementController.update(inputvalidationlistelement, results, model, request);
        assertEquals("The update view was not returned", "crud/inputvalidationlistelements/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        InputValidationListElement inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();
        Long id = inputvalidationlistelement.getId();
        Model model = getModel();
        String result = inputvalidationlistelementController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/inputvalidationlistelements/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        InputValidationListElement inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();
        Long id = inputvalidationlistelement.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = inputvalidationlistelementController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/inputvalidationlistelements", result);

        inputvalidationlistelement = InputValidationListElement.findInputValidationListElement(id);
        assertNull("InputValidationListElement was not deleted", inputvalidationlistelement);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        InputValidationListElement inputvalidationlistelement = inputvalidationlistelementDataOnDemand.getRandomInputValidationListElement();
        Long id = inputvalidationlistelement.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = inputvalidationlistelementController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/inputvalidationlistelements", result);

        inputvalidationlistelement = InputValidationListElement.findInputValidationListElement(id);
        assertNull("InputValidationListElement was not deleted", inputvalidationlistelement);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = inputvalidationlistelementController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputvalidationlistelements/list", result);
    }
}

