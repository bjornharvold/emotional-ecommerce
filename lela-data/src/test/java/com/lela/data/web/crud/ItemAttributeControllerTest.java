package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemAttribute;
import com.lela.data.domain.entity.ItemAttributeDataOnDemand;
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


public class ItemAttributeControllerTest extends AbstractControllerTest {

    private ItemAttributeController itemattributeController = new ItemAttributeController();

    @Autowired
    private ItemAttributeDataOnDemand itemattributeDataOnDemand;

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

        ItemAttribute itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();

        String result = itemattributeController.create(itemattribute, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemattributes/"+itemattribute.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();

        result = itemattributeController.create(itemattribute, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemattributes/"+itemattribute.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();

        result = itemattributeController.create(itemattribute, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemattributes/"+itemattribute.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemAttribute itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();

        String result = itemattributeController.create(itemattribute, results, model, request);
        assertEquals("The form was not returned", "crud/itemattributes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemattributeController.createForm(model);
        assertEquals("The form was not returned", "crud/itemattributes/create", result);
    }

    @Test
    public void show() {
        ItemAttribute itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();
        Long id = itemattribute.getId();
        Model model = getModel();
        String result = itemattributeController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemattributes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemattributeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemattributes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemattributeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemattributes/list", result);

        page = 1;
        size = null;
        itemattributeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemattributes/list", result);

        page = null;
        size = 10;
        itemattributeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemattributes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemAttribute itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemattributeController.update(itemattribute, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemattributes/"+itemattribute.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemAttribute itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemattributeController.update(itemattribute, results, model, request);
        assertEquals("The update view was not returned", "crud/itemattributes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemAttribute itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();
        Long id = itemattribute.getId();
        Model model = getModel();
        String result = itemattributeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemattributes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemAttribute itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();
        Long id = itemattribute.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemattributeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemattributes", result);

        itemattribute = ItemAttribute.findItemAttribute(id);
        assertNull("ItemAttribute was not deleted", itemattribute);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemAttribute itemattribute = itemattributeDataOnDemand.getRandomItemAttribute();
        Long id = itemattribute.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemattributeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemattributes", result);

        itemattribute = ItemAttribute.findItemAttribute(id);
        assertNull("ItemAttribute was not deleted", itemattribute);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemattributeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemattributes/list", result);
    }
}

