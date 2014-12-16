package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductDetailPart;
import com.lela.data.domain.entity.ProductDetailPartDataOnDemand;
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


public class ProductDetailPartControllerTest extends AbstractControllerTest {

    private ProductDetailPartController productdetailpartController = new ProductDetailPartController();

    @Autowired
    private ProductDetailPartDataOnDemand productdetailpartDataOnDemand;

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

        ProductDetailPart productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();

        String result = productdetailpartController.create(productdetailpart, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productdetailparts/"+productdetailpart.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();

        result = productdetailpartController.create(productdetailpart, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailparts/"+productdetailpart.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();

        result = productdetailpartController.create(productdetailpart, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailparts/"+productdetailpart.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductDetailPart productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();

        String result = productdetailpartController.create(productdetailpart, results, model, request);
        assertEquals("The form was not returned", "crud/productdetailparts/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productdetailpartController.createForm(model);
        assertEquals("The form was not returned", "crud/productdetailparts/create", result);
    }

    @Test
    public void show() {
        ProductDetailPart productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();
        Long id = productdetailpart.getId();
        Model model = getModel();
        String result = productdetailpartController.show(id, model);
        assertEquals("The show view was not returned", "crud/productdetailparts/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailpartController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailparts/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailpartController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailparts/list", result);

        page = 1;
        size = null;
        productdetailpartController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productdetailparts/list", result);

        page = null;
        size = 10;
        productdetailpartController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productdetailparts/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductDetailPart productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailpartController.update(productdetailpart, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productdetailparts/"+productdetailpart.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductDetailPart productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailpartController.update(productdetailpart, results, model, request);
        assertEquals("The update view was not returned", "crud/productdetailparts/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductDetailPart productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();
        Long id = productdetailpart.getId();
        Model model = getModel();
        String result = productdetailpartController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productdetailparts/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductDetailPart productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();
        Long id = productdetailpart.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailpartController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailparts", result);

        productdetailpart = ProductDetailPart.findProductDetailPart(id);
        assertNull("ProductDetailPart was not deleted", productdetailpart);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductDetailPart productdetailpart = productdetailpartDataOnDemand.getRandomProductDetailPart();
        Long id = productdetailpart.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailpartController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailparts", result);

        productdetailpart = ProductDetailPart.findProductDetailPart(id);
        assertNull("ProductDetailPart was not deleted", productdetailpart);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productdetailpartController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailparts/list", result);
    }
}

