package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Motivator;
import com.lela.data.domain.entity.MotivatorDataOnDemand;
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


public class MotivatorControllerTest extends AbstractControllerTest {

    private MotivatorController motivatorController = new MotivatorController();

    @Autowired
    private MotivatorDataOnDemand motivatorDataOnDemand;

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

        Motivator motivator = motivatorDataOnDemand.getRandomMotivator();

        String result = motivatorController.create(motivator, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/motivators/"+motivator.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        motivator = motivatorDataOnDemand.getRandomMotivator();

        result = motivatorController.create(motivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/motivators/"+motivator.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        motivator = motivatorDataOnDemand.getRandomMotivator();

        result = motivatorController.create(motivator, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/motivators/"+motivator.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Motivator motivator = motivatorDataOnDemand.getRandomMotivator();

        String result = motivatorController.create(motivator, results, model, request);
        assertEquals("The form was not returned", "crud/motivators/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = motivatorController.createForm(model);
        assertEquals("The form was not returned", "crud/motivators/create", result);
    }

    @Test
    public void show() {
        Motivator motivator = motivatorDataOnDemand.getRandomMotivator();
        Long id = motivator.getId();
        Model model = getModel();
        String result = motivatorController.show(id, model);
        assertEquals("The show view was not returned", "crud/motivators/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = motivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/motivators/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = motivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/motivators/list", result);

        page = 1;
        size = null;
        motivatorController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/motivators/list", result);

        page = null;
        size = 10;
        motivatorController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/motivators/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Motivator motivator = motivatorDataOnDemand.getRandomMotivator();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = motivatorController.update(motivator, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/motivators/"+motivator.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Motivator motivator = motivatorDataOnDemand.getRandomMotivator();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = motivatorController.update(motivator, results, model, request);
        assertEquals("The update view was not returned", "crud/motivators/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Motivator motivator = motivatorDataOnDemand.getRandomMotivator();
        Long id = motivator.getId();
        Model model = getModel();
        String result = motivatorController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/motivators/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Motivator motivator = motivatorDataOnDemand.getRandomMotivator();
        Long id = motivator.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = motivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/motivators", result);

        motivator = Motivator.findMotivator(id);
        assertNull("Motivator was not deleted", motivator);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Motivator motivator = motivatorDataOnDemand.getRandomMotivator();
        Long id = motivator.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = motivatorController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/motivators", result);

        motivator = Motivator.findMotivator(id);
        assertNull("Motivator was not deleted", motivator);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = motivatorController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/motivators/list", result);
    }
}

