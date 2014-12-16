package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ValidationType;
import com.lela.data.domain.entity.ValidationTypeDataOnDemand;
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


public class ValidationTypeControllerTest extends AbstractControllerTest {

    private ValidationTypeController validationtypeController = new ValidationTypeController();

    @Autowired
    private ValidationTypeDataOnDemand validationtypeDataOnDemand;

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

        ValidationType validationtype = validationtypeDataOnDemand.getRandomValidationType();

        String result = validationtypeController.create(validationtype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/validationtypes/"+validationtype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        validationtype = validationtypeDataOnDemand.getRandomValidationType();

        result = validationtypeController.create(validationtype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/validationtypes/"+validationtype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        validationtype = validationtypeDataOnDemand.getRandomValidationType();

        result = validationtypeController.create(validationtype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/validationtypes/"+validationtype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ValidationType validationtype = validationtypeDataOnDemand.getRandomValidationType();

        String result = validationtypeController.create(validationtype, results, model, request);
        assertEquals("The form was not returned", "crud/validationtypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = validationtypeController.createForm(model);
        assertEquals("The form was not returned", "crud/validationtypes/create", result);
    }

    @Test
    public void show() {
        ValidationType validationtype = validationtypeDataOnDemand.getRandomValidationType();
        Long id = validationtype.getId();
        Model model = getModel();
        String result = validationtypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/validationtypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = validationtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/validationtypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = validationtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/validationtypes/list", result);

        page = 1;
        size = null;
        validationtypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/validationtypes/list", result);

        page = null;
        size = 10;
        validationtypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/validationtypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ValidationType validationtype = validationtypeDataOnDemand.getRandomValidationType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = validationtypeController.update(validationtype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/validationtypes/"+validationtype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ValidationType validationtype = validationtypeDataOnDemand.getRandomValidationType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = validationtypeController.update(validationtype, results, model, request);
        assertEquals("The update view was not returned", "crud/validationtypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ValidationType validationtype = validationtypeDataOnDemand.getRandomValidationType();
        Long id = validationtype.getId();
        Model model = getModel();
        String result = validationtypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/validationtypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ValidationType validationtype = validationtypeDataOnDemand.getRandomValidationType();
        Long id = validationtype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = validationtypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/validationtypes", result);

        validationtype = ValidationType.findValidationType(id);
        assertNull("ValidationType was not deleted", validationtype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ValidationType validationtype = validationtypeDataOnDemand.getRandomValidationType();
        Long id = validationtype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = validationtypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/validationtypes", result);

        validationtype = ValidationType.findValidationType(id);
        assertNull("ValidationType was not deleted", validationtype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = validationtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/validationtypes/list", result);
    }
}

