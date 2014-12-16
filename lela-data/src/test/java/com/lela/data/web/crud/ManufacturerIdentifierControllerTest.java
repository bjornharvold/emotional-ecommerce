package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ManufacturerIdentifier;
import com.lela.data.domain.entity.ManufacturerIdentifierDataOnDemand;
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


public class ManufacturerIdentifierControllerTest extends AbstractControllerTest {

    private ManufacturerIdentifierController manufactureridentifierController = new ManufacturerIdentifierController();

    @Autowired
    private ManufacturerIdentifierDataOnDemand manufactureridentifierDataOnDemand;

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

        ManufacturerIdentifier manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();

        String result = manufactureridentifierController.create(manufactureridentifier, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/manufactureridentifiers/"+manufactureridentifier.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();

        result = manufactureridentifierController.create(manufactureridentifier, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/manufactureridentifiers/"+manufactureridentifier.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();

        result = manufactureridentifierController.create(manufactureridentifier, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/manufactureridentifiers/"+manufactureridentifier.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ManufacturerIdentifier manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();

        String result = manufactureridentifierController.create(manufactureridentifier, results, model, request);
        assertEquals("The form was not returned", "crud/manufactureridentifiers/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = manufactureridentifierController.createForm(model);
        assertEquals("The form was not returned", "crud/manufactureridentifiers/create", result);
    }

    @Test
    public void show() {
        ManufacturerIdentifier manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();
        Long id = manufactureridentifier.getId();
        Model model = getModel();
        String result = manufactureridentifierController.show(id, model);
        assertEquals("The show view was not returned", "crud/manufactureridentifiers/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = manufactureridentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufactureridentifiers/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = manufactureridentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufactureridentifiers/list", result);

        page = 1;
        size = null;
        manufactureridentifierController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/manufactureridentifiers/list", result);

        page = null;
        size = 10;
        manufactureridentifierController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/manufactureridentifiers/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ManufacturerIdentifier manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = manufactureridentifierController.update(manufactureridentifier, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/manufactureridentifiers/"+manufactureridentifier.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ManufacturerIdentifier manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = manufactureridentifierController.update(manufactureridentifier, results, model, request);
        assertEquals("The update view was not returned", "crud/manufactureridentifiers/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ManufacturerIdentifier manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();
        Long id = manufactureridentifier.getId();
        Model model = getModel();
        String result = manufactureridentifierController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/manufactureridentifiers/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ManufacturerIdentifier manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();
        Long id = manufactureridentifier.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = manufactureridentifierController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/manufactureridentifiers", result);

        manufactureridentifier = ManufacturerIdentifier.findManufacturerIdentifier(id);
        assertNull("ManufacturerIdentifier was not deleted", manufactureridentifier);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ManufacturerIdentifier manufactureridentifier = manufactureridentifierDataOnDemand.getRandomManufacturerIdentifier();
        Long id = manufactureridentifier.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = manufactureridentifierController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/manufactureridentifiers", result);

        manufactureridentifier = ManufacturerIdentifier.findManufacturerIdentifier(id);
        assertNull("ManufacturerIdentifier was not deleted", manufactureridentifier);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = manufactureridentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufactureridentifiers/list", result);
    }
}

