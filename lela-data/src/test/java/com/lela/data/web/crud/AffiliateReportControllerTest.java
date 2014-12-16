package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.AffiliateReport;
import com.lela.data.domain.entity.AffiliateReportDataOnDemand;
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


public class AffiliateReportControllerTest extends AbstractControllerTest {

    private AffiliateReportController affiliatereportController = new AffiliateReportController();

    @Autowired
    private AffiliateReportDataOnDemand affiliatereportDataOnDemand;

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

        AffiliateReport affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();

        String result = affiliatereportController.create(affiliatereport, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/affiliatereports/"+affiliatereport.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();

        result = affiliatereportController.create(affiliatereport, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/affiliatereports/"+affiliatereport.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();

        result = affiliatereportController.create(affiliatereport, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/affiliatereports/"+affiliatereport.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        AffiliateReport affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();

        String result = affiliatereportController.create(affiliatereport, results, model, request);
        assertEquals("The form was not returned", "crud/affiliatereports/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = affiliatereportController.createForm(model);
        assertEquals("The form was not returned", "crud/affiliatereports/create", result);
    }

    @Test
    public void show() {
        AffiliateReport affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();
        Long id = affiliatereport.getId();
        Model model = getModel();
        String result = affiliatereportController.show(id, model);
        assertEquals("The show view was not returned", "crud/affiliatereports/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = affiliatereportController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/affiliatereports/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = affiliatereportController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/affiliatereports/list", result);

        page = 1;
        size = null;
        affiliatereportController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/affiliatereports/list", result);

        page = null;
        size = 10;
        affiliatereportController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/affiliatereports/list", result);

    }

    @Test
    @Transactional
    public void update() {
        AffiliateReport affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = affiliatereportController.update(affiliatereport, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/affiliatereports/"+affiliatereport.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        AffiliateReport affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = affiliatereportController.update(affiliatereport, results, model, request);
        assertEquals("The update view was not returned", "crud/affiliatereports/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        AffiliateReport affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();
        Long id = affiliatereport.getId();
        Model model = getModel();
        String result = affiliatereportController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/affiliatereports/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        AffiliateReport affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();
        Long id = affiliatereport.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = affiliatereportController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/affiliatereports", result);

        affiliatereport = AffiliateReport.findAffiliateReport(id);
        assertNull("AffiliateReport was not deleted", affiliatereport);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        AffiliateReport affiliatereport = affiliatereportDataOnDemand.getRandomAffiliateReport();
        Long id = affiliatereport.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = affiliatereportController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/affiliatereports", result);

        affiliatereport = AffiliateReport.findAffiliateReport(id);
        assertNull("AffiliateReport was not deleted", affiliatereport);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = affiliatereportController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/affiliatereports/list", result);
    }
}

