package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.Series;
import com.lela.data.domain.entity.SeriesDataOnDemand;
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


public class SeriesControllerTest extends AbstractControllerTest {

    private SeriesController seriesController = new SeriesController();

    @Autowired
    private SeriesDataOnDemand seriesDataOnDemand;

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

        Series series = seriesDataOnDemand.getRandomSeries();

        String result = seriesController.create(series, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/series/"+series.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        series = seriesDataOnDemand.getRandomSeries();

        result = seriesController.create(series, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/series/"+series.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        series = seriesDataOnDemand.getRandomSeries();

        result = seriesController.create(series, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/series/"+series.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        Series series = seriesDataOnDemand.getRandomSeries();

        String result = seriesController.create(series, results, model, request);
        assertEquals("The form was not returned", "crud/series/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = seriesController.createForm(model);
        assertEquals("The form was not returned", "crud/series/create", result);
    }

    @Test
    public void show() {
        Series series = seriesDataOnDemand.getRandomSeries();
        Long id = series.getId();
        Model model = getModel();
        String result = seriesController.show(id, model);
        assertEquals("The show view was not returned", "crud/series/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = seriesController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/series/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = seriesController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/series/list", result);

        page = 1;
        size = null;
        seriesController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/series/list", result);

        page = null;
        size = 10;
        seriesController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/series/list", result);

    }

    @Test
    @Transactional
    public void update() {
        Series series = seriesDataOnDemand.getRandomSeries();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = seriesController.update(series, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/series/"+series.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        Series series = seriesDataOnDemand.getRandomSeries();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = seriesController.update(series, results, model, request);
        assertEquals("The update view was not returned", "crud/series/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        Series series = seriesDataOnDemand.getRandomSeries();
        Long id = series.getId();
        Model model = getModel();
        String result = seriesController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/series/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        Series series = seriesDataOnDemand.getRandomSeries();
        Long id = series.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = seriesController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/series", result);

        series = Series.findSeries(id);
        assertNull("Series was not deleted", series);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        Series series = seriesDataOnDemand.getRandomSeries();
        Long id = series.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = seriesController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/series", result);

        series = Series.findSeries(id);
        assertNull("Series was not deleted", series);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = seriesController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/series/list", result);
    }
}

