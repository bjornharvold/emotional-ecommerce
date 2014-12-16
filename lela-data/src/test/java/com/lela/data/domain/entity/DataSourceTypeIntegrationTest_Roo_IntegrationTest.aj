// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.DataSourceType;
import com.lela.data.domain.entity.DataSourceTypeDataOnDemand;
import com.lela.data.domain.entity.DataSourceTypeIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect DataSourceTypeIntegrationTest_Roo_IntegrationTest {
    
    declare @type: DataSourceTypeIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: DataSourceTypeIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: DataSourceTypeIntegrationTest: @Transactional;
    
    @Autowired
    DataSourceTypeDataOnDemand DataSourceTypeIntegrationTest.dod;
    
    @Test
    public void DataSourceTypeIntegrationTest.testCountDataSourceTypes() {
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to initialize correctly", dod.getRandomDataSourceType());
        long count = DataSourceType.countDataSourceTypes();
        Assert.assertTrue("Counter for 'DataSourceType' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void DataSourceTypeIntegrationTest.testFindDataSourceType() {
        DataSourceType obj = dod.getRandomDataSourceType();
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to provide an identifier", id);
        obj = DataSourceType.findDataSourceType(id);
        Assert.assertNotNull("Find method for 'DataSourceType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'DataSourceType' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void DataSourceTypeIntegrationTest.testFindAllDataSourceTypes() {
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to initialize correctly", dod.getRandomDataSourceType());
        long count = DataSourceType.countDataSourceTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'DataSourceType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<DataSourceType> result = DataSourceType.findAllDataSourceTypes();
        Assert.assertNotNull("Find all method for 'DataSourceType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'DataSourceType' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void DataSourceTypeIntegrationTest.testFindDataSourceTypeEntries() {
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to initialize correctly", dod.getRandomDataSourceType());
        long count = DataSourceType.countDataSourceTypes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<DataSourceType> result = DataSourceType.findDataSourceTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'DataSourceType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'DataSourceType' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void DataSourceTypeIntegrationTest.testFlush() {
        DataSourceType obj = dod.getRandomDataSourceType();
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to provide an identifier", id);
        obj = DataSourceType.findDataSourceType(id);
        Assert.assertNotNull("Find method for 'DataSourceType' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDataSourceType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'DataSourceType' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void DataSourceTypeIntegrationTest.testMergeUpdate() {
        DataSourceType obj = dod.getRandomDataSourceType();
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to provide an identifier", id);
        obj = DataSourceType.findDataSourceType(id);
        boolean modified =  dod.modifyDataSourceType(obj);
        Integer currentVersion = obj.getVersion();
        DataSourceType merged = (DataSourceType)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'DataSourceType' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void DataSourceTypeIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to initialize correctly", dod.getRandomDataSourceType());
        DataSourceType obj = dod.getNewTransientDataSourceType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'DataSourceType' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'DataSourceType' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void DataSourceTypeIntegrationTest.testRemove() {
        DataSourceType obj = dod.getRandomDataSourceType();
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'DataSourceType' failed to provide an identifier", id);
        obj = DataSourceType.findDataSourceType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'DataSourceType' with identifier '" + id + "'", DataSourceType.findDataSourceType(id));
    }
    
}