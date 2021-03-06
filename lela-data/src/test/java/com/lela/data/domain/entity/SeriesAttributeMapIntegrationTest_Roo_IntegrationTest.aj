// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.SeriesAttributeMap;
import com.lela.data.domain.entity.SeriesAttributeMapDataOnDemand;
import com.lela.data.domain.entity.SeriesAttributeMapIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SeriesAttributeMapIntegrationTest_Roo_IntegrationTest {
    
    declare @type: SeriesAttributeMapIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: SeriesAttributeMapIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: SeriesAttributeMapIntegrationTest: @Transactional;
    
    @Autowired
    SeriesAttributeMapDataOnDemand SeriesAttributeMapIntegrationTest.dod;
    
    @Test
    public void SeriesAttributeMapIntegrationTest.testCountSeriesAttributeMaps() {
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to initialize correctly", dod.getRandomSeriesAttributeMap());
        long count = SeriesAttributeMap.countSeriesAttributeMaps();
        Assert.assertTrue("Counter for 'SeriesAttributeMap' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void SeriesAttributeMapIntegrationTest.testFindSeriesAttributeMap() {
        SeriesAttributeMap obj = dod.getRandomSeriesAttributeMap();
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to provide an identifier", id);
        obj = SeriesAttributeMap.findSeriesAttributeMap(id);
        Assert.assertNotNull("Find method for 'SeriesAttributeMap' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'SeriesAttributeMap' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void SeriesAttributeMapIntegrationTest.testFindAllSeriesAttributeMaps() {
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to initialize correctly", dod.getRandomSeriesAttributeMap());
        long count = SeriesAttributeMap.countSeriesAttributeMaps();
        Assert.assertTrue("Too expensive to perform a find all test for 'SeriesAttributeMap', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<SeriesAttributeMap> result = SeriesAttributeMap.findAllSeriesAttributeMaps();
        Assert.assertNotNull("Find all method for 'SeriesAttributeMap' illegally returned null", result);
        Assert.assertTrue("Find all method for 'SeriesAttributeMap' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void SeriesAttributeMapIntegrationTest.testFindSeriesAttributeMapEntries() {
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to initialize correctly", dod.getRandomSeriesAttributeMap());
        long count = SeriesAttributeMap.countSeriesAttributeMaps();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<SeriesAttributeMap> result = SeriesAttributeMap.findSeriesAttributeMapEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'SeriesAttributeMap' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'SeriesAttributeMap' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void SeriesAttributeMapIntegrationTest.testFlush() {
        SeriesAttributeMap obj = dod.getRandomSeriesAttributeMap();
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to provide an identifier", id);
        obj = SeriesAttributeMap.findSeriesAttributeMap(id);
        Assert.assertNotNull("Find method for 'SeriesAttributeMap' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySeriesAttributeMap(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'SeriesAttributeMap' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SeriesAttributeMapIntegrationTest.testMergeUpdate() {
        SeriesAttributeMap obj = dod.getRandomSeriesAttributeMap();
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to provide an identifier", id);
        obj = SeriesAttributeMap.findSeriesAttributeMap(id);
        boolean modified =  dod.modifySeriesAttributeMap(obj);
        Integer currentVersion = obj.getVersion();
        SeriesAttributeMap merged = (SeriesAttributeMap)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'SeriesAttributeMap' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SeriesAttributeMapIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to initialize correctly", dod.getRandomSeriesAttributeMap());
        SeriesAttributeMap obj = dod.getNewTransientSeriesAttributeMap(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'SeriesAttributeMap' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'SeriesAttributeMap' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void SeriesAttributeMapIntegrationTest.testRemove() {
        SeriesAttributeMap obj = dod.getRandomSeriesAttributeMap();
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SeriesAttributeMap' failed to provide an identifier", id);
        obj = SeriesAttributeMap.findSeriesAttributeMap(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'SeriesAttributeMap' with identifier '" + id + "'", SeriesAttributeMap.findSeriesAttributeMap(id));
    }
    
}
