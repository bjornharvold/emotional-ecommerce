package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Navbar;
import com.lela.data.domain.entity.NavbarDataOnDemand;
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


public class NavbarControllerTest extends AbstractControllerTest {

    private NavbarController navbarController = new NavbarController();

    @Autowired
    private NavbarDataOnDemand navbarDataOnDemand;

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

        Navbar navbar = navbarDataOnDemand.getRandomNavbar();

        String result = navbarController.create(navbar, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/navbars/"+navbar.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        navbar = navbarDataOnDemand.getRandomNavbar();

        result = navbarController.create(navbar, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/navbars/"+navbar.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        navbar = navbarDataOnDemand.getRandomNavbar();

        result = navbarController.create(navbar, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/navbars/"+navbar.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Navbar navbar = navbarDataOnDemand.getRandomNavbar();

        String result = navbarController.create(navbar, results, model, request);
        assertEquals("The form was not returned", "crud/navbars/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = navbarController.createForm(model);
        assertEquals("The form was not returned", "crud/navbars/create", result);
    }

    @Test
    public void show() {
        Navbar navbar = navbarDataOnDemand.getRandomNavbar();
        Long id = navbar.getId();
        Model model = getModel();
        String result = navbarController.show(id, model);
        assertEquals("The show view was not returned", "crud/navbars/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = navbarController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/navbars/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = navbarController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/navbars/list", result);

        page = 1;
        size = null;
        navbarController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/navbars/list", result);

        page = null;
        size = 10;
        navbarController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/navbars/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Navbar navbar = navbarDataOnDemand.getRandomNavbar();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = navbarController.update(navbar, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/navbars/"+navbar.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Navbar navbar = navbarDataOnDemand.getRandomNavbar();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = navbarController.update(navbar, results, model, request);
        assertEquals("The update view was not returned", "crud/navbars/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Navbar navbar = navbarDataOnDemand.getRandomNavbar();
        Long id = navbar.getId();
        Model model = getModel();
        String result = navbarController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/navbars/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Navbar navbar = navbarDataOnDemand.getRandomNavbar();
        Long id = navbar.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = navbarController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/navbars", result);

        navbar = Navbar.findNavbar(id);
        assertNull("Navbar was not deleted", navbar);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Navbar navbar = navbarDataOnDemand.getRandomNavbar();
        Long id = navbar.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = navbarController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/navbars", result);

        navbar = Navbar.findNavbar(id);
        assertNull("Navbar was not deleted", navbar);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = navbarController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/navbars/list", result);
    }
}

