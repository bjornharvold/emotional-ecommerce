package com.lela.data.web.management;

import com.lela.commons.repository.CategoryInitiatorQueryRepository;
import com.lela.commons.service.CategoryInitiatorService;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.CommonControllerTest;
import com.lela.domain.document.CategoryInitiatorQuery;
import com.lela.domain.document.CategoryInitiatorResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/2/12
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryInitiatorControllerTest extends CommonControllerTest {

    @Mock
    CategoryInitiatorService categoryInitiatorService;

    Map<String, String> categories = new HashMap<String, String>();

    @InjectMocks
    CategoryInitiatorController categoryInitiatorController;

    @Before
    public void before()
    {
        categoryInitiatorController = new CategoryInitiatorController(categoryInitiatorService);
    }

    @Test
    public void testIndex() throws Exception {
        Model model = this.getModel();
        String result = categoryInitiatorController.index(model);
        assertEquals("Incorrect view", "manage/categorys/initiator/index", result);
    }

    @Test
    public void testCreate() throws Exception {
        Model model = this.getModel();
        String result = categoryInitiatorController.create(model);
        assertEquals("Incorrect view", "manage/categorys/initiator/create", result);
        assertNotNull("Should have form backing object", model.asMap().get("categoryInitiatorQuery"));
    }

    @Test
    public void testSaveCreate() throws Exception {
        Model model = this.getModel();
        BindingResult bindingResult = this.getBindingResult();
        CategoryInitiatorQuery categoryInitiatorQuery = new CategoryInitiatorQuery();
        categoryInitiatorController.save(categoryInitiatorQuery, null, null, null, bindingResult, model);
    }

    @Test
    public void testSaveValidationErrors() throws Exception {
        Model model = this.getModel();
        BindingResult bindingResult = this.getBindingResult();
        CategoryInitiatorQuery categoryInitiatorQuery = new CategoryInitiatorQuery();
        categoryInitiatorController.save(categoryInitiatorQuery, null, null, null, bindingResult, model);

    }

    @Test
    public void testUpdate() throws Exception {
        String id = "";
        Model model = this.getModel();
        String result = categoryInitiatorController.update(id, model);
        assertEquals("Should have the view ", "manage/categorys/initiator/create", result);
    }

    @Test
    public void testSaveUpdate() throws Exception {
        Model model = this.getModel();
        BindingResult bindingResult = this.getBindingResult();
        String id = "1";
        CategoryInitiatorQuery categoryInitiatorQuery = new CategoryInitiatorQuery();
        when(categoryInitiatorService.getQuery(id)).thenReturn(categoryInitiatorQuery);
        categoryInitiatorController.save(categoryInitiatorQuery, null, null, null, bindingResult, model);
    }

    @Test
    public void testRun() throws Exception {
        String id = "";
        categoryInitiatorController.run(id, "");
    }

    @Test
    public void testResults()
    {
        String id = "";
        Model model = this.getModel();
        categoryInitiatorController.results(id, model);
    }

    @Test
    public void testDownload() throws Exception {
        String id = "";
        CategoryInitiatorResult result = new CategoryInitiatorResult();
        result.setRl("http://www.google.com");
        when(categoryInitiatorService.getResult(id)).thenReturn(result);
        HttpServletResponse response = this.getHttpServletResponse();
        categoryInitiatorController.download(id, response);
    }

    @Test
    public void testDelete() throws Exception{
        String id = "";
        categoryInitiatorController.delete(id);
    }
}
