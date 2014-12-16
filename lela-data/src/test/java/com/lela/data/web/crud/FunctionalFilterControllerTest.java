package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.FunctionalFilter;
import com.lela.data.domain.entity.FunctionalFilterDataOnDemand;
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


public class FunctionalFilterControllerTest extends AbstractControllerTest {

    private FunctionalFilterController functionalfilterController = new FunctionalFilterController();

    @Autowired
    private FunctionalFilterDataOnDemand functionalfilterDataOnDemand;

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

        FunctionalFilter functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();

        String result = functionalfilterController.create(functionalfilter, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/functionalfilters/"+functionalfilter.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();

        result = functionalfilterController.create(functionalfilter, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/functionalfilters/"+functionalfilter.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();

        result = functionalfilterController.create(functionalfilter, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/functionalfilters/"+functionalfilter.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        FunctionalFilter functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();

        String result = functionalfilterController.create(functionalfilter, results, model, request);
        assertEquals("The form was not returned", "crud/functionalfilters/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = functionalfilterController.createForm(model);
        assertEquals("The form was not returned", "crud/functionalfilters/create", result);
    }

    @Test
    public void show() {
        FunctionalFilter functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();
        Long id = functionalfilter.getId();
        Model model = getModel();
        String result = functionalfilterController.show(id, model);
        assertEquals("The show view was not returned", "crud/functionalfilters/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = functionalfilterController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilters/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = functionalfilterController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilters/list", result);

        page = 1;
        size = null;
        functionalfilterController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/functionalfilters/list", result);

        page = null;
        size = 10;
        functionalfilterController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/functionalfilters/list", result);

    }

    @Test
    @Transactional
    public void update() {
        FunctionalFilter functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = functionalfilterController.update(functionalfilter, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/functionalfilters/"+functionalfilter.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        FunctionalFilter functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = functionalfilterController.update(functionalfilter, results, model, request);
        assertEquals("The update view was not returned", "crud/functionalfilters/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        FunctionalFilter functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();
        Long id = functionalfilter.getId();
        Model model = getModel();
        String result = functionalfilterController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/functionalfilters/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        FunctionalFilter functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();
        Long id = functionalfilter.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = functionalfilterController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/functionalfilters", result);

        functionalfilter = FunctionalFilter.findFunctionalFilter(id);
        assertNull("FunctionalFilter was not deleted", functionalfilter);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        FunctionalFilter functionalfilter = functionalfilterDataOnDemand.getRandomFunctionalFilter();
        Long id = functionalfilter.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = functionalfilterController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/functionalfilters", result);

        functionalfilter = FunctionalFilter.findFunctionalFilter(id);
        assertNull("FunctionalFilter was not deleted", functionalfilter);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = functionalfilterController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilters/list", result);
    }
}

