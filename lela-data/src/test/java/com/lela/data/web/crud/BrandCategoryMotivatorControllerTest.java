package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.BrandCategoryMotivator;
import com.lela.data.domain.entity.BrandCategoryMotivatorDataOnDemand;
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


public class BrandCategoryMotivatorControllerTest extends AbstractControllerTest {

    private BrandCategoryMotivatorController brandcategorymotivatorController = new BrandCategoryMotivatorController();

    @Autowired
    private BrandCategoryMotivatorDataOnDemand brandcategorymotivatorDataOnDemand;

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

        BrandCategoryMotivator brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();

        String result = brandcategorymotivatorController.create(brandcategorymotivator, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brandcategorymotivators/"+brandcategorymotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();

        result = brandcategorymotivatorController.create(brandcategorymotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandcategorymotivators/"+brandcategorymotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();

        result = brandcategorymotivatorController.create(brandcategorymotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandcategorymotivators/"+brandcategorymotivator.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        BrandCategoryMotivator brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();

        String result = brandcategorymotivatorController.create(brandcategorymotivator, results, model, request);
        assertEquals("The form was not returned", "crud/brandcategorymotivators/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandcategorymotivatorController.createForm(model);
        assertEquals("The form was not returned", "crud/brandcategorymotivators/create", result);
    }

    @Test
    public void show() {
        BrandCategoryMotivator brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();
        Long id = brandcategorymotivator.getId();
        Model model = getModel();
        String result = brandcategorymotivatorController.show(id, model);
        assertEquals("The show view was not returned", "crud/brandcategorymotivators/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandcategorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategorymotivators/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandcategorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategorymotivators/list", result);

        page = 1;
        size = null;
        brandcategorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brandcategorymotivators/list", result);

        page = null;
        size = 10;
        brandcategorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brandcategorymotivators/list", result);

    }

    @Test
    @Transactional
    public void update() {
        BrandCategoryMotivator brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandcategorymotivatorController.update(brandcategorymotivator, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brandcategorymotivators/"+brandcategorymotivator.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        BrandCategoryMotivator brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandcategorymotivatorController.update(brandcategorymotivator, results, model, request);
        assertEquals("The update view was not returned", "crud/brandcategorymotivators/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        BrandCategoryMotivator brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();
        Long id = brandcategorymotivator.getId();
        Model model = getModel();
        String result = brandcategorymotivatorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brandcategorymotivators/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        BrandCategoryMotivator brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();
        Long id = brandcategorymotivator.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandcategorymotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandcategorymotivators", result);

        brandcategorymotivator = BrandCategoryMotivator.findBrandCategoryMotivator(id);
        assertNull("BrandCategoryMotivator was not deleted", brandcategorymotivator);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        BrandCategoryMotivator brandcategorymotivator = brandcategorymotivatorDataOnDemand.getRandomBrandCategoryMotivator();
        Long id = brandcategorymotivator.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandcategorymotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandcategorymotivators", result);

        brandcategorymotivator = BrandCategoryMotivator.findBrandCategoryMotivator(id);
        assertNull("BrandCategoryMotivator was not deleted", brandcategorymotivator);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandcategorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategorymotivators/list", result);
    }
}

