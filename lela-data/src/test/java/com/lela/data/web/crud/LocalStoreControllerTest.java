package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.LocalStore;
import com.lela.data.domain.entity.LocalStoreDataOnDemand;
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


public class LocalStoreControllerTest extends AbstractControllerTest {

    private LocalStoreController localstoreController = new LocalStoreController();

    @Autowired
    private LocalStoreDataOnDemand localstoreDataOnDemand;

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

        LocalStore localstore = localstoreDataOnDemand.getRandomLocalStore();

        String result = localstoreController.create(localstore, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/localstores/"+localstore.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        localstore = localstoreDataOnDemand.getRandomLocalStore();

        result = localstoreController.create(localstore, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/localstores/"+localstore.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        localstore = localstoreDataOnDemand.getRandomLocalStore();

        result = localstoreController.create(localstore, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/localstores/"+localstore.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        LocalStore localstore = localstoreDataOnDemand.getRandomLocalStore();

        String result = localstoreController.create(localstore, results, model, request);
        assertEquals("The form was not returned", "crud/localstores/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = localstoreController.createForm(model);
        assertEquals("The form was not returned", "crud/localstores/create", result);
    }

    @Test
    public void show() {
        LocalStore localstore = localstoreDataOnDemand.getRandomLocalStore();
        Long id = localstore.getId();
        Model model = getModel();
        String result = localstoreController.show(id, model);
        assertEquals("The show view was not returned", "crud/localstores/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = localstoreController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/localstores/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = localstoreController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/localstores/list", result);

        page = 1;
        size = null;
        localstoreController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/localstores/list", result);

        page = null;
        size = 10;
        localstoreController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/localstores/list", result);

    }

    @Test
    @Transactional
    public void update() {
        LocalStore localstore = localstoreDataOnDemand.getRandomLocalStore();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = localstoreController.update(localstore, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/localstores/"+localstore.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        LocalStore localstore = localstoreDataOnDemand.getRandomLocalStore();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = localstoreController.update(localstore, results, model, request);
        assertEquals("The update view was not returned", "crud/localstores/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        LocalStore localstore = localstoreDataOnDemand.getRandomLocalStore();
        Long id = localstore.getId();
        Model model = getModel();
        String result = localstoreController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/localstores/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        LocalStore localstore = localstoreDataOnDemand.getRandomLocalStore();
        Long id = localstore.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = localstoreController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/localstores", result);

        localstore = LocalStore.findLocalStore(id);
        assertNull("LocalStore was not deleted", localstore);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        LocalStore localstore = localstoreDataOnDemand.getRandomLocalStore();
        Long id = localstore.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = localstoreController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/localstores", result);

        localstore = LocalStore.findLocalStore(id);
        assertNull("LocalStore was not deleted", localstore);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = localstoreController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/localstores/list", result);
    }
}

