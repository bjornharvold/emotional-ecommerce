package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemChanged;
import com.lela.data.domain.entity.ItemChangedDataOnDemand;
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


public class ItemChangedControllerTest extends AbstractControllerTest {

    private ItemChangedController itemchangedController = new ItemChangedController();

    @Autowired
    private ItemChangedDataOnDemand itemchangedDataOnDemand;

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

        ItemChanged itemchanged = itemchangedDataOnDemand.getRandomItemChanged();

        String result = itemchangedController.create(itemchanged, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemchangeds/"+itemchanged.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemchanged = itemchangedDataOnDemand.getRandomItemChanged();

        result = itemchangedController.create(itemchanged, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemchangeds/"+itemchanged.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemchanged = itemchangedDataOnDemand.getRandomItemChanged();

        result = itemchangedController.create(itemchanged, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemchangeds/"+itemchanged.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemChanged itemchanged = itemchangedDataOnDemand.getRandomItemChanged();

        String result = itemchangedController.create(itemchanged, results, model, request);
        assertEquals("The form was not returned", "crud/itemchangeds/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemchangedController.createForm(model);
        assertEquals("The form was not returned", "crud/itemchangeds/create", result);
    }

    @Test
    public void show() {
        ItemChanged itemchanged = itemchangedDataOnDemand.getRandomItemChanged();
        Long id = itemchanged.getId();
        Model model = getModel();
        String result = itemchangedController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemchangeds/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemchangedController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemchangeds/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemchangedController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemchangeds/list", result);

        page = 1;
        size = null;
        itemchangedController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemchangeds/list", result);

        page = null;
        size = 10;
        itemchangedController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemchangeds/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemChanged itemchanged = itemchangedDataOnDemand.getRandomItemChanged();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemchangedController.update(itemchanged, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemchangeds/"+itemchanged.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemChanged itemchanged = itemchangedDataOnDemand.getRandomItemChanged();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemchangedController.update(itemchanged, results, model, request);
        assertEquals("The update view was not returned", "crud/itemchangeds/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemChanged itemchanged = itemchangedDataOnDemand.getRandomItemChanged();
        Long id = itemchanged.getId();
        Model model = getModel();
        String result = itemchangedController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemchangeds/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemChanged itemchanged = itemchangedDataOnDemand.getRandomItemChanged();
        Long id = itemchanged.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemchangedController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemchangeds", result);

        itemchanged = ItemChanged.findItemChanged(id);
        assertNull("ItemChanged was not deleted", itemchanged);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemChanged itemchanged = itemchangedDataOnDemand.getRandomItemChanged();
        Long id = itemchanged.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemchangedController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemchangeds", result);

        itemchanged = ItemChanged.findItemChanged(id);
        assertNull("ItemChanged was not deleted", itemchanged);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemchangedController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemchangeds/list", result);
    }
}

