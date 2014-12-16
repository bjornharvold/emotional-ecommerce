package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductDetailSection;
import com.lela.data.domain.entity.ProductDetailSectionDataOnDemand;
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


public class ProductDetailSectionControllerTest extends AbstractControllerTest {

    private ProductDetailSectionController productdetailsectionController = new ProductDetailSectionController();

    @Autowired
    private ProductDetailSectionDataOnDemand productdetailsectionDataOnDemand;

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

        ProductDetailSection productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();

        String result = productdetailsectionController.create(productdetailsection, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productdetailsections/"+productdetailsection.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();

        result = productdetailsectionController.create(productdetailsection, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailsections/"+productdetailsection.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();

        result = productdetailsectionController.create(productdetailsection, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailsections/"+productdetailsection.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductDetailSection productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();

        String result = productdetailsectionController.create(productdetailsection, results, model, request);
        assertEquals("The form was not returned", "crud/productdetailsections/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productdetailsectionController.createForm(model);
        assertEquals("The form was not returned", "crud/productdetailsections/create", result);
    }

    @Test
    public void show() {
        ProductDetailSection productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();
        Long id = productdetailsection.getId();
        Model model = getModel();
        String result = productdetailsectionController.show(id, model);
        assertEquals("The show view was not returned", "crud/productdetailsections/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailsectionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailsections/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailsectionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailsections/list", result);

        page = 1;
        size = null;
        productdetailsectionController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productdetailsections/list", result);

        page = null;
        size = 10;
        productdetailsectionController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productdetailsections/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductDetailSection productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailsectionController.update(productdetailsection, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productdetailsections/"+productdetailsection.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductDetailSection productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailsectionController.update(productdetailsection, results, model, request);
        assertEquals("The update view was not returned", "crud/productdetailsections/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductDetailSection productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();
        Long id = productdetailsection.getId();
        Model model = getModel();
        String result = productdetailsectionController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productdetailsections/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductDetailSection productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();
        Long id = productdetailsection.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailsectionController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailsections", result);

        productdetailsection = ProductDetailSection.findProductDetailSection(id);
        assertNull("ProductDetailSection was not deleted", productdetailsection);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductDetailSection productdetailsection = productdetailsectionDataOnDemand.getRandomProductDetailSection();
        Long id = productdetailsection.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailsectionController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailsections", result);

        productdetailsection = ProductDetailSection.findProductDetailSection(id);
        assertNull("ProductDetailSection was not deleted", productdetailsection);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productdetailsectionController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailsections/list", result);
    }
}

