package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AdvertiserNameMerchant;
import com.lela.data.domain.entity.AdvertiserNameMerchantDataOnDemand;
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


public class AdvertiserNameMerchantControllerTest extends AbstractControllerTest {

    private AdvertiserNameMerchantController advertisernamemerchantController = new AdvertiserNameMerchantController();

    @Autowired
    private AdvertiserNameMerchantDataOnDemand advertisernamemerchantDataOnDemand;

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

        AdvertiserNameMerchant advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();

        String result = advertisernamemerchantController.create(advertisernamemerchant, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/advertisernamemerchants/"+advertisernamemerchant.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();

        result = advertisernamemerchantController.create(advertisernamemerchant, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/advertisernamemerchants/"+advertisernamemerchant.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();

        result = advertisernamemerchantController.create(advertisernamemerchant, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/advertisernamemerchants/"+advertisernamemerchant.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AdvertiserNameMerchant advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();

        String result = advertisernamemerchantController.create(advertisernamemerchant, results, model, request);
        assertEquals("The form was not returned", "crud/advertisernamemerchants/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = advertisernamemerchantController.createForm(model);
        assertEquals("The form was not returned", "crud/advertisernamemerchants/create", result);
    }

    @Test
    public void show() {
        AdvertiserNameMerchant advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();
        Long id = advertisernamemerchant.getId();
        Model model = getModel();
        String result = advertisernamemerchantController.show(id, model);
        assertEquals("The show view was not returned", "crud/advertisernamemerchants/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = advertisernamemerchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/advertisernamemerchants/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = advertisernamemerchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/advertisernamemerchants/list", result);

        page = 1;
        size = null;
        advertisernamemerchantController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/advertisernamemerchants/list", result);

        page = null;
        size = 10;
        advertisernamemerchantController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/advertisernamemerchants/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AdvertiserNameMerchant advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = advertisernamemerchantController.update(advertisernamemerchant, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/advertisernamemerchants/"+advertisernamemerchant.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AdvertiserNameMerchant advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = advertisernamemerchantController.update(advertisernamemerchant, results, model, request);
        assertEquals("The update view was not returned", "crud/advertisernamemerchants/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AdvertiserNameMerchant advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();
        Long id = advertisernamemerchant.getId();
        Model model = getModel();
        String result = advertisernamemerchantController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/advertisernamemerchants/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AdvertiserNameMerchant advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();
        Long id = advertisernamemerchant.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = advertisernamemerchantController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/advertisernamemerchants", result);

        advertisernamemerchant = AdvertiserNameMerchant.findAdvertiserNameMerchant(id);
        assertNull("AdvertiserNameMerchant was not deleted", advertisernamemerchant);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AdvertiserNameMerchant advertisernamemerchant = advertisernamemerchantDataOnDemand.getRandomAdvertiserNameMerchant();
        Long id = advertisernamemerchant.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = advertisernamemerchantController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/advertisernamemerchants", result);

        advertisernamemerchant = AdvertiserNameMerchant.findAdvertiserNameMerchant(id);
        assertNull("AdvertiserNameMerchant was not deleted", advertisernamemerchant);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = advertisernamemerchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/advertisernamemerchants/list", result);
    }
}

