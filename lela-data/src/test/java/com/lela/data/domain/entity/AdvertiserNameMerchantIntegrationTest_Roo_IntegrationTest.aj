// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AdvertiserNameMerchant;
import com.lela.data.domain.entity.AdvertiserNameMerchantDataOnDemand;
import com.lela.data.domain.entity.AdvertiserNameMerchantIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AdvertiserNameMerchantIntegrationTest_Roo_IntegrationTest {
    
    declare @type: AdvertiserNameMerchantIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: AdvertiserNameMerchantIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: AdvertiserNameMerchantIntegrationTest: @Transactional;
    
    @Autowired
    AdvertiserNameMerchantDataOnDemand AdvertiserNameMerchantIntegrationTest.dod;
    
    @Test
    public void AdvertiserNameMerchantIntegrationTest.testCountAdvertiserNameMerchants() {
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to initialize correctly", dod.getRandomAdvertiserNameMerchant());
        long count = AdvertiserNameMerchant.countAdvertiserNameMerchants();
        Assert.assertTrue("Counter for 'AdvertiserNameMerchant' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void AdvertiserNameMerchantIntegrationTest.testFindAdvertiserNameMerchant() {
        AdvertiserNameMerchant obj = dod.getRandomAdvertiserNameMerchant();
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to provide an identifier", id);
        obj = AdvertiserNameMerchant.findAdvertiserNameMerchant(id);
        Assert.assertNotNull("Find method for 'AdvertiserNameMerchant' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'AdvertiserNameMerchant' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void AdvertiserNameMerchantIntegrationTest.testFindAllAdvertiserNameMerchants() {
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to initialize correctly", dod.getRandomAdvertiserNameMerchant());
        long count = AdvertiserNameMerchant.countAdvertiserNameMerchants();
        Assert.assertTrue("Too expensive to perform a find all test for 'AdvertiserNameMerchant', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<AdvertiserNameMerchant> result = AdvertiserNameMerchant.findAllAdvertiserNameMerchants();
        Assert.assertNotNull("Find all method for 'AdvertiserNameMerchant' illegally returned null", result);
        Assert.assertTrue("Find all method for 'AdvertiserNameMerchant' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void AdvertiserNameMerchantIntegrationTest.testFindAdvertiserNameMerchantEntries() {
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to initialize correctly", dod.getRandomAdvertiserNameMerchant());
        long count = AdvertiserNameMerchant.countAdvertiserNameMerchants();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<AdvertiserNameMerchant> result = AdvertiserNameMerchant.findAdvertiserNameMerchantEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'AdvertiserNameMerchant' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'AdvertiserNameMerchant' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void AdvertiserNameMerchantIntegrationTest.testFlush() {
        AdvertiserNameMerchant obj = dod.getRandomAdvertiserNameMerchant();
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to provide an identifier", id);
        obj = AdvertiserNameMerchant.findAdvertiserNameMerchant(id);
        Assert.assertNotNull("Find method for 'AdvertiserNameMerchant' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAdvertiserNameMerchant(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'AdvertiserNameMerchant' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AdvertiserNameMerchantIntegrationTest.testMergeUpdate() {
        AdvertiserNameMerchant obj = dod.getRandomAdvertiserNameMerchant();
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to provide an identifier", id);
        obj = AdvertiserNameMerchant.findAdvertiserNameMerchant(id);
        boolean modified =  dod.modifyAdvertiserNameMerchant(obj);
        Integer currentVersion = obj.getVersion();
        AdvertiserNameMerchant merged = (AdvertiserNameMerchant)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'AdvertiserNameMerchant' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AdvertiserNameMerchantIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to initialize correctly", dod.getRandomAdvertiserNameMerchant());
        AdvertiserNameMerchant obj = dod.getNewTransientAdvertiserNameMerchant(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'AdvertiserNameMerchant' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'AdvertiserNameMerchant' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void AdvertiserNameMerchantIntegrationTest.testRemove() {
        AdvertiserNameMerchant obj = dod.getRandomAdvertiserNameMerchant();
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AdvertiserNameMerchant' failed to provide an identifier", id);
        obj = AdvertiserNameMerchant.findAdvertiserNameMerchant(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'AdvertiserNameMerchant' with identifier '" + id + "'", AdvertiserNameMerchant.findAdvertiserNameMerchant(id));
    }
    
}
