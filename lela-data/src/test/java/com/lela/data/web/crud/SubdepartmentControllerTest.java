package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Subdepartment;
import com.lela.data.domain.entity.SubdepartmentDataOnDemand;
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


public class SubdepartmentControllerTest extends AbstractControllerTest {

    private SubdepartmentController subdepartmentController = new SubdepartmentController();

    @Autowired
    private SubdepartmentDataOnDemand subdepartmentDataOnDemand;

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

        Subdepartment subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();

        String result = subdepartmentController.create(subdepartment, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/subdepartments/"+subdepartment.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();

        result = subdepartmentController.create(subdepartment, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/subdepartments/"+subdepartment.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();

        result = subdepartmentController.create(subdepartment, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/subdepartments/"+subdepartment.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Subdepartment subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();

        String result = subdepartmentController.create(subdepartment, results, model, request);
        assertEquals("The form was not returned", "crud/subdepartments/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = subdepartmentController.createForm(model);
        assertEquals("The form was not returned", "crud/subdepartments/create", result);
    }

    @Test
    public void show() {
        Subdepartment subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();
        Long id = subdepartment.getId();
        Model model = getModel();
        String result = subdepartmentController.show(id, model);
        assertEquals("The show view was not returned", "crud/subdepartments/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = subdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/subdepartments/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = subdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/subdepartments/list", result);

        page = 1;
        size = null;
        subdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/subdepartments/list", result);

        page = null;
        size = 10;
        subdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/subdepartments/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Subdepartment subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = subdepartmentController.update(subdepartment, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/subdepartments/"+subdepartment.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Subdepartment subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = subdepartmentController.update(subdepartment, results, model, request);
        assertEquals("The update view was not returned", "crud/subdepartments/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Subdepartment subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();
        Long id = subdepartment.getId();
        Model model = getModel();
        String result = subdepartmentController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/subdepartments/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Subdepartment subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();
        Long id = subdepartment.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = subdepartmentController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/subdepartments", result);

        subdepartment = Subdepartment.findSubdepartment(id);
        assertNull("Subdepartment was not deleted", subdepartment);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Subdepartment subdepartment = subdepartmentDataOnDemand.getRandomSubdepartment();
        Long id = subdepartment.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = subdepartmentController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/subdepartments", result);

        subdepartment = Subdepartment.findSubdepartment(id);
        assertNull("Subdepartment was not deleted", subdepartment);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = subdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/subdepartments/list", result);
    }
}

