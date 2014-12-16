package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ZipCode;
import com.lela.data.domain.entity.ZipCodeDataOnDemand;
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


public class ZipCodeControllerTest extends AbstractControllerTest {

    private ZipCodeController zipcodeController = new ZipCodeController();

    @Autowired
    private ZipCodeDataOnDemand zipcodeDataOnDemand;

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

        ZipCode zipcode = zipcodeDataOnDemand.getRandomZipCode();

        String result = zipcodeController.create(zipcode, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/zipcodes/"+zipcode.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        zipcode = zipcodeDataOnDemand.getRandomZipCode();

        result = zipcodeController.create(zipcode, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/zipcodes/"+zipcode.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        zipcode = zipcodeDataOnDemand.getRandomZipCode();

        result = zipcodeController.create(zipcode, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/zipcodes/"+zipcode.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ZipCode zipcode = zipcodeDataOnDemand.getRandomZipCode();

        String result = zipcodeController.create(zipcode, results, model, request);
        assertEquals("The form was not returned", "crud/zipcodes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = zipcodeController.createForm(model);
        assertEquals("The form was not returned", "crud/zipcodes/create", result);
    }

    @Test
    public void show() {
        ZipCode zipcode = zipcodeDataOnDemand.getRandomZipCode();
        Long id = zipcode.getId();
        Model model = getModel();
        String result = zipcodeController.show(id, model);
        assertEquals("The show view was not returned", "crud/zipcodes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = zipcodeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/zipcodes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = zipcodeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/zipcodes/list", result);

        page = 1;
        size = null;
        zipcodeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/zipcodes/list", result);

        page = null;
        size = 10;
        zipcodeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/zipcodes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ZipCode zipcode = zipcodeDataOnDemand.getRandomZipCode();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = zipcodeController.update(zipcode, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/zipcodes/"+zipcode.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ZipCode zipcode = zipcodeDataOnDemand.getRandomZipCode();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = zipcodeController.update(zipcode, results, model, request);
        assertEquals("The update view was not returned", "crud/zipcodes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ZipCode zipcode = zipcodeDataOnDemand.getRandomZipCode();
        Long id = zipcode.getId();
        Model model = getModel();
        String result = zipcodeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/zipcodes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ZipCode zipcode = zipcodeDataOnDemand.getRandomZipCode();
        Long id = zipcode.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = zipcodeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/zipcodes", result);

        zipcode = ZipCode.findZipCode(id);
        assertNull("ZipCode was not deleted", zipcode);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ZipCode zipcode = zipcodeDataOnDemand.getRandomZipCode();
        Long id = zipcode.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = zipcodeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/zipcodes", result);

        zipcode = ZipCode.findZipCode(id);
        assertNull("ZipCode was not deleted", zipcode);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = zipcodeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/zipcodes/list", result);
    }
}

