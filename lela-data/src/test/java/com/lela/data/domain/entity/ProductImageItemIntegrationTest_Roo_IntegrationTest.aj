// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ProductImageItem;
import com.lela.data.domain.entity.ProductImageItemDataOnDemand;
import com.lela.data.domain.entity.ProductImageItemIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ProductImageItemIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ProductImageItemIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ProductImageItemIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ProductImageItemIntegrationTest: @Transactional;
    
    @Autowired
    ProductImageItemDataOnDemand ProductImageItemIntegrationTest.dod;
    
    @Test
    public void ProductImageItemIntegrationTest.testCountProductImageItems() {
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to initialize correctly", dod.getRandomProductImageItem());
        long count = ProductImageItem.countProductImageItems();
        Assert.assertTrue("Counter for 'ProductImageItem' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ProductImageItemIntegrationTest.testFindProductImageItem() {
        ProductImageItem obj = dod.getRandomProductImageItem();
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to provide an identifier", id);
        obj = ProductImageItem.findProductImageItem(id);
        Assert.assertNotNull("Find method for 'ProductImageItem' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProductImageItem' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ProductImageItemIntegrationTest.testFindAllProductImageItems() {
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to initialize correctly", dod.getRandomProductImageItem());
        long count = ProductImageItem.countProductImageItems();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProductImageItem', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ProductImageItem> result = ProductImageItem.findAllProductImageItems();
        Assert.assertNotNull("Find all method for 'ProductImageItem' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProductImageItem' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ProductImageItemIntegrationTest.testFindProductImageItemEntries() {
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to initialize correctly", dod.getRandomProductImageItem());
        long count = ProductImageItem.countProductImageItems();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProductImageItem> result = ProductImageItem.findProductImageItemEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ProductImageItem' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProductImageItem' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ProductImageItemIntegrationTest.testFlush() {
        ProductImageItem obj = dod.getRandomProductImageItem();
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to provide an identifier", id);
        obj = ProductImageItem.findProductImageItem(id);
        Assert.assertNotNull("Find method for 'ProductImageItem' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyProductImageItem(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ProductImageItem' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ProductImageItemIntegrationTest.testMergeUpdate() {
        ProductImageItem obj = dod.getRandomProductImageItem();
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to provide an identifier", id);
        obj = ProductImageItem.findProductImageItem(id);
        boolean modified =  dod.modifyProductImageItem(obj);
        Integer currentVersion = obj.getVersion();
        ProductImageItem merged = (ProductImageItem)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ProductImageItem' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ProductImageItemIntegrationTest.testRemove() {
        ProductImageItem obj = dod.getRandomProductImageItem();
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProductImageItem' failed to provide an identifier", id);
        obj = ProductImageItem.findProductImageItem(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ProductImageItem' with identifier '" + id + "'", ProductImageItem.findProductImageItem(id));
    }
    
}
