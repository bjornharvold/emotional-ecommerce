/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.Item;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:54 PM
 * Responsibility: Sorts stores by store name; case insensitive
 */
public class ItemByNameComparator implements Comparator<Item> {

    /**
     * Method description
     *
     * @param a1 a1
     * @param a2 a2
     * @return Return value
     */
    @Override
    public int compare(Item a1, Item a2) {
        int result = 0;

        if (a1 != null && a2 != null) {
            if (a1.getNm() == null && a2.getNm() == null) {
                result = 0;
            } else if (a1.getNm() == null && a2.getNm() != null) {
                result = 1;
            } else if (a1.getNm() != null && a2.getNm() == null) {
                result = -1;
            } else if (a1.getNm() != null && a2.getNm() != null) {
                result = a1.getNm().toLowerCase().compareTo(a2.getNm().toLowerCase());
            } 
        }

        return result;
    }
    
    public static void main(String[] args) {
        ItemByNameComparator s = new ItemByNameComparator();

        System.out.println(s.compare(null, null));
        
        Item a1 = new Item();
        Item a2 = new Item();

        System.out.println(s.compare(a1, a2));

        a1.setNm("A");

        System.out.println(s.compare(a1, a2));

        a1.setNm(null);
        a2.setNm("B");

        System.out.println(s.compare(a1, a2));

        a1.setNm("A");

        System.out.println(s.compare(a1, a2));
    }
}
