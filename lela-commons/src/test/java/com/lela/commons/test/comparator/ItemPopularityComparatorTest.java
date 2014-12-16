/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.comparator;

import com.lela.commons.comparator.ItemPopularityLowestPriceComparator;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.Item;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 10/18/11
 * Time: 4:19 PM
 * Responsibility:
 */
public class ItemPopularityComparatorTest extends TestCase {
    @Test
    public void testItemPopularity() {
        Item item1 = new Item();
        item1.setNm("1");
        Map<String, Integer> map1 = new HashMap<String, Integer>(1);
        map1.put("B", 10);
        item1.setMtvtrs(map1);

        Item item2 = new Item();
        item2.setNm("2");
        List<Attribute> list = new ArrayList<Attribute>();
        list.add(new Attribute("LowestPrice", 10d));
        item2.setSbttrs(list);
        Map<String, Integer> map2 = new HashMap<String, Integer>(1);
        map2.put("B", 20);
        item2.setMtvtrs(map2);
        
        Item item3 = new Item();
        item3.setNm("3");
        list = new ArrayList<Attribute>();
        list.add(new Attribute("LowestPrice", 5d));
        item3.setSbttrs(list);
        Map<String, Integer> map3 = new HashMap<String, Integer>(1);
        map3.put("B", 20);
        item3.setMtvtrs(map3);

        List<Item> items = new ArrayList<Item>(2);
        items.add(item1);
        items.add(item2);
        items.add(item3);

        Collections.sort(items, new ItemPopularityLowestPriceComparator());

        assertEquals("Incorrect sorting", "3", items.get(0).getNm());
        assertEquals("Incorrect sorting", "2", items.get(1).getNm());
        assertEquals("Incorrect sorting", "1", items.get(2).getNm());
    }
}
