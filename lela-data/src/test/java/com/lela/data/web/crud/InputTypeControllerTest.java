package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.InputType;
import com.lela.data.domain.entity.InputTypeDataOnDemand;
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


public class InputTypeControllerTest extends AbstractControllerTest {

    private InputTypeController inputtypeController = new InputTypeController();

    @Autowired
    private InputTypeDataOnDemand inputtypeDataOnDemand;

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

        InputType inputtype = inputtypeDataOnDemand.getRandomInputType();

        String result = inputtypeController.create(inputtype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/inputtypes/"+inputtype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        inputtype = inputtypeDataOnDemand.getRandomInputType();

        result = inputtypeController.create(inputtype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/inputtypes/"+inputtype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        inputtype = inputtypeDataOnDemand.getRandomInputType();

        result = inputtypeController.create(inputtype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/inputtypes/"+inputtype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        InputType inputtype = inputtypeDataOnDemand.getRandomInputType();

        String result = inputtypeController.create(inputtype, results, model, request);
        assertEquals("The form was not returned", "crud/inputtypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = inputtypeController.createForm(model);
        assertEquals("The form was not returned", "crud/inputtypes/create", result);
    }

    @Test
    public void show() {
        InputType inputtype = inputtypeDataOnDemand.getRandomInputType();
        Long id = inputtype.getId();
        Model model = getModel();
        String result = inputtypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/inputtypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = inputtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputtypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = inputtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputtypes/list", result);

        page = 1;
        size = null;
        inputtypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/inputtypes/list", result);

        page = null;
        size = 10;
        inputtypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/inputtypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        InputType inputtype = inputtypeDataOnDemand.getRandomInputType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = inputtypeController.update(inputtype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/inputtypes/"+inputtype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        InputType inputtype = inputtypeDataOnDemand.getRandomInputType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = inputtypeController.update(inputtype, results, model, request);
        assertEquals("The update view was not returned", "crud/inputtypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        InputType inputtype = inputtypeDataOnDemand.getRandomInputType();
        Long id = inputtype.getId();
        Model model = getModel();
        String result = inputtypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/inputtypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        InputType inputtype = inputtypeDataOnDemand.getRandomInputType();
        Long id = inputtype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = inputtypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/inputtypes", result);

        inputtype = InputType.findInputType(id);
        assertNull("InputType was not deleted", inputtype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        InputType inputtype = inputtypeDataOnDemand.getRandomInputType();
        Long id = inputtype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = inputtypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/inputtypes", result);

        inputtype = InputType.findInputType(id);
        assertNull("InputType was not deleted", inputtype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = inputtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/inputtypes/list", result);
    }
}

