package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.MultiValuedType;
import com.lela.data.domain.entity.MultiValuedTypeDataOnDemand;
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


public class MultiValuedTypeControllerTest extends AbstractControllerTest {

    private MultiValuedTypeController multivaluedtypeController = new MultiValuedTypeController();

    @Autowired
    private MultiValuedTypeDataOnDemand multivaluedtypeDataOnDemand;

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

        MultiValuedType multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();

        String result = multivaluedtypeController.create(multivaluedtype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/multivaluedtypes/"+multivaluedtype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();

        result = multivaluedtypeController.create(multivaluedtype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/multivaluedtypes/"+multivaluedtype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();

        result = multivaluedtypeController.create(multivaluedtype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/multivaluedtypes/"+multivaluedtype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        MultiValuedType multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();

        String result = multivaluedtypeController.create(multivaluedtype, results, model, request);
        assertEquals("The form was not returned", "crud/multivaluedtypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = multivaluedtypeController.createForm(model);
        assertEquals("The form was not returned", "crud/multivaluedtypes/create", result);
    }

    @Test
    public void show() {
        MultiValuedType multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();
        Long id = multivaluedtype.getId();
        Model model = getModel();
        String result = multivaluedtypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/multivaluedtypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = multivaluedtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/multivaluedtypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = multivaluedtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/multivaluedtypes/list", result);

        page = 1;
        size = null;
        multivaluedtypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/multivaluedtypes/list", result);

        page = null;
        size = 10;
        multivaluedtypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/multivaluedtypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        MultiValuedType multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = multivaluedtypeController.update(multivaluedtype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/multivaluedtypes/"+multivaluedtype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        MultiValuedType multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = multivaluedtypeController.update(multivaluedtype, results, model, request);
        assertEquals("The update view was not returned", "crud/multivaluedtypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        MultiValuedType multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();
        Long id = multivaluedtype.getId();
        Model model = getModel();
        String result = multivaluedtypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/multivaluedtypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        MultiValuedType multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();
        Long id = multivaluedtype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = multivaluedtypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/multivaluedtypes", result);

        multivaluedtype = MultiValuedType.findMultiValuedType(id);
        assertNull("MultiValuedType was not deleted", multivaluedtype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        MultiValuedType multivaluedtype = multivaluedtypeDataOnDemand.getRandomMultiValuedType();
        Long id = multivaluedtype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = multivaluedtypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/multivaluedtypes", result);

        multivaluedtype = MultiValuedType.findMultiValuedType(id);
        assertNull("MultiValuedType was not deleted", multivaluedtype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = multivaluedtypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/multivaluedtypes/list", result);
    }
}

