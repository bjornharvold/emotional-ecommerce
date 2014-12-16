package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.SeriesAttributeMap;
import com.lela.data.domain.entity.SeriesAttributeMapDataOnDemand;
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


public class SeriesAttributeMapControllerTest extends AbstractControllerTest {

    private SeriesAttributeMapController seriesattributemapController = new SeriesAttributeMapController();

    @Autowired
    private SeriesAttributeMapDataOnDemand seriesattributemapDataOnDemand;

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

        SeriesAttributeMap seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();

        String result = seriesattributemapController.create(seriesattributemap, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/seriesattributemaps/"+seriesattributemap.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();

        result = seriesattributemapController.create(seriesattributemap, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/seriesattributemaps/"+seriesattributemap.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();

        result = seriesattributemapController.create(seriesattributemap, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/seriesattributemaps/"+seriesattributemap.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        SeriesAttributeMap seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();

        String result = seriesattributemapController.create(seriesattributemap, results, model, request);
        assertEquals("The form was not returned", "crud/seriesattributemaps/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = seriesattributemapController.createForm(model);
        assertEquals("The form was not returned", "crud/seriesattributemaps/create", result);
    }

    @Test
    public void show() {
        SeriesAttributeMap seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();
        Long id = seriesattributemap.getId();
        Model model = getModel();
        String result = seriesattributemapController.show(id, model);
        assertEquals("The show view was not returned", "crud/seriesattributemaps/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = seriesattributemapController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/seriesattributemaps/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = seriesattributemapController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/seriesattributemaps/list", result);

        page = 1;
        size = null;
        seriesattributemapController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/seriesattributemaps/list", result);

        page = null;
        size = 10;
        seriesattributemapController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/seriesattributemaps/list", result);

    }

    @Test
    @Transactional
    public void update() {
        SeriesAttributeMap seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = seriesattributemapController.update(seriesattributemap, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/seriesattributemaps/"+seriesattributemap.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        SeriesAttributeMap seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = seriesattributemapController.update(seriesattributemap, results, model, request);
        assertEquals("The update view was not returned", "crud/seriesattributemaps/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        SeriesAttributeMap seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();
        Long id = seriesattributemap.getId();
        Model model = getModel();
        String result = seriesattributemapController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/seriesattributemaps/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        SeriesAttributeMap seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();
        Long id = seriesattributemap.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = seriesattributemapController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/seriesattributemaps", result);

        seriesattributemap = SeriesAttributeMap.findSeriesAttributeMap(id);
        assertNull("SeriesAttributeMap was not deleted", seriesattributemap);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        SeriesAttributeMap seriesattributemap = seriesattributemapDataOnDemand.getRandomSeriesAttributeMap();
        Long id = seriesattributemap.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = seriesattributemapController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/seriesattributemaps", result);

        seriesattributemap = SeriesAttributeMap.findSeriesAttributeMap(id);
        assertNull("SeriesAttributeMap was not deleted", seriesattributemap);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = seriesattributemapController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/seriesattributemaps/list", result);
    }
}

