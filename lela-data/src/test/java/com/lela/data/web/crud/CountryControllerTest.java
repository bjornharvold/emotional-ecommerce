package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Country;
import com.lela.data.domain.entity.CountryDataOnDemand;
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


public class CountryControllerTest extends AbstractControllerTest {

    private CountryController countryController = new CountryController();

    @Autowired
    private CountryDataOnDemand countryDataOnDemand;

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

        Country country = countryDataOnDemand.getRandomCountry();

        String result = countryController.create(country, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/countrys/"+country.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        country = countryDataOnDemand.getRandomCountry();

        result = countryController.create(country, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/countrys/"+country.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        country = countryDataOnDemand.getRandomCountry();

        result = countryController.create(country, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/countrys/"+country.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Country country = countryDataOnDemand.getRandomCountry();

        String result = countryController.create(country, results, model, request);
        assertEquals("The form was not returned", "crud/countrys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = countryController.createForm(model);
        assertEquals("The form was not returned", "crud/countrys/create", result);
    }

    @Test
    public void show() {
        Country country = countryDataOnDemand.getRandomCountry();
        Long id = country.getId();
        Model model = getModel();
        String result = countryController.show(id, model);
        assertEquals("The show view was not returned", "crud/countrys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = countryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/countrys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = countryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/countrys/list", result);

        page = 1;
        size = null;
        countryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/countrys/list", result);

        page = null;
        size = 10;
        countryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/countrys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Country country = countryDataOnDemand.getRandomCountry();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = countryController.update(country, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/countrys/"+country.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Country country = countryDataOnDemand.getRandomCountry();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = countryController.update(country, results, model, request);
        assertEquals("The update view was not returned", "crud/countrys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Country country = countryDataOnDemand.getRandomCountry();
        Long id = country.getId();
        Model model = getModel();
        String result = countryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/countrys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Country country = countryDataOnDemand.getRandomCountry();
        Long id = country.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = countryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/countrys", result);

        country = Country.findCountry(id);
        assertNull("Country was not deleted", country);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Country country = countryDataOnDemand.getRandomCountry();
        Long id = country.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = countryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/countrys", result);

        country = Country.findCountry(id);
        assertNull("Country was not deleted", country);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = countryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/countrys/list", result);
    }
}

