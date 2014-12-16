package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.SeriesType;
import com.lela.data.domain.entity.SeriesTypeDataOnDemand;
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


public class SeriesTypeControllerTest extends AbstractControllerTest {

    private SeriesTypeController seriestypeController = new SeriesTypeController();

    @Autowired
    private SeriesTypeDataOnDemand seriestypeDataOnDemand;

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

        SeriesType seriestype = seriestypeDataOnDemand.getRandomSeriesType();

        String result = seriestypeController.create(seriestype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/seriestypes/"+seriestype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        seriestype = seriestypeDataOnDemand.getRandomSeriesType();

        result = seriestypeController.create(seriestype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/seriestypes/"+seriestype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        seriestype = seriestypeDataOnDemand.getRandomSeriesType();

        result = seriestypeController.create(seriestype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/seriestypes/"+seriestype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        SeriesType seriestype = seriestypeDataOnDemand.getRandomSeriesType();

        String result = seriestypeController.create(seriestype, results, model, request);
        assertEquals("The form was not returned", "crud/seriestypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = seriestypeController.createForm(model);
        assertEquals("The form was not returned", "crud/seriestypes/create", result);
    }

    @Test
    public void show() {
        SeriesType seriestype = seriestypeDataOnDemand.getRandomSeriesType();
        Long id = seriestype.getId();
        Model model = getModel();
        String result = seriestypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/seriestypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = seriestypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/seriestypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = seriestypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/seriestypes/list", result);

        page = 1;
        size = null;
        seriestypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/seriestypes/list", result);

        page = null;
        size = 10;
        seriestypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/seriestypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        SeriesType seriestype = seriestypeDataOnDemand.getRandomSeriesType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = seriestypeController.update(seriestype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/seriestypes/"+seriestype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        SeriesType seriestype = seriestypeDataOnDemand.getRandomSeriesType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = seriestypeController.update(seriestype, results, model, request);
        assertEquals("The update view was not returned", "crud/seriestypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        SeriesType seriestype = seriestypeDataOnDemand.getRandomSeriesType();
        Long id = seriestype.getId();
        Model model = getModel();
        String result = seriestypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/seriestypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        SeriesType seriestype = seriestypeDataOnDemand.getRandomSeriesType();
        Long id = seriestype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = seriestypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/seriestypes", result);

        seriestype = SeriesType.findSeriesType(id);
        assertNull("SeriesType was not deleted", seriestype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        SeriesType seriestype = seriestypeDataOnDemand.getRandomSeriesType();
        Long id = seriestype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = seriestypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/seriestypes", result);

        seriestype = SeriesType.findSeriesType(id);
        assertNull("SeriesType was not deleted", seriestype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = seriestypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/seriestypes/list", result);
    }
}

