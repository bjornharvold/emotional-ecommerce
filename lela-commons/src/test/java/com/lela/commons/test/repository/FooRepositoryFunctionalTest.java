/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.test.repository;

import com.lela.domain.document.Bar;
import com.lela.domain.document.Foo;
import com.lela.domain.document.FooBar;
import com.lela.domain.document.FunctionalFilter;
import com.lela.domain.document.FunctionalFilterOption;
import com.lela.domain.document.QBar;
import com.lela.domain.document.QFoo;
import com.lela.domain.enums.FunctionalFilterType;
import com.lela.commons.repository.FooRepository;
import com.lela.commons.test.AbstractFunctionalTest;
import com.mysema.query.types.Expression;
import com.mysema.query.types.path.ListPath;
import com.mysema.query.types.query.StringSubQuery;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Bjorn Harvold
 * Date: 7/18/11
 * Time: 12:06 PM
 * Responsibility:
 */
public class FooRepositoryFunctionalTest extends AbstractFunctionalTest {
    private Foo foo = null;

    @Autowired
    private FooRepository fooRepository;

    @After
    public void tearDown() {
        fooRepository.deleteAll();
    }

    @Test
    public void testFooRepository() {

        foo = new Foo();
        fooRepository.save(foo);
        foo.setOne(foo.getId());
        foo.setTwo(foo.getId().toString());

        fooRepository.save(foo);

        Foo foo2 = fooRepository.findOne(foo.getId());
        assertNotNull("Foo2 is null", foo2);

        Page<Foo> l2 = fooRepository.findByTwo(foo.getTwo(), new PageRequest(0, 10));
        assertNotNull("l2 is null", l2);

        Page<Foo> l1 = fooRepository.findByOne(foo.getOne(), new PageRequest(0, 10));
        assertNotNull("l1 is null", l1);

        fooRepository.delete(foo.getId());
        foo2 = fooRepository.findOne(foo.getId());
        assertNull("Foo2 is not null", foo2);
    }

    @Test
    public void testFooPagination() {

        for (int i = 0; i < 50; i++) {
            Foo f = new Foo();
            f.setTwo(RandomStringUtils.randomAlphabetic(10));
            fooRepository.save(f);
        }

        for (int i = 0; i < 5; i++) {
            Page<Foo> page = fooRepository.findAll(new PageRequest(i, 10));
            assertNotNull("page is null", page);
            assertEquals("page number is incorrect", i, page.getNumber(), 0);
            assertEquals("page number of elements is incorrect", 10, page.getNumberOfElements(), 0);
            assertEquals("page total number of elements is incorrect", 50l, page.getTotalElements(), 0);
            assertEquals("page total pages is incorrect", 5, page.getTotalPages(), 0);
            assertEquals("page size is incorrect", 10, page.getSize(), 0);
            assertEquals("page list size is incorrect", 10, page.getContent().size(), 0);
            assertTrue("page has content is incorrect", page.hasContent());

            if (i == 0) {
                assertTrue("has next is incorrect", page.hasNextPage());
                assertFalse("has previous is incorrect", page.hasPreviousPage());
            } else if (i > 0 && i < 4) {
                assertTrue("has next is incorrect", page.hasNextPage());
                assertTrue("has previous is incorrect", page.hasPreviousPage());
            } else if (i == 4) {
                assertFalse("has next is incorrect", page.hasNextPage());
                assertTrue("has previous is incorrect", page.hasPreviousPage());
            }
        }

        for (int i = 0; i < 5; i++) {
            Page<Foo> page = fooRepository.findAll(QFoo.foo.two.ne("a"), new PageRequest(i, 10));
            assertNotNull("page is null", page);
            assertEquals("page number is incorrect", i, page.getNumber(), 0);
            assertEquals("page number of elements is incorrect", 10, page.getNumberOfElements(), 0);
            assertEquals("page total number of elements is incorrect", 50l, page.getTotalElements(), 0);
            assertEquals("page total pages is incorrect", 5, page.getTotalPages(), 0);
            assertEquals("page size is incorrect", 10, page.getSize(), 0);
            assertEquals("page list size is incorrect", 10, page.getContent().size(), 0);
            assertTrue("page has content is incorrect", page.hasContent());

            if (i == 0) {
                assertTrue("has next is incorrect", page.hasNextPage());
                assertFalse("has previous is incorrect", page.hasPreviousPage());
            } else if (i > 0 && i < 4) {
                assertTrue("has next is incorrect", page.hasNextPage());
                assertTrue("has previous is incorrect", page.hasPreviousPage());
            } else if (i == 4) {
                assertFalse("has next is incorrect", page.hasNextPage());
                assertTrue("has previous is incorrect", page.hasPreviousPage());
            }
        }
    }

