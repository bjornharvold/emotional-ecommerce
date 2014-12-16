package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ConditionType;
import com.lela.data.domain.entity.ConditionTypeDataOnDemand;
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


public class ConditionTypeControllerTest extends AbstractControllerTest {

    private ConditionTypeController conditiontypeController = new ConditionTypeController();

    @Autowired
    private ConditionTypeDataOnDemand conditiontypeDataOnDemand;

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

        ConditionType conditiontype = conditiontypeDataOnDemand.getRandomConditionType();

        String result = conditiontypeController.create(conditiontype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/conditiontypes/"+conditiontype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        conditiontype = conditiontypeDataOnDemand.getRandomConditionType();

        result = conditiontypeController.create(conditiontype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/conditiontypes/"+conditiontype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        conditiontype = conditiontypeDataOnDemand.getRandomConditionType();

        result = conditiontypeController.create(conditiontype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/conditiontypes/"+conditiontype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ConditionType conditiontype = conditiontypeDataOnDemand.getRandomConditionType();

        String result = conditiontypeController.create(conditiontype, results, model, request);
        assertEquals("The form was not returned", "crud/conditiontypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = conditiontypeController.createForm(model);
        assertEquals("The form was not returned", "crud/conditiontypes/create", result);
    }

    @Test
    public void show() {
        ConditionType conditiontype = conditiontypeDataOnDemand.getRandomConditionType();
        Long id = conditiontype.getId();
        Model model = getModel();
        String result = conditiontypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/conditiontypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = conditiontypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/conditiontypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = conditiontypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/conditiontypes/list", result);

        page = 1;
        size = null;
        conditiontypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/conditiontypes/list", result);

        page = null;
        size = 10;
        conditiontypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/conditiontypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ConditionType conditiontype = conditiontypeDataOnDemand.getRandomConditionType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = conditiontypeController.update(conditiontype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/conditiontypes/"+conditiontype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ConditionType conditiontype = conditiontypeDataOnDemand.getRandomConditionType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = conditiontypeController.update(conditiontype, results, model, request);
        assertEquals("The update view was not returned", "crud/conditiontypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ConditionType conditiontype = conditiontypeDataOnDemand.getRandomConditionType();
        Long id = conditiontype.getId();
        Model model = getModel();
        String result = conditiontypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/conditiontypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ConditionType conditiontype = conditiontypeDataOnDemand.getRandomConditionType();
        Long id = conditiontype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = conditiontypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/conditiontypes", result);

        conditiontype = ConditionType.findConditionType(id);
        assertNull("ConditionType was not deleted", conditiontype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ConditionType conditiontype = conditiontypeDataOnDemand.getRandomConditionType();
        Long id = conditiontype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = conditiontypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/conditiontypes", result);

        conditiontype = ConditionType.findConditionType(id);
        assertNull("ConditionType was not deleted", conditiontype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = conditiontypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/conditiontypes/list", result);
    }
}

