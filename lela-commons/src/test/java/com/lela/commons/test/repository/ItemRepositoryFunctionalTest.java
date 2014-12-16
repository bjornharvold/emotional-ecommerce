/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.repository;

import com.lela.commons.repository.ItemRepository;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.Attribute;
import com.lela.domain.document.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bjorn Harvold
 * Date: 7/18/11
 * Time: 12:06 PM
 * Responsibility:
 */
public class ItemRepositoryFunctionalTest extends AbstractFunctionalTest {
    private Item item = null;

    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void setUp() {
        item = new Item();
        item.setNm(this.getClass().getSimpleName());
        item.setRlnm(this.getClass().getSimpleName());
        item.setSrlnm(this.getClass().getSimpleName());
        List<Attribute> attrs = new ArrayList<Attribute>();
        attrs.add(new Attribute("somekey", "someval"));
        attrs.add(new Attribute("somekey2", "someval2"));
        item.setAttrs(attrs);

        item = itemRepository.save(item);
    }

    @After
    public void tearDown() {
        itemRepository.delete(item);
    }

    @Test
    public void testFindCountByCategoriesAndFilters() {
        assertTrue("Not testable any longer", true);
    }

}