    @Test
    public void testUpdate() {
        Foo one = new Foo();
        one.setTwo("two");
        one = fooRepository.save(one);

        Iterable<Foo> iter = fooRepository.findAll(QFoo.foo.two.eq("two"));

        assertTrue("Iterable is not of type List", iter instanceof ArrayList);
        List<Foo> list = (List<Foo>) iter;
        assertNotNull("List is null", list);
        assertEquals("List size is incorrect", 1, list.size(), 0);

        one = fooRepository.save(one);

        list = (List<Foo>) fooRepository.findAll(QFoo.foo.two.eq("two"));
        assertNotNull("List is null", list);
        assertEquals("List size is incorrect", 1, list.size(), 0);
    }

    @Test
    public void testNestedLists() {
        Foo f = new Foo();
        f.setTwo("abcdefg");

        List<Bar> bars = new ArrayList<Bar>();
        Bar bar = new Bar();

        List<FooBar> foobars = new ArrayList<FooBar>();
        FooBar fb = new FooBar();
        fb.setKy("HELLO WORLD");
        foobars.add(fb);
        bar.setFoobars(foobars);
        bars.add(bar);
        f.setBars(bars);

        fooRepository.save(f);

        f = fooRepository.findOne(QFoo.foo.two.eq("abcdefg"));
        assertNotNull("Foo is null");
        assertNotNull("Bars is null", f.getBars());
        assertNotNull("FooBars is null", f.getBars().get(0).getFoobars());

        List<FunctionalFilter> tuners = new ArrayList<FunctionalFilter>();
        FunctionalFilter tq = new FunctionalFilter();
        tq.setLcl("en_US");
        tq.setKy("price.range");
        tq.setTp(FunctionalFilterType.DYNAMIC_RANGE);
        tq.setDtky("ListPrice");

        List<FunctionalFilterOption> answers = new ArrayList<FunctionalFilterOption>();
        FunctionalFilterOption ta1 = new FunctionalFilterOption();
        ta1.setKy("value");
        ta1.setVl("1");
        ta1.setRdr(1);
        FunctionalFilterOption ta2 = new FunctionalFilterOption();
        ta2.setKy("value");
        ta2.setVl("2");
        ta2.setRdr(2);
        FunctionalFilterOption ta3 = new FunctionalFilterOption();
        ta3.setKy("value");
        ta3.setVl("2+");
        ta3.setRdr(3);
        answers.add(ta1);
        answers.add(ta2);
        answers.add(ta3);
        tq.setPtns(answers);
        tuners.add(tq);
        f.setTnrs(tuners);

        fooRepository.save(f);

        f = fooRepository.findOne(QFoo.foo.two.eq("abcdefg"));
        assertNotNull("Foo is null");
        assertNotNull("Bars is null", f.getBars());
        assertNotNull("FooBars is null", f.getBars().get(0).getFoobars());
        assertEquals("FooBars key is incorrect", "HELLO WORLD", f.getBars().get(0).getFoobars().get(0).getKy());
        assertNotNull("Tuners are null", f.getTnrs());
        assertNotNull("Tuners answers are null", f.getTnrs().get(0).getPtns());
        assertFalse("Tuners answers are null", f.getTnrs().get(0).getPtns().isEmpty());
    }
}
