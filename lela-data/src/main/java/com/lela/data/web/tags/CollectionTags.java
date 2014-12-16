package com.lela.data.web.tags;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 7/31/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionTags {
    public static boolean collectionContains(Collection<?> coll, Object o) {
        return coll.contains(o);
    }

    public static boolean arrayContains(Object[] array, Object o)
    {
        boolean result = new HashSet(Arrays.asList(array)).contains(o);
        return result;
    }


}
