// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AccessoryGroup;
import com.lela.data.domain.entity.AccessoryGroupDataOnDemand;
import com.lela.data.domain.entity.AccessoryGroupIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AccessoryGroupIntegrationTest_Roo_IntegrationTest {
    
    declare @type: AccessoryGroupIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: AccessoryGroupIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: AccessoryGroupIntegrationTest: @Transactional;
    
    @Autowired
    AccessoryGroupDataOnDemand AccessoryGroupIntegrationTest.dod;
    
    @Test
    public void AccessoryGroupIntegrationTest.testCountAccessoryGroups() {
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to initialize correctly", dod.getRandomAccessoryGroup());
        long count = AccessoryGroup.countAccessoryGroups();
        Assert.assertTrue("Counter for 'AccessoryGroup' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void AccessoryGroupIntegrationTest.testFindAccessoryGroup() {
        AccessoryGroup obj = dod.getRandomAccessoryGroup();
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to provide an identifier", id);
        obj = AccessoryGroup.findAccessoryGroup(id);
        Assert.assertNotNull("Find method for 'AccessoryGroup' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'AccessoryGroup' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void AccessoryGroupIntegrationTest.testFindAllAccessoryGroups() {
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to initialize correctly", dod.getRandomAccessoryGroup());
        long count = AccessoryGroup.countAccessoryGroups();
        Assert.assertTrue("Too expensive to perform a find all test for 'AccessoryGroup', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<AccessoryGroup> result = AccessoryGroup.findAllAccessoryGroups();
        Assert.assertNotNull("Find all method for 'AccessoryGroup' illegally returned null", result);
        Assert.assertTrue("Find all method for 'AccessoryGroup' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void AccessoryGroupIntegrationTest.testFindAccessoryGroupEntries() {
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to initialize correctly", dod.getRandomAccessoryGroup());
        long count = AccessoryGroup.countAccessoryGroups();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<AccessoryGroup> result = AccessoryGroup.findAccessoryGroupEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'AccessoryGroup' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'AccessoryGroup' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void AccessoryGroupIntegrationTest.testFlush() {
        AccessoryGroup obj = dod.getRandomAccessoryGroup();
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to provide an identifier", id);
        obj = AccessoryGroup.findAccessoryGroup(id);
        Assert.assertNotNull("Find method for 'AccessoryGroup' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAccessoryGroup(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'AccessoryGroup' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AccessoryGroupIntegrationTest.testMergeUpdate() {
        AccessoryGroup obj = dod.getRandomAccessoryGroup();
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to provide an identifier", id);
        obj = AccessoryGroup.findAccessoryGroup(id);
        boolean modified =  dod.modifyAccessoryGroup(obj);
        Integer currentVersion = obj.getVersion();
        AccessoryGroup merged = (AccessoryGroup)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'AccessoryGroup' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AccessoryGroupIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to initialize correctly", dod.getRandomAccessoryGroup());
        AccessoryGroup obj = dod.getNewTransientAccessoryGroup(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'AccessoryGroup' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'AccessoryGroup' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void AccessoryGroupIntegrationTest.testRemove() {
        AccessoryGroup obj = dod.getRandomAccessoryGroup();
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AccessoryGroup' failed to provide an identifier", id);
        obj = AccessoryGroup.findAccessoryGroup(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'AccessoryGroup' with identifier '" + id + "'", AccessoryGroup.findAccessoryGroup(id));
    }
    
}
