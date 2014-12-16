/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */
package com.lela.domain.dto.facebook;

/**
 * User: Chris Tallent
 * Date: 3/23/12
 * Time: 1:28 PM
 */
public enum PoliticalAffiliation {
    DEMOCRAT, REPUBLICAN, INDEPENDENT, OTHER;
    
    public static PoliticalAffiliation getPoliticalAffiliation(String politics) {
        if (politics != null) {
            politics = politics.toUpperCase();

            if (politics.contains(DEMOCRAT.toString())) {
                return DEMOCRAT;
            } else if (politics.contains(REPUBLICAN.toString())) {
                return REPUBLICAN;
            } else if (politics.contains(INDEPENDENT.toString())) {
                return INDEPENDENT;
            }
        }

        return OTHER;
    }
}
