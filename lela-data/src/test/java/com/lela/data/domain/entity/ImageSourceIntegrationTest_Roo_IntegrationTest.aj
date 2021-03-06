// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.ImageSource;
import com.lela.data.domain.entity.ImageSourceDataOnDemand;
import com.lela.data.domain.entity.ImageSourceIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ImageSourceIntegrationTest_Roo_IntegrationTest {
    
    declare @type: ImageSourceIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: ImageSourceIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: ImageSourceIntegrationTest: @Transactional;
    
    @Autowired
    ImageSourceDataOnDemand ImageSourceIntegrationTest.dod;
    
    @Test
    public void ImageSourceIntegrationTest.testCountImageSources() {
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to initialize correctly", dod.getRandomImageSource());
        long count = ImageSource.countImageSources();
        Assert.assertTrue("Counter for 'ImageSource' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void ImageSourceIntegrationTest.testFindImageSource() {
        ImageSource obj = dod.getRandomImageSource();
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to provide an identifier", id);
        obj = ImageSource.findImageSource(id);
        Assert.assertNotNull("Find method for 'ImageSource' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ImageSource' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void ImageSourceIntegrationTest.testFindAllImageSources() {
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to initialize correctly", dod.getRandomImageSource());
        long count = ImageSource.countImageSources();
        Assert.assertTrue("Too expensive to perform a find all test for 'ImageSource', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ImageSource> result = ImageSource.findAllImageSources();
        Assert.assertNotNull("Find all method for 'ImageSource' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ImageSource' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void ImageSourceIntegrationTest.testFindImageSourceEntries() {
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to initialize correctly", dod.getRandomImageSource());
        long count = ImageSource.countImageSources();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ImageSource> result = ImageSource.findImageSourceEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ImageSource' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ImageSource' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void ImageSourceIntegrationTest.testFlush() {
        ImageSource obj = dod.getRandomImageSource();
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to provide an identifier", id);
        obj = ImageSource.findImageSource(id);
        Assert.assertNotNull("Find method for 'ImageSource' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyImageSource(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ImageSource' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ImageSourceIntegrationTest.testMergeUpdate() {
        ImageSource obj = dod.getRandomImageSource();
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to provide an identifier", id);
        obj = ImageSource.findImageSource(id);
        boolean modified =  dod.modifyImageSource(obj);
        Integer currentVersion = obj.getVersion();
        ImageSource merged = (ImageSource)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ImageSource' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void ImageSourceIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to initialize correctly", dod.getRandomImageSource());
        ImageSource obj = dod.getNewTransientImageSource(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ImageSource' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'ImageSource' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void ImageSourceIntegrationTest.testRemove() {
        ImageSource obj = dod.getRandomImageSource();
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ImageSource' failed to provide an identifier", id);
        obj = ImageSource.findImageSource(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ImageSource' with identifier '" + id + "'", ImageSource.findImageSource(id));
    }
    
}
