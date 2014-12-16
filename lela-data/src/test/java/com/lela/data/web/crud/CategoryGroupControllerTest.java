package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.CategoryGroup;
import com.lela.data.domain.entity.CategoryGroupDataOnDemand;
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


public class CategoryGroupControllerTest extends AbstractControllerTest {

    private CategoryGroupController categorygroupController = new CategoryGroupController();

    @Autowired
    private CategoryGroupDataOnDemand categorygroupDataOnDemand;

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

        CategoryGroup categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();

        String result = categorygroupController.create(categorygroup, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/categorygroups/"+categorygroup.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();

        result = categorygroupController.create(categorygroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorygroups/"+categorygroup.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();

        result = categorygroupController.create(categorygroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorygroups/"+categorygroup.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        CategoryGroup categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();

        String result = categorygroupController.create(categorygroup, results, model, request);
        assertEquals("The form was not returned", "crud/categorygroups/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = categorygroupController.createForm(model);
        assertEquals("The form was not returned", "crud/categorygroups/create", result);
    }

    @Test
    public void show() {
        CategoryGroup categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();
        Long id = categorygroup.getId();
        Model model = getModel();
        String result = categorygroupController.show(id, model);
        assertEquals("The show view was not returned", "crud/categorygroups/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorygroups/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorygroups/list", result);

        page = 1;
        size = null;
        categorygroupController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/categorygroups/list", result);

        page = null;
        size = 10;
        categorygroupController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/categorygroups/list", result);

    }

    @Test
    @Transactional
    public void update() {
        CategoryGroup categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorygroupController.update(categorygroup, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/categorygroups/"+categorygroup.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        CategoryGroup categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorygroupController.update(categorygroup, results, model, request);
        assertEquals("The update view was not returned", "crud/categorygroups/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        CategoryGroup categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();
        Long id = categorygroup.getId();
        Model model = getModel();
        String result = categorygroupController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/categorygroups/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        CategoryGroup categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();
        Long id = categorygroup.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorygroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorygroups", result);

        categorygroup = CategoryGroup.findCategoryGroup(id);
        assertNull("CategoryGroup was not deleted", categorygroup);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        CategoryGroup categorygroup = categorygroupDataOnDemand.getRandomCategoryGroup();
        Long id = categorygroup.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorygroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorygroups", result);

        categorygroup = CategoryGroup.findCategoryGroup(id);
        assertNull("CategoryGroup was not deleted", categorygroup);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = categorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorygroups/list", result);
    }
}

