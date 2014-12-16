package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.BrandCategory;
import com.lela.data.domain.entity.BrandCategoryDataOnDemand;
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


public class BrandCategoryControllerTest extends AbstractControllerTest {

    private BrandCategoryController brandcategoryController = new BrandCategoryController();

    @Autowired
    private BrandCategoryDataOnDemand brandcategoryDataOnDemand;

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

        BrandCategory brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();

        String result = brandcategoryController.create(brandcategory, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brandcategorys/"+brandcategory.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();

        result = brandcategoryController.create(brandcategory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandcategorys/"+brandcategory.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();

        result = brandcategoryController.create(brandcategory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandcategorys/"+brandcategory.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        BrandCategory brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();

        String result = brandcategoryController.create(brandcategory, results, model, request);
        assertEquals("The form was not returned", "crud/brandcategorys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandcategoryController.createForm(model);
        assertEquals("The form was not returned", "crud/brandcategorys/create", result);
    }

    @Test
    public void show() {
        BrandCategory brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();
        Long id = brandcategory.getId();
        Model model = getModel();
        String result = brandcategoryController.show(id, model);
        assertEquals("The show view was not returned", "crud/brandcategorys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandcategoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategorys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandcategoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategorys/list", result);

        page = 1;
        size = null;
        brandcategoryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brandcategorys/list", result);

        page = null;
        size = 10;
        brandcategoryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brandcategorys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        BrandCategory brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandcategoryController.update(brandcategory, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brandcategorys/"+brandcategory.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        BrandCategory brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandcategoryController.update(brandcategory, results, model, request);
        assertEquals("The update view was not returned", "crud/brandcategorys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        BrandCategory brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();
        Long id = brandcategory.getId();
        Model model = getModel();
        String result = brandcategoryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brandcategorys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        BrandCategory brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();
        Long id = brandcategory.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandcategoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandcategorys", result);

        brandcategory = BrandCategory.findBrandCategory(id);
        assertNull("BrandCategory was not deleted", brandcategory);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        BrandCategory brandcategory = brandcategoryDataOnDemand.getRandomBrandCategory();
        Long id = brandcategory.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandcategoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandcategorys", result);

        brandcategory = BrandCategory.findBrandCategory(id);
        assertNull("BrandCategory was not deleted", brandcategory);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandcategoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcategorys/list", result);
    }
}

