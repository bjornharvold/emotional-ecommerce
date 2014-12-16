// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.MerchantOfferKeyword;
import com.lela.data.domain.entity.MerchantOfferKeywordDataOnDemand;
import com.lela.data.domain.entity.MerchantOfferKeywordIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MerchantOfferKeywordIntegrationTest_Roo_IntegrationTest {
    
    declare @type: MerchantOfferKeywordIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: MerchantOfferKeywordIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: MerchantOfferKeywordIntegrationTest: @Transactional;
    
    @Autowired
    MerchantOfferKeywordDataOnDemand MerchantOfferKeywordIntegrationTest.dod;
    
    @Test
    public void MerchantOfferKeywordIntegrationTest.testCountMerchantOfferKeywords() {
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to initialize correctly", dod.getRandomMerchantOfferKeyword());
        long count = MerchantOfferKeyword.countMerchantOfferKeywords();
        Assert.assertTrue("Counter for 'MerchantOfferKeyword' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void MerchantOfferKeywordIntegrationTest.testFindMerchantOfferKeyword() {
        MerchantOfferKeyword obj = dod.getRandomMerchantOfferKeyword();
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to provide an identifier", id);
        obj = MerchantOfferKeyword.findMerchantOfferKeyword(id);
        Assert.assertNotNull("Find method for 'MerchantOfferKeyword' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'MerchantOfferKeyword' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void MerchantOfferKeywordIntegrationTest.testFindAllMerchantOfferKeywords() {
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to initialize correctly", dod.getRandomMerchantOfferKeyword());
        long count = MerchantOfferKeyword.countMerchantOfferKeywords();
        Assert.assertTrue("Too expensive to perform a find all test for 'MerchantOfferKeyword', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<MerchantOfferKeyword> result = MerchantOfferKeyword.findAllMerchantOfferKeywords();
        Assert.assertNotNull("Find all method for 'MerchantOfferKeyword' illegally returned null", result);
        Assert.assertTrue("Find all method for 'MerchantOfferKeyword' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void MerchantOfferKeywordIntegrationTest.testFindMerchantOfferKeywordEntries() {
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to initialize correctly", dod.getRandomMerchantOfferKeyword());
        long count = MerchantOfferKeyword.countMerchantOfferKeywords();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<MerchantOfferKeyword> result = MerchantOfferKeyword.findMerchantOfferKeywordEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'MerchantOfferKeyword' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'MerchantOfferKeyword' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void MerchantOfferKeywordIntegrationTest.testFlush() {
        MerchantOfferKeyword obj = dod.getRandomMerchantOfferKeyword();
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to provide an identifier", id);
        obj = MerchantOfferKeyword.findMerchantOfferKeyword(id);
        Assert.assertNotNull("Find method for 'MerchantOfferKeyword' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMerchantOfferKeyword(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'MerchantOfferKeyword' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MerchantOfferKeywordIntegrationTest.testMergeUpdate() {
        MerchantOfferKeyword obj = dod.getRandomMerchantOfferKeyword();
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to provide an identifier", id);
        obj = MerchantOfferKeyword.findMerchantOfferKeyword(id);
        boolean modified =  dod.modifyMerchantOfferKeyword(obj);
        Integer currentVersion = obj.getVersion();
        MerchantOfferKeyword merged = (MerchantOfferKeyword)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'MerchantOfferKeyword' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MerchantOfferKeywordIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to initialize correctly", dod.getRandomMerchantOfferKeyword());
        MerchantOfferKeyword obj = dod.getNewTransientMerchantOfferKeyword(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'MerchantOfferKeyword' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'MerchantOfferKeyword' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void MerchantOfferKeywordIntegrationTest.testRemove() {
        MerchantOfferKeyword obj = dod.getRandomMerchantOfferKeyword();
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'MerchantOfferKeyword' failed to provide an identifier", id);
        obj = MerchantOfferKeyword.findMerchantOfferKeyword(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'MerchantOfferKeyword' with identifier '" + id + "'", MerchantOfferKeyword.findMerchantOfferKeyword(id));
    }
    
}