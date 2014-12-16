package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductDetailAttributeValueType;
import com.lela.data.domain.entity.ProductDetailAttributeValueTypeDataOnDemand;
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


public class ProductDetailAttributeValueTypeControllerTest extends AbstractControllerTest {

    private ProductDetailAttributeValueTypeController productdetailattributevaluetypeController = new ProductDetailAttributeValueTypeController();

    @Autowired
    private ProductDetailAttributeValueTypeDataOnDemand productdetailattributevaluetypeDataOnDemand;

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

        ProductDetailAttributeValueType productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();

        String result = productdetailattributevaluetypeController.create(productdetailattributevaluetype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productdetailattributevaluetypes/"+productdetailattributevaluetype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();

        result = productdetailattributevaluetypeController.create(productdetailattributevaluetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailattributevaluetypes/"+productdetailattributevaluetype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();

        result = productdetailattributevaluetypeController.create(productdetailattributevaluetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailattributevaluetypes/"+productdetailattributevaluetype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductDetailAttributeValueType productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();

        String result = productdetailattributevaluetypeController.create(productdetailattributevaluetype, results, model, request);
        assertEquals("The form was not returned", "crud/productdetailattributevaluetypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productdetailattributevaluetypeController.createForm(model);
        assertEquals("The form was not returned", "crud/productdetailattributevaluetypes/create", result);
    }

    @Test
    public void show() {
        ProductDetailAttributeValueType productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();
        Long id = productdetailattributevaluetype.getId();
        Model model = getModel();
        String result = productdetailattributevaluetypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/productdetailattributevaluetypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailattributevaluetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailattributevaluetypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailattributevaluetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailattributevaluetypes/list", result);

        page = 1;
        size = null;
        productdetailattributevaluetypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productdetailattributevaluetypes/list", result);

        page = null;
        size = 10;
        productdetailattributevaluetypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productdetailattributevaluetypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductDetailAttributeValueType productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailattributevaluetypeController.update(productdetailattributevaluetype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productdetailattributevaluetypes/"+productdetailattributevaluetype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductDetailAttributeValueType productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailattributevaluetypeController.update(productdetailattributevaluetype, results, model, request);
        assertEquals("The update view was not returned", "crud/productdetailattributevaluetypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductDetailAttributeValueType productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();
        Long id = productdetailattributevaluetype.getId();
        Model model = getModel();
        String result = productdetailattributevaluetypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productdetailattributevaluetypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductDetailAttributeValueType productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();
        Long id = productdetailattributevaluetype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailattributevaluetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailattributevaluetypes", result);

        productdetailattributevaluetype = ProductDetailAttributeValueType.findProductDetailAttributeValueType(id);
        assertNull("ProductDetailAttributeValueType was not deleted", productdetailattributevaluetype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductDetailAttributeValueType productdetailattributevaluetype = productdetailattributevaluetypeDataOnDemand.getRandomProductDetailAttributeValueType();
        Long id = productdetailattributevaluetype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailattributevaluetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailattributevaluetypes", result);

        productdetailattributevaluetype = ProductDetailAttributeValueType.findProductDetailAttributeValueType(id);
        assertNull("ProductDetailAttributeValueType was not deleted", productdetailattributevaluetype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productdetailattributevaluetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailattributevaluetypes/list", result);
    }
}

