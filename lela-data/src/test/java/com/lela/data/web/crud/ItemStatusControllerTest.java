package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemStatus;
import com.lela.data.domain.entity.ItemStatusDataOnDemand;
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


public class ItemStatusControllerTest extends AbstractControllerTest {

    private ItemStatusController itemstatusController = new ItemStatusController();

    @Autowired
    private ItemStatusDataOnDemand itemstatusDataOnDemand;

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

        ItemStatus itemstatus = itemstatusDataOnDemand.getRandomItemStatus();

        String result = itemstatusController.create(itemstatus, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemstatus/"+itemstatus.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemstatus = itemstatusDataOnDemand.getRandomItemStatus();

        result = itemstatusController.create(itemstatus, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemstatus/"+itemstatus.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemstatus = itemstatusDataOnDemand.getRandomItemStatus();

        result = itemstatusController.create(itemstatus, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemstatus/"+itemstatus.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemStatus itemstatus = itemstatusDataOnDemand.getRandomItemStatus();

        String result = itemstatusController.create(itemstatus, results, model, request);
        assertEquals("The form was not returned", "crud/itemstatus/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemstatusController.createForm(model);
        assertEquals("The form was not returned", "crud/itemstatus/create", result);
    }

    @Test
    public void show() {
        ItemStatus itemstatus = itemstatusDataOnDemand.getRandomItemStatus();
        Long id = itemstatus.getId();
        Model model = getModel();
        String result = itemstatusController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemstatus/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemstatus/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemstatus/list", result);

        page = 1;
        size = null;
        itemstatusController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemstatus/list", result);

        page = null;
        size = 10;
        itemstatusController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemstatus/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemStatus itemstatus = itemstatusDataOnDemand.getRandomItemStatus();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemstatusController.update(itemstatus, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemstatus/"+itemstatus.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemStatus itemstatus = itemstatusDataOnDemand.getRandomItemStatus();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemstatusController.update(itemstatus, results, model, request);
        assertEquals("The update view was not returned", "crud/itemstatus/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemStatus itemstatus = itemstatusDataOnDemand.getRandomItemStatus();
        Long id = itemstatus.getId();
        Model model = getModel();
        String result = itemstatusController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemstatus/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemStatus itemstatus = itemstatusDataOnDemand.getRandomItemStatus();
        Long id = itemstatus.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemstatusController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemstatus", result);

        itemstatus = ItemStatus.findItemStatus(id);
        assertNull("ItemStatus was not deleted", itemstatus);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemStatus itemstatus = itemstatusDataOnDemand.getRandomItemStatus();
        Long id = itemstatus.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemstatusController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemstatus", result);

        itemstatus = ItemStatus.findItemStatus(id);
        assertNull("ItemStatus was not deleted", itemstatus);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemstatus/list", result);
    }
}

