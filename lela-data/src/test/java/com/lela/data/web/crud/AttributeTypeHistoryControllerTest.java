package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AttributeTypeHistory;
import com.lela.data.domain.entity.AttributeTypeHistoryDataOnDemand;
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


public class AttributeTypeHistoryControllerTest extends AbstractControllerTest {

    private AttributeTypeHistoryController attributetypehistoryController = new AttributeTypeHistoryController();

    @Autowired
    private AttributeTypeHistoryDataOnDemand attributetypehistoryDataOnDemand;

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

        AttributeTypeHistory attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();

        String result = attributetypehistoryController.create(attributetypehistory, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/attributetypehistorys/"+attributetypehistory.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();

        result = attributetypehistoryController.create(attributetypehistory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypehistorys/"+attributetypehistory.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();

        result = attributetypehistoryController.create(attributetypehistory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/attributetypehistorys/"+attributetypehistory.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AttributeTypeHistory attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();

        String result = attributetypehistoryController.create(attributetypehistory, results, model, request);
        assertEquals("The form was not returned", "crud/attributetypehistorys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = attributetypehistoryController.createForm(model);
        assertEquals("The form was not returned", "crud/attributetypehistorys/create", result);
    }

    @Test
    public void show() {
        AttributeTypeHistory attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();
        Long id = attributetypehistory.getId();
        Model model = getModel();
        String result = attributetypehistoryController.show(id, model);
        assertEquals("The show view was not returned", "crud/attributetypehistorys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypehistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypehistorys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypehistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypehistorys/list", result);

        page = 1;
        size = null;
        attributetypehistoryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/attributetypehistorys/list", result);

        page = null;
        size = 10;
        attributetypehistoryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/attributetypehistorys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AttributeTypeHistory attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypehistoryController.update(attributetypehistory, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/attributetypehistorys/"+attributetypehistory.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AttributeTypeHistory attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = attributetypehistoryController.update(attributetypehistory, results, model, request);
        assertEquals("The update view was not returned", "crud/attributetypehistorys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AttributeTypeHistory attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();
        Long id = attributetypehistory.getId();
        Model model = getModel();
        String result = attributetypehistoryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/attributetypehistorys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AttributeTypeHistory attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();
        Long id = attributetypehistory.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = attributetypehistoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypehistorys", result);

        attributetypehistory = AttributeTypeHistory.findAttributeTypeHistory(id);
        assertNull("AttributeTypeHistory was not deleted", attributetypehistory);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AttributeTypeHistory attributetypehistory = attributetypehistoryDataOnDemand.getRandomAttributeTypeHistory();
        Long id = attributetypehistory.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = attributetypehistoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/attributetypehistorys", result);

        attributetypehistory = AttributeTypeHistory.findAttributeTypeHistory(id);
        assertNull("AttributeTypeHistory was not deleted", attributetypehistory);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = attributetypehistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/attributetypehistorys/list", result);
    }
}

