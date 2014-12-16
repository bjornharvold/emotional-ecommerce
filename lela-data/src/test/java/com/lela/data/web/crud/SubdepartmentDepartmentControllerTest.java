package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.SubdepartmentDepartment;
import com.lela.data.domain.entity.SubdepartmentDepartmentDataOnDemand;
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


public class SubdepartmentDepartmentControllerTest extends AbstractControllerTest {

    private SubdepartmentDepartmentController subdepartmentdepartmentController = new SubdepartmentDepartmentController();

    @Autowired
    private SubdepartmentDepartmentDataOnDemand subdepartmentdepartmentDataOnDemand;

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

        SubdepartmentDepartment subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();

        String result = subdepartmentdepartmentController.create(subdepartmentdepartment, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/subdepartmentdepartments/"+subdepartmentdepartment.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();

        result = subdepartmentdepartmentController.create(subdepartmentdepartment, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/subdepartmentdepartments/"+subdepartmentdepartment.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();

        result = subdepartmentdepartmentController.create(subdepartmentdepartment, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/subdepartmentdepartments/"+subdepartmentdepartment.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        SubdepartmentDepartment subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();

        String result = subdepartmentdepartmentController.create(subdepartmentdepartment, results, model, request);
        assertEquals("The form was not returned", "crud/subdepartmentdepartments/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = subdepartmentdepartmentController.createForm(model);
        assertEquals("The form was not returned", "crud/subdepartmentdepartments/create", result);
    }

    @Test
    public void show() {
        SubdepartmentDepartment subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();
        Long id = subdepartmentdepartment.getId();
        Model model = getModel();
        String result = subdepartmentdepartmentController.show(id, model);
        assertEquals("The show view was not returned", "crud/subdepartmentdepartments/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = subdepartmentdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/subdepartmentdepartments/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = subdepartmentdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/subdepartmentdepartments/list", result);

        page = 1;
        size = null;
        subdepartmentdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/subdepartmentdepartments/list", result);

        page = null;
        size = 10;
        subdepartmentdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/subdepartmentdepartments/list", result);

    }

    @Test
    @Transactional
    public void update() {
        SubdepartmentDepartment subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = subdepartmentdepartmentController.update(subdepartmentdepartment, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/subdepartmentdepartments/"+subdepartmentdepartment.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        SubdepartmentDepartment subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = subdepartmentdepartmentController.update(subdepartmentdepartment, results, model, request);
        assertEquals("The update view was not returned", "crud/subdepartmentdepartments/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        SubdepartmentDepartment subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();
        Long id = subdepartmentdepartment.getId();
        Model model = getModel();
        String result = subdepartmentdepartmentController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/subdepartmentdepartments/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        SubdepartmentDepartment subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();
        Long id = subdepartmentdepartment.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = subdepartmentdepartmentController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/subdepartmentdepartments", result);

        subdepartmentdepartment = SubdepartmentDepartment.findSubdepartmentDepartment(id);
        assertNull("SubdepartmentDepartment was not deleted", subdepartmentdepartment);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        SubdepartmentDepartment subdepartmentdepartment = subdepartmentdepartmentDataOnDemand.getRandomSubdepartmentDepartment();
        Long id = subdepartmentdepartment.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = subdepartmentdepartmentController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/subdepartmentdepartments", result);

        subdepartmentdepartment = SubdepartmentDepartment.findSubdepartmentDepartment(id);
        assertNull("SubdepartmentDepartment was not deleted", subdepartmentdepartment);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = subdepartmentdepartmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/subdepartmentdepartments/list", result);
    }
}

