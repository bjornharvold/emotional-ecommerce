/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.test.integration.search;

import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.Color;
import com.lela.domain.document.Item;
import com.lela.domain.document.Owner;
import com.lela.domain.dto.search.GroupedSearchResult;
import com.lela.domain.dto.search.ItemSearchQuery;
import com.lela.domain.dto.search.ItemsForOwnerSearchQuery;
import com.lela.domain.dto.search.SearchResult;
import com.lela.domain.enums.StoreType;
import com.lela.util.test.integration.search.SolrServerRunning;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Bjorn Harvold
 * Date: 2/16/12
 * Time: 1:11 AM
 * Responsibility:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/META-INF/spring/search.xml", "/META-INF/spring/spring-property-configurer.xml",
        "/META-INF/spring/encryption.xml"})
public class SearchServiceIntegrationTest extends AbstractJUnit4SpringContextTests {
    private static final Logger log = LoggerFactory.getLogger(SearchServiceIntegrationTest.class);
    private static final String ITEM2_URL_NAME = "svenskkaviar";
    private static final String ITEM2_NAME = "Svensk Kaviar";
    private static final String OWNER_SVERIGE_URLNAME = "sverige";
    private static final String OWNER_SVERIGE_NAME = "Sverige";
    private static final String ITEM3_URL_NAME = "danskpolse";
    private static final String ITEM3_NAME = "Dansk Polse";
    private static final String OWNER_DANMARK_URLNAME = "danmark";
    private static final String OWNER_DANMARK_NAME = "Danmark";
    private static final String ITEM4_URL_NAME = "finsklakris";
    private static final String ITEM4_NAME = "Finsklakris";
    private static final String OWNER_FINLAND_URLNAME = "finland";
    private static final String OWNER_FINLAND_NAME = "Finland";
    private static final String ITEM1_URL_NAME = "miscreant";
    private static final String ITEM1_NAME = "Miscreant";
    private static final String CATEGORY_NAME = "blah blah";
    private static final String OWNER_UNITEDSTATES_URL_NAME = "unitedstates";
    private static final String OWNER_UNITED_STATES_NAME = "United States";
    private static final String COLOR_RED = "red";
    private static final String COLOR_BLUE = "blue";
    private static final String COLOR_BLACK = "black";
    private static final String COLOR_GREEN = "green";
    private static final String COLOR_WHITE = "white";
    private static final String CATEGORY_URL_NAME_INDEX = "ctgry";
    private static final String BEST_BUY_NAME = "Best Buy";
    private static final String BEST_BUY_URL_NAME = "bestbuy";
    private static final String TARGET_NAME = "Target";
    private static final String TARGET_URL_NAME = "target";
    private static final String BUY_COM_NAME = "buy.com";
    private static final String BUY_COM_URL_NAME = "buycom";
    private static final String CATEGORY_URL_NAME = "blahblahblah";
    private static final String CATERPILLAR_CATEGORY_URL_NAME = "caterpillar";
    private static final String CATERPILLARS_CATEGORY_URL_NAME = CATERPILLAR_CATEGORY_URL_NAME + "s";

    @Rule
    public SolrServerRunning serverRunning = SolrServerRunning.isRunning();

    @Autowired
    private SearchService searchService;

