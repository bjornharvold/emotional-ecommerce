// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.BrandCategory;
import com.lela.data.domain.entity.BrandCategoryDataOnDemand;
import com.lela.data.domain.entity.BrandCategoryIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect BrandCategoryIntegrationTest_Roo_IntegrationTest {
    
    declare @type: BrandCategoryIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: BrandCategoryIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: BrandCategoryIntegrationTest: @Transactional;
    
    @Autowired
    BrandCategoryDataOnDemand BrandCategoryIntegrationTest.dod;
    
    @Test
    public void BrandCategoryIntegrationTest.testCountBrandCategorys() {
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to initialize correctly", dod.getRandomBrandCategory());
        long count = BrandCategory.countBrandCategorys();
        Assert.assertTrue("Counter for 'BrandCategory' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void BrandCategoryIntegrationTest.testFindBrandCategory() {
        BrandCategory obj = dod.getRandomBrandCategory();
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to provide an identifier", id);
        obj = BrandCategory.findBrandCategory(id);
        Assert.assertNotNull("Find method for 'BrandCategory' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'BrandCategory' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void BrandCategoryIntegrationTest.testFindAllBrandCategorys() {
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to initialize correctly", dod.getRandomBrandCategory());
        long count = BrandCategory.countBrandCategorys();
        Assert.assertTrue("Too expensive to perform a find all test for 'BrandCategory', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<BrandCategory> result = BrandCategory.findAllBrandCategorys();
        Assert.assertNotNull("Find all method for 'BrandCategory' illegally returned null", result);
        Assert.assertTrue("Find all method for 'BrandCategory' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void BrandCategoryIntegrationTest.testFindBrandCategoryEntries() {
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to initialize correctly", dod.getRandomBrandCategory());
        long count = BrandCategory.countBrandCategorys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<BrandCategory> result = BrandCategory.findBrandCategoryEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'BrandCategory' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'BrandCategory' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void BrandCategoryIntegrationTest.testFlush() {
        BrandCategory obj = dod.getRandomBrandCategory();
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to provide an identifier", id);
        obj = BrandCategory.findBrandCategory(id);
        Assert.assertNotNull("Find method for 'BrandCategory' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyBrandCategory(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'BrandCategory' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void BrandCategoryIntegrationTest.testMergeUpdate() {
        BrandCategory obj = dod.getRandomBrandCategory();
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to provide an identifier", id);
        obj = BrandCategory.findBrandCategory(id);
        boolean modified =  dod.modifyBrandCategory(obj);
        Integer currentVersion = obj.getVersion();
        BrandCategory merged = (BrandCategory)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'BrandCategory' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void BrandCategoryIntegrationTest.testRemove() {
        BrandCategory obj = dod.getRandomBrandCategory();
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BrandCategory' failed to provide an identifier", id);
        obj = BrandCategory.findBrandCategory(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'BrandCategory' with identifier '" + id + "'", BrandCategory.findBrandCategory(id));
    }
    
}
