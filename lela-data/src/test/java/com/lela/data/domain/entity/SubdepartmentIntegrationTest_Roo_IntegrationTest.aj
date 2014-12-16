// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Subdepartment;
import com.lela.data.domain.entity.SubdepartmentDataOnDemand;
import com.lela.data.domain.entity.SubdepartmentIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SubdepartmentIntegrationTest_Roo_IntegrationTest {
    
    declare @type: SubdepartmentIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: SubdepartmentIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: SubdepartmentIntegrationTest: @Transactional;
    
    @Autowired
    SubdepartmentDataOnDemand SubdepartmentIntegrationTest.dod;
    
    @Test
    public void SubdepartmentIntegrationTest.testCountSubdepartments() {
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to initialize correctly", dod.getRandomSubdepartment());
        long count = Subdepartment.countSubdepartments();
        Assert.assertTrue("Counter for 'Subdepartment' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void SubdepartmentIntegrationTest.testFindSubdepartment() {
        Subdepartment obj = dod.getRandomSubdepartment();
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to provide an identifier", id);
        obj = Subdepartment.findSubdepartment(id);
        Assert.assertNotNull("Find method for 'Subdepartment' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Subdepartment' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void SubdepartmentIntegrationTest.testFindAllSubdepartments() {
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to initialize correctly", dod.getRandomSubdepartment());
        long count = Subdepartment.countSubdepartments();
        Assert.assertTrue("Too expensive to perform a find all test for 'Subdepartment', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Subdepartment> result = Subdepartment.findAllSubdepartments();
        Assert.assertNotNull("Find all method for 'Subdepartment' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Subdepartment' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void SubdepartmentIntegrationTest.testFindSubdepartmentEntries() {
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to initialize correctly", dod.getRandomSubdepartment());
        long count = Subdepartment.countSubdepartments();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Subdepartment> result = Subdepartment.findSubdepartmentEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Subdepartment' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Subdepartment' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void SubdepartmentIntegrationTest.testFlush() {
        Subdepartment obj = dod.getRandomSubdepartment();
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to provide an identifier", id);
        obj = Subdepartment.findSubdepartment(id);
        Assert.assertNotNull("Find method for 'Subdepartment' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySubdepartment(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Subdepartment' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SubdepartmentIntegrationTest.testMergeUpdate() {
        Subdepartment obj = dod.getRandomSubdepartment();
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to provide an identifier", id);
        obj = Subdepartment.findSubdepartment(id);
        boolean modified =  dod.modifySubdepartment(obj);
        Integer currentVersion = obj.getVersion();
        Subdepartment merged = (Subdepartment)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Subdepartment' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void SubdepartmentIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to initialize correctly", dod.getRandomSubdepartment());
        Subdepartment obj = dod.getNewTransientSubdepartment(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Subdepartment' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Subdepartment' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void SubdepartmentIntegrationTest.testRemove() {
        Subdepartment obj = dod.getRandomSubdepartment();
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Subdepartment' failed to provide an identifier", id);
        obj = Subdepartment.findSubdepartment(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Subdepartment' with identifier '" + id + "'", Subdepartment.findSubdepartment(id));
    }
    
}
