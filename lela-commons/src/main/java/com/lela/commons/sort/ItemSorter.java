package com.lela.commons.sort;

import com.google.common.collect.*;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 10/22/12
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemSorter implements ObjectSorter<Map, Integer> {
    @Override
    public Map<Integer, Collection<Map>> sort(List<Map> items) {
        ListMultimap<Integer, Map> multimap = ArrayListMultimap.create();
        for(Map item:items)
        {
            Integer key = (Integer)item.get("CategoryID");
            multimap.put(key, item);
        }
        return multimap.asMap();
    }
}
