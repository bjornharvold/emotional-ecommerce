// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemReview;
import com.lela.data.domain.entity.ItemReviewDataOnDemand;
import com.lela.data.domain.entity.ItemReviewIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemReviewIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ItemReviewIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ItemReviewIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ItemReviewIntegrationTest: @Transactional;
    
    @Autowired
    ItemReviewDataOnDemand ItemReviewIntegrationTest.dod;
    
    @Test
    public void ItemReviewIntegrationTest.testCountItemReviews() {
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to initialize correctly", dod.getRandomItemReview());
        long count = ItemReview.countItemReviews();
        Assert.assertTrue("Counter for 'ItemReview' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ItemReviewIntegrationTest.testFindItemReview() {
        ItemReview obj = dod.getRandomItemReview();
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to provide an identifier", id);
        obj = ItemReview.findItemReview(id);
        Assert.assertNotNull("Find method for 'ItemReview' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ItemReview' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ItemReviewIntegrationTest.testFindAllItemReviews() {
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to initialize correctly", dod.getRandomItemReview());
        long count = ItemReview.countItemReviews();
        Assert.assertTrue("Too expensive to perform a find all test for 'ItemReview', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ItemReview> result = ItemReview.findAllItemReviews();
        Assert.assertNotNull("Find all method for 'ItemReview' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ItemReview' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ItemReviewIntegrationTest.testFindItemReviewEntries() {
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to initialize correctly", dod.getRandomItemReview());
        long count = ItemReview.countItemReviews();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ItemReview> result = ItemReview.findItemReviewEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ItemReview' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ItemReview' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ItemReviewIntegrationTest.testFlush() {
        ItemReview obj = dod.getRandomItemReview();
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to provide an identifier", id);
        obj = ItemReview.findItemReview(id);
        Assert.assertNotNull("Find method for 'ItemReview' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyItemReview(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ItemReview' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ItemReviewIntegrationTest.testMergeUpdate() {
        ItemReview obj = dod.getRandomItemReview();
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to provide an identifier", id);
        obj = ItemReview.findItemReview(id);
        boolean modified =  dod.modifyItemReview(obj);
        Integer currentVersion = obj.getVersion();
        ItemReview merged = (ItemReview)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ItemReview' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ItemReviewIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to initialize correctly", dod.getRandomItemReview());
        ItemReview obj = dod.getNewTransientItemReview(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ItemReview' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ItemReview' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ItemReviewIntegrationTest.testRemove() {
        ItemReview obj = dod.getRandomItemReview();
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemReview' failed to provide an identifier", id);
        obj = ItemReview.findItemReview(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ItemReview' with identifier '" + id + "'", ItemReview.findItemReview(id));
    }
    
}
