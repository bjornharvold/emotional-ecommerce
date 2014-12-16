/*
 * Copyright 2006 Polar Rose <http://www.polarrose.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.polarrose.amazon.sdb.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.polarrose.amazon.aws.AwsAccount;
import com.polarrose.amazon.sdb.SdbAttributes;
import com.polarrose.amazon.sdb.SdbDomainMetaData;
import com.polarrose.amazon.sdb.SdbItem;
import com.polarrose.amazon.sdb.SdbResultSet;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SdbServiceImplTest
{
    private static SdbServiceImpl simpleDatabaseService;

    private static final String TEST_DOMAIN = "SdbServiceImplTest" + System.currentTimeMillis();

    @BeforeClass
    public static void setUp() throws Exception
    {
        AwsAccount account = new AwsAccount(
            System.getProperty("com.polarrose.amazon.AccessKeyId"),
            System.getProperty("com.polarrose.amazon.SecretAccessKey")
        );

        simpleDatabaseService = new SdbServiceImpl();
        simpleDatabaseService.setAccount(account);
    }

    @AfterClass
    public static void tearDown() throws Exception
    {
        BigDecimal usage = simpleDatabaseService.getTotalBoxUsage();
        usage = usage.multiply(new BigDecimal("0.15"));
        System.out.println("Total cost of the test = " + usage + " USD");
    }

    @Test
    public void testDeleteDomain()
    {
        simpleDatabaseService.deleteDomain(TEST_DOMAIN);
    }

    @Test
    public void testCreateDomain()
    {
        simpleDatabaseService.createDomain(TEST_DOMAIN);
    }

    @Test
    public void testListDomains()
    {
        List<String> domains = simpleDatabaseService.listDomains();
        assertTrue(domains.contains(TEST_DOMAIN));
    }

    //

    @Test
    public void testEncodings()
    {
        SdbAttributes attributes;

        String[] values = new String[] {
            "http://developer.amazonwebservices.com/connect/thread.jspa?threadID=27353&tstart=0",
            "~`!@#$%^&*()-_=+]}[{\\|'\";:/?.>,<"
        };

        attributes = new SdbAttributes();
        for (int i = 0; i < values.length; i++) {
            attributes.setAttribute("a" + i, values[i]);
        }

        simpleDatabaseService.putAttributes(TEST_DOMAIN, "TestEncodingsItem", attributes);

        //

        attributes = simpleDatabaseService.getAttributes(TEST_DOMAIN, "TestEncodingsItem");
        for (int i = 0; i < values.length; i++) {
            String value = attributes.getAttribute("a" + i);
            assertNotNull(value);
            assertEquals(values[i], value);
        }
    }

    //

    @Test
    public void testPutAttributes()
    {
        NumberFormat formatter = new DecimalFormat("00000000");

        for (int i = 0; i < 10; i++)
        {
            SdbAttributes attributes = new SdbAttributes();
            attributes.setAttribute("foo", formatter.format(i));
            attributes.setAttribute("bar", Integer.toHexString(i));

            simpleDatabaseService.putAttributes(TEST_DOMAIN, "Item-" + i, attributes);
        }
    }

    @Test
    public void testPutAndGetAttributesWithMultipleValues()
    {
        SdbAttributes attributes;

        attributes = new SdbAttributes();
        attributes.addAttribute("TestPutAndGetAttributesWithMultipleValuesAttribute", "amsterdam");
        attributes.addAttribute("TestPutAndGetAttributesWithMultipleValuesAttribute", "toronto");

        simpleDatabaseService.putAttributes(TEST_DOMAIN, "TestPutWithMultipleValuesItem", attributes);

        //

        boolean ok = false;
        for (int i = 0; i < 5; i++)
        {
            attributes = simpleDatabaseService.getAttributes(TEST_DOMAIN, "TestPutWithMultipleValuesItem");
            assertNotNull(attributes);
            if (attributes.containsAttribute("TestPutAndGetAttributesWithMultipleValuesAttribute"))
            {
                Collection<String> values = attributes.getAttributes("TestPutAndGetAttributesWithMultipleValuesAttribute");
                assertNotNull(values);

                assertEquals(2, values.size());
                assertTrue(values.contains("amsterdam"));
                assertTrue(values.contains("toronto"));

                ok = true;
                break;
            }

            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                // Ignore
            }
        }

        assertTrue("Could not get result back within 25 seconds", ok);
    }

    //

    @Test
    public void testGetAttributes()
    {
        NumberFormat formatter = new DecimalFormat("00000000");

        for (int i = 0; i < 10; i++) {
            SdbAttributes attributes = simpleDatabaseService.getAttributes(TEST_DOMAIN, "Item-" + i);
            assertNotNull(attributes);
            assertEquals(2, attributes.getAttributeNames().size());
            assertTrue(attributes.containsAttribute("foo"));
            assertEquals(formatter.format(i), attributes.getAttribute("foo"));
        }
    }

    @Test
    public void testGetAttributesWithSelection()
    {
        {
            SdbAttributes attributes = new SdbAttributes();
            attributes.setAttribute("a", "a");
            attributes.setAttribute("b", "b");
            attributes.setAttribute("c", "c");
            attributes.setAttribute("d", "d");

            simpleDatabaseService.putAttributes(TEST_DOMAIN, "SomeItem", attributes);
        }

        {
            Set<String> attributeNames = new HashSet<String>();
            attributeNames.add("a");
            attributeNames.add("d");

            SdbAttributes attributes = simpleDatabaseService.getAttributes(TEST_DOMAIN, "SomeItem", attributeNames);

            assertNotNull(attributes);
            assertEquals(attributeNames.size(), attributes.getAttributeNames().size());

            for (String attributeName : attributeNames) {
                assertTrue(attributes.containsAttribute(attributeName));
                assertEquals(attributes.getAttribute(attributeName), attributeName);
            }
        }
    }

    //

    @Test
    public void testQuery()
    {
        SdbResultSet result = simpleDatabaseService.query(TEST_DOMAIN, 100, "['foo' > '00000004']");
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(5, result.getItems().size());
        assertNull(result.getNextToken());
    }

    @Test
    public void testQueryWithPaging()
    {
        Set<SdbItem> items = new HashSet<SdbItem>();

        SdbResultSet result = simpleDatabaseService.query(TEST_DOMAIN, 7, "['foo' >= '00000000']");
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertNotNull(result.getNextToken());

        items.addAll(result.getItems());

        while (result.getNextToken() != null) {
            result = simpleDatabaseService.query(TEST_DOMAIN, 7, "['foo' >= '00000000']", result.getNextToken());
            assertNotNull(result);
            assertNotNull(result.getItems());
            items.addAll(result.getItems());
        }

        assertEquals(10, items.size());
    }

    @Test
    public void testQueryWithAttributes()
    {
        Set<String> attributeNames = new HashSet<String>();
        attributeNames.add("bar");

        SdbResultSet result = simpleDatabaseService.queryWithAttributes(TEST_DOMAIN, 100, "['foo' > '00000004']", attributeNames);
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(5, result.getItems().size());
        assertNull(result.getNextToken());

        for (SdbItem item : result.getItems()) {
            assertNotNull(item.getAttributes());
            assertEquals(1, item.getAttributes().getAttributeNames().size());
            assertTrue(item.getAttributes().containsAttribute("bar"));
        }
    }

    @Test
    public void testQueryWithAttributesWithPaging()
    {
        Set<String> attributeNames = new HashSet<String>();
        attributeNames.add("bar");

        Set<SdbItem> items = new HashSet<SdbItem>();

        SdbResultSet result = simpleDatabaseService.queryWithAttributes(TEST_DOMAIN, 7, "['foo' >= '00000000']", attributeNames);
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertNotNull(result.getNextToken());

        items.addAll(result.getItems());

        while (result.getNextToken() != null) {
            result = simpleDatabaseService.queryWithAttributes(TEST_DOMAIN, 7, "['foo' >= '00000000']", attributeNames, result.getNextToken());
            assertNotNull(result);
            assertNotNull(result.getItems());
            items.addAll(result.getItems());
        }

        assertEquals(10, items.size());

        for (SdbItem item : items) {
            assertNotNull(item.getAttributes());
            assertEquals(1, item.getAttributes().getAttributeNames().size());
            assertTrue(item.getAttributes().containsAttribute("bar"));
        }
    }

    //

    @Test
    public void testSelect()
    {
        SdbResultSet result = simpleDatabaseService.select("select foo,bar from " + TEST_DOMAIN + " where foo > '00000004'");
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(5, result.getItems().size());

        for (SdbItem sdbItem : result.getItems()) {
            assertNotNull(sdbItem);
            assertNotNull(sdbItem.getName());
            assertNotNull(sdbItem.getAttributes());
            assertEquals(2, sdbItem.getAttributes().getAttributeNames().size());
        }

        assertNull(result.getNextToken());
    }

    @Test
    public void testSelectWithPaging()
    {
        final String query = "select foo,bar from " + TEST_DOMAIN +" where foo >= '00000000' limit 7";

        Set<SdbItem> items = new HashSet<SdbItem>();

        SdbResultSet result = simpleDatabaseService.select(query);
        assertNotNull(result);
        assertNotNull(result.getNextToken());
        assertNotNull(result.getItems());
        items.addAll(result.getItems());

        while (result.getNextToken() != null) {
            result = simpleDatabaseService.select(query, result.getNextToken());
            assertNotNull(result);
            assertNotNull(result.getItems());
            items.addAll(result.getItems());
        }

        assertEquals(10, items.size());
    }

    @Test
    public void testPutAndSelectAttributesWithMultipleValues()
    {
        SdbAttributes attributes;

        attributes = new SdbAttributes();
        attributes.addAttribute("PutAndSelectAttributesWithMultipleValuesAttribute", "amsterdam");
        attributes.addAttribute("PutAndSelectAttributesWithMultipleValuesAttribute", "toronto");

        simpleDatabaseService.putAttributes(TEST_DOMAIN, "TestPutAndSelectAttributesWithMultipleValuesItem", attributes);

        //

        SdbResultSet resultSet = simpleDatabaseService.select("select * from " + TEST_DOMAIN + " where PutAndSelectAttributesWithMultipleValuesAttribute is not null");
        assertNotNull(resultSet);
        assertNotNull(resultSet.getItems());
        assertEquals(1, resultSet.getItems().size());

        SdbItem item = resultSet.getItems().get(0);
        assertNotNull(item);

        attributes = item.getAttributes();
        assertNotNull(attributes);

        assertTrue(attributes.containsAttribute("PutAndSelectAttributesWithMultipleValuesAttribute"));
        Collection<String> values = attributes.getAttributes("PutAndSelectAttributesWithMultipleValuesAttribute");
        assertNotNull(values);

        assertEquals(2, values.size());
        assertTrue(values.contains("amsterdam"));
        assertTrue(values.contains("toronto"));
    }

    //

    @Test
    public void testGetDomainMetaData()
    {
        SdbDomainMetaData domainMetaData = simpleDatabaseService.getDomainMetaData(TEST_DOMAIN);
        assertNotNull(domainMetaData);

        assertNotSame(0, domainMetaData.getAttributeNameCount());
        assertNotSame(0, domainMetaData.getAttributeNamesSize());
        assertNotSame(0, domainMetaData.getAttributeValueCount());
        assertNotSame(0, domainMetaData.getAttributeValuesSize());
        assertNotSame(0, domainMetaData.getItemCount());
        assertNotSame(0, domainMetaData.getItemNamesSize());
    }

    @Test
    public void testCleanupTestDomains()
    {
        List<String> domains = simpleDatabaseService.listDomains();
        assertNotNull(domains);
        assertTrue(domains.size() > 0);

        for (String domain : domains) {
            if (domain.startsWith("SdbServiceImplTest")) {
                simpleDatabaseService.deleteDomain(domain);
            }
        }
    }
}
