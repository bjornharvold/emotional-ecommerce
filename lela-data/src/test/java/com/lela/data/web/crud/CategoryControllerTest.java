package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.CategoryDataOnDemand;
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


public class CategoryControllerTest extends AbstractControllerTest {

    private CategoryController categoryController = new CategoryController();

    @Autowired
    private CategoryDataOnDemand categoryDataOnDemand;

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

        Category category = categoryDataOnDemand.getRandomCategory();

        String result = categoryController.create(category, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/categorys/"+category.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        category = categoryDataOnDemand.getRandomCategory();

        result = categoryController.create(category, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorys/"+category.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        category = categoryDataOnDemand.getRandomCategory();

        result = categoryController.create(category, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorys/"+category.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Category category = categoryDataOnDemand.getRandomCategory();

        String result = categoryController.create(category, results, model, request);
        assertEquals("The form was not returned", "crud/categorys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = categoryController.createForm(model);
        assertEquals("The form was not returned", "crud/categorys/create", result);
    }

    @Test
    public void show() {
        Category category = categoryDataOnDemand.getRandomCategory();
        Long id = category.getId();
        Model model = getModel();
        String result = categoryController.show(id, model);
        assertEquals("The show view was not returned", "crud/categorys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorys/list", result);

        page = 1;
        size = null;
        categoryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/categorys/list", result);

        page = null;
        size = 10;
        categoryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/categorys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Category category = categoryDataOnDemand.getRandomCategory();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categoryController.update(category, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/categorys/"+category.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Category category = categoryDataOnDemand.getRandomCategory();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categoryController.update(category, results, model, request);
        assertEquals("The update view was not returned", "crud/categorys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Category category = categoryDataOnDemand.getRandomCategory();
        Long id = category.getId();
        Model model = getModel();
        String result = categoryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/categorys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Category category = categoryDataOnDemand.getRandomCategory();
        Long id = category.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorys", result);

        category = Category.findCategory(id);
        assertNull("Category was not deleted", category);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Category category = categoryDataOnDemand.getRandomCategory();
        Long id = category.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorys", result);

        category = Category.findCategory(id);
        assertNull("Category was not deleted", category);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = categoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorys/list", result);
    }
}

