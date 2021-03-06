// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ItemAttribute;
import com.lela.data.domain.entity.ItemAttributeDataOnDemand;
import com.lela.data.domain.entity.ItemAttributeIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItemAttributeIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ItemAttributeIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ItemAttributeIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ItemAttributeIntegrationTest: @Transactional;
    
    @Autowired
    ItemAttributeDataOnDemand ItemAttributeIntegrationTest.dod;
    
    @Test
    public void ItemAttributeIntegrationTest.testCountItemAttributes() {
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to initialize correctly", dod.getRandomItemAttribute());
        long count = ItemAttribute.countItemAttributes();
        Assert.assertTrue("Counter for 'ItemAttribute' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ItemAttributeIntegrationTest.testFindItemAttribute() {
        ItemAttribute obj = dod.getRandomItemAttribute();
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to provide an identifier", id);
        obj = ItemAttribute.findItemAttribute(id);
        Assert.assertNotNull("Find method for 'ItemAttribute' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ItemAttribute' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ItemAttributeIntegrationTest.testFindAllItemAttributes() {
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to initialize correctly", dod.getRandomItemAttribute());
        long count = ItemAttribute.countItemAttributes();
        Assert.assertTrue("Too expensive to perform a find all test for 'ItemAttribute', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ItemAttribute> result = ItemAttribute.findAllItemAttributes();
        Assert.assertNotNull("Find all method for 'ItemAttribute' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ItemAttribute' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ItemAttributeIntegrationTest.testFindItemAttributeEntries() {
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to initialize correctly", dod.getRandomItemAttribute());
        long count = ItemAttribute.countItemAttributes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ItemAttribute> result = ItemAttribute.findItemAttributeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ItemAttribute' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ItemAttribute' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ItemAttributeIntegrationTest.testFlush() {
        ItemAttribute obj = dod.getRandomItemAttribute();
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to provide an identifier", id);
        obj = ItemAttribute.findItemAttribute(id);
        Assert.assertNotNull("Find method for 'ItemAttribute' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyItemAttribute(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ItemAttribute' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ItemAttributeIntegrationTest.testMergeUpdate() {
        ItemAttribute obj = dod.getRandomItemAttribute();
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to provide an identifier", id);
        obj = ItemAttribute.findItemAttribute(id);
        boolean modified =  dod.modifyItemAttribute(obj);
        Integer currentVersion = obj.getVersion();
        ItemAttribute merged = (ItemAttribute)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ItemAttribute' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ItemAttributeIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to initialize correctly", dod.getRandomItemAttribute());
        ItemAttribute obj = dod.getNewTransientItemAttribute(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ItemAttribute' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ItemAttribute' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ItemAttributeIntegrationTest.testRemove() {
        ItemAttribute obj = dod.getRandomItemAttribute();
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItemAttribute' failed to provide an identifier", id);
        obj = ItemAttribute.findItemAttribute(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ItemAttribute' with identifier '" + id + "'", ItemAttribute.findItemAttribute(id));
    }
    
}
