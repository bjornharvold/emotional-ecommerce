package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductImageItem;
import com.lela.data.domain.entity.ProductImageItemDataOnDemand;
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


public class ProductImageItemControllerTest extends AbstractControllerTest {

    private ProductImageItemController productimageitemController = new ProductImageItemController();

    @Autowired
    private ProductImageItemDataOnDemand productimageitemDataOnDemand;

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

        ProductImageItem productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();

        String result = productimageitemController.create(productimageitem, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productimageitems/"+productimageitem.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();

        result = productimageitemController.create(productimageitem, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productimageitems/"+productimageitem.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();

        result = productimageitemController.create(productimageitem, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productimageitems/"+productimageitem.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductImageItem productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();

        String result = productimageitemController.create(productimageitem, results, model, request);
        assertEquals("The form was not returned", "crud/productimageitems/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productimageitemController.createForm(model);
        assertEquals("The form was not returned", "crud/productimageitems/create", result);
    }

    @Test
    public void show() {
        ProductImageItem productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();
        Long id = productimageitem.getId();
        Model model = getModel();
        String result = productimageitemController.show(id, model);
        assertEquals("The show view was not returned", "crud/productimageitems/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productimageitemController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimageitems/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productimageitemController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimageitems/list", result);

        page = 1;
        size = null;
        productimageitemController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productimageitems/list", result);

        page = null;
        size = 10;
        productimageitemController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productimageitems/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductImageItem productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productimageitemController.update(productimageitem, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productimageitems/"+productimageitem.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductImageItem productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productimageitemController.update(productimageitem, results, model, request);
        assertEquals("The update view was not returned", "crud/productimageitems/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductImageItem productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();
        Long id = productimageitem.getId();
        Model model = getModel();
        String result = productimageitemController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productimageitems/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductImageItem productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();
        Long id = productimageitem.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productimageitemController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productimageitems", result);

        productimageitem = ProductImageItem.findProductImageItem(id);
        assertNull("ProductImageItem was not deleted", productimageitem);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductImageItem productimageitem = productimageitemDataOnDemand.getRandomProductImageItem();
        Long id = productimageitem.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productimageitemController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productimageitems", result);

        productimageitem = ProductImageItem.findProductImageItem(id);
        assertNull("ProductImageItem was not deleted", productimageitem);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productimageitemController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productimageitems/list", result);
    }
}

