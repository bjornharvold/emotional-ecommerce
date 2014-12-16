package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductImageItemStatus;
import com.lela.data.domain.entity.ProductImageItemStatusDataOnDemand;
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


public class ProductImageItemStatusControllerTest extends AbstractControllerTest {

    private ProductImageItemStatusController productimageitemstatusController = new ProductImageItemStatusController();

    @Autowired
    private ProductImageItemStatusDataOnDemand productimageitemstatusDataOnDemand;

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

        ProductImageItemStatus productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();

        String result = productimageitemstatusController.create(productimageitemstatus, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productimageitemstatus/"+productimageitemstatus.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();

        result = productimageitemstatusController.create(productimageitemstatus, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productimageitemstatus/"+productimageitemstatus.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();

        result = productimageitemstatusController.create(productimageitemstatus, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productimageitemstatus/"+productimageitemstatus.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductImageItemStatus productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();

        String result = productimageitemstatusController.create(productimageitemstatus, results, model, request);
        assertEquals("The form was not returned", "crud/productimageitemstatus/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productimageitemstatusController.createForm(model);
        assertEquals("The form was not returned", "crud/productimageitemstatus/create", result);
    }

    @Test
    public void show() {
        ProductImageItemStatus productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();
        Long id = productimageitemstatus.getId();
        Model model = getModel();
        String result = productimageitemstatusController.show(id, model);
        assertEquals("The show view was not returned", "crud/productimageitemstatus/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productimageitemstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimageitemstatus/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productimageitemstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimageitemstatus/list", result);

        page = 1;
        size = null;
        productimageitemstatusController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productimageitemstatus/list", result);

        page = null;
        size = 10;
        productimageitemstatusController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productimageitemstatus/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductImageItemStatus productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productimageitemstatusController.update(productimageitemstatus, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productimageitemstatus/"+productimageitemstatus.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductImageItemStatus productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productimageitemstatusController.update(productimageitemstatus, results, model, request);
        assertEquals("The update view was not returned", "crud/productimageitemstatus/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductImageItemStatus productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();
        Long id = productimageitemstatus.getId();
        Model model = getModel();
        String result = productimageitemstatusController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productimageitemstatus/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductImageItemStatus productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();
        Long id = productimageitemstatus.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productimageitemstatusController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productimageitemstatus", result);

        productimageitemstatus = ProductImageItemStatus.findProductImageItemStatus(id);
        assertNull("ProductImageItemStatus was not deleted", productimageitemstatus);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductImageItemStatus productimageitemstatus = productimageitemstatusDataOnDemand.getRandomProductImageItemStatus();
        Long id = productimageitemstatus.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productimageitemstatusController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productimageitemstatus", result);

        productimageitemstatus = ProductImageItemStatus.findProductImageItemStatus(id);
        assertNull("ProductImageItemStatus was not deleted", productimageitemstatus);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productimageitemstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimageitemstatus/list", result);
    }
}

