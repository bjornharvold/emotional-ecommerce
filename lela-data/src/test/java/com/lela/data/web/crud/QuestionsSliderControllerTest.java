package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.QuestionsSlider;
import com.lela.data.domain.entity.QuestionsSliderDataOnDemand;
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


public class QuestionsSliderControllerTest extends AbstractControllerTest {

    private QuestionsSliderController questionssliderController = new QuestionsSliderController();

    @Autowired
    private QuestionsSliderDataOnDemand questionssliderDataOnDemand;

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

        QuestionsSlider questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();

        String result = questionssliderController.create(questionsslider, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/questionssliders/"+questionsslider.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();

        result = questionssliderController.create(questionsslider, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/questionssliders/"+questionsslider.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();

        result = questionssliderController.create(questionsslider, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/questionssliders/"+questionsslider.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        QuestionsSlider questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();

        String result = questionssliderController.create(questionsslider, results, model, request);
        assertEquals("The form was not returned", "crud/questionssliders/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = questionssliderController.createForm(model);
        assertEquals("The form was not returned", "crud/questionssliders/create", result);
    }

    @Test
    public void show() {
        QuestionsSlider questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();
        Long id = questionsslider.getId();
        Model model = getModel();
        String result = questionssliderController.show(id, model);
        assertEquals("The show view was not returned", "crud/questionssliders/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = questionssliderController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/questionssliders/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = questionssliderController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/questionssliders/list", result);

        page = 1;
        size = null;
        questionssliderController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/questionssliders/list", result);

        page = null;
        size = 10;
        questionssliderController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/questionssliders/list", result);

    }

    @Test
    @Transactional
    public void update() {
        QuestionsSlider questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = questionssliderController.update(questionsslider, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/questionssliders/"+questionsslider.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        QuestionsSlider questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = questionssliderController.update(questionsslider, results, model, request);
        assertEquals("The update view was not returned", "crud/questionssliders/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        QuestionsSlider questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();
        Long id = questionsslider.getId();
        Model model = getModel();
        String result = questionssliderController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/questionssliders/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        QuestionsSlider questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();
        Long id = questionsslider.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = questionssliderController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/questionssliders", result);

        questionsslider = QuestionsSlider.findQuestionsSlider(id);
        assertNull("QuestionsSlider was not deleted", questionsslider);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        QuestionsSlider questionsslider = questionssliderDataOnDemand.getRandomQuestionsSlider();
        Long id = questionsslider.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = questionssliderController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/questionssliders", result);

        questionsslider = QuestionsSlider.findQuestionsSlider(id);
        assertNull("QuestionsSlider was not deleted", questionsslider);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = questionssliderController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/questionssliders/list", result);
    }
}

