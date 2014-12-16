package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.BrandHistory;
import com.lela.data.domain.entity.BrandHistoryDataOnDemand;
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


public class BrandHistoryControllerTest extends AbstractControllerTest {

    private BrandHistoryController brandhistoryController = new BrandHistoryController();

    @Autowired
    private BrandHistoryDataOnDemand brandhistoryDataOnDemand;

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

        BrandHistory brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();

        String result = brandhistoryController.create(brandhistory, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/brandhistorys/"+brandhistory.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();

        result = brandhistoryController.create(brandhistory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandhistorys/"+brandhistory.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();

        result = brandhistoryController.create(brandhistory, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/brandhistorys/"+brandhistory.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        BrandHistory brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();

        String result = brandhistoryController.create(brandhistory, results, model, request);
        assertEquals("The form was not returned", "crud/brandhistorys/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = brandhistoryController.createForm(model);
        assertEquals("The form was not returned", "crud/brandhistorys/create", result);
    }

    @Test
    public void show() {
        BrandHistory brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();
        Long id = brandhistory.getId();
        Model model = getModel();
        String result = brandhistoryController.show(id, model);
        assertEquals("The show view was not returned", "crud/brandhistorys/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandhistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandhistorys/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandhistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandhistorys/list", result);

        page = 1;
        size = null;
        brandhistoryController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/brandhistorys/list", result);

        page = null;
        size = 10;
        brandhistoryController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/brandhistorys/list", result);

    }

    @Test
    @Transactional
    public void update() {
        BrandHistory brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandhistoryController.update(brandhistory, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/brandhistorys/"+brandhistory.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        BrandHistory brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = brandhistoryController.update(brandhistory, results, model, request);
        assertEquals("The update view was not returned", "crud/brandhistorys/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        BrandHistory brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();
        Long id = brandhistory.getId();
        Model model = getModel();
        String result = brandhistoryController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/brandhistorys/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        BrandHistory brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();
        Long id = brandhistory.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = brandhistoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandhistorys", result);

        brandhistory = BrandHistory.findBrandHistory(id);
        assertNull("BrandHistory was not deleted", brandhistory);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        BrandHistory brandhistory = brandhistoryDataOnDemand.getRandomBrandHistory();
        Long id = brandhistory.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = brandhistoryController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/brandhistorys", result);

        brandhistory = BrandHistory.findBrandHistory(id);
        assertNull("BrandHistory was not deleted", brandhistory);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = brandhistoryController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/brandhistorys/list", result);
    }
}

