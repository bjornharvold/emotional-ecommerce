package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemDataOnDemand;
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


public class ItemControllerTest extends AbstractControllerTest {

    private ItemController itemController = new ItemController();

    @Autowired
    private ItemDataOnDemand itemDataOnDemand;

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

        Item item = itemDataOnDemand.getRandomItem();

        String result = itemController.create(item, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/items/"+item.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        item = itemDataOnDemand.getRandomItem();

        result = itemController.create(item, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/items/"+item.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        item = itemDataOnDemand.getRandomItem();

        result = itemController.create(item, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/items/"+item.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Item item = itemDataOnDemand.getRandomItem();

        String result = itemController.create(item, results, model, request);
        assertEquals("The form was not returned", "crud/items/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemController.createForm(model);
        assertEquals("The form was not returned", "crud/items/create", result);
    }

    @Test
    public void show() {
        Item item = itemDataOnDemand.getRandomItem();
        Long id = item.getId();
        Model model = getModel();
        String result = itemController.show(id, model);
        assertEquals("The show view was not returned", "crud/items/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/items/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/items/list", result);

        page = 1;
        size = null;
        itemController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/items/list", result);

        page = null;
        size = 10;
        itemController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/items/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Item item = itemDataOnDemand.getRandomItem();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemController.update(item, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/items/"+item.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Item item = itemDataOnDemand.getRandomItem();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemController.update(item, results, model, request);
        assertEquals("The update view was not returned", "crud/items/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Item item = itemDataOnDemand.getRandomItem();
        Long id = item.getId();
        Model model = getModel();
        String result = itemController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/items/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Item item = itemDataOnDemand.getRandomItem();
        Long id = item.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/items", result);

        item = Item.findItem(id);
        assertNull("Item was not deleted", item);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Item item = itemDataOnDemand.getRandomItem();
        Long id = item.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/items", result);

        item = Item.findItem(id);
        assertNull("Item was not deleted", item);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/items/list", result);
    }
}

