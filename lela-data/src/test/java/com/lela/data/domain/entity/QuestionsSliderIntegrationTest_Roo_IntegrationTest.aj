// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.QuestionsSlider;
import com.lela.data.domain.entity.QuestionsSliderDataOnDemand;
import com.lela.data.domain.entity.QuestionsSliderIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect QuestionsSliderIntegrationTest_Roo_IntegrationTest {
    
    declare @type: QuestionsSliderIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: QuestionsSliderIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: QuestionsSliderIntegrationTest: @Transactional;
    
    @Autowired
    QuestionsSliderDataOnDemand QuestionsSliderIntegrationTest.dod;
    
    @Test
    public void QuestionsSliderIntegrationTest.testCountQuestionsSliders() {
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to initialize correctly", dod.getRandomQuestionsSlider());
        long count = QuestionsSlider.countQuestionsSliders();
        Assert.assertTrue("Counter for 'QuestionsSlider' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void QuestionsSliderIntegrationTest.testFindQuestionsSlider() {
        QuestionsSlider obj = dod.getRandomQuestionsSlider();
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to provide an identifier", id);
        obj = QuestionsSlider.findQuestionsSlider(id);
        Assert.assertNotNull("Find method for 'QuestionsSlider' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'QuestionsSlider' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void QuestionsSliderIntegrationTest.testFindAllQuestionsSliders() {
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to initialize correctly", dod.getRandomQuestionsSlider());
        long count = QuestionsSlider.countQuestionsSliders();
        Assert.assertTrue("Too expensive to perform a find all test for 'QuestionsSlider', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<QuestionsSlider> result = QuestionsSlider.findAllQuestionsSliders();
        Assert.assertNotNull("Find all method for 'QuestionsSlider' illegally returned null", result);
        Assert.assertTrue("Find all method for 'QuestionsSlider' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void QuestionsSliderIntegrationTest.testFindQuestionsSliderEntries() {
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to initialize correctly", dod.getRandomQuestionsSlider());
        long count = QuestionsSlider.countQuestionsSliders();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<QuestionsSlider> result = QuestionsSlider.findQuestionsSliderEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'QuestionsSlider' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'QuestionsSlider' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void QuestionsSliderIntegrationTest.testFlush() {
        QuestionsSlider obj = dod.getRandomQuestionsSlider();
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to provide an identifier", id);
        obj = QuestionsSlider.findQuestionsSlider(id);
        Assert.assertNotNull("Find method for 'QuestionsSlider' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyQuestionsSlider(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'QuestionsSlider' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void QuestionsSliderIntegrationTest.testMergeUpdate() {
        QuestionsSlider obj = dod.getRandomQuestionsSlider();
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to provide an identifier", id);
        obj = QuestionsSlider.findQuestionsSlider(id);
        boolean modified =  dod.modifyQuestionsSlider(obj);
        Integer currentVersion = obj.getVersion();
        QuestionsSlider merged = (QuestionsSlider)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'QuestionsSlider' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void QuestionsSliderIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to initialize correctly", dod.getRandomQuestionsSlider());
        QuestionsSlider obj = dod.getNewTransientQuestionsSlider(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'QuestionsSlider' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'QuestionsSlider' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void QuestionsSliderIntegrationTest.testRemove() {
        QuestionsSlider obj = dod.getRandomQuestionsSlider();
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'QuestionsSlider' failed to provide an identifier", id);
        obj = QuestionsSlider.findQuestionsSlider(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'QuestionsSlider' with identifier '" + id + "'", QuestionsSlider.findQuestionsSlider(id));
    }
    
}
