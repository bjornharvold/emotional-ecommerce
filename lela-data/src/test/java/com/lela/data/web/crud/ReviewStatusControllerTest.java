package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ReviewStatus;
import com.lela.data.domain.entity.ReviewStatusDataOnDemand;
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


public class ReviewStatusControllerTest extends AbstractControllerTest {

    private ReviewStatusController reviewstatusController = new ReviewStatusController();

    @Autowired
    private ReviewStatusDataOnDemand reviewstatusDataOnDemand;

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

        ReviewStatus reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();

        String result = reviewstatusController.create(reviewstatus, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/reviewstatus/"+reviewstatus.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();

        result = reviewstatusController.create(reviewstatus, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/reviewstatus/"+reviewstatus.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();

        result = reviewstatusController.create(reviewstatus, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/reviewstatus/"+reviewstatus.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ReviewStatus reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();

        String result = reviewstatusController.create(reviewstatus, results, model, request);
        assertEquals("The form was not returned", "crud/reviewstatus/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = reviewstatusController.createForm(model);
        assertEquals("The form was not returned", "crud/reviewstatus/create", result);
    }

    @Test
    public void show() {
        ReviewStatus reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();
        Long id = reviewstatus.getId();
        Model model = getModel();
        String result = reviewstatusController.show(id, model);
        assertEquals("The show view was not returned", "crud/reviewstatus/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = reviewstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/reviewstatus/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = reviewstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/reviewstatus/list", result);

        page = 1;
        size = null;
        reviewstatusController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/reviewstatus/list", result);

        page = null;
        size = 10;
        reviewstatusController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/reviewstatus/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ReviewStatus reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = reviewstatusController.update(reviewstatus, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/reviewstatus/"+reviewstatus.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ReviewStatus reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = reviewstatusController.update(reviewstatus, results, model, request);
        assertEquals("The update view was not returned", "crud/reviewstatus/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ReviewStatus reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();
        Long id = reviewstatus.getId();
        Model model = getModel();
        String result = reviewstatusController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/reviewstatus/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ReviewStatus reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();
        Long id = reviewstatus.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = reviewstatusController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/reviewstatus", result);

        reviewstatus = ReviewStatus.findReviewStatus(id);
        assertNull("ReviewStatus was not deleted", reviewstatus);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ReviewStatus reviewstatus = reviewstatusDataOnDemand.getRandomReviewStatus();
        Long id = reviewstatus.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = reviewstatusController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/reviewstatus", result);

        reviewstatus = ReviewStatus.findReviewStatus(id);
        assertNull("ReviewStatus was not deleted", reviewstatus);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = reviewstatusController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/reviewstatus/list", result);
    }
}

