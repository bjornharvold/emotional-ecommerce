// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Series;
import com.lela.data.domain.entity.SeriesDataOnDemand;
import com.lela.data.domain.entity.SeriesIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SeriesIntegrationTest_Roo_IntegrationTest {
    
    declare @type: SeriesIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: SeriesIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: SeriesIntegrationTest: @Transactional;
    
    @Autowired
    SeriesDataOnDemand SeriesIntegrationTest.dod;
    
    @Test
    public void SeriesIntegrationTest.testCountSeries() {
        Assert.assertNotNull("Data on demand for 'Series' failed to initialize correctly", dod.getRandomSeries());
        long count = Series.countSeries();
        Assert.assertTrue("Counter for 'Series' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void SeriesIntegrationTest.testFindSeries() {
        Series obj = dod.getRandomSeries();
        Assert.assertNotNull("Data on demand for 'Series' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Series' failed to provide an identifier", id);
        obj = Series.findSeries(id);
        Assert.assertNotNull("Find method for 'Series' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Series' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void SeriesIntegrationTest.testFindAllSeries() {
        Assert.assertNotNull("Data on demand for 'Series' failed to initialize correctly", dod.getRandomSeries());
        long count = Series.countSeries();
        Assert.assertTrue("Too expensive to perform a find all test for 'Series', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Series> result = Series.findAllSeries();
        Assert.assertNotNull("Find all method for 'Series' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Series' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void SeriesIntegrationTest.testFindSeriesEntries() {
        Assert.assertNotNull("Data on demand for 'Series' failed to initialize correctly", dod.getRandomSeries());
        long count = Series.countSeries();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Series> result = Series.findSeriesEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Series' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Series' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void SeriesIntegrationTest.testFlush() {
        Series obj = dod.getRandomSeries();
        Assert.assertNotNull("Data on demand for 'Series' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Series' failed to provide an identifier", id);
        obj = Series.findSeries(id);
        Assert.assertNotNull("Find method for 'Series' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySeries(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Series' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SeriesIntegrationTest.testMergeUpdate() {
        Series obj = dod.getRandomSeries();
        Assert.assertNotNull("Data on demand for 'Series' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Series' failed to provide an identifier", id);
        obj = Series.findSeries(id);
        boolean modified =  dod.modifySeries(obj);
        Integer currentVersion = obj.getVersion();
        Series merged = (Series)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Series' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SeriesIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Series' failed to initialize correctly", dod.getRandomSeries());
        Series obj = dod.getNewTransientSeries(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Series' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Series' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Series' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void SeriesIntegrationTest.testRemove() {
        Series obj = dod.getRandomSeries();
        Assert.assertNotNull("Data on demand for 'Series' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Series' failed to provide an identifier", id);
        obj = Series.findSeries(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Series' with identifier '" + id + "'", Series.findSeries(id));
    }
    
}
