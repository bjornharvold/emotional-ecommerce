package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemSeries;
import com.lela.data.domain.entity.ItemSeriesDataOnDemand;
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


public class ItemSeriesControllerTest extends AbstractControllerTest {

    private ItemSeriesController itemseriesController = new ItemSeriesController();

    @Autowired
    private ItemSeriesDataOnDemand itemseriesDataOnDemand;

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

        ItemSeries itemseries = itemseriesDataOnDemand.getRandomItemSeries();

        String result = itemseriesController.create(itemseries, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemseries/"+itemseries.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemseries = itemseriesDataOnDemand.getRandomItemSeries();

        result = itemseriesController.create(itemseries, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemseries/"+itemseries.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemseries = itemseriesDataOnDemand.getRandomItemSeries();

        result = itemseriesController.create(itemseries, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemseries/"+itemseries.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemSeries itemseries = itemseriesDataOnDemand.getRandomItemSeries();

        String result = itemseriesController.create(itemseries, results, model, request);
        assertEquals("The form was not returned", "crud/itemseries/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemseriesController.createForm(model);
        assertEquals("The form was not returned", "crud/itemseries/create", result);
    }

    @Test
    public void show() {
        ItemSeries itemseries = itemseriesDataOnDemand.getRandomItemSeries();
        Long id = itemseries.getId();
        Model model = getModel();
        String result = itemseriesController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemseries/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemseriesController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemseries/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemseriesController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemseries/list", result);

        page = 1;
        size = null;
        itemseriesController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemseries/list", result);

        page = null;
        size = 10;
        itemseriesController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemseries/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemSeries itemseries = itemseriesDataOnDemand.getRandomItemSeries();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemseriesController.update(itemseries, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemseries/"+itemseries.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemSeries itemseries = itemseriesDataOnDemand.getRandomItemSeries();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemseriesController.update(itemseries, results, model, request);
        assertEquals("The update view was not returned", "crud/itemseries/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemSeries itemseries = itemseriesDataOnDemand.getRandomItemSeries();
        Long id = itemseries.getId();
        Model model = getModel();
        String result = itemseriesController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemseries/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemSeries itemseries = itemseriesDataOnDemand.getRandomItemSeries();
        Long id = itemseries.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemseriesController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemseries", result);

        itemseries = ItemSeries.findItemSeries(id);
        assertNull("ItemSeries was not deleted", itemseries);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemSeries itemseries = itemseriesDataOnDemand.getRandomItemSeries();
        Long id = itemseries.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemseriesController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemseries", result);

        itemseries = ItemSeries.findItemSeries(id);
        assertNull("ItemSeries was not deleted", itemseries);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemseriesController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemseries/list", result);
    }
}

