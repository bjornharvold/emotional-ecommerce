package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ColorPrimaryColor;
import com.lela.data.domain.entity.ColorPrimaryColorDataOnDemand;
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


public class ColorPrimaryColorControllerTest extends AbstractControllerTest {

    private ColorPrimaryColorController colorprimarycolorController = new ColorPrimaryColorController();

    @Autowired
    private ColorPrimaryColorDataOnDemand colorprimarycolorDataOnDemand;

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

        ColorPrimaryColor colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();

        String result = colorprimarycolorController.create(colorprimarycolor, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/colorprimarycolors/"+colorprimarycolor.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();

        result = colorprimarycolorController.create(colorprimarycolor, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/colorprimarycolors/"+colorprimarycolor.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();

        result = colorprimarycolorController.create(colorprimarycolor, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/colorprimarycolors/"+colorprimarycolor.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ColorPrimaryColor colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();

        String result = colorprimarycolorController.create(colorprimarycolor, results, model, request);
        assertEquals("The form was not returned", "crud/colorprimarycolors/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = colorprimarycolorController.createForm(model);
        assertEquals("The form was not returned", "crud/colorprimarycolors/create", result);
    }

    @Test
    public void show() {
        ColorPrimaryColor colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();
        Long id = colorprimarycolor.getId();
        Model model = getModel();
        String result = colorprimarycolorController.show(id, model);
        assertEquals("The show view was not returned", "crud/colorprimarycolors/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = colorprimarycolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/colorprimarycolors/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = colorprimarycolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/colorprimarycolors/list", result);

        page = 1;
        size = null;
        colorprimarycolorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/colorprimarycolors/list", result);

        page = null;
        size = 10;
        colorprimarycolorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/colorprimarycolors/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ColorPrimaryColor colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = colorprimarycolorController.update(colorprimarycolor, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/colorprimarycolors/"+colorprimarycolor.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ColorPrimaryColor colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = colorprimarycolorController.update(colorprimarycolor, results, model, request);
        assertEquals("The update view was not returned", "crud/colorprimarycolors/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ColorPrimaryColor colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();
        Long id = colorprimarycolor.getId();
        Model model = getModel();
        String result = colorprimarycolorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/colorprimarycolors/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ColorPrimaryColor colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();
        Long id = colorprimarycolor.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = colorprimarycolorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/colorprimarycolors", result);

        colorprimarycolor = ColorPrimaryColor.findColorPrimaryColor(id);
        assertNull("ColorPrimaryColor was not deleted", colorprimarycolor);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ColorPrimaryColor colorprimarycolor = colorprimarycolorDataOnDemand.getRandomColorPrimaryColor();
        Long id = colorprimarycolor.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = colorprimarycolorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/colorprimarycolors", result);

        colorprimarycolor = ColorPrimaryColor.findColorPrimaryColor(id);
        assertNull("ColorPrimaryColor was not deleted", colorprimarycolor);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = colorprimarycolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/colorprimarycolors/list", result);
    }
}

