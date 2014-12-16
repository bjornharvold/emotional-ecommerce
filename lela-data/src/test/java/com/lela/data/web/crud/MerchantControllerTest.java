package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Merchant;
import com.lela.data.domain.entity.MerchantDataOnDemand;
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


public class MerchantControllerTest extends AbstractControllerTest {

    private MerchantController merchantController = new MerchantController();

    @Autowired
    private MerchantDataOnDemand merchantDataOnDemand;

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

        Merchant merchant = merchantDataOnDemand.getRandomMerchant();

        String result = merchantController.create(merchant, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/merchants/"+merchant.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchant = merchantDataOnDemand.getRandomMerchant();

        result = merchantController.create(merchant, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchants/"+merchant.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchant = merchantDataOnDemand.getRandomMerchant();

        result = merchantController.create(merchant, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchants/"+merchant.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Merchant merchant = merchantDataOnDemand.getRandomMerchant();

        String result = merchantController.create(merchant, results, model, request);
        assertEquals("The form was not returned", "crud/merchants/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = merchantController.createForm(model);
        assertEquals("The form was not returned", "crud/merchants/create", result);
    }

    @Test
    public void show() {
        Merchant merchant = merchantDataOnDemand.getRandomMerchant();
        Long id = merchant.getId();
        Model model = getModel();
        String result = merchantController.show(id, model);
        assertEquals("The show view was not returned", "crud/merchants/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchants/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchants/list", result);

        page = 1;
        size = null;
        merchantController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/merchants/list", result);

        page = null;
        size = 10;
        merchantController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/merchants/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Merchant merchant = merchantDataOnDemand.getRandomMerchant();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantController.update(merchant, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/merchants/"+merchant.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Merchant merchant = merchantDataOnDemand.getRandomMerchant();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantController.update(merchant, results, model, request);
        assertEquals("The update view was not returned", "crud/merchants/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Merchant merchant = merchantDataOnDemand.getRandomMerchant();
        Long id = merchant.getId();
        Model model = getModel();
        String result = merchantController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/merchants/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Merchant merchant = merchantDataOnDemand.getRandomMerchant();
        Long id = merchant.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchants", result);

        merchant = Merchant.findMerchant(id);
        assertNull("Merchant was not deleted", merchant);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Merchant merchant = merchantDataOnDemand.getRandomMerchant();
        Long id = merchant.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchants", result);

        merchant = Merchant.findMerchant(id);
        assertNull("Merchant was not deleted", merchant);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = merchantController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchants/list", result);
    }
}

