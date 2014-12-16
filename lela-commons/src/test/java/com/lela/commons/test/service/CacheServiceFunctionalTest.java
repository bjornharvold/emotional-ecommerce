package com.lela.commons.test.service;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.test.context.ContextConfiguration;

import com.lela.commons.service.CacheService;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.Item;
import com.lela.domain.document.Merchant;


/**
 * The tests in this class address the condition described here:
 * https://lululu.jira.com/browse/LULU-2659
 * 
 * The tests loads up the cache configuration (without loading the bootstrapped cached items).
 * It uses the 'item' cache to place some number of elements of a simulated deep graph on the cache
 * It then loops through and increments that number of items with each iteration
 * The test places the object graph in the cache and then attempts to retrieve it.
 * It checks the integrity of each retrieved item (checks for nulled out values)
 * 
 * The test continues till the cache limit is reached and an OutOfMemoryError is thrown.
 * If an OutOfMemoryError is reached without reaching a nulled out value the test succeeds.
 * If a nulled out value is reached before an OutOfMemoryError is reached, the test fails.
 * 
 * The value of the maxBytesLocalHeap can be adjusted in ehcache.xml
 */

//The following contexts uses a test cache (lelaweb-test) and uses a cloned cache config (in ehcache-test.xml)
//It also disables the population of the cache via the OnStartupBootStrapperService
//It does so to isolate caching behavior to just this test.
@ContextConfiguration(locations = {"/META-INF/spring/disable-bootstrap.xml"})
public class CacheServiceFunctionalTest extends AbstractFunctionalTest{
	
	private final static Logger LOG = LoggerFactory.getLogger(CacheServiceFunctionalTest.class);
	
	@Autowired
	private CacheService cacheService;
	
	private boolean testFailed;
	
	private final String TEST_CACHE = "item";
	private final String TEST_CACHE_KEY = "testCacheKey";
	private final String WRAPPER_METHOD = "wrapper";
	private final String FAILSAFE_METHOD = "failsafe";
	
	@Before
	public void beforeEach(){
		testFailed = false;
	}
	
	/**
	 * This test uses the wrapped retrieveFromCache() method and 
	 * fails demonstrating the problem outlined in the JIRA ticket
	 * https://lululu.jira.com/browse/LULU-2659
	 * 
	 * The failure is asserted and so the JUnit test passes.
	 * 
	 * @throws Exception
	 */
	
	//This test is very dependent upon the state of the java heap. Therefore it is not deterministic.
	//In some circumstances, it returns a null retrievedItemsList and therefore the testFails is false, so the test fails
	//In others, it returns a non-null retrievedItemList and partially filled objects resulting in testFails=true so the test passes
	//Because it is not deterministic, commenting for now - Sep, 2012 PT
	//@Test
	public void testCacheServiceUsingWrapper() throws Exception{

		
		LOG.info("Starting cache test using wrapper method..."); 
		long tryWith = 100;
		for (int i = 0; i < 100; i++){
			tryWith = tryWith + 100;
			LOG.info("Starting test with cacheSize " + tryWith);
			try {
			   if (runTestForSize(tryWith, WRAPPER_METHOD)) {
				   break;
			   }
			} catch (OutOfMemoryError oom){
				LOG.info("Ran out of memory for cacheSize of: " + tryWith);
				break;
			}
		}
		if (testFailed){
			LOG.warn("Test testCacheServiceUsingWrapper Failed!!!");
		} else {
			LOG.info("Test testCacheServiceUsingWrapper passed!!!");
		}
		Assert.assertTrue(testFailed);
	}
	
