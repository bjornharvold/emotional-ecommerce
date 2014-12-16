package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.MerchantOfferHistory;
import com.lela.data.domain.entity.MerchantOfferHistoryDataOnDemand;
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


public class MerchantOfferHistoryControllerTest extends AbstractControllerTest {

    private MerchantOfferHistoryController merchantofferhistoryController = new MerchantOfferHistoryController();

    @Autowired
    private MerchantOfferHistoryDataOnDemand merchantofferhistoryDataOnDemand;

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

        MerchantOfferHistory merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();

        String result = merchantofferhistoryController.create(merchantofferhistory, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/merchantofferhistorys/"+merchantofferhistory.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();

        result = merchantofferhistoryController.create(merchantofferhistory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantofferhistorys/"+merchantofferhistory.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();

        result = merchantofferhistoryController.create(merchantofferhistory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantofferhistorys/"+merchantofferhistory.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        MerchantOfferHistory merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();

        String result = merchantofferhistoryController.create(merchantofferhistory, results, model, request);
        assertEquals("The form was not returned", "crud/merchantofferhistorys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = merchantofferhistoryController.createForm(model);
        assertEquals("The form was not returned", "crud/merchantofferhistorys/create", result);
    }

    @Test
    public void show() {
        MerchantOfferHistory merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();
        Long id = merchantofferhistory.getId();
        Model model = getModel();
        String result = merchantofferhistoryController.show(id, model);
        assertEquals("The show view was not returned", "crud/merchantofferhistorys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantofferhistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantofferhistorys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantofferhistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantofferhistorys/list", result);

        page = 1;
        size = null;
        merchantofferhistoryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/merchantofferhistorys/list", result);

        page = null;
        size = 10;
        merchantofferhistoryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/merchantofferhistorys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        MerchantOfferHistory merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantofferhistoryController.update(merchantofferhistory, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/merchantofferhistorys/"+merchantofferhistory.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        MerchantOfferHistory merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantofferhistoryController.update(merchantofferhistory, results, model, request);
        assertEquals("The update view was not returned", "crud/merchantofferhistorys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        MerchantOfferHistory merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();
        Long id = merchantofferhistory.getId();
        Model model = getModel();
        String result = merchantofferhistoryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/merchantofferhistorys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        MerchantOfferHistory merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();
        Long id = merchantofferhistory.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantofferhistoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantofferhistorys", result);

        merchantofferhistory = MerchantOfferHistory.findMerchantOfferHistory(id);
        assertNull("MerchantOfferHistory was not deleted", merchantofferhistory);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        MerchantOfferHistory merchantofferhistory = merchantofferhistoryDataOnDemand.getRandomMerchantOfferHistory();
        Long id = merchantofferhistory.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantofferhistoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantofferhistorys", result);

        merchantofferhistory = MerchantOfferHistory.findMerchantOfferHistory(id);
        assertNull("MerchantOfferHistory was not deleted", merchantofferhistory);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = merchantofferhistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantofferhistorys/list", result);
    }
}

