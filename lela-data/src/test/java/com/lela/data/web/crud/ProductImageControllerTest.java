package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductImage;
import com.lela.data.domain.entity.ProductImageDataOnDemand;
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


public class ProductImageControllerTest extends AbstractControllerTest {

    private ProductImageController productimageController = new ProductImageController();

    @Autowired
    private ProductImageDataOnDemand productimageDataOnDemand;

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

        ProductImage productimage = productimageDataOnDemand.getRandomProductImage();

        String result = productimageController.create(productimage, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productimages/"+productimage.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productimage = productimageDataOnDemand.getRandomProductImage();

        result = productimageController.create(productimage, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productimages/"+productimage.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productimage = productimageDataOnDemand.getRandomProductImage();

        result = productimageController.create(productimage, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productimages/"+productimage.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductImage productimage = productimageDataOnDemand.getRandomProductImage();

        String result = productimageController.create(productimage, results, model, request);
        assertEquals("The form was not returned", "crud/productimages/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productimageController.createForm(model);
        assertEquals("The form was not returned", "crud/productimages/create", result);
    }

    @Test
    public void show() {
        ProductImage productimage = productimageDataOnDemand.getRandomProductImage();
        Long id = productimage.getId();
        Model model = getModel();
        String result = productimageController.show(id, model);
        assertEquals("The show view was not returned", "crud/productimages/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productimageController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimages/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productimageController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimages/list", result);

        page = 1;
        size = null;
        productimageController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productimages/list", result);

        page = null;
        size = 10;
        productimageController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productimages/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductImage productimage = productimageDataOnDemand.getRandomProductImage();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productimageController.update(productimage, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productimages/"+productimage.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductImage productimage = productimageDataOnDemand.getRandomProductImage();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productimageController.update(productimage, results, model, request);
        assertEquals("The update view was not returned", "crud/productimages/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductImage productimage = productimageDataOnDemand.getRandomProductImage();
        Long id = productimage.getId();
        Model model = getModel();
        String result = productimageController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productimages/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductImage productimage = productimageDataOnDemand.getRandomProductImage();
        Long id = productimage.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productimageController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productimages", result);

        productimage = ProductImage.findProductImage(id);
        assertNull("ProductImage was not deleted", productimage);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductImage productimage = productimageDataOnDemand.getRandomProductImage();
        Long id = productimage.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productimageController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productimages", result);

        productimage = ProductImage.findProductImage(id);
        assertNull("ProductImage was not deleted", productimage);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productimageController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimages/list", result);
    }
}

