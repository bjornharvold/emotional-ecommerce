package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemAttributeHistory;
import com.lela.data.domain.entity.ItemAttributeHistoryDataOnDemand;
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


public class ItemAttributeHistoryControllerTest extends AbstractControllerTest {

    private ItemAttributeHistoryController itemattributehistoryController = new ItemAttributeHistoryController();

    @Autowired
    private ItemAttributeHistoryDataOnDemand itemattributehistoryDataOnDemand;

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

        ItemAttributeHistory itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();

        String result = itemattributehistoryController.create(itemattributehistory, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemattributehistorys/"+itemattributehistory.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();

        result = itemattributehistoryController.create(itemattributehistory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemattributehistorys/"+itemattributehistory.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();

        result = itemattributehistoryController.create(itemattributehistory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemattributehistorys/"+itemattributehistory.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemAttributeHistory itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();

        String result = itemattributehistoryController.create(itemattributehistory, results, model, request);
        assertEquals("The form was not returned", "crud/itemattributehistorys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemattributehistoryController.createForm(model);
        assertEquals("The form was not returned", "crud/itemattributehistorys/create", result);
    }

    @Test
    public void show() {
        ItemAttributeHistory itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();
        Long id = itemattributehistory.getId();
        Model model = getModel();
        String result = itemattributehistoryController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemattributehistorys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemattributehistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemattributehistorys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemattributehistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemattributehistorys/list", result);

        page = 1;
        size = null;
        itemattributehistoryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemattributehistorys/list", result);

        page = null;
        size = 10;
        itemattributehistoryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemattributehistorys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemAttributeHistory itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemattributehistoryController.update(itemattributehistory, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemattributehistorys/"+itemattributehistory.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemAttributeHistory itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemattributehistoryController.update(itemattributehistory, results, model, request);
        assertEquals("The update view was not returned", "crud/itemattributehistorys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemAttributeHistory itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();
        Long id = itemattributehistory.getId();
        Model model = getModel();
        String result = itemattributehistoryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemattributehistorys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemAttributeHistory itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();
        Long id = itemattributehistory.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemattributehistoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemattributehistorys", result);

        itemattributehistory = ItemAttributeHistory.findItemAttributeHistory(id);
        assertNull("ItemAttributeHistory was not deleted", itemattributehistory);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemAttributeHistory itemattributehistory = itemattributehistoryDataOnDemand.getRandomItemAttributeHistory();
        Long id = itemattributehistory.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemattributehistoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemattributehistorys", result);

        itemattributehistory = ItemAttributeHistory.findItemAttributeHistory(id);
        assertNull("ItemAttributeHistory was not deleted", itemattributehistory);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemattributehistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemattributehistorys/list", result);
    }
}

