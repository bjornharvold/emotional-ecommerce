package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemIdentifier;
import com.lela.data.domain.entity.ItemIdentifierDataOnDemand;
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


public class ItemIdentifierControllerTest extends AbstractControllerTest {

    private ItemIdentifierController itemidentifierController = new ItemIdentifierController();

    @Autowired
    private ItemIdentifierDataOnDemand itemidentifierDataOnDemand;

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

        ItemIdentifier itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();

        String result = itemidentifierController.create(itemidentifier, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemidentifiers/"+itemidentifier.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();

        result = itemidentifierController.create(itemidentifier, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemidentifiers/"+itemidentifier.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();

        result = itemidentifierController.create(itemidentifier, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemidentifiers/"+itemidentifier.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemIdentifier itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();

        String result = itemidentifierController.create(itemidentifier, results, model, request);
        assertEquals("The form was not returned", "crud/itemidentifiers/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemidentifierController.createForm(model);
        assertEquals("The form was not returned", "crud/itemidentifiers/create", result);
    }

    @Test
    public void show() {
        ItemIdentifier itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();
        Long id = itemidentifier.getId();
        Model model = getModel();
        String result = itemidentifierController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemidentifiers/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemidentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemidentifiers/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemidentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemidentifiers/list", result);

        page = 1;
        size = null;
        itemidentifierController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemidentifiers/list", result);

        page = null;
        size = 10;
        itemidentifierController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemidentifiers/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemIdentifier itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemidentifierController.update(itemidentifier, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemidentifiers/"+itemidentifier.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemIdentifier itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemidentifierController.update(itemidentifier, results, model, request);
        assertEquals("The update view was not returned", "crud/itemidentifiers/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemIdentifier itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();
        Long id = itemidentifier.getId();
        Model model = getModel();
        String result = itemidentifierController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemidentifiers/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemIdentifier itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();
        Long id = itemidentifier.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemidentifierController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemidentifiers", result);

        itemidentifier = ItemIdentifier.findItemIdentifier(id);
        assertNull("ItemIdentifier was not deleted", itemidentifier);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemIdentifier itemidentifier = itemidentifierDataOnDemand.getRandomItemIdentifier();
        Long id = itemidentifier.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemidentifierController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemidentifiers", result);

        itemidentifier = ItemIdentifier.findItemIdentifier(id);
        assertNull("ItemIdentifier was not deleted", itemidentifier);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemidentifierController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemidentifiers/list", result);
    }
}

