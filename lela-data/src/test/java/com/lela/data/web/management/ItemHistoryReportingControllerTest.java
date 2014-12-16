package com.lela.data.web.management;

import com.google.common.collect.Lists;
import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.ItemHistory;
import com.lela.data.enums.ItemStatuses;
import com.lela.data.web.CommonControllerTest;
import com.lela.data.web.commands.ItemHistoryReportingCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/14/12
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ItemHistory.class, Category.class})
public class ItemHistoryReportingControllerTest extends CommonControllerTest {
    ItemHistoryReportingController itemHistoryReportingController = new ItemHistoryReportingController();

    @Test
    public void testIndex()
    {
        Category category = PowerMockito.mock(Category.class);
        PowerMockito.mockStatic(Category.class);
        List<Category> categories = Lists.newArrayList(category);
        Mockito.when(Category.findAllCategorys()).thenReturn(categories);

        Model uiModel = this.getModel();
        itemHistoryReportingController.reporting(uiModel);
        assertNotNull("Category list is not present", uiModel.asMap().get("categorys"));
        assertTrue("Category list is not empty", ((List) uiModel.asMap().get("categorys")).size() > 0);
        assertNotNull("ItemStatus list is not present", uiModel.asMap().get("itemStatuses"));
        assertTrue("ItemStatus list is not empty", ((ItemStatuses[]) uiModel.asMap().get("itemStatuses")).length > 0);
        assertNotNull("ItemHistoryReportingCommand is not present", uiModel.asMap().get("itemHistoryReportingCommand"));
    }

    @Test
    public void testGenerateReport() throws Exception
    {
        BindingResult bindingResult = this.getBindingResult();

        Model uiModel = this.getModel();
        HttpServletRequest request = this.getHttpServletRequest();
        Principal principal = mock(Principal.class);
        ItemHistoryReportingCommand itemHistoryReportingCommand = new ItemHistoryReportingCommand();

        ItemHistory itemHistory = PowerMockito.mock(ItemHistory.class);
        List<ItemHistory> itemHistories = Lists.newArrayList(itemHistory);
        PowerMockito.mockStatic(ItemHistory.class);
        Mockito.when(ItemHistory.findChangedItems(itemHistoryReportingCommand)).thenReturn(itemHistories);

        Category category = PowerMockito.mock(Category.class);
        PowerMockito.mockStatic(Category.class);
        List<Category> categories = Lists.newArrayList(category);
        Mockito.when(Category.findAllCategorys()).thenReturn(categories);

        itemHistoryReportingController.generateItemReport(itemHistoryReportingCommand, bindingResult, uiModel, request);

        assertNotNull("Category list is not present", uiModel.asMap().get("categorys"));
        assertTrue("Category list is not empty", ((List) uiModel.asMap().get("categorys")).size() > 0);

        assertNotNull("ItemHistory list is not present", uiModel.asMap().get("itemHistories"));
        assertTrue("ItemHistory list is empty", ((List)uiModel.asMap().get("itemHistories")).size() > 0);

        assertNotNull("ItemStatus list is not present", uiModel.asMap().get("itemStatuses"));
        assertTrue("ItemStatus list is not empty", ((ItemStatuses[]) uiModel.asMap().get("itemStatuses")).length > 0);

        assertNotNull("ItemHistoryReportingCommand list is not present", uiModel.asMap().get("itemHistoryReportingCommand"));

    }
}
