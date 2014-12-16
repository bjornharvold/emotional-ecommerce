package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Locale;
import com.lela.data.domain.entity.LocaleDataOnDemand;
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


public class LocaleControllerTest extends AbstractControllerTest {

    private LocaleController localeController = new LocaleController();

    @Autowired
    private LocaleDataOnDemand localeDataOnDemand;

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

        Locale locale = localeDataOnDemand.getRandomLocale();

        String result = localeController.create(locale, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/locales/"+locale.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        locale = localeDataOnDemand.getRandomLocale();

        result = localeController.create(locale, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/locales/"+locale.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        locale = localeDataOnDemand.getRandomLocale();

        result = localeController.create(locale, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/locales/"+locale.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Locale locale = localeDataOnDemand.getRandomLocale();

        String result = localeController.create(locale, results, model, request);
        assertEquals("The form was not returned", "crud/locales/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = localeController.createForm(model);
        assertEquals("The form was not returned", "crud/locales/create", result);
    }

    @Test
    public void show() {
        Locale locale = localeDataOnDemand.getRandomLocale();
        Long id = locale.getId();
        Model model = getModel();
        String result = localeController.show(id, model);
        assertEquals("The show view was not returned", "crud/locales/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = localeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/locales/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = localeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/locales/list", result);

        page = 1;
        size = null;
        localeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/locales/list", result);

        page = null;
        size = 10;
        localeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/locales/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Locale locale = localeDataOnDemand.getRandomLocale();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = localeController.update(locale, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/locales/"+locale.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Locale locale = localeDataOnDemand.getRandomLocale();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = localeController.update(locale, results, model, request);
        assertEquals("The update view was not returned", "crud/locales/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Locale locale = localeDataOnDemand.getRandomLocale();
        Long id = locale.getId();
        Model model = getModel();
        String result = localeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/locales/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Locale locale = localeDataOnDemand.getRandomLocale();
        Long id = locale.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = localeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/locales", result);

        locale = Locale.findLocale(id);
        assertNull("Locale was not deleted", locale);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Locale locale = localeDataOnDemand.getRandomLocale();
        Long id = locale.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = localeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/locales", result);

        locale = Locale.findLocale(id);
        assertNull("Locale was not deleted", locale);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = localeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/locales/list", result);
    }
}

