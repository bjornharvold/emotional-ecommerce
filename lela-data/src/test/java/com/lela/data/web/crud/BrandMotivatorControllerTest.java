package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.BrandMotivator;
import com.lela.data.domain.entity.BrandMotivatorDataOnDemand;
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


public class BrandMotivatorControllerTest extends AbstractControllerTest {

    private BrandMotivatorController brandmotivatorController = new BrandMotivatorController();

    @Autowired
    private BrandMotivatorDataOnDemand brandmotivatorDataOnDemand;

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

        BrandMotivator brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();

        String result = brandmotivatorController.create(brandmotivator, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brandmotivators/"+brandmotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();

        result = brandmotivatorController.create(brandmotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandmotivators/"+brandmotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();

        result = brandmotivatorController.create(brandmotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandmotivators/"+brandmotivator.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        BrandMotivator brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();

        String result = brandmotivatorController.create(brandmotivator, results, model, request);
        assertEquals("The form was not returned", "crud/brandmotivators/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandmotivatorController.createForm(model);
        assertEquals("The form was not returned", "crud/brandmotivators/create", result);
    }

    @Test
    public void show() {
        BrandMotivator brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();
        Long id = brandmotivator.getId();
        Model model = getModel();
        String result = brandmotivatorController.show(id, model);
        assertEquals("The show view was not returned", "crud/brandmotivators/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandmotivators/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandmotivators/list", result);

        page = 1;
        size = null;
        brandmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brandmotivators/list", result);

        page = null;
        size = 10;
        brandmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brandmotivators/list", result);

    }

    @Test
    @Transactional
    public void update() {
        BrandMotivator brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandmotivatorController.update(brandmotivator, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brandmotivators/"+brandmotivator.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        BrandMotivator brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandmotivatorController.update(brandmotivator, results, model, request);
        assertEquals("The update view was not returned", "crud/brandmotivators/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        BrandMotivator brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();
        Long id = brandmotivator.getId();
        Model model = getModel();
        String result = brandmotivatorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brandmotivators/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        BrandMotivator brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();
        Long id = brandmotivator.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandmotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandmotivators", result);

        brandmotivator = BrandMotivator.findBrandMotivator(id);
        assertNull("BrandMotivator was not deleted", brandmotivator);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        BrandMotivator brandmotivator = brandmotivatorDataOnDemand.getRandomBrandMotivator();
        Long id = brandmotivator.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandmotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandmotivators", result);

        brandmotivator = BrandMotivator.findBrandMotivator(id);
        assertNull("BrandMotivator was not deleted", brandmotivator);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandmotivators/list", result);
    }
}

