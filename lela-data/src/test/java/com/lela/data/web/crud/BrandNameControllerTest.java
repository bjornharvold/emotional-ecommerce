package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.BrandName;
import com.lela.data.domain.entity.BrandNameDataOnDemand;
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


public class BrandNameControllerTest extends AbstractControllerTest {

    private BrandNameController brandnameController = new BrandNameController();

    @Autowired
    private BrandNameDataOnDemand brandnameDataOnDemand;

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

        BrandName brandname = brandnameDataOnDemand.getRandomBrandName();

        String result = brandnameController.create(brandname, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brandnames/"+brandname.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandname = brandnameDataOnDemand.getRandomBrandName();

        result = brandnameController.create(brandname, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandnames/"+brandname.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandname = brandnameDataOnDemand.getRandomBrandName();

        result = brandnameController.create(brandname, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandnames/"+brandname.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        BrandName brandname = brandnameDataOnDemand.getRandomBrandName();

        String result = brandnameController.create(brandname, results, model, request);
        assertEquals("The form was not returned", "crud/brandnames/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandnameController.createForm(model);
        assertEquals("The form was not returned", "crud/brandnames/create", result);
    }

    @Test
    public void show() {
        BrandName brandname = brandnameDataOnDemand.getRandomBrandName();
        Long id = brandname.getId();
        Model model = getModel();
        String result = brandnameController.show(id, model);
        assertEquals("The show view was not returned", "crud/brandnames/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandnameController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandnames/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandnameController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandnames/list", result);

        page = 1;
        size = null;
        brandnameController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brandnames/list", result);

        page = null;
        size = 10;
        brandnameController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brandnames/list", result);

    }

    @Test
    @Transactional
    public void update() {
        BrandName brandname = brandnameDataOnDemand.getRandomBrandName();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandnameController.update(brandname, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brandnames/"+brandname.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        BrandName brandname = brandnameDataOnDemand.getRandomBrandName();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandnameController.update(brandname, results, model, request);
        assertEquals("The update view was not returned", "crud/brandnames/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        BrandName brandname = brandnameDataOnDemand.getRandomBrandName();
        Long id = brandname.getId();
        Model model = getModel();
        String result = brandnameController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brandnames/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        BrandName brandname = brandnameDataOnDemand.getRandomBrandName();
        Long id = brandname.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandnameController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandnames", result);

        brandname = BrandName.findBrandName(id);
        assertNull("BrandName was not deleted", brandname);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        BrandName brandname = brandnameDataOnDemand.getRandomBrandName();
        Long id = brandname.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandnameController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandnames", result);

        brandname = BrandName.findBrandName(id);
        assertNull("BrandName was not deleted", brandname);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandnameController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandnames/list", result);
    }
}

