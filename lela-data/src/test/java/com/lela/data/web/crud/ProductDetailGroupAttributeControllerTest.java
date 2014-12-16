package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductDetailGroupAttribute;
import com.lela.data.domain.entity.ProductDetailGroupAttributeDataOnDemand;
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


public class ProductDetailGroupAttributeControllerTest extends AbstractControllerTest {

    private ProductDetailGroupAttributeController productdetailgroupattributeController = new ProductDetailGroupAttributeController();

    @Autowired
    private ProductDetailGroupAttributeDataOnDemand productdetailgroupattributeDataOnDemand;

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

        ProductDetailGroupAttribute productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();

        String result = productdetailgroupattributeController.create(productdetailgroupattribute, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productdetailgroupattributes/"+productdetailgroupattribute.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();

        result = productdetailgroupattributeController.create(productdetailgroupattribute, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailgroupattributes/"+productdetailgroupattribute.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();

        result = productdetailgroupattributeController.create(productdetailgroupattribute, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailgroupattributes/"+productdetailgroupattribute.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductDetailGroupAttribute productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();

        String result = productdetailgroupattributeController.create(productdetailgroupattribute, results, model, request);
        assertEquals("The form was not returned", "crud/productdetailgroupattributes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productdetailgroupattributeController.createForm(model);
        assertEquals("The form was not returned", "crud/productdetailgroupattributes/create", result);
    }

    @Test
    public void show() {
        ProductDetailGroupAttribute productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();
        Long id = productdetailgroupattribute.getId();
        Model model = getModel();
        String result = productdetailgroupattributeController.show(id, model);
        assertEquals("The show view was not returned", "crud/productdetailgroupattributes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailgroupattributeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailgroupattributes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailgroupattributeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailgroupattributes/list", result);

        page = 1;
        size = null;
        productdetailgroupattributeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productdetailgroupattributes/list", result);

        page = null;
        size = 10;
        productdetailgroupattributeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productdetailgroupattributes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductDetailGroupAttribute productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailgroupattributeController.update(productdetailgroupattribute, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productdetailgroupattributes/"+productdetailgroupattribute.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductDetailGroupAttribute productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailgroupattributeController.update(productdetailgroupattribute, results, model, request);
        assertEquals("The update view was not returned", "crud/productdetailgroupattributes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductDetailGroupAttribute productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();
        Long id = productdetailgroupattribute.getId();
        Model model = getModel();
        String result = productdetailgroupattributeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productdetailgroupattributes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductDetailGroupAttribute productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();
        Long id = productdetailgroupattribute.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailgroupattributeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailgroupattributes", result);

        productdetailgroupattribute = ProductDetailGroupAttribute.findProductDetailGroupAttribute(id);
        assertNull("ProductDetailGroupAttribute was not deleted", productdetailgroupattribute);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductDetailGroupAttribute productdetailgroupattribute = productdetailgroupattributeDataOnDemand.getRandomProductDetailGroupAttribute();
        Long id = productdetailgroupattribute.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailgroupattributeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailgroupattributes", result);

        productdetailgroupattribute = ProductDetailGroupAttribute.findProductDetailGroupAttribute(id);
        assertNull("ProductDetailGroupAttribute was not deleted", productdetailgroupattribute);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productdetailgroupattributeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailgroupattributes/list", result);
    }
}

