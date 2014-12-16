package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.MerchantOffer;
import com.lela.data.domain.entity.MerchantOfferDataOnDemand;
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


public class MerchantOfferControllerTest extends AbstractControllerTest {

    private MerchantOfferController merchantofferController = new MerchantOfferController();

    @Autowired
    private MerchantOfferDataOnDemand merchantofferDataOnDemand;

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

        MerchantOffer merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();

        String result = merchantofferController.create(merchantoffer, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/merchantoffers/"+merchantoffer.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();

        result = merchantofferController.create(merchantoffer, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantoffers/"+merchantoffer.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();

        result = merchantofferController.create(merchantoffer, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantoffers/"+merchantoffer.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        MerchantOffer merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();

        String result = merchantofferController.create(merchantoffer, results, model, request);
        assertEquals("The form was not returned", "crud/merchantoffers/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = merchantofferController.createForm(model);
        assertEquals("The form was not returned", "crud/merchantoffers/create", result);
    }

    @Test
    public void show() {
        MerchantOffer merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();
        Long id = merchantoffer.getId();
        Model model = getModel();
        String result = merchantofferController.show(id, model);
        assertEquals("The show view was not returned", "crud/merchantoffers/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantofferController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantoffers/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantofferController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantoffers/list", result);

        page = 1;
        size = null;
        merchantofferController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/merchantoffers/list", result);

        page = null;
        size = 10;
        merchantofferController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/merchantoffers/list", result);

    }

    @Test
    @Transactional
    public void update() {
        MerchantOffer merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantofferController.update(merchantoffer, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/merchantoffers/"+merchantoffer.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        MerchantOffer merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantofferController.update(merchantoffer, results, model, request);
        assertEquals("The update view was not returned", "crud/merchantoffers/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        MerchantOffer merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();
        Long id = merchantoffer.getId();
        Model model = getModel();
        String result = merchantofferController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/merchantoffers/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        MerchantOffer merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();
        Long id = merchantoffer.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantofferController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantoffers", result);

        merchantoffer = MerchantOffer.findMerchantOffer(id);
        assertNull("MerchantOffer was not deleted", merchantoffer);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        MerchantOffer merchantoffer = merchantofferDataOnDemand.getRandomMerchantOffer();
        Long id = merchantoffer.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantofferController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantoffers", result);

        merchantoffer = MerchantOffer.findMerchantOffer(id);
        assertNull("MerchantOffer was not deleted", merchantoffer);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = merchantofferController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantoffers/list", result);
    }
}

