/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/24/11
 * Time: 8:16 PM
 * Responsibility:
 */
public class Bar {
    private String name;
    private List<FooBar> foobars = new ArrayList<FooBar>();

    public List<FooBar> getFoobars() {
        return foobars;
    }

    public void setFoobars(List<FooBar> foobars) {
        this.foobars = foobars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
