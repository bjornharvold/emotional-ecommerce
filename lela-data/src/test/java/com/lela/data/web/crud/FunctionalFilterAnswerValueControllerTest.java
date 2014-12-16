package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.FunctionalFilterAnswerValue;
import com.lela.data.domain.entity.FunctionalFilterAnswerValueDataOnDemand;
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


public class FunctionalFilterAnswerValueControllerTest extends AbstractControllerTest {

    private FunctionalFilterAnswerValueController functionalfilteranswervalueController = new FunctionalFilterAnswerValueController();

    @Autowired
    private FunctionalFilterAnswerValueDataOnDemand functionalfilteranswervalueDataOnDemand;

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

        FunctionalFilterAnswerValue functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();

        String result = functionalfilteranswervalueController.create(functionalfilteranswervalue, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/functionalfilteranswervalues/"+functionalfilteranswervalue.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();

        result = functionalfilteranswervalueController.create(functionalfilteranswervalue, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/functionalfilteranswervalues/"+functionalfilteranswervalue.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();

        result = functionalfilteranswervalueController.create(functionalfilteranswervalue, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/functionalfilteranswervalues/"+functionalfilteranswervalue.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        FunctionalFilterAnswerValue functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();

        String result = functionalfilteranswervalueController.create(functionalfilteranswervalue, results, model, request);
        assertEquals("The form was not returned", "crud/functionalfilteranswervalues/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = functionalfilteranswervalueController.createForm(model);
        assertEquals("The form was not returned", "crud/functionalfilteranswervalues/create", result);
    }

    @Test
    public void show() {
        FunctionalFilterAnswerValue functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();
        Long id = functionalfilteranswervalue.getId();
        Model model = getModel();
        String result = functionalfilteranswervalueController.show(id, model);
        assertEquals("The show view was not returned", "crud/functionalfilteranswervalues/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = functionalfilteranswervalueController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilteranswervalues/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = functionalfilteranswervalueController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilteranswervalues/list", result);

        page = 1;
        size = null;
        functionalfilteranswervalueController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/functionalfilteranswervalues/list", result);

        page = null;
        size = 10;
        functionalfilteranswervalueController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/functionalfilteranswervalues/list", result);

    }

    @Test
    @Transactional
    public void update() {
        FunctionalFilterAnswerValue functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = functionalfilteranswervalueController.update(functionalfilteranswervalue, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/functionalfilteranswervalues/"+functionalfilteranswervalue.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        FunctionalFilterAnswerValue functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = functionalfilteranswervalueController.update(functionalfilteranswervalue, results, model, request);
        assertEquals("The update view was not returned", "crud/functionalfilteranswervalues/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        FunctionalFilterAnswerValue functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();
        Long id = functionalfilteranswervalue.getId();
        Model model = getModel();
        String result = functionalfilteranswervalueController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/functionalfilteranswervalues/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        FunctionalFilterAnswerValue functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();
        Long id = functionalfilteranswervalue.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = functionalfilteranswervalueController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/functionalfilteranswervalues", result);

        functionalfilteranswervalue = FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id);
        assertNull("FunctionalFilterAnswerValue was not deleted", functionalfilteranswervalue);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        FunctionalFilterAnswerValue functionalfilteranswervalue = functionalfilteranswervalueDataOnDemand.getRandomFunctionalFilterAnswerValue();
        Long id = functionalfilteranswervalue.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = functionalfilteranswervalueController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/functionalfilteranswervalues", result);

        functionalfilteranswervalue = FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id);
        assertNull("FunctionalFilterAnswerValue was not deleted", functionalfilteranswervalue);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = functionalfilteranswervalueController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/functionalfilteranswervalues/list", result);
    }
}

