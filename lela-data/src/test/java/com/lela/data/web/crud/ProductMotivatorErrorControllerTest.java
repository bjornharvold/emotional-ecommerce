package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductMotivatorError;
import com.lela.data.domain.entity.ProductMotivatorErrorDataOnDemand;
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


public class ProductMotivatorErrorControllerTest extends AbstractControllerTest {

    private ProductMotivatorErrorController productmotivatorerrorController = new ProductMotivatorErrorController();

    @Autowired
    private ProductMotivatorErrorDataOnDemand productmotivatorerrorDataOnDemand;

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

        ProductMotivatorError productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();

        String result = productmotivatorerrorController.create(productmotivatorerror, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productmotivatorerrors/"+productmotivatorerror.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();

        result = productmotivatorerrorController.create(productmotivatorerror, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productmotivatorerrors/"+productmotivatorerror.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();

        result = productmotivatorerrorController.create(productmotivatorerror, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productmotivatorerrors/"+productmotivatorerror.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductMotivatorError productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();

        String result = productmotivatorerrorController.create(productmotivatorerror, results, model, request);
        assertEquals("The form was not returned", "crud/productmotivatorerrors/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productmotivatorerrorController.createForm(model);
        assertEquals("The form was not returned", "crud/productmotivatorerrors/create", result);
    }

    @Test
    public void show() {
        ProductMotivatorError productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();
        Long id = productmotivatorerror.getId();
        Model model = getModel();
        String result = productmotivatorerrorController.show(id, model);
        assertEquals("The show view was not returned", "crud/productmotivatorerrors/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productmotivatorerrorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productmotivatorerrors/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productmotivatorerrorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productmotivatorerrors/list", result);

        page = 1;
        size = null;
        productmotivatorerrorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productmotivatorerrors/list", result);

        page = null;
        size = 10;
        productmotivatorerrorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productmotivatorerrors/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductMotivatorError productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productmotivatorerrorController.update(productmotivatorerror, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productmotivatorerrors/"+productmotivatorerror.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductMotivatorError productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productmotivatorerrorController.update(productmotivatorerror, results, model, request);
        assertEquals("The update view was not returned", "crud/productmotivatorerrors/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductMotivatorError productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();
        Long id = productmotivatorerror.getId();
        Model model = getModel();
        String result = productmotivatorerrorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productmotivatorerrors/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductMotivatorError productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();
        Long id = productmotivatorerror.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productmotivatorerrorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productmotivatorerrors", result);

        productmotivatorerror = ProductMotivatorError.findProductMotivatorError(id);
        assertNull("ProductMotivatorError was not deleted", productmotivatorerror);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductMotivatorError productmotivatorerror = productmotivatorerrorDataOnDemand.getRandomProductMotivatorError();
        Long id = productmotivatorerror.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productmotivatorerrorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productmotivatorerrors", result);

        productmotivatorerror = ProductMotivatorError.findProductMotivatorError(id);
        assertNull("ProductMotivatorError was not deleted", productmotivatorerror);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productmotivatorerrorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productmotivatorerrors/list", result);
    }
}

