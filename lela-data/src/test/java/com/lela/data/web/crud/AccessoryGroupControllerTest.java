package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AccessoryGroup;
import com.lela.data.domain.entity.AccessoryGroupDataOnDemand;
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


public class AccessoryGroupControllerTest extends AbstractControllerTest {

    private AccessoryGroupController accessorygroupController = new AccessoryGroupController();

    @Autowired
    private AccessoryGroupDataOnDemand accessorygroupDataOnDemand;

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

        AccessoryGroup accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();

        String result = accessorygroupController.create(accessorygroup, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/accessorygroups/"+accessorygroup.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();

        result = accessorygroupController.create(accessorygroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/accessorygroups/"+accessorygroup.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();

        result = accessorygroupController.create(accessorygroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/accessorygroups/"+accessorygroup.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AccessoryGroup accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();

        String result = accessorygroupController.create(accessorygroup, results, model, request);
        assertEquals("The form was not returned", "crud/accessorygroups/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = accessorygroupController.createForm(model);
        assertEquals("The form was not returned", "crud/accessorygroups/create", result);
    }

    @Test
    public void show() {
        AccessoryGroup accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();
        Long id = accessorygroup.getId();
        Model model = getModel();
        String result = accessorygroupController.show(id, model);
        assertEquals("The show view was not returned", "crud/accessorygroups/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = accessorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/accessorygroups/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = accessorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/accessorygroups/list", result);

        page = 1;
        size = null;
        accessorygroupController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/accessorygroups/list", result);

        page = null;
        size = 10;
        accessorygroupController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/accessorygroups/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AccessoryGroup accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = accessorygroupController.update(accessorygroup, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/accessorygroups/"+accessorygroup.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AccessoryGroup accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = accessorygroupController.update(accessorygroup, results, model, request);
        assertEquals("The update view was not returned", "crud/accessorygroups/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AccessoryGroup accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();
        Long id = accessorygroup.getId();
        Model model = getModel();
        String result = accessorygroupController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/accessorygroups/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AccessoryGroup accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();
        Long id = accessorygroup.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = accessorygroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/accessorygroups", result);

        accessorygroup = AccessoryGroup.findAccessoryGroup(id);
        assertNull("AccessoryGroup was not deleted", accessorygroup);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AccessoryGroup accessorygroup = accessorygroupDataOnDemand.getRandomAccessoryGroup();
        Long id = accessorygroup.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = accessorygroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/accessorygroups", result);

        accessorygroup = AccessoryGroup.findAccessoryGroup(id);
        assertNull("AccessoryGroup was not deleted", accessorygroup);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = accessorygroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/accessorygroups/list", result);
    }
}

