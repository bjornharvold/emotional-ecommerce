package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.CategoryDataSource;
import com.lela.data.domain.entity.CategoryDataSourceDataOnDemand;
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


public class CategoryDataSourceControllerTest extends AbstractControllerTest {

    private CategoryDataSourceController categorydatasourceController = new CategoryDataSourceController();

    @Autowired
    private CategoryDataSourceDataOnDemand categorydatasourceDataOnDemand;

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

        CategoryDataSource categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();

        String result = categorydatasourceController.create(categorydatasource, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/categorydatasources/"+categorydatasource.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();

        result = categorydatasourceController.create(categorydatasource, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorydatasources/"+categorydatasource.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();

        result = categorydatasourceController.create(categorydatasource, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorydatasources/"+categorydatasource.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        CategoryDataSource categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();

        String result = categorydatasourceController.create(categorydatasource, results, model, request);
        assertEquals("The form was not returned", "crud/categorydatasources/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = categorydatasourceController.createForm(model);
        assertEquals("The form was not returned", "crud/categorydatasources/create", result);
    }

    @Test
    public void show() {
        CategoryDataSource categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();
        Long id = categorydatasource.getId();
        Model model = getModel();
        String result = categorydatasourceController.show(id, model);
        assertEquals("The show view was not returned", "crud/categorydatasources/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorydatasourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorydatasources/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorydatasourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorydatasources/list", result);

        page = 1;
        size = null;
        categorydatasourceController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/categorydatasources/list", result);

        page = null;
        size = 10;
        categorydatasourceController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/categorydatasources/list", result);

    }

    @Test
    @Transactional
    public void update() {
        CategoryDataSource categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorydatasourceController.update(categorydatasource, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/categorydatasources/"+categorydatasource.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        CategoryDataSource categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorydatasourceController.update(categorydatasource, results, model, request);
        assertEquals("The update view was not returned", "crud/categorydatasources/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        CategoryDataSource categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();
        Long id = categorydatasource.getId();
        Model model = getModel();
        String result = categorydatasourceController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/categorydatasources/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        CategoryDataSource categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();
        Long id = categorydatasource.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorydatasourceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorydatasources", result);

        categorydatasource = CategoryDataSource.findCategoryDataSource(id);
        assertNull("CategoryDataSource was not deleted", categorydatasource);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        CategoryDataSource categorydatasource = categorydatasourceDataOnDemand.getRandomCategoryDataSource();
        Long id = categorydatasource.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorydatasourceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorydatasources", result);

        categorydatasource = CategoryDataSource.findCategoryDataSource(id);
        assertNull("CategoryDataSource was not deleted", categorydatasource);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = categorydatasourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorydatasources/list", result);
    }
}

