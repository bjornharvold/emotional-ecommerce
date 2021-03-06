// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ConditionType;
import com.lela.data.domain.entity.ConditionTypeDataOnDemand;
import com.lela.data.domain.entity.ConditionTypeIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ConditionTypeIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ConditionTypeIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ConditionTypeIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ConditionTypeIntegrationTest: @Transactional;
    
    @Autowired
    ConditionTypeDataOnDemand ConditionTypeIntegrationTest.dod;
    
    @Test
    public void ConditionTypeIntegrationTest.testCountConditionTypes() {
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to initialize correctly", dod.getRandomConditionType());
        long count = ConditionType.countConditionTypes();
        Assert.assertTrue("Counter for 'ConditionType' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ConditionTypeIntegrationTest.testFindConditionType() {
        ConditionType obj = dod.getRandomConditionType();
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to provide an identifier", id);
        obj = ConditionType.findConditionType(id);
        Assert.assertNotNull("Find method for 'ConditionType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ConditionType' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ConditionTypeIntegrationTest.testFindAllConditionTypes() {
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to initialize correctly", dod.getRandomConditionType());
        long count = ConditionType.countConditionTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'ConditionType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ConditionType> result = ConditionType.findAllConditionTypes();
        Assert.assertNotNull("Find all method for 'ConditionType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ConditionType' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ConditionTypeIntegrationTest.testFindConditionTypeEntries() {
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to initialize correctly", dod.getRandomConditionType());
        long count = ConditionType.countConditionTypes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ConditionType> result = ConditionType.findConditionTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ConditionType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ConditionType' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ConditionTypeIntegrationTest.testFlush() {
        ConditionType obj = dod.getRandomConditionType();
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to provide an identifier", id);
        obj = ConditionType.findConditionType(id);
        Assert.assertNotNull("Find method for 'ConditionType' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyConditionType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ConditionType' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ConditionTypeIntegrationTest.testMergeUpdate() {
        ConditionType obj = dod.getRandomConditionType();
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to provide an identifier", id);
        obj = ConditionType.findConditionType(id);
        boolean modified =  dod.modifyConditionType(obj);
        Integer currentVersion = obj.getVersion();
        ConditionType merged = (ConditionType)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ConditionType' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ConditionTypeIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to initialize correctly", dod.getRandomConditionType());
        ConditionType obj = dod.getNewTransientConditionType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ConditionType' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ConditionType' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ConditionTypeIntegrationTest.testRemove() {
        ConditionType obj = dod.getRandomConditionType();
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ConditionType' failed to provide an identifier", id);
        obj = ConditionType.findConditionType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ConditionType' with identifier '" + id + "'", ConditionType.findConditionType(id));
    }
    
}
