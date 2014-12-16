package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.FunctionalFilterType;
import com.lela.data.domain.entity.FunctionalFilterTypeDataOnDemand;
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


public class FunctionalFilterTypeControllerTest extends AbstractControllerTest {

    private FunctionalFilterTypeController functionalfiltertypeController = new FunctionalFilterTypeController();

    @Autowired
    private FunctionalFilterTypeDataOnDemand functionalfiltertypeDataOnDemand;

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

        FunctionalFilterType functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();

        String result = functionalfiltertypeController.create(functionalfiltertype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/functionalfiltertypes/"+functionalfiltertype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();

        result = functionalfiltertypeController.create(functionalfiltertype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/functionalfiltertypes/"+functionalfiltertype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();

        result = functionalfiltertypeController.create(functionalfiltertype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/functionalfiltertypes/"+functionalfiltertype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        FunctionalFilterType functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();

        String result = functionalfiltertypeController.create(functionalfiltertype, results, model, request);
        assertEquals("The form was not returned", "crud/functionalfiltertypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = functionalfiltertypeController.createForm(model);
        assertEquals("The form was not returned", "crud/functionalfiltertypes/create", result);
    }

    @Test
    public void show() {
        FunctionalFilterType functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();
        Long id = functionalfiltertype.getId();
        Model model = getModel();
        String result = functionalfiltertypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/functionalfiltertypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = functionalfiltertypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfiltertypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = functionalfiltertypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfiltertypes/list", result);

        page = 1;
        size = null;
        functionalfiltertypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/functionalfiltertypes/list", result);

        page = null;
        size = 10;
        functionalfiltertypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/functionalfiltertypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        FunctionalFilterType functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = functionalfiltertypeController.update(functionalfiltertype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/functionalfiltertypes/"+functionalfiltertype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        FunctionalFilterType functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = functionalfiltertypeController.update(functionalfiltertype, results, model, request);
        assertEquals("The update view was not returned", "crud/functionalfiltertypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        FunctionalFilterType functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();
        Long id = functionalfiltertype.getId();
        Model model = getModel();
        String result = functionalfiltertypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/functionalfiltertypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        FunctionalFilterType functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();
        Long id = functionalfiltertype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = functionalfiltertypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/functionalfiltertypes", result);

        functionalfiltertype = FunctionalFilterType.findFunctionalFilterType(id);
        assertNull("FunctionalFilterType was not deleted", functionalfiltertype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        FunctionalFilterType functionalfiltertype = functionalfiltertypeDataOnDemand.getRandomFunctionalFilterType();
        Long id = functionalfiltertype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = functionalfiltertypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/functionalfiltertypes", result);

        functionalfiltertype = FunctionalFilterType.findFunctionalFilterType(id);
        assertNull("FunctionalFilterType was not deleted", functionalfiltertype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = functionalfiltertypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfiltertypes/list", result);
    }
}

