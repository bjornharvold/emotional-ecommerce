package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.CategoryMotivator;
import com.lela.data.domain.entity.CategoryMotivatorDataOnDemand;
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


public class CategoryMotivatorControllerTest extends AbstractControllerTest {

    private CategoryMotivatorController categorymotivatorController = new CategoryMotivatorController();

    @Autowired
    private CategoryMotivatorDataOnDemand categorymotivatorDataOnDemand;

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

        CategoryMotivator categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();

        String result = categorymotivatorController.create(categorymotivator, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/categorymotivators/"+categorymotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();

        result = categorymotivatorController.create(categorymotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorymotivators/"+categorymotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();

        result = categorymotivatorController.create(categorymotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorymotivators/"+categorymotivator.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        CategoryMotivator categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();

        String result = categorymotivatorController.create(categorymotivator, results, model, request);
        assertEquals("The form was not returned", "crud/categorymotivators/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = categorymotivatorController.createForm(model);
        assertEquals("The form was not returned", "crud/categorymotivators/create", result);
    }

    @Test
    public void show() {
        CategoryMotivator categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();
        Long id = categorymotivator.getId();
        Model model = getModel();
        String result = categorymotivatorController.show(id, model);
        assertEquals("The show view was not returned", "crud/categorymotivators/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorymotivators/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorymotivators/list", result);

        page = 1;
        size = null;
        categorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/categorymotivators/list", result);

        page = null;
        size = 10;
        categorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/categorymotivators/list", result);

    }

    @Test
    @Transactional
    public void update() {
        CategoryMotivator categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorymotivatorController.update(categorymotivator, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/categorymotivators/"+categorymotivator.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        CategoryMotivator categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorymotivatorController.update(categorymotivator, results, model, request);
        assertEquals("The update view was not returned", "crud/categorymotivators/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        CategoryMotivator categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();
        Long id = categorymotivator.getId();
        Model model = getModel();
        String result = categorymotivatorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/categorymotivators/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        CategoryMotivator categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();
        Long id = categorymotivator.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorymotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorymotivators", result);

        categorymotivator = CategoryMotivator.findCategoryMotivator(id);
        assertNull("CategoryMotivator was not deleted", categorymotivator);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        CategoryMotivator categorymotivator = categorymotivatorDataOnDemand.getRandomCategoryMotivator();
        Long id = categorymotivator.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorymotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorymotivators", result);

        categorymotivator = CategoryMotivator.findCategoryMotivator(id);
        assertNull("CategoryMotivator was not deleted", categorymotivator);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = categorymotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorymotivators/list", result);
    }
}

