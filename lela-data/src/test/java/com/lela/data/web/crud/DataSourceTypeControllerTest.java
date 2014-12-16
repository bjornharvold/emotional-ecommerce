package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.DataSourceType;
import com.lela.data.domain.entity.DataSourceTypeDataOnDemand;
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


public class DataSourceTypeControllerTest extends AbstractControllerTest {

    private DataSourceTypeController datasourcetypeController = new DataSourceTypeController();

    @Autowired
    private DataSourceTypeDataOnDemand datasourcetypeDataOnDemand;

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

        DataSourceType datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();

        String result = datasourcetypeController.create(datasourcetype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/datasourcetypes/"+datasourcetype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();

        result = datasourcetypeController.create(datasourcetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/datasourcetypes/"+datasourcetype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();

        result = datasourcetypeController.create(datasourcetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/datasourcetypes/"+datasourcetype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        DataSourceType datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();

        String result = datasourcetypeController.create(datasourcetype, results, model, request);
        assertEquals("The form was not returned", "crud/datasourcetypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = datasourcetypeController.createForm(model);
        assertEquals("The form was not returned", "crud/datasourcetypes/create", result);
    }

    @Test
    public void show() {
        DataSourceType datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();
        Long id = datasourcetype.getId();
        Model model = getModel();
        String result = datasourcetypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/datasourcetypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = datasourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/datasourcetypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = datasourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/datasourcetypes/list", result);

        page = 1;
        size = null;
        datasourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/datasourcetypes/list", result);

        page = null;
        size = 10;
        datasourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/datasourcetypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        DataSourceType datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = datasourcetypeController.update(datasourcetype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/datasourcetypes/"+datasourcetype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        DataSourceType datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = datasourcetypeController.update(datasourcetype, results, model, request);
        assertEquals("The update view was not returned", "crud/datasourcetypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        DataSourceType datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();
        Long id = datasourcetype.getId();
        Model model = getModel();
        String result = datasourcetypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/datasourcetypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        DataSourceType datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();
        Long id = datasourcetype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = datasourcetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/datasourcetypes", result);

        datasourcetype = DataSourceType.findDataSourceType(id);
        assertNull("DataSourceType was not deleted", datasourcetype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        DataSourceType datasourcetype = datasourcetypeDataOnDemand.getRandomDataSourceType();
        Long id = datasourcetype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = datasourcetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/datasourcetypes", result);

        datasourcetype = DataSourceType.findDataSourceType(id);
        assertNull("DataSourceType was not deleted", datasourcetype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = datasourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/datasourcetypes/list", result);
    }
}

