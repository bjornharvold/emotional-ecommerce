// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Affiliate;
import com.lela.data.domain.entity.AffiliateDataOnDemand;
import com.lela.data.domain.entity.AffiliateIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AffiliateIntegrationTest_Roo_IntegrationTest {
    
    declare @type: AffiliateIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: AffiliateIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: AffiliateIntegrationTest: @Transactional;
    
    @Autowired
    AffiliateDataOnDemand AffiliateIntegrationTest.dod;
    
    @Test
    public void AffiliateIntegrationTest.testCountAffiliates() {
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to initialize correctly", dod.getRandomAffiliate());
        long count = Affiliate.countAffiliates();
        Assert.assertTrue("Counter for 'Affiliate' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void AffiliateIntegrationTest.testFindAffiliate() {
        Affiliate obj = dod.getRandomAffiliate();
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to provide an identifier", id);
        obj = Affiliate.findAffiliate(id);
        Assert.assertNotNull("Find method for 'Affiliate' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Affiliate' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void AffiliateIntegrationTest.testFindAllAffiliates() {
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to initialize correctly", dod.getRandomAffiliate());
        long count = Affiliate.countAffiliates();
        Assert.assertTrue("Too expensive to perform a find all test for 'Affiliate', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Affiliate> result = Affiliate.findAllAffiliates();
        Assert.assertNotNull("Find all method for 'Affiliate' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Affiliate' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void AffiliateIntegrationTest.testFindAffiliateEntries() {
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to initialize correctly", dod.getRandomAffiliate());
        long count = Affiliate.countAffiliates();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Affiliate> result = Affiliate.findAffiliateEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Affiliate' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Affiliate' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void AffiliateIntegrationTest.testFlush() {
        Affiliate obj = dod.getRandomAffiliate();
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to provide an identifier", id);
        obj = Affiliate.findAffiliate(id);
        Assert.assertNotNull("Find method for 'Affiliate' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAffiliate(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Affiliate' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AffiliateIntegrationTest.testMergeUpdate() {
        Affiliate obj = dod.getRandomAffiliate();
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to provide an identifier", id);
        obj = Affiliate.findAffiliate(id);
        boolean modified =  dod.modifyAffiliate(obj);
        Integer currentVersion = obj.getVersion();
        Affiliate merged = (Affiliate)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Affiliate' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AffiliateIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to initialize correctly", dod.getRandomAffiliate());
        Affiliate obj = dod.getNewTransientAffiliate(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Affiliate' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Affiliate' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void AffiliateIntegrationTest.testRemove() {
        Affiliate obj = dod.getRandomAffiliate();
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Affiliate' failed to provide an identifier", id);
        obj = Affiliate.findAffiliate(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Affiliate' with identifier '" + id + "'", Affiliate.findAffiliate(id));
    }
    
}
