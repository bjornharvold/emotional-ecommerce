package com.lela.data.domain.finder;

import com.lela.data.domain.entity.Item;
import com.lela.data.domain.entity.ItemDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/30/12
 * Time: 4:47 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/applicationContext.xml"})
public class ItemFinderTest {
    @Autowired
    ItemDataOnDemand itemDataOnDemand;

    @Test
    public void testFindItemsByLelaUrl()
    {
        Item item = itemDataOnDemand.getRandomItem();
        List<Item> items = Item.findItemsByLelaUrl(item.getLelaUrl());
        assertEquals("Did not find correct amount of items.", 1, items.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindItemsByLelaUrlWithNull()
    {
        Item.findItemsByLelaUrl(null);
    }
}
