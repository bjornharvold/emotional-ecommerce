// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemRecall;
import com.lela.data.domain.entity.ItemRecallDataOnDemand;
import com.lela.data.domain.entity.ItemRecallIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemRecallIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ItemRecallIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ItemRecallIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ItemRecallIntegrationTest: @Transactional;
    
    @Autowired
    ItemRecallDataOnDemand ItemRecallIntegrationTest.dod;
    
    @Test
    public void ItemRecallIntegrationTest.testCountItemRecalls() {
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to initialize correctly", dod.getRandomItemRecall());
        long count = ItemRecall.countItemRecalls();
        Assert.assertTrue("Counter for 'ItemRecall' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ItemRecallIntegrationTest.testFindItemRecall() {
        ItemRecall obj = dod.getRandomItemRecall();
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to provide an identifier", id);
        obj = ItemRecall.findItemRecall(id);
        Assert.assertNotNull("Find method for 'ItemRecall' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ItemRecall' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ItemRecallIntegrationTest.testFindAllItemRecalls() {
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to initialize correctly", dod.getRandomItemRecall());
        long count = ItemRecall.countItemRecalls();
        Assert.assertTrue("Too expensive to perform a find all test for 'ItemRecall', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ItemRecall> result = ItemRecall.findAllItemRecalls();
        Assert.assertNotNull("Find all method for 'ItemRecall' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ItemRecall' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ItemRecallIntegrationTest.testFindItemRecallEntries() {
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to initialize correctly", dod.getRandomItemRecall());
        long count = ItemRecall.countItemRecalls();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ItemRecall> result = ItemRecall.findItemRecallEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ItemRecall' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ItemRecall' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ItemRecallIntegrationTest.testFlush() {
        ItemRecall obj = dod.getRandomItemRecall();
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to provide an identifier", id);
        obj = ItemRecall.findItemRecall(id);
        Assert.assertNotNull("Find method for 'ItemRecall' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyItemRecall(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ItemRecall' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ItemRecallIntegrationTest.testMergeUpdate() {
        ItemRecall obj = dod.getRandomItemRecall();
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to provide an identifier", id);
        obj = ItemRecall.findItemRecall(id);
        boolean modified =  dod.modifyItemRecall(obj);
        Integer currentVersion = obj.getVersion();
        ItemRecall merged = (ItemRecall)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ItemRecall' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ItemRecallIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to initialize correctly", dod.getRandomItemRecall());
        ItemRecall obj = dod.getNewTransientItemRecall(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ItemRecall' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ItemRecall' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ItemRecallIntegrationTest.testRemove() {
        ItemRecall obj = dod.getRandomItemRecall();
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemRecall' failed to provide an identifier", id);
        obj = ItemRecall.findItemRecall(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ItemRecall' with identifier '" + id + "'", ItemRecall.findItemRecall(id));
    }
    
}
