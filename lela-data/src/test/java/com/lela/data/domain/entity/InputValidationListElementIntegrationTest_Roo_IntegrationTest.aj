// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.InputValidationListElement;
import com.lela.data.domain.entity.InputValidationListElementDataOnDemand;
import com.lela.data.domain.entity.InputValidationListElementIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect InputValidationListElementIntegrationTest_Roo_IntegrationTest {
    
    declare @type: InputValidationListElementIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: InputValidationListElementIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: InputValidationListElementIntegrationTest: @Transactional;
    
    @Autowired
    InputValidationListElementDataOnDemand InputValidationListElementIntegrationTest.dod;
    
    @Test
    public void InputValidationListElementIntegrationTest.testCountInputValidationListElements() {
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to initialize correctly", dod.getRandomInputValidationListElement());
        long count = InputValidationListElement.countInputValidationListElements();
        Assert.assertTrue("Counter for 'InputValidationListElement' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void InputValidationListElementIntegrationTest.testFindInputValidationListElement() {
        InputValidationListElement obj = dod.getRandomInputValidationListElement();
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to provide an identifier", id);
        obj = InputValidationListElement.findInputValidationListElement(id);
        Assert.assertNotNull("Find method for 'InputValidationListElement' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'InputValidationListElement' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void InputValidationListElementIntegrationTest.testFindAllInputValidationListElements() {
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to initialize correctly", dod.getRandomInputValidationListElement());
        long count = InputValidationListElement.countInputValidationListElements();
        Assert.assertTrue("Too expensive to perform a find all test for 'InputValidationListElement', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<InputValidationListElement> result = InputValidationListElement.findAllInputValidationListElements();
        Assert.assertNotNull("Find all method for 'InputValidationListElement' illegally returned null", result);
        Assert.assertTrue("Find all method for 'InputValidationListElement' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void InputValidationListElementIntegrationTest.testFindInputValidationListElementEntries() {
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to initialize correctly", dod.getRandomInputValidationListElement());
        long count = InputValidationListElement.countInputValidationListElements();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<InputValidationListElement> result = InputValidationListElement.findInputValidationListElementEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'InputValidationListElement' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'InputValidationListElement' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void InputValidationListElementIntegrationTest.testFlush() {
        InputValidationListElement obj = dod.getRandomInputValidationListElement();
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to provide an identifier", id);
        obj = InputValidationListElement.findInputValidationListElement(id);
        Assert.assertNotNull("Find method for 'InputValidationListElement' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyInputValidationListElement(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'InputValidationListElement' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void InputValidationListElementIntegrationTest.testMergeUpdate() {
        InputValidationListElement obj = dod.getRandomInputValidationListElement();
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to provide an identifier", id);
        obj = InputValidationListElement.findInputValidationListElement(id);
        boolean modified =  dod.modifyInputValidationListElement(obj);
        Integer currentVersion = obj.getVersion();
        InputValidationListElement merged = (InputValidationListElement)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'InputValidationListElement' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void InputValidationListElementIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to initialize correctly", dod.getRandomInputValidationListElement());
        InputValidationListElement obj = dod.getNewTransientInputValidationListElement(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'InputValidationListElement' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'InputValidationListElement' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void InputValidationListElementIntegrationTest.testRemove() {
        InputValidationListElement obj = dod.getRandomInputValidationListElement();
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InputValidationListElement' failed to provide an identifier", id);
        obj = InputValidationListElement.findInputValidationListElement(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'InputValidationListElement' with identifier '" + id + "'", InputValidationListElement.findInputValidationListElement(id));
    }
    
}