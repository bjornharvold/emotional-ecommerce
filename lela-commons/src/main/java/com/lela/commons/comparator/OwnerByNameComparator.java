/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Owner;

import java.util.Comparator;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:54 PM
 * Responsibility: Sorts stores by store name; case insensitive
 */
public class OwnerByNameComparator implements Comparator<Owner> {

    /**
     * Method description
     *
     * @param a1 a1
     * @param a2 a2
     * @return Return value
     */
    @Override
    public int compare(Owner a1, Owner a2) {
        int result = 0;

        if (a1 != null && a2 != null) {
            if (a1.getNm() == null && a2.getNm() == null) {
                result = 0;
            } else if (a1.getNm() == null && a2.getNm() != null) {
                result = -1;
            } else if (a1.getNm() != null && a2.getNm() == null) {
                result = 1;
            } else if (a1.getNm() != null && a2.getNm() != null) {
                result = a2.getNm().toLowerCase().compareTo(a1.getNm().toLowerCase());
            } 
        }

        return result;
    }
    
    public static void main(String[] args) {
        OwnerByNameComparator s = new OwnerByNameComparator();

        System.out.println(s.compare(null, null));

        Owner a1 = new Owner();
        Owner a2 = new Owner();

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
