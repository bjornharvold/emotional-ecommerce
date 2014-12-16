package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.BrandCategoryItemtypeMotivator;
import com.lela.data.domain.entity.BrandCategoryItemtypeMotivatorDataOnDemand;
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


public class BrandCategoryItemtypeMotivatorControllerTest extends AbstractControllerTest {

    private BrandCategoryItemtypeMotivatorController brandcategoryitemtypemotivatorController = new BrandCategoryItemtypeMotivatorController();

    @Autowired
    private BrandCategoryItemtypeMotivatorDataOnDemand brandcategoryitemtypemotivatorDataOnDemand;

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

        BrandCategoryItemtypeMotivator brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();

        String result = brandcategoryitemtypemotivatorController.create(brandcategoryitemtypemotivator, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brandcategoryitemtypemotivators/"+brandcategoryitemtypemotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();

        result = brandcategoryitemtypemotivatorController.create(brandcategoryitemtypemotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandcategoryitemtypemotivators/"+brandcategoryitemtypemotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();

        result = brandcategoryitemtypemotivatorController.create(brandcategoryitemtypemotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandcategoryitemtypemotivators/"+brandcategoryitemtypemotivator.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        BrandCategoryItemtypeMotivator brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();

        String result = brandcategoryitemtypemotivatorController.create(brandcategoryitemtypemotivator, results, model, request);
        assertEquals("The form was not returned", "crud/brandcategoryitemtypemotivators/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandcategoryitemtypemotivatorController.createForm(model);
        assertEquals("The form was not returned", "crud/brandcategoryitemtypemotivators/create", result);
    }

    @Test
    public void show() {
        BrandCategoryItemtypeMotivator brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();
        Long id = brandcategoryitemtypemotivator.getId();
        Model model = getModel();
        String result = brandcategoryitemtypemotivatorController.show(id, model);
        assertEquals("The show view was not returned", "crud/brandcategoryitemtypemotivators/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandcategoryitemtypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategoryitemtypemotivators/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandcategoryitemtypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategoryitemtypemotivators/list", result);

        page = 1;
        size = null;
        brandcategoryitemtypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brandcategoryitemtypemotivators/list", result);

        page = null;
        size = 10;
        brandcategoryitemtypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brandcategoryitemtypemotivators/list", result);

    }

    @Test
    @Transactional
    public void update() {
        BrandCategoryItemtypeMotivator brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandcategoryitemtypemotivatorController.update(brandcategoryitemtypemotivator, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brandcategoryitemtypemotivators/"+brandcategoryitemtypemotivator.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        BrandCategoryItemtypeMotivator brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandcategoryitemtypemotivatorController.update(brandcategoryitemtypemotivator, results, model, request);
        assertEquals("The update view was not returned", "crud/brandcategoryitemtypemotivators/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        BrandCategoryItemtypeMotivator brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();
        Long id = brandcategoryitemtypemotivator.getId();
        Model model = getModel();
        String result = brandcategoryitemtypemotivatorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brandcategoryitemtypemotivators/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        BrandCategoryItemtypeMotivator brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();
        Long id = brandcategoryitemtypemotivator.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandcategoryitemtypemotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandcategoryitemtypemotivators", result);

        brandcategoryitemtypemotivator = BrandCategoryItemtypeMotivator.findBrandCategoryItemtypeMotivator(id);
        assertNull("BrandCategoryItemtypeMotivator was not deleted", brandcategoryitemtypemotivator);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        BrandCategoryItemtypeMotivator brandcategoryitemtypemotivator = brandcategoryitemtypemotivatorDataOnDemand.getRandomBrandCategoryItemtypeMotivator();
        Long id = brandcategoryitemtypemotivator.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandcategoryitemtypemotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandcategoryitemtypemotivators", result);

        brandcategoryitemtypemotivator = BrandCategoryItemtypeMotivator.findBrandCategoryItemtypeMotivator(id);
        assertNull("BrandCategoryItemtypeMotivator was not deleted", brandcategoryitemtypemotivator);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandcategoryitemtypemotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategoryitemtypemotivators/list", result);
    }
}

