// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AccessoryValueGroup;
import com.lela.data.domain.entity.AccessoryValueGroupDataOnDemand;
import com.lela.data.domain.entity.AccessoryValueGroupIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AccessoryValueGroupIntegrationTest_Roo_IntegrationTest {
    
    declare @type: AccessoryValueGroupIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: AccessoryValueGroupIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: AccessoryValueGroupIntegrationTest: @Transactional;
    
    @Autowired
    AccessoryValueGroupDataOnDemand AccessoryValueGroupIntegrationTest.dod;
    
    @Test
    public void AccessoryValueGroupIntegrationTest.testCountAccessoryValueGroups() {
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to initialize correctly", dod.getRandomAccessoryValueGroup());
        long count = AccessoryValueGroup.countAccessoryValueGroups();
        Assert.assertTrue("Counter for 'AccessoryValueGroup' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void AccessoryValueGroupIntegrationTest.testFindAccessoryValueGroup() {
        AccessoryValueGroup obj = dod.getRandomAccessoryValueGroup();
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to provide an identifier", id);
        obj = AccessoryValueGroup.findAccessoryValueGroup(id);
        Assert.assertNotNull("Find method for 'AccessoryValueGroup' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'AccessoryValueGroup' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void AccessoryValueGroupIntegrationTest.testFindAllAccessoryValueGroups() {
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to initialize correctly", dod.getRandomAccessoryValueGroup());
        long count = AccessoryValueGroup.countAccessoryValueGroups();
        Assert.assertTrue("Too expensive to perform a find all test for 'AccessoryValueGroup', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<AccessoryValueGroup> result = AccessoryValueGroup.findAllAccessoryValueGroups();
        Assert.assertNotNull("Find all method for 'AccessoryValueGroup' illegally returned null", result);
        Assert.assertTrue("Find all method for 'AccessoryValueGroup' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void AccessoryValueGroupIntegrationTest.testFindAccessoryValueGroupEntries() {
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to initialize correctly", dod.getRandomAccessoryValueGroup());
        long count = AccessoryValueGroup.countAccessoryValueGroups();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<AccessoryValueGroup> result = AccessoryValueGroup.findAccessoryValueGroupEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'AccessoryValueGroup' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'AccessoryValueGroup' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void AccessoryValueGroupIntegrationTest.testFlush() {
        AccessoryValueGroup obj = dod.getRandomAccessoryValueGroup();
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to provide an identifier", id);
        obj = AccessoryValueGroup.findAccessoryValueGroup(id);
        Assert.assertNotNull("Find method for 'AccessoryValueGroup' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAccessoryValueGroup(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'AccessoryValueGroup' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AccessoryValueGroupIntegrationTest.testMergeUpdate() {
        AccessoryValueGroup obj = dod.getRandomAccessoryValueGroup();
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to provide an identifier", id);
        obj = AccessoryValueGroup.findAccessoryValueGroup(id);
        boolean modified =  dod.modifyAccessoryValueGroup(obj);
        Integer currentVersion = obj.getVersion();
        AccessoryValueGroup merged = (AccessoryValueGroup)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'AccessoryValueGroup' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AccessoryValueGroupIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to initialize correctly", dod.getRandomAccessoryValueGroup());
        AccessoryValueGroup obj = dod.getNewTransientAccessoryValueGroup(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'AccessoryValueGroup' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'AccessoryValueGroup' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void AccessoryValueGroupIntegrationTest.testRemove() {
        AccessoryValueGroup obj = dod.getRandomAccessoryValueGroup();
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AccessoryValueGroup' failed to provide an identifier", id);
        obj = AccessoryValueGroup.findAccessoryValueGroup(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'AccessoryValueGroup' with identifier '" + id + "'", AccessoryValueGroup.findAccessoryValueGroup(id));
    }
    
}
