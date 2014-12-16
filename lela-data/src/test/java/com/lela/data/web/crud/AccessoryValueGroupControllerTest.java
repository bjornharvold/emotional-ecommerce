package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AccessoryValueGroup;
import com.lela.data.domain.entity.AccessoryValueGroupDataOnDemand;
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


public class AccessoryValueGroupControllerTest extends AbstractControllerTest {

    private AccessoryValueGroupController accessoryvaluegroupController = new AccessoryValueGroupController();

    @Autowired
    private AccessoryValueGroupDataOnDemand accessoryvaluegroupDataOnDemand;

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

        AccessoryValueGroup accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();

        String result = accessoryvaluegroupController.create(accessoryvaluegroup, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/accessoryvaluegroups/"+accessoryvaluegroup.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();

        result = accessoryvaluegroupController.create(accessoryvaluegroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/accessoryvaluegroups/"+accessoryvaluegroup.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();

        result = accessoryvaluegroupController.create(accessoryvaluegroup, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/accessoryvaluegroups/"+accessoryvaluegroup.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AccessoryValueGroup accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();

        String result = accessoryvaluegroupController.create(accessoryvaluegroup, results, model, request);
        assertEquals("The form was not returned", "crud/accessoryvaluegroups/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = accessoryvaluegroupController.createForm(model);
        assertEquals("The form was not returned", "crud/accessoryvaluegroups/create", result);
    }

    @Test
    public void show() {
        AccessoryValueGroup accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();
        Long id = accessoryvaluegroup.getId();
        Model model = getModel();
        String result = accessoryvaluegroupController.show(id, model);
        assertEquals("The show view was not returned", "crud/accessoryvaluegroups/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = accessoryvaluegroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/accessoryvaluegroups/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = accessoryvaluegroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/accessoryvaluegroups/list", result);

        page = 1;
        size = null;
        accessoryvaluegroupController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/accessoryvaluegroups/list", result);

        page = null;
        size = 10;
        accessoryvaluegroupController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/accessoryvaluegroups/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AccessoryValueGroup accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = accessoryvaluegroupController.update(accessoryvaluegroup, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/accessoryvaluegroups/"+accessoryvaluegroup.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AccessoryValueGroup accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = accessoryvaluegroupController.update(accessoryvaluegroup, results, model, request);
        assertEquals("The update view was not returned", "crud/accessoryvaluegroups/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AccessoryValueGroup accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();
        Long id = accessoryvaluegroup.getId();
        Model model = getModel();
        String result = accessoryvaluegroupController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/accessoryvaluegroups/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AccessoryValueGroup accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();
        Long id = accessoryvaluegroup.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = accessoryvaluegroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/accessoryvaluegroups", result);

        accessoryvaluegroup = AccessoryValueGroup.findAccessoryValueGroup(id);
        assertNull("AccessoryValueGroup was not deleted", accessoryvaluegroup);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AccessoryValueGroup accessoryvaluegroup = accessoryvaluegroupDataOnDemand.getRandomAccessoryValueGroup();
        Long id = accessoryvaluegroup.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = accessoryvaluegroupController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/accessoryvaluegroups", result);

        accessoryvaluegroup = AccessoryValueGroup.findAccessoryValueGroup(id);
        assertNull("AccessoryValueGroup was not deleted", accessoryvaluegroup);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = accessoryvaluegroupController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/accessoryvaluegroups/list", result);
    }
}