	/**
	 * This test uses the failsafe retrieveCacheableList() method and 
	 * succeeds demonstrating a work around for the problem outlined in the JIRA ticket
	 * https://lululu.jira.com/browse/LULU-2659
	 * 
	 * The success is asserted and so the JUnit should pass.
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testCacheServiceUsingFailsafe() throws Exception{

		
		LOG.info("Starting cache test. using failsafe method.."); 
		long tryWith = 100;
		for (int i = 0; i < 100; i++){
			tryWith = tryWith + 100;
			LOG.info("Starting test with cacheSize " + tryWith);
			try {
			   if (runTestForSize(tryWith, FAILSAFE_METHOD)) {
				   break;
			   }
			} catch (OutOfMemoryError oom){
				LOG.info("Ran out of memory for cacheSize of: " + tryWith);
				break;
			}
		}
		if (testFailed){
			LOG.warn("Test testCacheServiceUsingFailsafe Failed!!!");
		} else {
			LOG.info("Test testCacheServiceUsingFailsafe passed!!!");
		}
		Assert.assertFalse(testFailed);
	}
	private boolean runTestForSize(long cacheSize, String method) throws OutOfMemoryError{
		
		boolean testFinished = false;
		
		List<Item> itemList = new ArrayList<Item>();
		for(int i = 0; i < cacheSize; i++) {
			Item item = new Item();
			item.setRlnm("testRlnm" + i);
			List<Merchant> merchantList = new ArrayList<Merchant>();
			long count = 0;
			//Simulate a deep link object graph
			for (int j = 0; j < i; j++) {
				Merchant m = new Merchant();
				m.setAsin("aSin" + j);
				m.setPrdd("prdd" + j);
				merchantList.add(m);
				count++;
			}
			//Hijack the Tld attrib to store how many merchants are stored with each Item
			item.setTld(count);
			item.setMrchnts(merchantList);
			itemList.add(item);
		}
		LOG.info("Starting to place " + cacheSize + " elements in cache");
		cacheService.putInCache(TEST_CACHE, TEST_CACHE_KEY, itemList);
		
		LOG.info("Placed " + cacheSize + " elements in cache");
		
		//Retrieve it to test integrity of the graph
		List<Item> retrievedItemList = null;

		if (method.equals(FAILSAFE_METHOD)){
			retrievedItemList = this.retrieveFroMCacheUsingFailsafeMethod();
		} else {
			//WRAPPER_METHOD
			retrievedItemList = this.retrieveFromCacheUsingWrapper();
		}
        
		if (retrievedItemList != null){
			int itemCount = 0;
	        //Ensure that each item element has the right number of Merchant elements
	        for (Item item : retrievedItemList) {
	        	itemCount++;
	        	if (StringUtils.isEmpty(item.getRlnm())) {
					LOG.warn("Cache misbehaved with a size of "+ cacheSize + ",Item rlnm lost values at itemCount: " + itemCount);
					testFailed = true;
					break;
	        	}
				List<Merchant> retrievedMerchantList = item.getMrchnts();
				if (retrievedMerchantList == null){
					LOG.warn("Cache misbehaved with a size of "+ cacheSize + ", List entry with Item rlnm: " + item.getRlnm() + " lost values!");
					testFailed = true;
					break;
				}
				long count = 0;
				for (Merchant merchant : retrievedMerchantList) {
					//that are not nulled out...
					if (!StringUtils.isEmpty(merchant.getAsin()) && !StringUtils.isEmpty(merchant.getPrdd())){
						count++;
					}
				}
				if (item.getTld() != count){
					LOG.warn("Cache misbehaved with a size of " + cacheSize + ", Entry with rlnm : " + item.getRlnm() + " lost values!");
					testFailed = true;
					break;
				}
			}
		} else {
			LOG.info("Retrieved null value from cache. Probably ran out of memory");
			testFinished = true;
		}
	    
		if (testFailed ){
			LOG.info("Test failed for cacheSize of : " + cacheSize);
			return true;
		} else if (testFinished) {
			LOG.info("Test finished with cacheSize of : " + cacheSize);
			return true;
		}else {
			return false;
		}
	}
	private List<Item> retrieveFromCacheUsingWrapper() {
		List<Item> retrievedItemList;
		Cache.ValueWrapper wrapper = cacheService.retrieveFromCache(TEST_CACHE, TEST_CACHE_KEY);
        LOG.info("Retrieved elements from cache");
        
        if (wrapper == null){
        	Assert.fail("Could not retrieve cached values - wrapper is null");
        }
        if (wrapper.get() == null){
        	Assert.fail("Could not retrieve cached values - wrapper.get() is null");
        }        
        if (!(wrapper.get() instanceof List<?>)){
        	Assert.fail("Could not retrieve cached values - wrapper.get() not instanceof List");
        }
        retrievedItemList = (List<Item>) wrapper.get();
		return retrievedItemList;
	}
	
	private List<Item> retrieveFroMCacheUsingFailsafeMethod(){
		return (List<Item>) cacheService.retrieveCacheableList(TEST_CACHE, TEST_CACHE_KEY);
	}
}
