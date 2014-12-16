package com.lela.data.web.crud;
import com.lela.commons.spring.security.SpringSecurityHelper;

import com.lela.data.domain.entity.ItemVideo;
import com.lela.data.domain.entity.ItemVideoDataOnDemand;
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


public class ItemVideoControllerTest extends AbstractControllerTest {

    private ItemVideoController itemvideoController = new ItemVideoController();

    @Autowired
    private ItemVideoDataOnDemand itemvideoDataOnDemand;

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

        ItemVideo itemvideo = itemvideoDataOnDemand.getRandomItemVideo();

        String result = itemvideoController.create(itemvideo, results, model, request);
        assertEquals("The form was not returned", "redirect:/crud/itemvideos/"+itemvideo.getId(), result);

        try
        {
          request.setCharacterEncoding("UTF-8");
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemvideo = itemvideoDataOnDemand.getRandomItemVideo();

        result = itemvideoController.create(itemvideo, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemvideos/"+itemvideo.getId(), result);

        try
        {
          request.setCharacterEncoding("ABCDEFGHIJK");//bad encoding
        }
        catch (UnsupportedEncodingException ioe)
        {
            //won't happen
        }

        itemvideo = itemvideoDataOnDemand.getRandomItemVideo();

        result = itemvideoController.create(itemvideo, results, model, request);
        assertEquals("The form was not returned when encoding supplied", "redirect:/crud/itemvideos/"+itemvideo.getId(), result);

    }

    @Test
    @Transactional
    public void createWithBindingErrors() {
        HttpServletRequest request = getHttpServletRequest();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();

        ItemVideo itemvideo = itemvideoDataOnDemand.getRandomItemVideo();

        String result = itemvideoController.create(itemvideo, results, model, request);
        assertEquals("The form was not returned", "crud/itemvideos/create", result);
    }


    @Test
    @Transactional
    public void createForm() {
        Model model = getModel();
        String result = itemvideoController.createForm(model);
        assertEquals("The form was not returned", "crud/itemvideos/create", result);
    }

    @Test
    public void show() {
        ItemVideo itemvideo = itemvideoDataOnDemand.getRandomItemVideo();
        Long id = itemvideo.getId();
        Model model = getModel();
        String result = itemvideoController.show(id, model);
        assertEquals("The show view was not returned", "crud/itemvideos/show", result);
    }

    @Test
     public void list() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemvideoController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemvideos/list", result);
    }

    @Test
    public void listAllEntities() {
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemvideoController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemvideos/list", result);

        page = 1;
        size = null;
        itemvideoController.list(page, size, model);
        assertEquals("The list view was not returned when size not supplied", "crud/itemvideos/list", result);

        page = null;
        size = 10;
        itemvideoController.list(page, size, model);
        assertEquals("The list view was not returned when page not supplied", "crud/itemvideos/list", result);

    }

    @Test
    @Transactional
    public void update() {
        ItemVideo itemvideo = itemvideoDataOnDemand.getRandomItemVideo();
        BindingResult results = getBindingResult();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemvideoController.update(itemvideo, results, model, request);
        assertEquals("The update view was not returned", "redirect:/crud/itemvideos/"+itemvideo.getId(), result);
    }

    @Test
    @Transactional
    public void updateWithBindingErrors() {
        ItemVideo itemvideo = itemvideoDataOnDemand.getRandomItemVideo();
        BindingResult results = getBindingResultErrors();
        Model model = getModel();
        HttpServletRequest request = getHttpServletRequest();
        String result = itemvideoController.update(itemvideo, results, model, request);
        assertEquals("The update view was not returned", "crud/itemvideos/update", result);
    }

    @Test
    @Transactional
    public void updateForm() {
        ItemVideo itemvideo = itemvideoDataOnDemand.getRandomItemVideo();
        Long id = itemvideo.getId();
        Model model = getModel();
        String result = itemvideoController.updateForm(id, model);
        assertEquals("The update view was not returned", "crud/itemvideos/update", result);
    }

    @Test
    @Transactional
    public void delete() {
        ItemVideo itemvideo = itemvideoDataOnDemand.getRandomItemVideo();
        Long id = itemvideo.getId();
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        String result = itemvideoController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemvideos", result);

        itemvideo = ItemVideo.findItemVideo(id);
        assertNull("ItemVideo was not deleted", itemvideo);

    }

    @Test
    @Transactional
    public void deleteWithNullParameters() {
        ItemVideo itemvideo = itemvideoDataOnDemand.getRandomItemVideo();
        Long id = itemvideo.getId();
        Model model = getModel();
        Integer page = null;
        Integer size = null;
        String result = itemvideoController.delete(id, page, size, model);
        assertEquals("The delete view was not returned", "redirect:/crud/itemvideos", result);

        itemvideo = ItemVideo.findItemVideo(id);
        assertNull("ItemVideo was not deleted", itemvideo);
    }

    @Test
    public void listWithEvenPages() {
        Model model = getModel();
        Integer page = 1;
        Integer size = 3;
        String result = itemvideoController.list(page, size, model);
        assertEquals("The list view was not returned", "crud/itemvideos/list", result);
    }
}

