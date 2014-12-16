package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ObjectIdSequence;
import com.lela.data.domain.entity.ObjectIdSequenceDataOnDemand;
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


public class ObjectIdSequenceControllerTest extends AbstractControllerTest {

    private ObjectIdSequenceController objectidsequenceController = new ObjectIdSequenceController();

    @Autowired
    private ObjectIdSequenceDataOnDemand objectidsequenceDataOnDemand;

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

        ObjectIdSequence objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();

        String result = objectidsequenceController.create(objectidsequence, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/objectidsequences/"+objectidsequence.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();

        result = objectidsequenceController.create(objectidsequence, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/objectidsequences/"+objectidsequence.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();

        result = objectidsequenceController.create(objectidsequence, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/objectidsequences/"+objectidsequence.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ObjectIdSequence objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();

        String result = objectidsequenceController.create(objectidsequence, results, model, request);
        assertEquals("The form was not returned", "crud/objectidsequences/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = objectidsequenceController.createForm(model);
        assertEquals("The form was not returned", "crud/objectidsequences/create", result);
    }

    @Test
    public void show() {
        ObjectIdSequence objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();
        Long id = objectidsequence.getId();
        Model model = getModel();
        String result = objectidsequenceController.show(id, model);
        assertEquals("The show view was not returned", "crud/objectidsequences/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = objectidsequenceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/objectidsequences/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = objectidsequenceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/objectidsequences/list", result);

        page = 1;
        size = null;
        objectidsequenceController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/objectidsequences/list", result);

        page = null;
        size = 10;
        objectidsequenceController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/objectidsequences/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ObjectIdSequence objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = objectidsequenceController.update(objectidsequence, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/objectidsequences/"+objectidsequence.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ObjectIdSequence objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = objectidsequenceController.update(objectidsequence, results, model, request);
        assertEquals("The update view was not returned", "crud/objectidsequences/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ObjectIdSequence objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();
        Long id = objectidsequence.getId();
        Model model = getModel();
        String result = objectidsequenceController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/objectidsequences/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ObjectIdSequence objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();
        Long id = objectidsequence.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = objectidsequenceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/objectidsequences", result);

        objectidsequence = ObjectIdSequence.findObjectIdSequence(id);
        assertNull("ObjectIdSequence was not deleted", objectidsequence);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ObjectIdSequence objectidsequence = objectidsequenceDataOnDemand.getRandomObjectIdSequence();
        Long id = objectidsequence.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = objectidsequenceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/objectidsequences", result);

        objectidsequence = ObjectIdSequence.findObjectIdSequence(id);
        assertNull("ObjectIdSequence was not deleted", objectidsequence);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = objectidsequenceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/objectidsequences/list", result);
    }
}

