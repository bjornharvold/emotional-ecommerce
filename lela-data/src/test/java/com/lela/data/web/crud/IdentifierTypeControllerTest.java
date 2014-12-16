package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.IdentifierType;
import com.lela.data.domain.entity.IdentifierTypeDataOnDemand;
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


public class IdentifierTypeControllerTest extends AbstractControllerTest {

    private IdentifierTypeController identifiertypeController = new IdentifierTypeController();

    @Autowired
    private IdentifierTypeDataOnDemand identifiertypeDataOnDemand;

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

        IdentifierType identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();

        String result = identifiertypeController.create(identifiertype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/identifiertypes/"+identifiertype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();

        result = identifiertypeController.create(identifiertype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/identifiertypes/"+identifiertype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();

        result = identifiertypeController.create(identifiertype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/identifiertypes/"+identifiertype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        IdentifierType identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();

        String result = identifiertypeController.create(identifiertype, results, model, request);
        assertEquals("The form was not returned", "crud/identifiertypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = identifiertypeController.createForm(model);
        assertEquals("The form was not returned", "crud/identifiertypes/create", result);
    }

    @Test
    public void show() {
        IdentifierType identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();
        Long id = identifiertype.getId();
        Model model = getModel();
        String result = identifiertypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/identifiertypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = identifiertypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/identifiertypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = identifiertypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/identifiertypes/list", result);

        page = 1;
        size = null;
        identifiertypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/identifiertypes/list", result);

        page = null;
        size = 10;
        identifiertypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/identifiertypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        IdentifierType identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = identifiertypeController.update(identifiertype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/identifiertypes/"+identifiertype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        IdentifierType identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = identifiertypeController.update(identifiertype, results, model, request);
        assertEquals("The update view was not returned", "crud/identifiertypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        IdentifierType identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();
        Long id = identifiertype.getId();
        Model model = getModel();
        String result = identifiertypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/identifiertypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        IdentifierType identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();
        Long id = identifiertype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = identifiertypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/identifiertypes", result);

        identifiertype = IdentifierType.findIdentifierType(id);
        assertNull("IdentifierType was not deleted", identifiertype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        IdentifierType identifiertype = identifiertypeDataOnDemand.getRandomIdentifierType();
        Long id = identifiertype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = identifiertypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/identifiertypes", result);

        identifiertype = IdentifierType.findIdentifierType(id);
        assertNull("IdentifierType was not deleted", identifiertype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = identifiertypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/identifiertypes/list", result);
    }
}

