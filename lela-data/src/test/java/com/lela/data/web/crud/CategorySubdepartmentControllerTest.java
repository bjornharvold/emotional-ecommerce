package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.CategorySubdepartment;
import com.lela.data.domain.entity.CategorySubdepartmentDataOnDemand;
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


public class CategorySubdepartmentControllerTest extends AbstractControllerTest {

    private CategorySubdepartmentController categorysubdepartmentController = new CategorySubdepartmentController();

    @Autowired
    private CategorySubdepartmentDataOnDemand categorysubdepartmentDataOnDemand;

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

        CategorySubdepartment categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();

        String result = categorysubdepartmentController.create(categorysubdepartment, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/categorysubdepartments/"+categorysubdepartment.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();

        result = categorysubdepartmentController.create(categorysubdepartment, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorysubdepartments/"+categorysubdepartment.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();

        result = categorysubdepartmentController.create(categorysubdepartment, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorysubdepartments/"+categorysubdepartment.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        CategorySubdepartment categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();

        String result = categorysubdepartmentController.create(categorysubdepartment, results, model, request);
        assertEquals("The form was not returned", "crud/categorysubdepartments/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = categorysubdepartmentController.createForm(model);
        assertEquals("The form was not returned", "crud/categorysubdepartments/create", result);
    }

    @Test
    public void show() {
        CategorySubdepartment categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();
        Long id = categorysubdepartment.getId();
        Model model = getModel();
        String result = categorysubdepartmentController.show(id, model);
        assertEquals("The show view was not returned", "crud/categorysubdepartments/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorysubdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorysubdepartments/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorysubdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorysubdepartments/list", result);

        page = 1;
        size = null;
        categorysubdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/categorysubdepartments/list", result);

        page = null;
        size = 10;
        categorysubdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/categorysubdepartments/list", result);

    }

    @Test
    @Transactional
    public void update() {
        CategorySubdepartment categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorysubdepartmentController.update(categorysubdepartment, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/categorysubdepartments/"+categorysubdepartment.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        CategorySubdepartment categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorysubdepartmentController.update(categorysubdepartment, results, model, request);
        assertEquals("The update view was not returned", "crud/categorysubdepartments/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        CategorySubdepartment categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();
        Long id = categorysubdepartment.getId();
        Model model = getModel();
        String result = categorysubdepartmentController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/categorysubdepartments/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        CategorySubdepartment categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();
        Long id = categorysubdepartment.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorysubdepartmentController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorysubdepartments", result);

        categorysubdepartment = CategorySubdepartment.findCategorySubdepartment(id);
        assertNull("CategorySubdepartment was not deleted", categorysubdepartment);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        CategorySubdepartment categorysubdepartment = categorysubdepartmentDataOnDemand.getRandomCategorySubdepartment();
        Long id = categorysubdepartment.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorysubdepartmentController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorysubdepartments", result);

        categorysubdepartment = CategorySubdepartment.findCategorySubdepartment(id);
        assertNull("CategorySubdepartment was not deleted", categorysubdepartment);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = categorysubdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorysubdepartments/list", result);
    }
}

