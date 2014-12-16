package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ManufacturerName;
import com.lela.data.domain.entity.ManufacturerNameDataOnDemand;
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


public class ManufacturerNameControllerTest extends AbstractControllerTest {

    private ManufacturerNameController manufacturernameController = new ManufacturerNameController();

    @Autowired
    private ManufacturerNameDataOnDemand manufacturernameDataOnDemand;

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

        ManufacturerName manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();

        String result = manufacturernameController.create(manufacturername, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/manufacturernames/"+manufacturername.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();

        result = manufacturernameController.create(manufacturername, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/manufacturernames/"+manufacturername.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();

        result = manufacturernameController.create(manufacturername, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/manufacturernames/"+manufacturername.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ManufacturerName manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();

        String result = manufacturernameController.create(manufacturername, results, model, request);
        assertEquals("The form was not returned", "crud/manufacturernames/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = manufacturernameController.createForm(model);
        assertEquals("The form was not returned", "crud/manufacturernames/create", result);
    }

    @Test
    public void show() {
        ManufacturerName manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();
        Long id = manufacturername.getId();
        Model model = getModel();
        String result = manufacturernameController.show(id, model);
        assertEquals("The show view was not returned", "crud/manufacturernames/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = manufacturernameController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufacturernames/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = manufacturernameController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufacturernames/list", result);

        page = 1;
        size = null;
        manufacturernameController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/manufacturernames/list", result);

        page = null;
        size = 10;
        manufacturernameController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/manufacturernames/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ManufacturerName manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = manufacturernameController.update(manufacturername, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/manufacturernames/"+manufacturername.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ManufacturerName manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = manufacturernameController.update(manufacturername, results, model, request);
        assertEquals("The update view was not returned", "crud/manufacturernames/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ManufacturerName manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();
        Long id = manufacturername.getId();
        Model model = getModel();
        String result = manufacturernameController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/manufacturernames/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ManufacturerName manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();
        Long id = manufacturername.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = manufacturernameController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/manufacturernames", result);

        manufacturername = ManufacturerName.findManufacturerName(id);
        assertNull("ManufacturerName was not deleted", manufacturername);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ManufacturerName manufacturername = manufacturernameDataOnDemand.getRandomManufacturerName();
        Long id = manufacturername.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = manufacturernameController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/manufacturernames", result);

        manufacturername = ManufacturerName.findManufacturerName(id);
        assertNull("ManufacturerName was not deleted", manufacturername);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = manufacturernameController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufacturernames/list", result);
    }
}

