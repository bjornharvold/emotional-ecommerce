package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.MerchantSource;
import com.lela.data.domain.entity.MerchantSourceDataOnDemand;
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


public class MerchantSourceControllerTest extends AbstractControllerTest {

    private MerchantSourceController merchantsourceController = new MerchantSourceController();

    @Autowired
    private MerchantSourceDataOnDemand merchantsourceDataOnDemand;

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

        MerchantSource merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();

        String result = merchantsourceController.create(merchantsource, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/merchantsources/"+merchantsource.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();

        result = merchantsourceController.create(merchantsource, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantsources/"+merchantsource.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();

        result = merchantsourceController.create(merchantsource, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantsources/"+merchantsource.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        MerchantSource merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();

        String result = merchantsourceController.create(merchantsource, results, model, request);
        assertEquals("The form was not returned", "crud/merchantsources/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = merchantsourceController.createForm(model);
        assertEquals("The form was not returned", "crud/merchantsources/create", result);
    }

    @Test
    public void show() {
        MerchantSource merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();
        Long id = merchantsource.getId();
        Model model = getModel();
        String result = merchantsourceController.show(id, model);
        assertEquals("The show view was not returned", "crud/merchantsources/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantsourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantsources/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantsourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantsources/list", result);

        page = 1;
        size = null;
        merchantsourceController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/merchantsources/list", result);

        page = null;
        size = 10;
        merchantsourceController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/merchantsources/list", result);

    }

    @Test
    @Transactional
    public void update() {
        MerchantSource merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantsourceController.update(merchantsource, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/merchantsources/"+merchantsource.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        MerchantSource merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantsourceController.update(merchantsource, results, model, request);
        assertEquals("The update view was not returned", "crud/merchantsources/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        MerchantSource merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();
        Long id = merchantsource.getId();
        Model model = getModel();
        String result = merchantsourceController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/merchantsources/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        MerchantSource merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();
        Long id = merchantsource.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantsourceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantsources", result);

        merchantsource = MerchantSource.findMerchantSource(id);
        assertNull("MerchantSource was not deleted", merchantsource);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        MerchantSource merchantsource = merchantsourceDataOnDemand.getRandomMerchantSource();
        Long id = merchantsource.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantsourceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantsources", result);

        merchantsource = MerchantSource.findMerchantSource(id);
        assertNull("MerchantSource was not deleted", merchantsource);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = merchantsourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantsources/list", result);
    }
}

