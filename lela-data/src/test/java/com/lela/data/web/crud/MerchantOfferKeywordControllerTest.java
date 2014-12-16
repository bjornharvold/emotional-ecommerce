package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.MerchantOfferKeyword;
import com.lela.data.domain.entity.MerchantOfferKeywordDataOnDemand;
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


public class MerchantOfferKeywordControllerTest extends AbstractControllerTest {

    private MerchantOfferKeywordController merchantofferkeywordController = new MerchantOfferKeywordController();

    @Autowired
    private MerchantOfferKeywordDataOnDemand merchantofferkeywordDataOnDemand;

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

        MerchantOfferKeyword merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();

        String result = merchantofferkeywordController.create(merchantofferkeyword, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/merchantofferkeywords/"+merchantofferkeyword.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();

        result = merchantofferkeywordController.create(merchantofferkeyword, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantofferkeywords/"+merchantofferkeyword.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();

        result = merchantofferkeywordController.create(merchantofferkeyword, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantofferkeywords/"+merchantofferkeyword.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        MerchantOfferKeyword merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();

        String result = merchantofferkeywordController.create(merchantofferkeyword, results, model, request);
        assertEquals("The form was not returned", "crud/merchantofferkeywords/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = merchantofferkeywordController.createForm(model);
        assertEquals("The form was not returned", "crud/merchantofferkeywords/create", result);
    }

    @Test
    public void show() {
        MerchantOfferKeyword merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();
        Long id = merchantofferkeyword.getId();
        Model model = getModel();
        String result = merchantofferkeywordController.show(id, model);
        assertEquals("The show view was not returned", "crud/merchantofferkeywords/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantofferkeywordController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantofferkeywords/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantofferkeywordController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantofferkeywords/list", result);

        page = 1;
        size = null;
        merchantofferkeywordController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/merchantofferkeywords/list", result);

        page = null;
        size = 10;
        merchantofferkeywordController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/merchantofferkeywords/list", result);

    }

    @Test
    @Transactional
    public void update() {
        MerchantOfferKeyword merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantofferkeywordController.update(merchantofferkeyword, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/merchantofferkeywords/"+merchantofferkeyword.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        MerchantOfferKeyword merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantofferkeywordController.update(merchantofferkeyword, results, model, request);
        assertEquals("The update view was not returned", "crud/merchantofferkeywords/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        MerchantOfferKeyword merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();
        Long id = merchantofferkeyword.getId();
        Model model = getModel();
        String result = merchantofferkeywordController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/merchantofferkeywords/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        MerchantOfferKeyword merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();
        Long id = merchantofferkeyword.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantofferkeywordController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantofferkeywords", result);

        merchantofferkeyword = MerchantOfferKeyword.findMerchantOfferKeyword(id);
        assertNull("MerchantOfferKeyword was not deleted", merchantofferkeyword);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        MerchantOfferKeyword merchantofferkeyword = merchantofferkeywordDataOnDemand.getRandomMerchantOfferKeyword();
        Long id = merchantofferkeyword.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantofferkeywordController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantofferkeywords", result);

        merchantofferkeyword = MerchantOfferKeyword.findMerchantOfferKeyword(id);
        assertNull("MerchantOfferKeyword was not deleted", merchantofferkeyword);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = merchantofferkeywordController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantofferkeywords/list", result);
    }
}

