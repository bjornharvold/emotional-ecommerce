package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ProductDetailGroup;
import com.lela.data.domain.entity.ProductDetailGroupDataOnDemand;
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


public class ProductDetailGroupControllerTest extends AbstractControllerTest {

    private ProductDetailGroupController productdetailgroupController = new ProductDetailGroupController();

    @Autowired
    private ProductDetailGroupDataOnDemand productdetailgroupDataOnDemand;

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

        ProductDetailGroup productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();

        String result = productdetailgroupController.create(productdetailgroup, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/productdetailgroups/"+productdetailgroup.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();

        result = productdetailgroupController.create(productdetailgroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailgroups/"+productdetailgroup.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();

        result = productdetailgroupController.create(productdetailgroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/productdetailgroups/"+productdetailgroup.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ProductDetailGroup productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();

        String result = productdetailgroupController.create(productdetailgroup, results, model, request);
        assertEquals("The form was not returned", "crud/productdetailgroups/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = productdetailgroupController.createForm(model);
        assertEquals("The form was not returned", "crud/productdetailgroups/create", result);
    }

    @Test
    public void show() {
        ProductDetailGroup productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();
        Long id = productdetailgroup.getId();
        Model model = getModel();
        String result = productdetailgroupController.show(id, model);
        assertEquals("The show view was not returned", "crud/productdetailgroups/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailgroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailgroups/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailgroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailgroups/list", result);

        page = 1;
        size = null;
        productdetailgroupController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/productdetailgroups/list", result);

        page = null;
        size = 10;
        productdetailgroupController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/productdetailgroups/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ProductDetailGroup productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailgroupController.update(productdetailgroup, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/productdetailgroups/"+productdetailgroup.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ProductDetailGroup productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = productdetailgroupController.update(productdetailgroup, results, model, request);
        assertEquals("The update view was not returned", "crud/productdetailgroups/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ProductDetailGroup productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();
        Long id = productdetailgroup.getId();
        Model model = getModel();
        String result = productdetailgroupController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/productdetailgroups/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ProductDetailGroup productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();
        Long id = productdetailgroup.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = productdetailgroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailgroups", result);

        productdetailgroup = ProductDetailGroup.findProductDetailGroup(id);
        assertNull("ProductDetailGroup was not deleted", productdetailgroup);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ProductDetailGroup productdetailgroup = productdetailgroupDataOnDemand.getRandomProductDetailGroup();
        Long id = productdetailgroup.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = productdetailgroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/productdetailgroups", result);

        productdetailgroup = ProductDetailGroup.findProductDetailGroup(id);
        assertNull("ProductDetailGroup was not deleted", productdetailgroup);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = productdetailgroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/productdetailgroups/list", result);
    }
}

