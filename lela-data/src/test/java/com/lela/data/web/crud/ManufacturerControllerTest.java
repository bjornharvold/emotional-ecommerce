package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Manufacturer;
import com.lela.data.domain.entity.ManufacturerDataOnDemand;
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


public class ManufacturerControllerTest extends AbstractControllerTest {

    private ManufacturerController manufacturerController = new ManufacturerController();

    @Autowired
    private ManufacturerDataOnDemand manufacturerDataOnDemand;

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

        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();

        String result = manufacturerController.create(manufacturer, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/manufacturers/"+manufacturer.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        manufacturer = manufacturerDataOnDemand.getRandomManufacturer();

        result = manufacturerController.create(manufacturer, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/manufacturers/"+manufacturer.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        manufacturer = manufacturerDataOnDemand.getRandomManufacturer();

        result = manufacturerController.create(manufacturer, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/manufacturers/"+manufacturer.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();

        String result = manufacturerController.create(manufacturer, results, model, request);
        assertEquals("The form was not returned", "crud/manufacturers/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = manufacturerController.createForm(model);
        assertEquals("The form was not returned", "crud/manufacturers/create", result);
    }

    @Test
    public void show() {
        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();
        Long id = manufacturer.getId();
        Model model = getModel();
        String result = manufacturerController.show(id, model);
        assertEquals("The show view was not returned", "crud/manufacturers/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = manufacturerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufacturers/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = manufacturerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufacturers/list", result);

        page = 1;
        size = null;
        manufacturerController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/manufacturers/list", result);

        page = null;
        size = 10;
        manufacturerController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/manufacturers/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = manufacturerController.update(manufacturer, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/manufacturers/"+manufacturer.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = manufacturerController.update(manufacturer, results, model, request);
        assertEquals("The update view was not returned", "crud/manufacturers/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();
        Long id = manufacturer.getId();
        Model model = getModel();
        String result = manufacturerController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/manufacturers/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();
        Long id = manufacturer.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = manufacturerController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/manufacturers", result);

        manufacturer = Manufacturer.findManufacturer(id);
        assertNull("Manufacturer was not deleted", manufacturer);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Manufacturer manufacturer = manufacturerDataOnDemand.getRandomManufacturer();
        Long id = manufacturer.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = manufacturerController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/manufacturers", result);

        manufacturer = Manufacturer.findManufacturer(id);
        assertNull("Manufacturer was not deleted", manufacturer);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = manufacturerController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/manufacturers/list", result);
    }
}

