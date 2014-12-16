package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Color;
import com.lela.data.domain.entity.ColorDataOnDemand;
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


public class ColorControllerTest extends AbstractControllerTest {

    private ColorController colorController = new ColorController();

    @Autowired
    private ColorDataOnDemand colorDataOnDemand;

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

        Color color = colorDataOnDemand.getRandomColor();

        String result = colorController.create(color, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/colors/"+color.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        color = colorDataOnDemand.getRandomColor();

        result = colorController.create(color, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/colors/"+color.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        color = colorDataOnDemand.getRandomColor();

        result = colorController.create(color, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/colors/"+color.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Color color = colorDataOnDemand.getRandomColor();

        String result = colorController.create(color, results, model, request);
        assertEquals("The form was not returned", "crud/colors/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = colorController.createForm(model);
        assertEquals("The form was not returned", "crud/colors/create", result);
    }

    @Test
    public void show() {
        Color color = colorDataOnDemand.getRandomColor();
        Long id = color.getId();
        Model model = getModel();
        String result = colorController.show(id, model);
        assertEquals("The show view was not returned", "crud/colors/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = colorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/colors/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = colorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/colors/list", result);

        page = 1;
        size = null;
        colorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/colors/list", result);

        page = null;
        size = 10;
        colorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/colors/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Color color = colorDataOnDemand.getRandomColor();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = colorController.update(color, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/colors/"+color.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Color color = colorDataOnDemand.getRandomColor();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = colorController.update(color, results, model, request);
        assertEquals("The update view was not returned", "crud/colors/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Color color = colorDataOnDemand.getRandomColor();
        Long id = color.getId();
        Model model = getModel();
        String result = colorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/colors/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Color color = colorDataOnDemand.getRandomColor();
        Long id = color.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = colorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/colors", result);

        color = Color.findColor(id);
        assertNull("Color was not deleted", color);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Color color = colorDataOnDemand.getRandomColor();
        Long id = color.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = colorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/colors", result);

        color = Color.findColor(id);
        assertNull("Color was not deleted", color);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = colorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/colors/list", result);
    }
}

