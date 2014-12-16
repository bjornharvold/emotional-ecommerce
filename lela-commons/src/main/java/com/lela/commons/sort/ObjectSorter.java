package com.lela.commons.sort;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 10/22/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectSorter<T extends Object, C extends Object> {
    public Map<C, Collection<T>> sort(List<T> objects);
}
