package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.BrandIdentifier;
import com.lela.data.domain.entity.BrandIdentifierDataOnDemand;
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


public class BrandIdentifierControllerTest extends AbstractControllerTest {

    private BrandIdentifierController brandidentifierController = new BrandIdentifierController();

    @Autowired
    private BrandIdentifierDataOnDemand brandidentifierDataOnDemand;

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

        BrandIdentifier brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();

        String result = brandidentifierController.create(brandidentifier, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brandidentifiers/"+brandidentifier.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();

        result = brandidentifierController.create(brandidentifier, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandidentifiers/"+brandidentifier.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();

        result = brandidentifierController.create(brandidentifier, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandidentifiers/"+brandidentifier.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        BrandIdentifier brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();

        String result = brandidentifierController.create(brandidentifier, results, model, request);
        assertEquals("The form was not returned", "crud/brandidentifiers/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandidentifierController.createForm(model);
        assertEquals("The form was not returned", "crud/brandidentifiers/create", result);
    }

    @Test
    public void show() {
        BrandIdentifier brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();
        Long id = brandidentifier.getId();
        Model model = getModel();
        String result = brandidentifierController.show(id, model);
        assertEquals("The show view was not returned", "crud/brandidentifiers/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandidentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandidentifiers/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandidentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandidentifiers/list", result);

        page = 1;
        size = null;
        brandidentifierController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brandidentifiers/list", result);

        page = null;
        size = 10;
        brandidentifierController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brandidentifiers/list", result);

    }

    @Test
    @Transactional
    public void update() {
        BrandIdentifier brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandidentifierController.update(brandidentifier, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brandidentifiers/"+brandidentifier.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        BrandIdentifier brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandidentifierController.update(brandidentifier, results, model, request);
        assertEquals("The update view was not returned", "crud/brandidentifiers/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        BrandIdentifier brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();
        Long id = brandidentifier.getId();
        Model model = getModel();
        String result = brandidentifierController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brandidentifiers/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        BrandIdentifier brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();
        Long id = brandidentifier.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandidentifierController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandidentifiers", result);

        brandidentifier = BrandIdentifier.findBrandIdentifier(id);
        assertNull("BrandIdentifier was not deleted", brandidentifier);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        BrandIdentifier brandidentifier = brandidentifierDataOnDemand.getRandomBrandIdentifier();
        Long id = brandidentifier.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandidentifierController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandidentifiers", result);

        brandidentifier = BrandIdentifier.findBrandIdentifier(id);
        assertNull("BrandIdentifier was not deleted", brandidentifier);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandidentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandidentifiers/list", result);
    }
}

