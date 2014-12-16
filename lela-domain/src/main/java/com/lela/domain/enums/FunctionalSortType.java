/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.enums;

/**
 * Created by Bjorn Harvold
 * Date: 10/18/11
 * Time: 3:52 PM
 * Responsibility:
 */
public enum FunctionalSortType {
    POPULARITY, RECOMMENDED, PRICE_LOW_HIGH, PRICE_HIGH_LOW;
    
	public static FunctionalSortType getEnumByValue(String value) {
		
		FunctionalSortType[] values = FunctionalSortType.values();
		FunctionalSortType theEnum = null;
		for (FunctionalSortType anEnum : values) {
			String enumValue = anEnum.toString();
			if (enumValue.equals(value)) {
				theEnum = anEnum;
				break;
			}
		}
		return theEnum;
	}
}
