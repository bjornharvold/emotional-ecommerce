package com.lela.data.web.management;

import com.lela.data.domain.entity.Category;
import com.lela.data.enums.ItemReportingAttribute;
import com.lela.data.excel.ItemWorkbook;
import com.lela.data.service.ItemWorkbookService;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.CommonControllerTest;
import com.lela.data.web.commands.ItemReportingCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/9/12
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Category.class)
public class ItemReportingControllerTest extends CommonControllerTest {

    ItemReportingController itemController = new ItemReportingController();


    Map<String, String> itemReportingStatusMap;

    @Before
    public void init()
    {
        itemReportingStatusMap = new HashMap<String, String>();
        itemReportingStatusMap.put("a", "b");
        itemController.itemReportingStatusMap = itemReportingStatusMap;
    }

    @Test
    @Transactional
    public void reportingTest() throws Exception
    {
        mockFindAllCategories();
        Model model = getModel();
        itemController.reporting(model);
        PowerMockito.verifyStatic(times(1));
    }


    @Test
    @Transactional
    public void testGenerateItemReport() throws Exception
    {
        ApplicationContext ctx = PowerMockito.mock(ApplicationContext.class);
        ItemWorkbook itemWorkbook = new ItemWorkbook("emailaddress");
        when(ctx.getBean("itemWorkbook")).thenReturn(itemWorkbook);
        itemController.setApplicationContext(ctx);

        ItemWorkbookService itemWorkbookService = PowerMockito.mock(ItemWorkbookService.class);

        mockFindAllCategories();

        itemController.itemWorkbookService = itemWorkbookService;
        Model model = getModel();
        MockHttpServletRequest request = getHttpServletRequest();
        Principal principal = mock(Principal.class);
        ItemReportingCommand command = new ItemReportingCommand();
        itemController.generateItemReport(command, model, request, principal);
        verify(itemWorkbookService).run(itemWorkbook);
    }

    @Test
    @Transactional
    public void testDownloadCompletedReport() throws Exception
    {

        mockFindAllCategories();
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();

        ItemWorkbook itemWorkbook = PowerMockito.mock(ItemWorkbook.class);
        itemWorkbook.setItemReportingCommand(itemReportingCommand);
        itemReportingCommand.getItemSearchCommand().setCategoryId(1l);
        itemReportingCommand.getItemWorkbookCommand().setShowAttribute(ItemReportingAttribute.NONE);
        itemWorkbook.getId();
        MockHttpServletRequest request = getHttpServletRequest();
        MockHttpServletResponse response = getHttpServletResponse();
        List<ItemWorkbook> itemWorkbooks = new ArrayList<ItemWorkbook>();
        itemWorkbooks.add(itemWorkbook);
        request.getSession().setAttribute("workbooks", itemWorkbooks);
        when(itemWorkbook.getId()).thenReturn("abc");
        itemController.downloadCompletedReport("abc", request, response);
        verify(itemWorkbook).writeCached(any(OutputStream.class));
    }

    @Test
    @Transactional
    public void testDownloadException() throws Exception
    {

        mockFindAllCategories();
        ItemReportingCommand itemReportingCommand = new ItemReportingCommand();

        ItemWorkbook itemWorkbook = PowerMockito.mock(ItemWorkbook.class);
        itemWorkbook.setItemReportingCommand(itemReportingCommand);
        itemReportingCommand.getItemSearchCommand().setCategoryId(1l);
        itemReportingCommand.getItemWorkbookCommand().setShowAttribute(ItemReportingAttribute.NONE);
        itemWorkbook.getId();
        MockHttpServletRequest request = getHttpServletRequest();
        MockHttpServletResponse response = getHttpServletResponse();
        List<ItemWorkbook> itemWorkbooks = new ArrayList<ItemWorkbook>();
        itemWorkbooks.add(itemWorkbook);
        request.getSession().setAttribute("workbooks", itemWorkbooks);
        when(itemWorkbook.getId()).thenReturn("abc");
        when(itemWorkbook.getException()).thenReturn(new Exception());
        itemController.downloadCompletedReport("abc", request, response);

    }

    private void mockFindAllCategories() {
        List<Category> categories = new ArrayList<Category>();
        Category category = PowerMockito.mock(Category.class);
        Mockito.when(category.getId()).thenReturn(1l);
        Mockito.when(category.getCategoryName()).thenReturn("Category Name");

        categories.add(category);
        PowerMockito.mockStatic(Category.class);

        Mockito.when(Category.findAllCategorys()).thenReturn(categories);
        Mockito.when(Category.findCategory(1l)).thenReturn(category);
    }


}
