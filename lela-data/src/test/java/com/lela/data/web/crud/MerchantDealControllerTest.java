package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.MerchantDeal;
import com.lela.data.domain.entity.MerchantDealDataOnDemand;
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


public class MerchantDealControllerTest extends AbstractControllerTest {

    private MerchantDealController merchantdealController = new MerchantDealController();

    @Autowired
    private MerchantDealDataOnDemand merchantdealDataOnDemand;

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

        MerchantDeal merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();

        String result = merchantdealController.create(merchantdeal, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/merchantdeals/"+merchantdeal.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();

        result = merchantdealController.create(merchantdeal, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantdeals/"+merchantdeal.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();

        result = merchantdealController.create(merchantdeal, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantdeals/"+merchantdeal.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        MerchantDeal merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();

        String result = merchantdealController.create(merchantdeal, results, model, request);
        assertEquals("The form was not returned", "crud/merchantdeals/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = merchantdealController.createForm(model);
        assertEquals("The form was not returned", "crud/merchantdeals/create", result);
    }

    @Test
    public void show() {
        MerchantDeal merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();
        Long id = merchantdeal.getId();
        Model model = getModel();
        String result = merchantdealController.show(id, model);
        assertEquals("The show view was not returned", "crud/merchantdeals/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantdealController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantdeals/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantdealController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantdeals/list", result);

        page = 1;
        size = null;
        merchantdealController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/merchantdeals/list", result);

        page = null;
        size = 10;
        merchantdealController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/merchantdeals/list", result);

    }

    @Test
    @Transactional
    public void update() {
        MerchantDeal merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantdealController.update(merchantdeal, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/merchantdeals/"+merchantdeal.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        MerchantDeal merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantdealController.update(merchantdeal, results, model, request);
        assertEquals("The update view was not returned", "crud/merchantdeals/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        MerchantDeal merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();
        Long id = merchantdeal.getId();
        Model model = getModel();
        String result = merchantdealController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/merchantdeals/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        MerchantDeal merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();
        Long id = merchantdeal.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantdealController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantdeals", result);

        merchantdeal = MerchantDeal.findMerchantDeal(id);
        assertNull("MerchantDeal was not deleted", merchantdeal);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        MerchantDeal merchantdeal = merchantdealDataOnDemand.getRandomMerchantDeal();
        Long id = merchantdeal.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantdealController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantdeals", result);

        merchantdeal = MerchantDeal.findMerchantDeal(id);
        assertNull("MerchantDeal was not deleted", merchantdeal);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = merchantdealController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantdeals/list", result);
    }
}

