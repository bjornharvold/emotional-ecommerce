// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilter;
import com.lela.data.domain.entity.FunctionalFilterDataOnDemand;
import com.lela.data.domain.entity.FunctionalFilterIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FunctionalFilterIntegrationTest_Roo_IntegrationTest {
    
    declare @type: FunctionalFilterIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: FunctionalFilterIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: FunctionalFilterIntegrationTest: @Transactional;
    
    @Autowired
    FunctionalFilterDataOnDemand FunctionalFilterIntegrationTest.dod;
    
    @Test
    public void FunctionalFilterIntegrationTest.testCountFunctionalFilters() {
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to initialize correctly", dod.getRandomFunctionalFilter());
        long count = FunctionalFilter.countFunctionalFilters();
        Assert.assertTrue("Counter for 'FunctionalFilter' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void FunctionalFilterIntegrationTest.testFindFunctionalFilter() {
        FunctionalFilter obj = dod.getRandomFunctionalFilter();
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to provide an identifier", id);
        obj = FunctionalFilter.findFunctionalFilter(id);
        Assert.assertNotNull("Find method for 'FunctionalFilter' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'FunctionalFilter' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void FunctionalFilterIntegrationTest.testFindAllFunctionalFilters() {
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to initialize correctly", dod.getRandomFunctionalFilter());
        long count = FunctionalFilter.countFunctionalFilters();
        Assert.assertTrue("Too expensive to perform a find all test for 'FunctionalFilter', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<FunctionalFilter> result = FunctionalFilter.findAllFunctionalFilters();
        Assert.assertNotNull("Find all method for 'FunctionalFilter' illegally returned null", result);
        Assert.assertTrue("Find all method for 'FunctionalFilter' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void FunctionalFilterIntegrationTest.testFindFunctionalFilterEntries() {
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to initialize correctly", dod.getRandomFunctionalFilter());
        long count = FunctionalFilter.countFunctionalFilters();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<FunctionalFilter> result = FunctionalFilter.findFunctionalFilterEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'FunctionalFilter' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'FunctionalFilter' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void FunctionalFilterIntegrationTest.testFlush() {
        FunctionalFilter obj = dod.getRandomFunctionalFilter();
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to provide an identifier", id);
        obj = FunctionalFilter.findFunctionalFilter(id);
        Assert.assertNotNull("Find method for 'FunctionalFilter' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFunctionalFilter(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'FunctionalFilter' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FunctionalFilterIntegrationTest.testMergeUpdate() {
        FunctionalFilter obj = dod.getRandomFunctionalFilter();
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to provide an identifier", id);
        obj = FunctionalFilter.findFunctionalFilter(id);
        boolean modified =  dod.modifyFunctionalFilter(obj);
        Integer currentVersion = obj.getVersion();
        FunctionalFilter merged = (FunctionalFilter)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'FunctionalFilter' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FunctionalFilterIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to initialize correctly", dod.getRandomFunctionalFilter());
        FunctionalFilter obj = dod.getNewTransientFunctionalFilter(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'FunctionalFilter' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'FunctionalFilter' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void FunctionalFilterIntegrationTest.testRemove() {
        FunctionalFilter obj = dod.getRandomFunctionalFilter();
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FunctionalFilter' failed to provide an identifier", id);
        obj = FunctionalFilter.findFunctionalFilter(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'FunctionalFilter' with identifier '" + id + "'", FunctionalFilter.findFunctionalFilter(id));
    }
    
}
