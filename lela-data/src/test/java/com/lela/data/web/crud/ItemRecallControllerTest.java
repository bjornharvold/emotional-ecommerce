package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemRecall;
import com.lela.data.domain.entity.ItemRecallDataOnDemand;
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


public class ItemRecallControllerTest extends AbstractControllerTest {

    private ItemRecallController itemrecallController = new ItemRecallController();

    @Autowired
    private ItemRecallDataOnDemand itemrecallDataOnDemand;

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

        ItemRecall itemrecall = itemrecallDataOnDemand.getRandomItemRecall();

        String result = itemrecallController.create(itemrecall, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemrecalls/"+itemrecall.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemrecall = itemrecallDataOnDemand.getRandomItemRecall();

        result = itemrecallController.create(itemrecall, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemrecalls/"+itemrecall.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemrecall = itemrecallDataOnDemand.getRandomItemRecall();

        result = itemrecallController.create(itemrecall, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemrecalls/"+itemrecall.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemRecall itemrecall = itemrecallDataOnDemand.getRandomItemRecall();

        String result = itemrecallController.create(itemrecall, results, model, request);
        assertEquals("The form was not returned", "crud/itemrecalls/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemrecallController.createForm(model);
        assertEquals("The form was not returned", "crud/itemrecalls/create", result);
    }

    @Test
    public void show() {
        ItemRecall itemrecall = itemrecallDataOnDemand.getRandomItemRecall();
        Long id = itemrecall.getId();
        Model model = getModel();
        String result = itemrecallController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemrecalls/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemrecallController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemrecalls/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemrecallController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemrecalls/list", result);

        page = 1;
        size = null;
        itemrecallController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemrecalls/list", result);

        page = null;
        size = 10;
        itemrecallController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemrecalls/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemRecall itemrecall = itemrecallDataOnDemand.getRandomItemRecall();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemrecallController.update(itemrecall, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemrecalls/"+itemrecall.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemRecall itemrecall = itemrecallDataOnDemand.getRandomItemRecall();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemrecallController.update(itemrecall, results, model, request);
        assertEquals("The update view was not returned", "crud/itemrecalls/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemRecall itemrecall = itemrecallDataOnDemand.getRandomItemRecall();
        Long id = itemrecall.getId();
        Model model = getModel();
        String result = itemrecallController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemrecalls/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemRecall itemrecall = itemrecallDataOnDemand.getRandomItemRecall();
        Long id = itemrecall.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemrecallController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemrecalls", result);

        itemrecall = ItemRecall.findItemRecall(id);
        assertNull("ItemRecall was not deleted", itemrecall);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemRecall itemrecall = itemrecallDataOnDemand.getRandomItemRecall();
        Long id = itemrecall.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemrecallController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemrecalls", result);

        itemrecall = ItemRecall.findItemRecall(id);
        assertNull("ItemRecall was not deleted", itemrecall);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemrecallController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemrecalls/list", result);
    }
}

