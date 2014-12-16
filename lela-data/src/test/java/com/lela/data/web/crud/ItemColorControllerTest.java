package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemColor;
import com.lela.data.domain.entity.ItemColorDataOnDemand;
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


public class ItemColorControllerTest extends AbstractControllerTest {

    private ItemColorController itemcolorController = new ItemColorController();

    @Autowired
    private ItemColorDataOnDemand itemcolorDataOnDemand;

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

        ItemColor itemcolor = itemcolorDataOnDemand.getRandomItemColor();

        String result = itemcolorController.create(itemcolor, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemcolors/"+itemcolor.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemcolor = itemcolorDataOnDemand.getRandomItemColor();

        result = itemcolorController.create(itemcolor, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemcolors/"+itemcolor.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemcolor = itemcolorDataOnDemand.getRandomItemColor();

        result = itemcolorController.create(itemcolor, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemcolors/"+itemcolor.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemColor itemcolor = itemcolorDataOnDemand.getRandomItemColor();

        String result = itemcolorController.create(itemcolor, results, model, request);
        assertEquals("The form was not returned", "crud/itemcolors/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemcolorController.createForm(model);
        assertEquals("The form was not returned", "crud/itemcolors/create", result);
    }

    @Test
    public void show() {
        ItemColor itemcolor = itemcolorDataOnDemand.getRandomItemColor();
        Long id = itemcolor.getId();
        Model model = getModel();
        String result = itemcolorController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemcolors/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemcolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemcolors/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemcolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemcolors/list", result);

        page = 1;
        size = null;
        itemcolorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemcolors/list", result);

        page = null;
        size = 10;
        itemcolorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemcolors/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemColor itemcolor = itemcolorDataOnDemand.getRandomItemColor();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemcolorController.update(itemcolor, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemcolors/"+itemcolor.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemColor itemcolor = itemcolorDataOnDemand.getRandomItemColor();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemcolorController.update(itemcolor, results, model, request);
        assertEquals("The update view was not returned", "crud/itemcolors/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemColor itemcolor = itemcolorDataOnDemand.getRandomItemColor();
        Long id = itemcolor.getId();
        Model model = getModel();
        String result = itemcolorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemcolors/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemColor itemcolor = itemcolorDataOnDemand.getRandomItemColor();
        Long id = itemcolor.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemcolorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemcolors", result);

        itemcolor = ItemColor.findItemColor(id);
        assertNull("ItemColor was not deleted", itemcolor);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemColor itemcolor = itemcolorDataOnDemand.getRandomItemColor();
        Long id = itemcolor.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemcolorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemcolors", result);

        itemcolor = ItemColor.findItemColor(id);
        assertNull("ItemColor was not deleted", itemcolor);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemcolorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemcolors/list", result);
    }
}

