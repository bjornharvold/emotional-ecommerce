package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.InputValidationList;
import com.lela.data.domain.entity.InputValidationListDataOnDemand;
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


public class InputValidationListControllerTest extends AbstractControllerTest {

    private InputValidationListController inputvalidationlistController = new InputValidationListController();

    @Autowired
    private InputValidationListDataOnDemand inputvalidationlistDataOnDemand;

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

        InputValidationList inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();

        String result = inputvalidationlistController.create(inputvalidationlist, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/inputvalidationlists/"+inputvalidationlist.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();

        result = inputvalidationlistController.create(inputvalidationlist, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/inputvalidationlists/"+inputvalidationlist.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();

        result = inputvalidationlistController.create(inputvalidationlist, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/inputvalidationlists/"+inputvalidationlist.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        InputValidationList inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();

        String result = inputvalidationlistController.create(inputvalidationlist, results, model, request);
        assertEquals("The form was not returned", "crud/inputvalidationlists/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = inputvalidationlistController.createForm(model);
        assertEquals("The form was not returned", "crud/inputvalidationlists/create", result);
    }

    @Test
    public void show() {
        InputValidationList inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();
        Long id = inputvalidationlist.getId();
        Model model = getModel();
        String result = inputvalidationlistController.show(id, model);
        assertEquals("The show view was not returned", "crud/inputvalidationlists/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = inputvalidationlistController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputvalidationlists/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = inputvalidationlistController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputvalidationlists/list", result);

        page = 1;
        size = null;
        inputvalidationlistController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/inputvalidationlists/list", result);

        page = null;
        size = 10;
        inputvalidationlistController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/inputvalidationlists/list", result);

    }

    @Test
    @Transactional
    public void update() {
        InputValidationList inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = inputvalidationlistController.update(inputvalidationlist, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/inputvalidationlists/"+inputvalidationlist.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        InputValidationList inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = inputvalidationlistController.update(inputvalidationlist, results, model, request);
        assertEquals("The update view was not returned", "crud/inputvalidationlists/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        InputValidationList inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();
        Long id = inputvalidationlist.getId();
        Model model = getModel();
        String result = inputvalidationlistController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/inputvalidationlists/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        InputValidationList inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();
        Long id = inputvalidationlist.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = inputvalidationlistController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/inputvalidationlists", result);

        inputvalidationlist = InputValidationList.findInputValidationList(id);
        assertNull("InputValidationList was not deleted", inputvalidationlist);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        InputValidationList inputvalidationlist = inputvalidationlistDataOnDemand.getRandomInputValidationList();
        Long id = inputvalidationlist.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = inputvalidationlistController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/inputvalidationlists", result);

        inputvalidationlist = InputValidationList.findInputValidationList(id);
        assertNull("InputValidationList was not deleted", inputvalidationlist);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = inputvalidationlistController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputvalidationlists/list", result);
    }
}

