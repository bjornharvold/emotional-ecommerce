package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemColorMerchant;
import com.lela.data.domain.entity.ItemColorMerchantDataOnDemand;
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


public class ItemColorMerchantControllerTest extends AbstractControllerTest {

    private ItemColorMerchantController itemcolormerchantController = new ItemColorMerchantController();

    @Autowired
    private ItemColorMerchantDataOnDemand itemcolormerchantDataOnDemand;

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

        ItemColorMerchant itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();

        String result = itemcolormerchantController.create(itemcolormerchant, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemcolormerchants/"+itemcolormerchant.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();

        result = itemcolormerchantController.create(itemcolormerchant, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemcolormerchants/"+itemcolormerchant.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();

        result = itemcolormerchantController.create(itemcolormerchant, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemcolormerchants/"+itemcolormerchant.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemColorMerchant itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();

        String result = itemcolormerchantController.create(itemcolormerchant, results, model, request);
        assertEquals("The form was not returned", "crud/itemcolormerchants/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemcolormerchantController.createForm(model);
        assertEquals("The form was not returned", "crud/itemcolormerchants/create", result);
    }

    @Test
    public void show() {
        ItemColorMerchant itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();
        Long id = itemcolormerchant.getId();
        Model model = getModel();
        String result = itemcolormerchantController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemcolormerchants/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemcolormerchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemcolormerchants/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemcolormerchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemcolormerchants/list", result);

        page = 1;
        size = null;
        itemcolormerchantController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemcolormerchants/list", result);

        page = null;
        size = 10;
        itemcolormerchantController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemcolormerchants/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemColorMerchant itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemcolormerchantController.update(itemcolormerchant, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemcolormerchants/"+itemcolormerchant.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemColorMerchant itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemcolormerchantController.update(itemcolormerchant, results, model, request);
        assertEquals("The update view was not returned", "crud/itemcolormerchants/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemColorMerchant itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();
        Long id = itemcolormerchant.getId();
        Model model = getModel();
        String result = itemcolormerchantController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemcolormerchants/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemColorMerchant itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();
        Long id = itemcolormerchant.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemcolormerchantController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemcolormerchants", result);

        itemcolormerchant = ItemColorMerchant.findItemColorMerchant(id);
        assertNull("ItemColorMerchant was not deleted", itemcolormerchant);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemColorMerchant itemcolormerchant = itemcolormerchantDataOnDemand.getRandomItemColorMerchant();
        Long id = itemcolormerchant.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemcolormerchantController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemcolormerchants", result);

        itemcolormerchant = ItemColorMerchant.findItemColorMerchant(id);
        assertNull("ItemColorMerchant was not deleted", itemcolormerchant);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemcolormerchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemcolormerchants/list", result);
    }
}

