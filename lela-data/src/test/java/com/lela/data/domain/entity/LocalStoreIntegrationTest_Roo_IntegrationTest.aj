// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.LocalStore;
import com.lela.data.domain.entity.LocalStoreDataOnDemand;
import com.lela.data.domain.entity.LocalStoreIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LocalStoreIntegrationTest_Roo_IntegrationTest {
    
    declare @type: LocalStoreIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: LocalStoreIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: LocalStoreIntegrationTest: @Transactional;
    
    @Autowired
    LocalStoreDataOnDemand LocalStoreIntegrationTest.dod;
    
    @Test
    public void LocalStoreIntegrationTest.testCountLocalStores() {
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to initialize correctly", dod.getRandomLocalStore());
        long count = LocalStore.countLocalStores();
        Assert.assertTrue("Counter for 'LocalStore' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void LocalStoreIntegrationTest.testFindLocalStore() {
        LocalStore obj = dod.getRandomLocalStore();
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to provide an identifier", id);
        obj = LocalStore.findLocalStore(id);
        Assert.assertNotNull("Find method for 'LocalStore' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'LocalStore' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void LocalStoreIntegrationTest.testFindAllLocalStores() {
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to initialize correctly", dod.getRandomLocalStore());
        long count = LocalStore.countLocalStores();
        Assert.assertTrue("Too expensive to perform a find all test for 'LocalStore', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<LocalStore> result = LocalStore.findAllLocalStores();
        Assert.assertNotNull("Find all method for 'LocalStore' illegally returned null", result);
        Assert.assertTrue("Find all method for 'LocalStore' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void LocalStoreIntegrationTest.testFindLocalStoreEntries() {
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to initialize correctly", dod.getRandomLocalStore());
        long count = LocalStore.countLocalStores();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<LocalStore> result = LocalStore.findLocalStoreEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'LocalStore' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'LocalStore' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void LocalStoreIntegrationTest.testFlush() {
        LocalStore obj = dod.getRandomLocalStore();
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to provide an identifier", id);
        obj = LocalStore.findLocalStore(id);
        Assert.assertNotNull("Find method for 'LocalStore' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLocalStore(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'LocalStore' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void LocalStoreIntegrationTest.testMergeUpdate() {
        LocalStore obj = dod.getRandomLocalStore();
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to provide an identifier", id);
        obj = LocalStore.findLocalStore(id);
        boolean modified =  dod.modifyLocalStore(obj);
        Integer currentVersion = obj.getVersion();
        LocalStore merged = (LocalStore)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'LocalStore' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void LocalStoreIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to initialize correctly", dod.getRandomLocalStore());
        LocalStore obj = dod.getNewTransientLocalStore(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'LocalStore' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'LocalStore' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void LocalStoreIntegrationTest.testRemove() {
        LocalStore obj = dod.getRandomLocalStore();
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LocalStore' failed to provide an identifier", id);
        obj = LocalStore.findLocalStore(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'LocalStore' with identifier '" + id + "'", LocalStore.findLocalStore(id));
    }
    
}
