// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ReviewStatus;
import com.lela.data.domain.entity.ReviewStatusDataOnDemand;
import com.lela.data.domain.entity.ReviewStatusIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ReviewStatusIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ReviewStatusIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ReviewStatusIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ReviewStatusIntegrationTest: @Transactional;
    
    @Autowired
    ReviewStatusDataOnDemand ReviewStatusIntegrationTest.dod;
    
    @Test
    public void ReviewStatusIntegrationTest.testCountReviewStatuses() {
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to initialize correctly", dod.getRandomReviewStatus());
        long count = ReviewStatus.countReviewStatuses();
        Assert.assertTrue("Counter for 'ReviewStatus' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ReviewStatusIntegrationTest.testFindReviewStatus() {
        ReviewStatus obj = dod.getRandomReviewStatus();
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to provide an identifier", id);
        obj = ReviewStatus.findReviewStatus(id);
        Assert.assertNotNull("Find method for 'ReviewStatus' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ReviewStatus' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ReviewStatusIntegrationTest.testFindAllReviewStatuses() {
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to initialize correctly", dod.getRandomReviewStatus());
        long count = ReviewStatus.countReviewStatuses();
        Assert.assertTrue("Too expensive to perform a find all test for 'ReviewStatus', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ReviewStatus> result = ReviewStatus.findAllReviewStatuses();
        Assert.assertNotNull("Find all method for 'ReviewStatus' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ReviewStatus' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ReviewStatusIntegrationTest.testFindReviewStatusEntries() {
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to initialize correctly", dod.getRandomReviewStatus());
        long count = ReviewStatus.countReviewStatuses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ReviewStatus> result = ReviewStatus.findReviewStatusEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ReviewStatus' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ReviewStatus' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ReviewStatusIntegrationTest.testFlush() {
        ReviewStatus obj = dod.getRandomReviewStatus();
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to provide an identifier", id);
        obj = ReviewStatus.findReviewStatus(id);
        Assert.assertNotNull("Find method for 'ReviewStatus' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyReviewStatus(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ReviewStatus' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ReviewStatusIntegrationTest.testMergeUpdate() {
        ReviewStatus obj = dod.getRandomReviewStatus();
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to provide an identifier", id);
        obj = ReviewStatus.findReviewStatus(id);
        boolean modified =  dod.modifyReviewStatus(obj);
        Integer currentVersion = obj.getVersion();
        ReviewStatus merged = (ReviewStatus)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ReviewStatus' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ReviewStatusIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to initialize correctly", dod.getRandomReviewStatus());
        ReviewStatus obj = dod.getNewTransientReviewStatus(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ReviewStatus' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ReviewStatus' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ReviewStatusIntegrationTest.testRemove() {
        ReviewStatus obj = dod.getRandomReviewStatus();
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ReviewStatus' failed to provide an identifier", id);
        obj = ReviewStatus.findReviewStatus(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ReviewStatus' with identifier '" + id + "'", ReviewStatus.findReviewStatus(id));
    }
    
}
