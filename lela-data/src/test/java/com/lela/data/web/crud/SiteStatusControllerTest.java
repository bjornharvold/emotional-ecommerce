package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.SiteStatus;
import com.lela.data.domain.entity.SiteStatusDataOnDemand;
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


public class SiteStatusControllerTest extends AbstractControllerTest {

    private SiteStatusController sitestatusController = new SiteStatusController();

    @Autowired
    private SiteStatusDataOnDemand sitestatusDataOnDemand;

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

        SiteStatus sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();

        String result = sitestatusController.create(sitestatus, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/sitestatus/"+sitestatus.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();

        result = sitestatusController.create(sitestatus, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/sitestatus/"+sitestatus.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();

        result = sitestatusController.create(sitestatus, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/sitestatus/"+sitestatus.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        SiteStatus sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();

        String result = sitestatusController.create(sitestatus, results, model, request);
        assertEquals("The form was not returned", "crud/sitestatus/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = sitestatusController.createForm(model);
        assertEquals("The form was not returned", "crud/sitestatus/create", result);
    }

    @Test
    public void show() {
        SiteStatus sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();
        Long id = sitestatus.getId();
        Model model = getModel();
        String result = sitestatusController.show(id, model);
        assertEquals("The show view was not returned", "crud/sitestatus/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = sitestatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/sitestatus/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = sitestatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/sitestatus/list", result);

        page = 1;
        size = null;
        sitestatusController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/sitestatus/list", result);

        page = null;
        size = 10;
        sitestatusController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/sitestatus/list", result);

    }

    @Test
    @Transactional
    public void update() {
        SiteStatus sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = sitestatusController.update(sitestatus, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/sitestatus/"+sitestatus.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        SiteStatus sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = sitestatusController.update(sitestatus, results, model, request);
        assertEquals("The update view was not returned", "crud/sitestatus/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        SiteStatus sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();
        Long id = sitestatus.getId();
        Model model = getModel();
        String result = sitestatusController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/sitestatus/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        SiteStatus sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();
        Long id = sitestatus.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = sitestatusController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/sitestatus", result);

        sitestatus = SiteStatus.findSiteStatus(id);
        assertNull("SiteStatus was not deleted", sitestatus);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        SiteStatus sitestatus = sitestatusDataOnDemand.getRandomSiteStatus();
        Long id = sitestatus.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = sitestatusController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/sitestatus", result);

        sitestatus = SiteStatus.findSiteStatus(id);
        assertNull("SiteStatus was not deleted", sitestatus);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = sitestatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/sitestatus/list", result);
    }
}

