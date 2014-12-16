package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ImageSourceType;
import com.lela.data.domain.entity.ImageSourceTypeDataOnDemand;
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


public class ImageSourceTypeControllerTest extends AbstractControllerTest {

    private ImageSourceTypeController imagesourcetypeController = new ImageSourceTypeController();

    @Autowired
    private ImageSourceTypeDataOnDemand imagesourcetypeDataOnDemand;

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

        ImageSourceType imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();

        String result = imagesourcetypeController.create(imagesourcetype, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/imagesourcetypes/"+imagesourcetype.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();

        result = imagesourcetypeController.create(imagesourcetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/imagesourcetypes/"+imagesourcetype.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();

        result = imagesourcetypeController.create(imagesourcetype, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/imagesourcetypes/"+imagesourcetype.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ImageSourceType imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();

        String result = imagesourcetypeController.create(imagesourcetype, results, model, request);
        assertEquals("The form was not returned", "crud/imagesourcetypes/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = imagesourcetypeController.createForm(model);
        assertEquals("The form was not returned", "crud/imagesourcetypes/create", result);
    }

    @Test
    public void show() {
        ImageSourceType imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();
        Long id = imagesourcetype.getId();
        Model model = getModel();
        String result = imagesourcetypeController.show(id, model);
        assertEquals("The show view was not returned", "crud/imagesourcetypes/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = imagesourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/imagesourcetypes/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = imagesourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/imagesourcetypes/list", result);

        page = 1;
        size = null;
        imagesourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/imagesourcetypes/list", result);

        page = null;
        size = 10;
        imagesourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/imagesourcetypes/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ImageSourceType imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = imagesourcetypeController.update(imagesourcetype, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/imagesourcetypes/"+imagesourcetype.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ImageSourceType imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = imagesourcetypeController.update(imagesourcetype, results, model, request);
        assertEquals("The update view was not returned", "crud/imagesourcetypes/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ImageSourceType imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();
        Long id = imagesourcetype.getId();
        Model model = getModel();
        String result = imagesourcetypeController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/imagesourcetypes/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ImageSourceType imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();
        Long id = imagesourcetype.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = imagesourcetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/imagesourcetypes", result);

        imagesourcetype = ImageSourceType.findImageSourceType(id);
        assertNull("ImageSourceType was not deleted", imagesourcetype);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ImageSourceType imagesourcetype = imagesourcetypeDataOnDemand.getRandomImageSourceType();
        Long id = imagesourcetype.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = imagesourcetypeController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/imagesourcetypes", result);

        imagesourcetype = ImageSourceType.findImageSourceType(id);
        assertNull("ImageSourceType was not deleted", imagesourcetype);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = imagesourcetypeController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/imagesourcetypes/list", result);
    }
}

