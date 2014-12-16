package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductDetailPartValue;
import com.lela.data.domain.entity.ProductDetailPartValueDataOnDemand;
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


public class ProductDetailPartValueControllerTest extends AbstractControllerTest {

    private ProductDetailPartValueController productdetailpartvalueController = new ProductDetailPartValueController();

    @Autowired
    private ProductDetailPartValueDataOnDemand productdetailpartvalueDataOnDemand;

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

        ProductDetailPartValue productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();

        String result = productdetailpartvalueController.create(productdetailpartvalue, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productdetailpartvalues/"+productdetailpartvalue.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();

        result = productdetailpartvalueController.create(productdetailpartvalue, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailpartvalues/"+productdetailpartvalue.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();

        result = productdetailpartvalueController.create(productdetailpartvalue, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailpartvalues/"+productdetailpartvalue.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductDetailPartValue productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();

        String result = productdetailpartvalueController.create(productdetailpartvalue, results, model, request);
        assertEquals("The form was not returned", "crud/productdetailpartvalues/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productdetailpartvalueController.createForm(model);
        assertEquals("The form was not returned", "crud/productdetailpartvalues/create", result);
    }

    @Test
    public void show() {
        ProductDetailPartValue productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();
        Long id = productdetailpartvalue.getId();
        Model model = getModel();
        String result = productdetailpartvalueController.show(id, model);
        assertEquals("The show view was not returned", "crud/productdetailpartvalues/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailpartvalueController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailpartvalues/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailpartvalueController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailpartvalues/list", result);

        page = 1;
        size = null;
        productdetailpartvalueController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productdetailpartvalues/list", result);

        page = null;
        size = 10;
        productdetailpartvalueController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productdetailpartvalues/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductDetailPartValue productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailpartvalueController.update(productdetailpartvalue, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productdetailpartvalues/"+productdetailpartvalue.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductDetailPartValue productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailpartvalueController.update(productdetailpartvalue, results, model, request);
        assertEquals("The update view was not returned", "crud/productdetailpartvalues/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductDetailPartValue productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();
        Long id = productdetailpartvalue.getId();
        Model model = getModel();
        String result = productdetailpartvalueController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productdetailpartvalues/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductDetailPartValue productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();
        Long id = productdetailpartvalue.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailpartvalueController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailpartvalues", result);

        productdetailpartvalue = ProductDetailPartValue.findProductDetailPartValue(id);
        assertNull("ProductDetailPartValue was not deleted", productdetailpartvalue);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductDetailPartValue productdetailpartvalue = productdetailpartvalueDataOnDemand.getRandomProductDetailPartValue();
        Long id = productdetailpartvalue.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailpartvalueController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailpartvalues", result);

        productdetailpartvalue = ProductDetailPartValue.findProductDetailPartValue(id);
        assertNull("ProductDetailPartValue was not deleted", productdetailpartvalue);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productdetailpartvalueController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailpartvalues/list", result);
    }
}

