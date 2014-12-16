// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.BrandMotivator;
import com.lela.data.domain.entity.BrandMotivatorDataOnDemand;
import com.lela.data.domain.entity.BrandMotivatorIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect BrandMotivatorIntegrationTest_Roo_IntegrationTest {
    
    declare @type: BrandMotivatorIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: BrandMotivatorIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: BrandMotivatorIntegrationTest: @Transactional;
    
    @Autowired
    BrandMotivatorDataOnDemand BrandMotivatorIntegrationTest.dod;
    
    @Test
    public void BrandMotivatorIntegrationTest.testCountBrandMotivators() {
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to initialize correctly", dod.getRandomBrandMotivator());
        long count = BrandMotivator.countBrandMotivators();
        Assert.assertTrue("Counter for 'BrandMotivator' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void BrandMotivatorIntegrationTest.testFindBrandMotivator() {
        BrandMotivator obj = dod.getRandomBrandMotivator();
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to provide an identifier", id);
        obj = BrandMotivator.findBrandMotivator(id);
        Assert.assertNotNull("Find method for 'BrandMotivator' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'BrandMotivator' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void BrandMotivatorIntegrationTest.testFindAllBrandMotivators() {
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to initialize correctly", dod.getRandomBrandMotivator());
        long count = BrandMotivator.countBrandMotivators();
        Assert.assertTrue("Too expensive to perform a find all test for 'BrandMotivator', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<BrandMotivator> result = BrandMotivator.findAllBrandMotivators();
        Assert.assertNotNull("Find all method for 'BrandMotivator' illegally returned null", result);
        Assert.assertTrue("Find all method for 'BrandMotivator' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void BrandMotivatorIntegrationTest.testFindBrandMotivatorEntries() {
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to initialize correctly", dod.getRandomBrandMotivator());
        long count = BrandMotivator.countBrandMotivators();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<BrandMotivator> result = BrandMotivator.findBrandMotivatorEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'BrandMotivator' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'BrandMotivator' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void BrandMotivatorIntegrationTest.testFlush() {
        BrandMotivator obj = dod.getRandomBrandMotivator();
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to provide an identifier", id);
        obj = BrandMotivator.findBrandMotivator(id);
        Assert.assertNotNull("Find method for 'BrandMotivator' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyBrandMotivator(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'BrandMotivator' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void BrandMotivatorIntegrationTest.testMergeUpdate() {
        BrandMotivator obj = dod.getRandomBrandMotivator();
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to provide an identifier", id);
        obj = BrandMotivator.findBrandMotivator(id);
        boolean modified =  dod.modifyBrandMotivator(obj);
        Integer currentVersion = obj.getVersion();
        BrandMotivator merged = (BrandMotivator)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'BrandMotivator' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void BrandMotivatorIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to initialize correctly", dod.getRandomBrandMotivator());
        BrandMotivator obj = dod.getNewTransientBrandMotivator(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'BrandMotivator' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'BrandMotivator' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void BrandMotivatorIntegrationTest.testRemove() {
        BrandMotivator obj = dod.getRandomBrandMotivator();
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BrandMotivator' failed to provide an identifier", id);
        obj = BrandMotivator.findBrandMotivator(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'BrandMotivator' with identifier '" + id + "'", BrandMotivator.findBrandMotivator(id));
    }
    
}