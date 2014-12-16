package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Network;
import com.lela.data.domain.entity.NetworkDataOnDemand;
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


public class NetworkControllerTest extends AbstractControllerTest {

    private NetworkController networkController = new NetworkController();

    @Autowired
    private NetworkDataOnDemand networkDataOnDemand;

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

        Network network = networkDataOnDemand.getRandomNetwork();

        String result = networkController.create(network, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/networks/"+network.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        network = networkDataOnDemand.getRandomNetwork();

        result = networkController.create(network, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/networks/"+network.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        network = networkDataOnDemand.getRandomNetwork();

        result = networkController.create(network, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/networks/"+network.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Network network = networkDataOnDemand.getRandomNetwork();

        String result = networkController.create(network, results, model, request);
        assertEquals("The form was not returned", "crud/networks/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = networkController.createForm(model);
        assertEquals("The form was not returned", "crud/networks/create", result);
    }

    @Test
    public void show() {
        Network network = networkDataOnDemand.getRandomNetwork();
        Long id = network.getId();
        Model model = getModel();
        String result = networkController.show(id, model);
        assertEquals("The show view was not returned", "crud/networks/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = networkController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/networks/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = networkController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/networks/list", result);

        page = 1;
        size = null;
        networkController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/networks/list", result);

        page = null;
        size = 10;
        networkController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/networks/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Network network = networkDataOnDemand.getRandomNetwork();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = networkController.update(network, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/networks/"+network.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Network network = networkDataOnDemand.getRandomNetwork();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = networkController.update(network, results, model, request);
        assertEquals("The update view was not returned", "crud/networks/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Network network = networkDataOnDemand.getRandomNetwork();
        Long id = network.getId();
        Model model = getModel();
        String result = networkController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/networks/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Network network = networkDataOnDemand.getRandomNetwork();
        Long id = network.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = networkController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/networks", result);

        network = Network.findNetwork(id);
        assertNull("Network was not deleted", network);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Network network = networkDataOnDemand.getRandomNetwork();
        Long id = network.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = networkController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/networks", result);

        network = Network.findNetwork(id);
        assertNull("Network was not deleted", network);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = networkController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/networks/list", result);
    }
}

