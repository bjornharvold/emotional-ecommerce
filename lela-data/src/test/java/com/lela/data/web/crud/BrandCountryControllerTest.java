package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.BrandCountry;
import com.lela.data.domain.entity.BrandCountryDataOnDemand;
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


public class BrandCountryControllerTest extends AbstractControllerTest {

    private BrandCountryController brandcountryController = new BrandCountryController();

    @Autowired
    private BrandCountryDataOnDemand brandcountryDataOnDemand;

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

        BrandCountry brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();

        String result = brandcountryController.create(brandcountry, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brandcountrys/"+brandcountry.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();

        result = brandcountryController.create(brandcountry, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandcountrys/"+brandcountry.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();

        result = brandcountryController.create(brandcountry, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandcountrys/"+brandcountry.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        BrandCountry brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();

        String result = brandcountryController.create(brandcountry, results, model, request);
        assertEquals("The form was not returned", "crud/brandcountrys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandcountryController.createForm(model);
        assertEquals("The form was not returned", "crud/brandcountrys/create", result);
    }

    @Test
    public void show() {
        BrandCountry brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();
        Long id = brandcountry.getId();
        Model model = getModel();
        String result = brandcountryController.show(id, model);
        assertEquals("The show view was not returned", "crud/brandcountrys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandcountryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcountrys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandcountryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcountrys/list", result);

        page = 1;
        size = null;
        brandcountryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brandcountrys/list", result);

        page = null;
        size = 10;
        brandcountryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brandcountrys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        BrandCountry brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandcountryController.update(brandcountry, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brandcountrys/"+brandcountry.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        BrandCountry brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandcountryController.update(brandcountry, results, model, request);
        assertEquals("The update view was not returned", "crud/brandcountrys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        BrandCountry brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();
        Long id = brandcountry.getId();
        Model model = getModel();
        String result = brandcountryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brandcountrys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        BrandCountry brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();
        Long id = brandcountry.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandcountryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandcountrys", result);

        brandcountry = BrandCountry.findBrandCountry(id);
        assertNull("BrandCountry was not deleted", brandcountry);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        BrandCountry brandcountry = brandcountryDataOnDemand.getRandomBrandCountry();
        Long id = brandcountry.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandcountryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandcountrys", result);

        brandcountry = BrandCountry.findBrandCountry(id);
        assertNull("BrandCountry was not deleted", brandcountry);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandcountryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandcountrys/list", result);
    }
}

