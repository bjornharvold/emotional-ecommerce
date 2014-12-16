package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.PrimaryColor;
import com.lela.data.domain.entity.PrimaryColorDataOnDemand;
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


public class PrimaryColorControllerTest extends AbstractControllerTest {

    private PrimaryColorController primarycolorController = new PrimaryColorController();

    @Autowired
    private PrimaryColorDataOnDemand primarycolorDataOnDemand;

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

        PrimaryColor primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();

        String result = primarycolorController.create(primarycolor, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/primarycolors/"+primarycolor.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();

        result = primarycolorController.create(primarycolor, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/primarycolors/"+primarycolor.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();

        result = primarycolorController.create(primarycolor, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/primarycolors/"+primarycolor.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        PrimaryColor primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();

        String result = primarycolorController.create(primarycolor, results, model, request);
        assertEquals("The form was not returned", "crud/primarycolors/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = primarycolorController.createForm(model);
        assertEquals("The form was not returned", "crud/primarycolors/create", result);
    }

    @Test
    public void show() {
        PrimaryColor primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();
        Long id = primarycolor.getId();
        Model model = getModel();
        String result = primarycolorController.show(id, model);
        assertEquals("The show view was not returned", "crud/primarycolors/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = primarycolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/primarycolors/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = primarycolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/primarycolors/list", result);

        page = 1;
        size = null;
        primarycolorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/primarycolors/list", result);

        page = null;
        size = 10;
        primarycolorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/primarycolors/list", result);

    }

    @Test
    @Transactional
    public void update() {
        PrimaryColor primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = primarycolorController.update(primarycolor, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/primarycolors/"+primarycolor.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        PrimaryColor primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = primarycolorController.update(primarycolor, results, model, request);
        assertEquals("The update view was not returned", "crud/primarycolors/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        PrimaryColor primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();
        Long id = primarycolor.getId();
        Model model = getModel();
        String result = primarycolorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/primarycolors/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        PrimaryColor primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();
        Long id = primarycolor.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = primarycolorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/primarycolors", result);

        primarycolor = PrimaryColor.findPrimaryColor(id);
        assertNull("PrimaryColor was not deleted", primarycolor);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        PrimaryColor primarycolor = primarycolorDataOnDemand.getRandomPrimaryColor();
        Long id = primarycolor.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = primarycolorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/primarycolors", result);

        primarycolor = PrimaryColor.findPrimaryColor(id);
        assertNull("PrimaryColor was not deleted", primarycolor);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = primarycolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/primarycolors/list", result);
    }
}