    @Before
    public void setUp() {
        try {
            searchService.deleteItemsByQuery("ctgry:" + CATEGORY_URL_NAME);
            searchService.deleteItemsByQuery("ctgry:" + CATERPILLAR_CATEGORY_URL_NAME);
        } catch (SearchException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try {
            searchService.deleteItemsByQuery("ctgry:" + CATEGORY_URL_NAME);
            searchService.deleteItemsByQuery("ctgry:" + CATERPILLAR_CATEGORY_URL_NAME);
        } catch (SearchException e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void testSearchService() {
        log.info("Testing search service...");

        try {
            assertNotNull("Search service is null", searchService);

            List<Item> testDataList = createTestData();
            List<String> ids = new ArrayList<String>(testDataList.size());
            for (Item doc : testDataList) {
                ids.add(doc.getId().toString());
            }

            searchService.indexItems(testDataList);

            Thread.sleep(1000);

            log.info("First we search by category");
            ItemSearchQuery sq = new ItemSearchQuery();
            sq.setCat(CATEGORY_URL_NAME);

            SearchResult solrResult = searchService.searchForItems(sq);
            QueryResponse response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            SolrDocumentList list = response.getResults();
            assertNotNull("Group response is null", list);
            assertEquals("Solr input size doesn't match solr output size", new Integer(testDataList.size()).longValue(), list.getNumFound(), 0);

            log.info("Now we search on something more specific that will only give us a subset of documents.");

            log.info("Let's search for item1 name first...");
            sq = new ItemSearchQuery();
            sq.setTerms(ITEM1_NAME);
            sq.setCat(CATEGORY_URL_NAME);
            solrResult = searchService.searchForItems(sq);
            response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            list = response.getResults();
            assertNotNull("Group response is null", list);
            assertEquals("Solr input size doesn't match solr output size", 1L, list.getNumFound(), 0);

            // the facet is the "group meta data" from solr
            FacetField facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 1, facet.getValueCount(), 0);
            log.info("Search for item1 name was successful");

            log.info("Let's compare the results of the last query with the same query, but this time by not using the extended dismax query parser");
            sq = new ItemSearchQuery();
            sq.setTerms(ITEM1_NAME);
            sq.setCat(CATEGORY_URL_NAME);
            solrResult = searchService.searchForItems(sq, false);
            response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            list = response.getResults();
            assertNotNull("Group response is null", list);
            assertEquals("Solr input size doesn't match solr output size", 1L, list.getNumFound(), 0);

            // the facet is the "group meta data" from solr
            facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 1, facet.getValueCount(), 0);


            log.info("Let's search for item2 name second...");
            sq = new ItemSearchQuery();
            sq.setTerms(ITEM2_NAME);
            sq.setCat(CATEGORY_URL_NAME);
            solrResult = searchService.searchForItems(sq);
            response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            list = response.getResults();
            assertNotNull("Group response is null", list);
            assertEquals("Solr input size doesn't match solr output size", 1L, list.getNumFound(), 0);

            facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 1, facet.getValueCount(), 0);
            log.info("Search for item2 name was successful");

            log.info("That worked marvellously. Now let's search for brand.");
            sq = new ItemSearchQuery();
            sq.setTerms(OWNER_UNITED_STATES_NAME);
            sq.setCat(CATEGORY_URL_NAME);
            solrResult = searchService.searchForItems(sq);
            response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            list = response.getResults();
            assertNotNull("Group response is null", list);
            assertEquals("Solr input size doesn't match solr output size", 1L, list.getNumFound(), 0);

            facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 1, facet.getValueCount(), 0);

            log.info("We can also search for brand using the dedicated brand method");
            ItemsForOwnerSearchQuery ifosq = new ItemsForOwnerSearchQuery();
            ifosq.setUrlName(OWNER_UNITEDSTATES_URL_NAME);
//            ifosq.setCat(CATEGORY_URL_NAME);
            GroupedSearchResult groupedSolrResult = searchService.searchForItemsForOwner(ifosq);
            response = groupedSolrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            GroupResponse groupResponse = response.getGroupResponse();
            assertNotNull("Group response is null", groupResponse);

            facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 1, facet.getValueCount(), 0);

            log.info("Brand search successful. Let's search for colors");
            sq = new ItemSearchQuery();
            sq.setTerms(COLOR_BLUE);
            sq.setCat(CATEGORY_URL_NAME);
            solrResult = searchService.searchForItems(sq);
            response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            list = response.getResults();
            assertNotNull("Group response is null", list);
            assertEquals("Solr input size doesn't match solr output size", 2L, list.getNumFound(), 0);

            facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 1, facet.getValueCount(), 0);

            log.info("Color search successful. Finally we search on attributes");

            log.info("Color search successful. Let's search for stores");
            sq = new ItemSearchQuery();
            sq.setTerms(BEST_BUY_NAME);
            sq.setCat(CATEGORY_URL_NAME);
            solrResult = searchService.searchForItems(sq);
            response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            list = response.getResults();
            assertNotNull("Group response is null", list);
            assertEquals("Solr input size doesn't match solr output size", 4L, list.getNumFound(), 0);

            facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 1, facet.getValueCount(), 0);

            log.info("Color search successful. Finally we search on attributes");

            log.info("Attribute search successful.");

            log.info("Now we want to use the specific category field to retrieve data");
            sq = new ItemSearchQuery();
            sq.setCat(CATEGORY_URL_NAME);
            solrResult = searchService.searchForItems(sq);

            response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            list = response.getResults();
            assertNotNull("Result is null", list);
            assertEquals("Solr input size doesn't match solr output size", 4L, list.getNumFound(), 0);

            facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 1, facet.getValueCount(), 0);

            log.info("Searching by category field worked");

            log.info("Nothing left to do so we delete all the Solr documents");
            searchService.deleteItemsById(ids);

            solrResult = searchService.searchForItems(sq);
            response = solrResult.getQueryResponse();
            assertNotNull("Solr response is null", response);

            list = response.getResults();
            assertNotNull("Group response is null", list);
            assertEquals("Solr input size doesn't match solr output size", 0L, list.getNumFound(), 0);

            facet = response.getFacetField(CATEGORY_URL_NAME_INDEX);
            assertNotNull("Facet is null", facet);
            assertEquals("Solr search result is incorrect", 0, facet.getValueCount(), 0);

        } catch (Exception ex) {
            log.info(ex.getMessage(), ex);
            fail(ex.getMessage());
        }

        log.info("Testing search service complete");
    }

    @Test
    public void testExceptions() {
        log.info("Testing passing no terms across...");

        try {
            SearchResult sr = searchService.searchForItems(new ItemSearchQuery());
            fail("Should've failed.");
        } catch (SearchException e) {
            assertNotNull("Search exception message is null", e.getMessage());
        }

        log.info("Testing passing no terms across completed");
    }

    @Test
    public void testPlurals() {
        log.info("Testing searching for '" + CATERPILLARS_CATEGORY_URL_NAME + "' and '" + CATERPILLAR_CATEGORY_URL_NAME + "'...");

        try {

            log.info("First we have to create some test data");
            List<Item> list = new ArrayList<Item>();
            List<AvailableInStore> stores = new ArrayList<AvailableInStore>(1);
            AvailableInStore store1 = new AvailableInStore(BEST_BUY_NAME, BEST_BUY_URL_NAME, StoreType.ONLINE);
            stores.add(store1);

            // Item 1
            Item item1 = new Item();
            item1.setId(ObjectId.get());
            item1.setPc(item1.getId().toString());
            item1.setLlpc(item1.getId().toString());
            item1.setRlnm(ITEM1_URL_NAME);
            item1.setNm(ITEM1_NAME);
            item1.setStrs(stores);

            Category cat = new Category();
            cat.setRlnm(CATERPILLAR_CATEGORY_URL_NAME);
            cat.setNm(CATERPILLAR_CATEGORY_URL_NAME);
            item1.setCtgry(cat);

            Owner owner = new Owner();
            owner.setRlnm(OWNER_UNITEDSTATES_URL_NAME);
            owner.setNm(OWNER_UNITED_STATES_NAME);
            item1.setWnr(owner);

            list.add(item1);

            List<String> ids = new ArrayList<String>(list.size());
            for (Item doc : list) {
                ids.add(doc.getId().toString());
            }

            searchService.indexItems(list);

            Thread.sleep(1000);

            ItemSearchQuery query = new ItemSearchQuery();
            query.setTerms(ITEM1_NAME);

            SearchResult sr = searchService.searchForItems(query);
            QueryResponse response = sr.getQueryResponse();
            assertNotNull("Response is null", response);
            assertNotNull("Search result is null for " + CATERPILLAR_CATEGORY_URL_NAME, response.getResults());
            log.info(String.format("Returned %d results for '" + CATERPILLAR_CATEGORY_URL_NAME + "'", response.getResults().size()));
            assertEquals("Search result size incorrect", 1, response.getResults().size(), 0);

            query = new ItemSearchQuery();
            query.setTerms(ITEM1_NAME + "s");
            sr = searchService.searchForItems(query);
            response = sr.getQueryResponse();
            assertNotNull("Response is null", response);
            assertNotNull("Search result is null for " + CATERPILLARS_CATEGORY_URL_NAME, response.getResults());
            log.info(String.format("Returned %d results for '" + CATERPILLARS_CATEGORY_URL_NAME + "'", response.getResults().size()));
            assertEquals("Search result size incorrect", 1, response.getResults().size(), 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            fail(e.getMessage());
        }

        log.info("Testing searching for '" + CATERPILLARS_CATEGORY_URL_NAME + "' and '" + CATERPILLAR_CATEGORY_URL_NAME + "' complete");
    }

    private List<Item> createTestData() {
        List<Item> result = new ArrayList<Item>();

        List<AvailableInStore> stores = new ArrayList<AvailableInStore>(3);
        AvailableInStore store1 = new AvailableInStore(BEST_BUY_NAME, BEST_BUY_URL_NAME, StoreType.ONLINE);
        AvailableInStore store2 = new AvailableInStore(TARGET_NAME, TARGET_URL_NAME, StoreType.ONLINE);
        AvailableInStore store3 = new AvailableInStore(BUY_COM_NAME, BUY_COM_URL_NAME, StoreType.ONLINE);
        stores.add(store1);
        stores.add(store2);
        stores.add(store3);

        // Item 1
        Item item1 = new Item();
        item1.setId(ObjectId.get());
        item1.setPc(item1.getId().toString());
        item1.setLlpc(item1.getId().toString());
        item1.setRlnm(ITEM1_URL_NAME);
        item1.setNm(ITEM1_NAME);
        item1.setStrs(stores);

        Category cat = new Category();
        cat.setRlnm(CATEGORY_URL_NAME);
        cat.setNm(CATEGORY_NAME);
        item1.setCtgry(cat);

        Owner owner = new Owner();
        owner.setRlnm(OWNER_UNITEDSTATES_URL_NAME);
        owner.setNm(OWNER_UNITED_STATES_NAME);
        item1.setWnr(owner);

        List<Color> clrs = new ArrayList<Color>();
        Color red = new Color();
        red.setNm(COLOR_RED);
        clrs.add(red);
        Color blue = new Color();
        blue.setNm(COLOR_BLUE);
        clrs.add(blue);
        item1.setClrs(clrs);

        /*
        List<String> attrs = new ArrayList<String>();
        attrs.add("fun");
        item1.setAttrs(attrs);
        */

        result.add(item1);

        // Item 2
        Item item2 = new Item();
        item2.setId(ObjectId.get());
        item2.setPc(item2.getId().toString());
        item2.setLlpc(item2.getId().toString());
        item2.setRlnm(ITEM2_URL_NAME);
        item2.setNm(ITEM2_NAME);
        item2.setCtgry(cat);
        owner = new Owner();
        owner.setRlnm(OWNER_SVERIGE_URLNAME);
        owner.setNm(OWNER_SVERIGE_NAME);
        item2.setWnr(owner);
        item2.setStrs(stores);

        clrs = new ArrayList<Color>();
        Color black = new Color();
        black.setNm(COLOR_BLACK);
        clrs.add(black);
        clrs.add(blue);
        item2.setClrs(clrs);

        /*
        attrs = new ArrayList<String>();
        attrs.add("sporty");
        item2.setAttrs(attrs);
        */

        result.add(item2);

        // Item 3
        Item item3 = new Item();
        item3.setId(ObjectId.get());
        item3.setLlpc(item3.getId().toString());
        item3.setRlnm(ITEM3_URL_NAME);
        item3.setNm(ITEM3_NAME);
        item3.setCtgry(cat);
        owner = new Owner();
        owner.setRlnm(OWNER_DANMARK_URLNAME);
        owner.setNm(OWNER_DANMARK_NAME);
        item3.setWnr(owner);
        item3.setStrs(stores);

        clrs = new ArrayList<Color>();
        Color green = new Color();
        green.setNm(COLOR_GREEN);
        clrs.add(red);
        clrs.add(green);
        item3.setClrs(clrs);

        /*
        attrs = new ArrayList<String>();
        attrs.add("chic");
        item3.setAttrs(attrs);
        */

        result.add(item3);

        // Item 4
        Item item4 = new Item();
        item4.setId(ObjectId.get());
        item4.setLlpc(item4.getId().toString());
        item4.setRlnm(ITEM4_URL_NAME);
        item4.setNm(ITEM4_NAME);
        item4.setCtgry(cat);
        owner = new Owner();
        owner.setRlnm(OWNER_FINLAND_URLNAME);
        owner.setRlnm(OWNER_FINLAND_NAME);
        item4.setWnr(owner);
        item4.setStrs(stores);

        clrs = new ArrayList<Color>();
        Color white = new Color();
        white.setNm(COLOR_WHITE);
        clrs.add(black);
        clrs.add(white);
        item4.setClrs(clrs);

        /*
        attrs = new ArrayList<String>();
        attrs.add("modern");
        item4.setAttrs(attrs);
        */

        result.add(item4);

        return result;
    }
}
