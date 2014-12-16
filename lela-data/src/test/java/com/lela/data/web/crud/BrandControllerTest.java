package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Brand;
import com.lela.data.domain.entity.BrandDataOnDemand;
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


public class BrandControllerTest extends AbstractControllerTest {

    private BrandController brandController = new BrandController();

    @Autowired
    private BrandDataOnDemand brandDataOnDemand;

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

        Brand brand = brandDataOnDemand.getRandomBrand();

        String result = brandController.create(brand, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brands/"+brand.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brand = brandDataOnDemand.getRandomBrand();

        result = brandController.create(brand, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brands/"+brand.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brand = brandDataOnDemand.getRandomBrand();

        result = brandController.create(brand, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brands/"+brand.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Brand brand = brandDataOnDemand.getRandomBrand();

        String result = brandController.create(brand, results, model, request);
        assertEquals("The form was not returned", "crud/brands/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandController.createForm(model);
        assertEquals("The form was not returned", "crud/brands/create", result);
    }

    @Test
    public void show() {
        Brand brand = brandDataOnDemand.getRandomBrand();
        Long id = brand.getId();
        Model model = getModel();
        String result = brandController.show(id, model);
        assertEquals("The show view was not returned", "crud/brands/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brands/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brands/list", result);

        page = 1;
        size = null;
        brandController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brands/list", result);

        page = null;
        size = 10;
        brandController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brands/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Brand brand = brandDataOnDemand.getRandomBrand();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandController.update(brand, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brands/"+brand.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Brand brand = brandDataOnDemand.getRandomBrand();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandController.update(brand, results, model, request);
        assertEquals("The update view was not returned", "crud/brands/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Brand brand = brandDataOnDemand.getRandomBrand();
        Long id = brand.getId();
        Model model = getModel();
        String result = brandController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brands/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Brand brand = brandDataOnDemand.getRandomBrand();
        Long id = brand.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brands", result);

        brand = Brand.findBrand(id);
        assertNull("Brand was not deleted", brand);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Brand brand = brandDataOnDemand.getRandomBrand();
        Long id = brand.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brands", result);

        brand = Brand.findBrand(id);
        assertNull("Brand was not deleted", brand);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brands/list", result);
    }
}

