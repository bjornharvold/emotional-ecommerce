// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.FunctionalFilterAnswerValue;
import com.lela.data.domain.entity.FunctionalFilterAnswerValueDataOnDemand;
import com.lela.data.domain.entity.FunctionalFilterAnswerValueIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FunctionalFilterAnswerValueIntegrationTest_Roo_IntegrationTest {
    
    declare @type: FunctionalFilterAnswerValueIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: FunctionalFilterAnswerValueIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: FunctionalFilterAnswerValueIntegrationTest: @Transactional;
    
    @Autowired
    FunctionalFilterAnswerValueDataOnDemand FunctionalFilterAnswerValueIntegrationTest.dod;
    
    @Test
    public void FunctionalFilterAnswerValueIntegrationTest.testCountFunctionalFilterAnswerValues() {
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to initialize correctly", dod.getRandomFunctionalFilterAnswerValue());
        long count = FunctionalFilterAnswerValue.countFunctionalFilterAnswerValues();
        Assert.assertTrue("Counter for 'FunctionalFilterAnswerValue' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void FunctionalFilterAnswerValueIntegrationTest.testFindFunctionalFilterAnswerValue() {
        FunctionalFilterAnswerValue obj = dod.getRandomFunctionalFilterAnswerValue();
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to provide an identifier", id);
        obj = FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id);
        Assert.assertNotNull("Find method for 'FunctionalFilterAnswerValue' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'FunctionalFilterAnswerValue' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void FunctionalFilterAnswerValueIntegrationTest.testFindAllFunctionalFilterAnswerValues() {
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to initialize correctly", dod.getRandomFunctionalFilterAnswerValue());
        long count = FunctionalFilterAnswerValue.countFunctionalFilterAnswerValues();
        Assert.assertTrue("Too expensive to perform a find all test for 'FunctionalFilterAnswerValue', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<FunctionalFilterAnswerValue> result = FunctionalFilterAnswerValue.findAllFunctionalFilterAnswerValues();
        Assert.assertNotNull("Find all method for 'FunctionalFilterAnswerValue' illegally returned null", result);
        Assert.assertTrue("Find all method for 'FunctionalFilterAnswerValue' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void FunctionalFilterAnswerValueIntegrationTest.testFindFunctionalFilterAnswerValueEntries() {
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to initialize correctly", dod.getRandomFunctionalFilterAnswerValue());
        long count = FunctionalFilterAnswerValue.countFunctionalFilterAnswerValues();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<FunctionalFilterAnswerValue> result = FunctionalFilterAnswerValue.findFunctionalFilterAnswerValueEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'FunctionalFilterAnswerValue' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'FunctionalFilterAnswerValue' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void FunctionalFilterAnswerValueIntegrationTest.testFlush() {
        FunctionalFilterAnswerValue obj = dod.getRandomFunctionalFilterAnswerValue();
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to provide an identifier", id);
        obj = FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id);
        Assert.assertNotNull("Find method for 'FunctionalFilterAnswerValue' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFunctionalFilterAnswerValue(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'FunctionalFilterAnswerValue' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FunctionalFilterAnswerValueIntegrationTest.testMergeUpdate() {
        FunctionalFilterAnswerValue obj = dod.getRandomFunctionalFilterAnswerValue();
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to provide an identifier", id);
        obj = FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id);
        boolean modified =  dod.modifyFunctionalFilterAnswerValue(obj);
        Integer currentVersion = obj.getVersion();
        FunctionalFilterAnswerValue merged = (FunctionalFilterAnswerValue)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'FunctionalFilterAnswerValue' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FunctionalFilterAnswerValueIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to initialize correctly", dod.getRandomFunctionalFilterAnswerValue());
        FunctionalFilterAnswerValue obj = dod.getNewTransientFunctionalFilterAnswerValue(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'FunctionalFilterAnswerValue' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'FunctionalFilterAnswerValue' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void FunctionalFilterAnswerValueIntegrationTest.testRemove() {
        FunctionalFilterAnswerValue obj = dod.getRandomFunctionalFilterAnswerValue();
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'FunctionalFilterAnswerValue' failed to provide an identifier", id);
        obj = FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'FunctionalFilterAnswerValue' with identifier '" + id + "'", FunctionalFilterAnswerValue.findFunctionalFilterAnswerValue(id));
    }
    
}
