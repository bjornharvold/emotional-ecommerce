package com.lela.data.web.browse;

import com.lela.commons.web.utils.WebConstants;
import com.lela.data.domain.entity.Category;
import com.lela.data.domain.entity.CategoryDataOnDemand;
import com.lela.data.domain.entity.DataSourceType;
import com.lela.data.web.DataBrowserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;
import org.springframework.validation.support.BindingAwareModelMap;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/applicationContext.xml"})
public class DataBrowserControllerTest {

    private DataBrowserController dataBrowserController = new DataBrowserController();

    @Autowired
    CategoryDataOnDemand categoryDataOnDemand;

    @Before
    public void before()
    {
        categoryDataOnDemand.getRandomCategory();
    }

    @Test
    public void index() {

        ModelMap modelMap = new BindingAwareModelMap();
        dataBrowserController.index(modelMap);

        assertNotNull("Model did not return Categories", modelMap.get(WebConstants.CATEGORIES));
        assertEquals("Model should contain 1 category", Category.countCategorys(), ((List)modelMap.get(WebConstants.CATEGORIES)).size());
    }
}
