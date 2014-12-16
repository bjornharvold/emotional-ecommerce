package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductDetailAttributeType;
import com.lela.data.domain.entity.ProductDetailAttributeTypeDataOnDemand;
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


public class ProductDetailAttributeTypeControllerTest extends AbstractControllerTest {

    private ProductDetailAttributeTypeController productdetailattributetypeController = new ProductDetailAttributeTypeController();

    @Autowired
    private ProductDetailAttributeTypeDataOnDemand productdetailattributetypeDataOnDemand;

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

        ProductDetailAttributeType productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();

        String result = productdetailattributetypeController.create(productdetailattributetype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productdetailattributetypes/"+productdetailattributetype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();

        result = productdetailattributetypeController.create(productdetailattributetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailattributetypes/"+productdetailattributetype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();

        result = productdetailattributetypeController.create(productdetailattributetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailattributetypes/"+productdetailattributetype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductDetailAttributeType productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();

        String result = productdetailattributetypeController.create(productdetailattributetype, results, model, request);
        assertEquals("The form was not returned", "crud/productdetailattributetypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productdetailattributetypeController.createForm(model);
        assertEquals("The form was not returned", "crud/productdetailattributetypes/create", result);
    }

    @Test
    public void show() {
        ProductDetailAttributeType productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();
        Long id = productdetailattributetype.getId();
        Model model = getModel();
        String result = productdetailattributetypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/productdetailattributetypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailattributetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailattributetypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailattributetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailattributetypes/list", result);

        page = 1;
        size = null;
        productdetailattributetypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productdetailattributetypes/list", result);

        page = null;
        size = 10;
        productdetailattributetypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productdetailattributetypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductDetailAttributeType productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailattributetypeController.update(productdetailattributetype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productdetailattributetypes/"+productdetailattributetype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductDetailAttributeType productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailattributetypeController.update(productdetailattributetype, results, model, request);
        assertEquals("The update view was not returned", "crud/productdetailattributetypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductDetailAttributeType productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();
        Long id = productdetailattributetype.getId();
        Model model = getModel();
        String result = productdetailattributetypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productdetailattributetypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductDetailAttributeType productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();
        Long id = productdetailattributetype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailattributetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailattributetypes", result);

        productdetailattributetype = ProductDetailAttributeType.findProductDetailAttributeType(id);
        assertNull("ProductDetailAttributeType was not deleted", productdetailattributetype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductDetailAttributeType productdetailattributetype = productdetailattributetypeDataOnDemand.getRandomProductDetailAttributeType();
        Long id = productdetailattributetype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailattributetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailattributetypes", result);

        productdetailattributetype = ProductDetailAttributeType.findProductDetailAttributeType(id);
        assertNull("ProductDetailAttributeType was not deleted", productdetailattributetype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productdetailattributetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailattributetypes/list", result);
    }
}

