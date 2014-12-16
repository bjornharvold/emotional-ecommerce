package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductMotivator;
import com.lela.data.domain.entity.ProductMotivatorDataOnDemand;
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


public class ProductMotivatorControllerTest extends AbstractControllerTest {

    private ProductMotivatorController productmotivatorController = new ProductMotivatorController();

    @Autowired
    private ProductMotivatorDataOnDemand productmotivatorDataOnDemand;

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

        ProductMotivator productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();

        String result = productmotivatorController.create(productmotivator, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productmotivators/"+productmotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();

        result = productmotivatorController.create(productmotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productmotivators/"+productmotivator.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();

        result = productmotivatorController.create(productmotivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productmotivators/"+productmotivator.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductMotivator productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();

        String result = productmotivatorController.create(productmotivator, results, model, request);
        assertEquals("The form was not returned", "crud/productmotivators/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productmotivatorController.createForm(model);
        assertEquals("The form was not returned", "crud/productmotivators/create", result);
    }

    @Test
    public void show() {
        ProductMotivator productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();
        Long id = productmotivator.getId();
        Model model = getModel();
        String result = productmotivatorController.show(id, model);
        assertEquals("The show view was not returned", "crud/productmotivators/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productmotivators/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productmotivators/list", result);

        page = 1;
        size = null;
        productmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productmotivators/list", result);

        page = null;
        size = 10;
        productmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productmotivators/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductMotivator productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productmotivatorController.update(productmotivator, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productmotivators/"+productmotivator.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductMotivator productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productmotivatorController.update(productmotivator, results, model, request);
        assertEquals("The update view was not returned", "crud/productmotivators/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductMotivator productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();
        Long id = productmotivator.getId();
        Model model = getModel();
        String result = productmotivatorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productmotivators/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductMotivator productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();
        Long id = productmotivator.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productmotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productmotivators", result);

        productmotivator = ProductMotivator.findProductMotivator(id);
        assertNull("ProductMotivator was not deleted", productmotivator);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductMotivator productmotivator = productmotivatorDataOnDemand.getRandomProductMotivator();
        Long id = productmotivator.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productmotivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productmotivators", result);

        productmotivator = ProductMotivator.findProductMotivator(id);
        assertNull("ProductMotivator was not deleted", productmotivator);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productmotivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productmotivators/list", result);
    }
}

