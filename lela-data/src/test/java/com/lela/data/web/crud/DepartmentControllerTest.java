package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Department;
import com.lela.data.domain.entity.DepartmentDataOnDemand;
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


public class DepartmentControllerTest extends AbstractControllerTest {

    private DepartmentController departmentController = new DepartmentController();

    @Autowired
    private DepartmentDataOnDemand departmentDataOnDemand;

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

        Department department = departmentDataOnDemand.getRandomDepartment();

        String result = departmentController.create(department, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/departments/"+department.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        department = departmentDataOnDemand.getRandomDepartment();

        result = departmentController.create(department, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/departments/"+department.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        department = departmentDataOnDemand.getRandomDepartment();

        result = departmentController.create(department, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/departments/"+department.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Department department = departmentDataOnDemand.getRandomDepartment();

        String result = departmentController.create(department, results, model, request);
        assertEquals("The form was not returned", "crud/departments/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = departmentController.createForm(model);
        assertEquals("The form was not returned", "crud/departments/create", result);
    }

    @Test
    public void show() {
        Department department = departmentDataOnDemand.getRandomDepartment();
        Long id = department.getId();
        Model model = getModel();
        String result = departmentController.show(id, model);
        assertEquals("The show view was not returned", "crud/departments/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = departmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/departments/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = departmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/departments/list", result);

        page = 1;
        size = null;
        departmentController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/departments/list", result);

        page = null;
        size = 10;
        departmentController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/departments/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Department department = departmentDataOnDemand.getRandomDepartment();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = departmentController.update(department, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/departments/"+department.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Department department = departmentDataOnDemand.getRandomDepartment();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = departmentController.update(department, results, model, request);
        assertEquals("The update view was not returned", "crud/departments/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Department department = departmentDataOnDemand.getRandomDepartment();
        Long id = department.getId();
        Model model = getModel();
        String result = departmentController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/departments/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Department department = departmentDataOnDemand.getRandomDepartment();
        Long id = department.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = departmentController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/departments", result);

        department = Department.findDepartment(id);
        assertNull("Department was not deleted", department);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Department department = departmentDataOnDemand.getRandomDepartment();
        Long id = department.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = departmentController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/departments", result);

        department = Department.findDepartment(id);
        assertNull("Department was not deleted", department);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = departmentController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/departments/list", result);
    }
}

