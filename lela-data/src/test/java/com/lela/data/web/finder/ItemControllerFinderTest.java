package com.lela.data.web.finder;

import com.lela.data.domain.entity.ItemDataOnDemand;
import com.lela.data.web.AbstractControllerTest;
import com.lela.data.web.ItemController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemControllerFinderTest extends AbstractControllerTest {
    ItemController itemController = new ItemController();
    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Test
    public void testFindItemsByLelaUrlForm()
    {
        String result = itemController.findItemsByLelaUrlForm(getModel());
        assertEquals("Incorrect item finder view returned.", "crud/items/findItemsByLelaUrl", result);
    }

    @Test
    public void testFindItemsByLelaUrl()
    {
        String lelaUrl = itemDataOnDemand.getRandomItem().getLelaUrl();

        Model model = getModel();
        String result = itemController.findItemsByLelaUrl(lelaUrl, model);
        assertEquals("Incorrect item finder result view returned.", "crud/items/list", result);
        assertTrue("Items not returned from finder.", model.containsAttribute("items"));
    }
}
