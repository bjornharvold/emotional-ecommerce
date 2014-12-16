package com.lela.data.web.browse;

import com.lela.commons.spring.security.SpringSecurityHelper;
import com.lela.data.domain.entity.ItemAttributeDataOnDemand;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.ItemAttributeController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemAttributeControllerBrowseTest extends AbstractControllerTest {
    ItemAttributeController itemAttributeController = new ItemAttributeController();

    @Autowired
    ItemAttributeDataOnDemand itemAttributeDataOnDemand;

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
     public void testListByItem()
    {
        Model model = getModel();
        Integer page = 1;
        Integer size = 10;
        itemAttributeDataOnDemand.getRandomItemAttribute();
        String result = itemAttributeController.listByItem(itemAttributeDataOnDemand.getRandomItemAttribute().getItem().getCategory().getId(), page, size, model);
        assertEquals("Incorrect result page returned.", "crud/itemattributes/list", result);

        page = null;
        size = 10;
        result = itemAttributeController.listByItem(itemAttributeDataOnDemand.getRandomItemAttribute().getItem().getCategory().getId(), page, size, model);
        assertEquals("Incorrect result page returned.", "crud/itemattributes/list", result);

        page = 1;
        size = null;
        result = itemAttributeController.listByItem(itemAttributeDataOnDemand.getRandomItemAttribute().getItem().getCategory().getId(), page, size, model);
        assertEquals("Incorrect result page returned.", "crud/itemattributes/list", result);

        page = null;
        size = null;
        result = itemAttributeController.listByItem(itemAttributeDataOnDemand.getRandomItemAttribute().getItem().getCategory().getId(), page, size, model);
        assertEquals("Incorrect result page returned.", "crud/itemattributes/list", result);
    }

}
