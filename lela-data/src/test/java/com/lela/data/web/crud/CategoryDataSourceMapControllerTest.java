package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.CategoryDataSourceMap;
import com.lela.data.domain.entity.CategoryDataSourceMapDataOnDemand;
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


public class CategoryDataSourceMapControllerTest extends AbstractControllerTest {

    private CategoryDataSourceMapController categorydatasourcemapController = new CategoryDataSourceMapController();

    @Autowired
    private CategoryDataSourceMapDataOnDemand categorydatasourcemapDataOnDemand;

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

        CategoryDataSourceMap categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();

        String result = categorydatasourcemapController.create(categorydatasourcemap, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/categorydatasourcemaps/"+categorydatasourcemap.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();

        result = categorydatasourcemapController.create(categorydatasourcemap, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorydatasourcemaps/"+categorydatasourcemap.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();

        result = categorydatasourcemapController.create(categorydatasourcemap, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorydatasourcemaps/"+categorydatasourcemap.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        CategoryDataSourceMap categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();

        String result = categorydatasourcemapController.create(categorydatasourcemap, results, model, request);
        assertEquals("The form was not returned", "crud/categorydatasourcemaps/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = categorydatasourcemapController.createForm(model);
        assertEquals("The form was not returned", "crud/categorydatasourcemaps/create", result);
    }

    @Test
    public void show() {
        CategoryDataSourceMap categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();
        Long id = categorydatasourcemap.getId();
        Model model = getModel();
        String result = categorydatasourcemapController.show(id, model);
        assertEquals("The show view was not returned", "crud/categorydatasourcemaps/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorydatasourcemapController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorydatasourcemaps/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorydatasourcemapController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorydatasourcemaps/list", result);

        page = 1;
        size = null;
        categorydatasourcemapController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/categorydatasourcemaps/list", result);

        page = null;
        size = 10;
        categorydatasourcemapController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/categorydatasourcemaps/list", result);

    }

    @Test
    @Transactional
    public void update() {
        CategoryDataSourceMap categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorydatasourcemapController.update(categorydatasourcemap, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/categorydatasourcemaps/"+categorydatasourcemap.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        CategoryDataSourceMap categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorydatasourcemapController.update(categorydatasourcemap, results, model, request);
        assertEquals("The update view was not returned", "crud/categorydatasourcemaps/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        CategoryDataSourceMap categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();
        Long id = categorydatasourcemap.getId();
        Model model = getModel();
        String result = categorydatasourcemapController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/categorydatasourcemaps/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        CategoryDataSourceMap categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();
        Long id = categorydatasourcemap.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorydatasourcemapController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorydatasourcemaps", result);

        categorydatasourcemap = CategoryDataSourceMap.findCategoryDataSourceMap(id);
        assertNull("CategoryDataSourceMap was not deleted", categorydatasourcemap);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        CategoryDataSourceMap categorydatasourcemap = categorydatasourcemapDataOnDemand.getRandomCategoryDataSourceMap();
        Long id = categorydatasourcemap.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorydatasourcemapController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorydatasourcemaps", result);

        categorydatasourcemap = CategoryDataSourceMap.findCategoryDataSourceMap(id);
        assertNull("CategoryDataSourceMap was not deleted", categorydatasourcemap);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = categorydatasourcemapController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorydatasourcemaps/list", result);
    }
}

