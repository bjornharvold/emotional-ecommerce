package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.CategoryCategoryGroup;
import com.lela.data.domain.entity.CategoryCategoryGroupDataOnDemand;
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


public class CategoryCategoryGroupControllerTest extends AbstractControllerTest {

    private CategoryCategoryGroupController categorycategorygroupController = new CategoryCategoryGroupController();

    @Autowired
    private CategoryCategoryGroupDataOnDemand categorycategorygroupDataOnDemand;

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

        CategoryCategoryGroup categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();

        String result = categorycategorygroupController.create(categorycategorygroup, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/categorycategorygroups/"+categorycategorygroup.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();

        result = categorycategorygroupController.create(categorycategorygroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorycategorygroups/"+categorycategorygroup.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();

        result = categorycategorygroupController.create(categorycategorygroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/categorycategorygroups/"+categorycategorygroup.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        CategoryCategoryGroup categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();

        String result = categorycategorygroupController.create(categorycategorygroup, results, model, request);
        assertEquals("The form was not returned", "crud/categorycategorygroups/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = categorycategorygroupController.createForm(model);
        assertEquals("The form was not returned", "crud/categorycategorygroups/create", result);
    }

    @Test
    public void show() {
        CategoryCategoryGroup categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();
        Long id = categorycategorygroup.getId();
        Model model = getModel();
        String result = categorycategorygroupController.show(id, model);
        assertEquals("The show view was not returned", "crud/categorycategorygroups/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorycategorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorycategorygroups/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorycategorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorycategorygroups/list", result);

        page = 1;
        size = null;
        categorycategorygroupController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/categorycategorygroups/list", result);

        page = null;
        size = 10;
        categorycategorygroupController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/categorycategorygroups/list", result);

    }

    @Test
    @Transactional
    public void update() {
        CategoryCategoryGroup categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorycategorygroupController.update(categorycategorygroup, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/categorycategorygroups/"+categorycategorygroup.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        CategoryCategoryGroup categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = categorycategorygroupController.update(categorycategorygroup, results, model, request);
        assertEquals("The update view was not returned", "crud/categorycategorygroups/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        CategoryCategoryGroup categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();
        Long id = categorycategorygroup.getId();
        Model model = getModel();
        String result = categorycategorygroupController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/categorycategorygroups/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        CategoryCategoryGroup categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();
        Long id = categorycategorygroup.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = categorycategorygroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorycategorygroups", result);

        categorycategorygroup = CategoryCategoryGroup.findCategoryCategoryGroup(id);
        assertNull("CategoryCategoryGroup was not deleted", categorycategorygroup);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        CategoryCategoryGroup categorycategorygroup = categorycategorygroupDataOnDemand.getRandomCategoryCategoryGroup();
        Long id = categorycategorygroup.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = categorycategorygroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/categorycategorygroups", result);

        categorycategorygroup = CategoryCategoryGroup.findCategoryCategoryGroup(id);
        assertNull("CategoryCategoryGroup was not deleted", categorycategorygroup);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = categorycategorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/categorycategorygroups/list", result);
    }
}

