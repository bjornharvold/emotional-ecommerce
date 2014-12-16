// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.AffiliateTransaction;
import com.lela.data.domain.entity.AffiliateTransactionDataOnDemand;
import com.lela.data.domain.entity.AffiliateTransactionIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect AffiliateTransactionIntegrationTest_Roo_IntegrationTest {
    
    declare @type: AffiliateTransactionIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: AffiliateTransactionIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: AffiliateTransactionIntegrationTest: @Transactional;
    
    @Autowired
    AffiliateTransactionDataOnDemand AffiliateTransactionIntegrationTest.dod;
    
    @Test
    public void AffiliateTransactionIntegrationTest.testCountAffiliateTransactions() {
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to initialize correctly", dod.getRandomAffiliateTransaction());
        long count = AffiliateTransaction.countAffiliateTransactions();
        Assert.assertTrue("Counter for 'AffiliateTransaction' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void AffiliateTransactionIntegrationTest.testFindAffiliateTransaction() {
        AffiliateTransaction obj = dod.getRandomAffiliateTransaction();
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to provide an identifier", id);
        obj = AffiliateTransaction.findAffiliateTransaction(id);
        Assert.assertNotNull("Find method for 'AffiliateTransaction' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'AffiliateTransaction' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void AffiliateTransactionIntegrationTest.testFindAllAffiliateTransactions() {
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to initialize correctly", dod.getRandomAffiliateTransaction());
        long count = AffiliateTransaction.countAffiliateTransactions();
        Assert.assertTrue("Too expensive to perform a find all test for 'AffiliateTransaction', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<AffiliateTransaction> result = AffiliateTransaction.findAllAffiliateTransactions();
        Assert.assertNotNull("Find all method for 'AffiliateTransaction' illegally returned null", result);
        Assert.assertTrue("Find all method for 'AffiliateTransaction' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void AffiliateTransactionIntegrationTest.testFindAffiliateTransactionEntries() {
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to initialize correctly", dod.getRandomAffiliateTransaction());
        long count = AffiliateTransaction.countAffiliateTransactions();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<AffiliateTransaction> result = AffiliateTransaction.findAffiliateTransactionEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'AffiliateTransaction' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'AffiliateTransaction' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void AffiliateTransactionIntegrationTest.testFlush() {
        AffiliateTransaction obj = dod.getRandomAffiliateTransaction();
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to provide an identifier", id);
        obj = AffiliateTransaction.findAffiliateTransaction(id);
        Assert.assertNotNull("Find method for 'AffiliateTransaction' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAffiliateTransaction(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'AffiliateTransaction' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AffiliateTransactionIntegrationTest.testMergeUpdate() {
        AffiliateTransaction obj = dod.getRandomAffiliateTransaction();
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to provide an identifier", id);
        obj = AffiliateTransaction.findAffiliateTransaction(id);
        boolean modified =  dod.modifyAffiliateTransaction(obj);
        Integer currentVersion = obj.getVersion();
        AffiliateTransaction merged = (AffiliateTransaction)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'AffiliateTransaction' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void AffiliateTransactionIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to initialize correctly", dod.getRandomAffiliateTransaction());
        AffiliateTransaction obj = dod.getNewTransientAffiliateTransaction(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'AffiliateTransaction' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'AffiliateTransaction' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void AffiliateTransactionIntegrationTest.testRemove() {
        AffiliateTransaction obj = dod.getRandomAffiliateTransaction();
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AffiliateTransaction' failed to provide an identifier", id);
        obj = AffiliateTransaction.findAffiliateTransaction(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'AffiliateTransaction' with identifier '" + id + "'", AffiliateTransaction.findAffiliateTransaction(id));
    }
    
}
