package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ImageSource;
import com.lela.data.domain.entity.ImageSourceDataOnDemand;
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


public class ImageSourceControllerTest extends AbstractControllerTest {

    private ImageSourceController imagesourceController = new ImageSourceController();

    @Autowired
    private ImageSourceDataOnDemand imagesourceDataOnDemand;

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

        ImageSource imagesource = imagesourceDataOnDemand.getRandomImageSource();

        String result = imagesourceController.create(imagesource, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/imagesources/"+imagesource.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        imagesource = imagesourceDataOnDemand.getRandomImageSource();

        result = imagesourceController.create(imagesource, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/imagesources/"+imagesource.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        imagesource = imagesourceDataOnDemand.getRandomImageSource();

        result = imagesourceController.create(imagesource, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/imagesources/"+imagesource.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ImageSource imagesource = imagesourceDataOnDemand.getRandomImageSource();

        String result = imagesourceController.create(imagesource, results, model, request);
        assertEquals("The form was not returned", "crud/imagesources/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = imagesourceController.createForm(model);
        assertEquals("The form was not returned", "crud/imagesources/create", result);
    }

    @Test
    public void show() {
        ImageSource imagesource = imagesourceDataOnDemand.getRandomImageSource();
        Long id = imagesource.getId();
        Model model = getModel();
        String result = imagesourceController.show(id, model);
        assertEquals("The show view was not returned", "crud/imagesources/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = imagesourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/imagesources/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = imagesourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/imagesources/list", result);

        page = 1;
        size = null;
        imagesourceController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/imagesources/list", result);

        page = null;
        size = 10;
        imagesourceController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/imagesources/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ImageSource imagesource = imagesourceDataOnDemand.getRandomImageSource();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = imagesourceController.update(imagesource, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/imagesources/"+imagesource.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ImageSource imagesource = imagesourceDataOnDemand.getRandomImageSource();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = imagesourceController.update(imagesource, results, model, request);
        assertEquals("The update view was not returned", "crud/imagesources/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ImageSource imagesource = imagesourceDataOnDemand.getRandomImageSource();
        Long id = imagesource.getId();
        Model model = getModel();
        String result = imagesourceController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/imagesources/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ImageSource imagesource = imagesourceDataOnDemand.getRandomImageSource();
        Long id = imagesource.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = imagesourceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/imagesources", result);

        imagesource = ImageSource.findImageSource(id);
        assertNull("ImageSource was not deleted", imagesource);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ImageSource imagesource = imagesourceDataOnDemand.getRandomImageSource();
        Long id = imagesource.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = imagesourceController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/imagesources", result);

        imagesource = ImageSource.findImageSource(id);
        assertNull("ImageSource was not deleted", imagesource);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = imagesourceController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/imagesources/list", result);
    }
}

