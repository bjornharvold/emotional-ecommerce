// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AffiliateMerchant;
import com.lela.data.domain.entity.AffiliateMerchantDataOnDemand;
import com.lela.data.domain.entity.AffiliateMerchantIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AffiliateMerchantIntegrationTest_Roo_IntegrationTest {
    
    declare @type: AffiliateMerchantIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: AffiliateMerchantIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: AffiliateMerchantIntegrationTest: @Transactional;
    
    @Autowired
    AffiliateMerchantDataOnDemand AffiliateMerchantIntegrationTest.dod;
    
    @Test
    public void AffiliateMerchantIntegrationTest.testCountAffiliateMerchants() {
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to initialize correctly", dod.getRandomAffiliateMerchant());
        long count = AffiliateMerchant.countAffiliateMerchants();
        Assert.assertTrue("Counter for 'AffiliateMerchant' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void AffiliateMerchantIntegrationTest.testFindAffiliateMerchant() {
        AffiliateMerchant obj = dod.getRandomAffiliateMerchant();
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to provide an identifier", id);
        obj = AffiliateMerchant.findAffiliateMerchant(id);
        Assert.assertNotNull("Find method for 'AffiliateMerchant' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'AffiliateMerchant' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void AffiliateMerchantIntegrationTest.testFindAllAffiliateMerchants() {
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to initialize correctly", dod.getRandomAffiliateMerchant());
        long count = AffiliateMerchant.countAffiliateMerchants();
        Assert.assertTrue("Too expensive to perform a find all test for 'AffiliateMerchant', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<AffiliateMerchant> result = AffiliateMerchant.findAllAffiliateMerchants();
        Assert.assertNotNull("Find all method for 'AffiliateMerchant' illegally returned null", result);
        Assert.assertTrue("Find all method for 'AffiliateMerchant' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void AffiliateMerchantIntegrationTest.testFindAffiliateMerchantEntries() {
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to initialize correctly", dod.getRandomAffiliateMerchant());
        long count = AffiliateMerchant.countAffiliateMerchants();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<AffiliateMerchant> result = AffiliateMerchant.findAffiliateMerchantEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'AffiliateMerchant' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'AffiliateMerchant' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void AffiliateMerchantIntegrationTest.testFlush() {
        AffiliateMerchant obj = dod.getRandomAffiliateMerchant();
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to provide an identifier", id);
        obj = AffiliateMerchant.findAffiliateMerchant(id);
        Assert.assertNotNull("Find method for 'AffiliateMerchant' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAffiliateMerchant(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'AffiliateMerchant' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AffiliateMerchantIntegrationTest.testMergeUpdate() {
        AffiliateMerchant obj = dod.getRandomAffiliateMerchant();
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to provide an identifier", id);
        obj = AffiliateMerchant.findAffiliateMerchant(id);
        boolean modified =  dod.modifyAffiliateMerchant(obj);
        Integer currentVersion = obj.getVersion();
        AffiliateMerchant merged = (AffiliateMerchant)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'AffiliateMerchant' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AffiliateMerchantIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to initialize correctly", dod.getRandomAffiliateMerchant());
        AffiliateMerchant obj = dod.getNewTransientAffiliateMerchant(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'AffiliateMerchant' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'AffiliateMerchant' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void AffiliateMerchantIntegrationTest.testRemove() {
        AffiliateMerchant obj = dod.getRandomAffiliateMerchant();
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AffiliateMerchant' failed to provide an identifier", id);
        obj = AffiliateMerchant.findAffiliateMerchant(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'AffiliateMerchant' with identifier '" + id + "'", AffiliateMerchant.findAffiliateMerchant(id));
    }
    
}
