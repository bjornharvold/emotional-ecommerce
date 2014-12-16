package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.MerchantSourceType;
import com.lela.data.domain.entity.MerchantSourceTypeDataOnDemand;
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


public class MerchantSourceTypeControllerTest extends AbstractControllerTest {

    private MerchantSourceTypeController merchantsourcetypeController = new MerchantSourceTypeController();

    @Autowired
    private MerchantSourceTypeDataOnDemand merchantsourcetypeDataOnDemand;

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

        MerchantSourceType merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();

        String result = merchantsourcetypeController.create(merchantsourcetype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/merchantsourcetypes/"+merchantsourcetype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();

        result = merchantsourcetypeController.create(merchantsourcetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantsourcetypes/"+merchantsourcetype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();

        result = merchantsourcetypeController.create(merchantsourcetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/merchantsourcetypes/"+merchantsourcetype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        MerchantSourceType merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();

        String result = merchantsourcetypeController.create(merchantsourcetype, results, model, request);
        assertEquals("The form was not returned", "crud/merchantsourcetypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = merchantsourcetypeController.createForm(model);
        assertEquals("The form was not returned", "crud/merchantsourcetypes/create", result);
    }

    @Test
    public void show() {
        MerchantSourceType merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();
        Long id = merchantsourcetype.getId();
        Model model = getModel();
        String result = merchantsourcetypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/merchantsourcetypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantsourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantsourcetypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantsourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantsourcetypes/list", result);

        page = 1;
        size = null;
        merchantsourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/merchantsourcetypes/list", result);

        page = null;
        size = 10;
        merchantsourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/merchantsourcetypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        MerchantSourceType merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantsourcetypeController.update(merchantsourcetype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/merchantsourcetypes/"+merchantsourcetype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        MerchantSourceType merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = merchantsourcetypeController.update(merchantsourcetype, results, model, request);
        assertEquals("The update view was not returned", "crud/merchantsourcetypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        MerchantSourceType merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();
        Long id = merchantsourcetype.getId();
        Model model = getModel();
        String result = merchantsourcetypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/merchantsourcetypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        MerchantSourceType merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();
        Long id = merchantsourcetype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = merchantsourcetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantsourcetypes", result);

        merchantsourcetype = MerchantSourceType.findMerchantSourceType(id);
        assertNull("MerchantSourceType was not deleted", merchantsourcetype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        MerchantSourceType merchantsourcetype = merchantsourcetypeDataOnDemand.getRandomMerchantSourceType();
        Long id = merchantsourcetype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = merchantsourcetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/merchantsourcetypes", result);

        merchantsourcetype = MerchantSourceType.findMerchantSourceType(id);
        assertNull("MerchantSourceType was not deleted", merchantsourcetype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = merchantsourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/merchantsourcetypes/list", result);
    }
}

