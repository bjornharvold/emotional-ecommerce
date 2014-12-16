// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemChanged;
import com.lela.data.domain.entity.ItemChangedDataOnDemand;
import com.lela.data.domain.entity.ItemChangedIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemChangedIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ItemChangedIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ItemChangedIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ItemChangedIntegrationTest: @Transactional;
    
    @Autowired
    ItemChangedDataOnDemand ItemChangedIntegrationTest.dod;
    
    @Test
    public void ItemChangedIntegrationTest.testCountItemChangeds() {
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to initialize correctly", dod.getRandomItemChanged());
        long count = ItemChanged.countItemChangeds();
        Assert.assertTrue("Counter for 'ItemChanged' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ItemChangedIntegrationTest.testFindItemChanged() {
        ItemChanged obj = dod.getRandomItemChanged();
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to provide an identifier", id);
        obj = ItemChanged.findItemChanged(id);
        Assert.assertNotNull("Find method for 'ItemChanged' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ItemChanged' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ItemChangedIntegrationTest.testFindAllItemChangeds() {
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to initialize correctly", dod.getRandomItemChanged());
        long count = ItemChanged.countItemChangeds();
        Assert.assertTrue("Too expensive to perform a find all test for 'ItemChanged', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ItemChanged> result = ItemChanged.findAllItemChangeds();
        Assert.assertNotNull("Find all method for 'ItemChanged' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ItemChanged' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ItemChangedIntegrationTest.testFindItemChangedEntries() {
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to initialize correctly", dod.getRandomItemChanged());
        long count = ItemChanged.countItemChangeds();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ItemChanged> result = ItemChanged.findItemChangedEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ItemChanged' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ItemChanged' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ItemChangedIntegrationTest.testFlush() {
        ItemChanged obj = dod.getRandomItemChanged();
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to provide an identifier", id);
        obj = ItemChanged.findItemChanged(id);
        Assert.assertNotNull("Find method for 'ItemChanged' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyItemChanged(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ItemChanged' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ItemChangedIntegrationTest.testMergeUpdate() {
        ItemChanged obj = dod.getRandomItemChanged();
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to provide an identifier", id);
        obj = ItemChanged.findItemChanged(id);
        boolean modified =  dod.modifyItemChanged(obj);
        Integer currentVersion = obj.getVersion();
        ItemChanged merged = (ItemChanged)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ItemChanged' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ItemChangedIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to initialize correctly", dod.getRandomItemChanged());
        ItemChanged obj = dod.getNewTransientItemChanged(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ItemChanged' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ItemChanged' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ItemChangedIntegrationTest.testRemove() {
        ItemChanged obj = dod.getRandomItemChanged();
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemChanged' failed to provide an identifier", id);
        obj = ItemChanged.findItemChanged(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ItemChanged' with identifier '" + id + "'", ItemChanged.findItemChanged(id));
    }
    
}