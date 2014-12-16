package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.CleanseRule;
import com.lela.data.domain.entity.CleanseRuleDataOnDemand;
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


public class CleanseRuleControllerTest extends AbstractControllerTest {

    private CleanseRuleController cleanseruleController = new CleanseRuleController();

    @Autowired
    private CleanseRuleDataOnDemand cleanseruleDataOnDemand;

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

        CleanseRule cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();

        String result = cleanseruleController.create(cleanserule, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/cleanserules/"+cleanserule.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();

        result = cleanseruleController.create(cleanserule, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/cleanserules/"+cleanserule.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();

        result = cleanseruleController.create(cleanserule, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/cleanserules/"+cleanserule.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        CleanseRule cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();

        String result = cleanseruleController.create(cleanserule, results, model, request);
        assertEquals("The form was not returned", "crud/cleanserules/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = cleanseruleController.createForm(model);
        assertEquals("The form was not returned", "crud/cleanserules/create", result);
    }

    @Test
    public void show() {
        CleanseRule cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();
        Long id = cleanserule.getId();
        Model model = getModel();
        String result = cleanseruleController.show(id, model);
        assertEquals("The show view was not returned", "crud/cleanserules/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = cleanseruleController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/cleanserules/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = cleanseruleController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/cleanserules/list", result);

        page = 1;
        size = null;
        cleanseruleController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/cleanserules/list", result);

        page = null;
        size = 10;
        cleanseruleController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/cleanserules/list", result);

    }

    @Test
    @Transactional
    public void update() {
        CleanseRule cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = cleanseruleController.update(cleanserule, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/cleanserules/"+cleanserule.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        CleanseRule cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = cleanseruleController.update(cleanserule, results, model, request);
        assertEquals("The update view was not returned", "crud/cleanserules/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        CleanseRule cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();
        Long id = cleanserule.getId();
        Model model = getModel();
        String result = cleanseruleController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/cleanserules/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        CleanseRule cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();
        Long id = cleanserule.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = cleanseruleController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/cleanserules", result);

        cleanserule = CleanseRule.findCleanseRule(id);
        assertNull("CleanseRule was not deleted", cleanserule);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        CleanseRule cleanserule = cleanseruleDataOnDemand.getRandomCleanseRule();
        Long id = cleanserule.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = cleanseruleController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/cleanserules", result);

        cleanserule = CleanseRule.findCleanseRule(id);
        assertNull("CleanseRule was not deleted", cleanserule);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = cleanseruleController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/cleanserules/list", result);
    }
}

