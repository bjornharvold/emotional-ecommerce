package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AffiliateTransaction;
import com.lela.data.domain.entity.AffiliateTransactionDataOnDemand;
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


public class AffiliateTransactionControllerTest extends AbstractControllerTest {

    private AffiliateTransactionController affiliatetransactionController = new AffiliateTransactionController();

    @Autowired
    private AffiliateTransactionDataOnDemand affiliatetransactionDataOnDemand;

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

        AffiliateTransaction affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();

        String result = affiliatetransactionController.create(affiliatetransaction, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/affiliatetransactions/"+affiliatetransaction.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();

        result = affiliatetransactionController.create(affiliatetransaction, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/affiliatetransactions/"+affiliatetransaction.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();

        result = affiliatetransactionController.create(affiliatetransaction, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/affiliatetransactions/"+affiliatetransaction.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AffiliateTransaction affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();

        String result = affiliatetransactionController.create(affiliatetransaction, results, model, request);
        assertEquals("The form was not returned", "crud/affiliatetransactions/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = affiliatetransactionController.createForm(model);
        assertEquals("The form was not returned", "crud/affiliatetransactions/create", result);
    }

    @Test
    public void show() {
        AffiliateTransaction affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();
        Long id = affiliatetransaction.getId();
        Model model = getModel();
        String result = affiliatetransactionController.show(id, model);
        assertEquals("The show view was not returned", "crud/affiliatetransactions/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = affiliatetransactionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/affiliatetransactions/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = affiliatetransactionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/affiliatetransactions/list", result);

        page = 1;
        size = null;
        affiliatetransactionController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/affiliatetransactions/list", result);

        page = null;
        size = 10;
        affiliatetransactionController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/affiliatetransactions/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AffiliateTransaction affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = affiliatetransactionController.update(affiliatetransaction, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/affiliatetransactions/"+affiliatetransaction.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AffiliateTransaction affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = affiliatetransactionController.update(affiliatetransaction, results, model, request);
        assertEquals("The update view was not returned", "crud/affiliatetransactions/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AffiliateTransaction affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();
        Long id = affiliatetransaction.getId();
        Model model = getModel();
        String result = affiliatetransactionController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/affiliatetransactions/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AffiliateTransaction affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();
        Long id = affiliatetransaction.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = affiliatetransactionController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/affiliatetransactions", result);

        affiliatetransaction = AffiliateTransaction.findAffiliateTransaction(id);
        assertNull("AffiliateTransaction was not deleted", affiliatetransaction);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AffiliateTransaction affiliatetransaction = affiliatetransactionDataOnDemand.getRandomAffiliateTransaction();
        Long id = affiliatetransaction.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = affiliatetransactionController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/affiliatetransactions", result);

        affiliatetransaction = AffiliateTransaction.findAffiliateTransaction(id);
        assertNull("AffiliateTransaction was not deleted", affiliatetransaction);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = affiliatetransactionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/affiliatetransactions/list", result);
    }
}

