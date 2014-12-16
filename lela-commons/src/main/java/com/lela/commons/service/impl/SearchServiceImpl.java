/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.service.SearchException;
import com.lela.commons.service.SearchService;
import com.lela.domain.document.Item;
import com.lela.domain.dto.search.AbstractSearchResult;
import com.lela.domain.dto.search.FacetSearchQuery;
import com.lela.domain.dto.search.GroupedSearchResult;
import com.lela.domain.dto.search.ItemSearchQuery;
import com.lela.domain.dto.search.ItemSolrDocument;
import com.lela.domain.dto.search.ItemsForOwnerSearchQuery;
import com.lela.domain.dto.search.ItemsInStoreSearchQuery;
import com.lela.domain.dto.search.KeywordTerms;
import com.lela.domain.dto.search.NameValueAggregate;
import com.lela.domain.dto.search.SearchConditionType;
import com.lela.domain.dto.search.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 2/15/12
 * Time: 11:42 PM
 * Responsibility:
 */
public class SearchServiceImpl implements SearchService {
    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);
    private static final String SOLR_SERVER_ERROR = "Solr server is NOT available! This might cause havoc on the search functionality.";
    private static final String ITEM_URL_NAME_INDEX = "rlnm";
    private static final String NM_INDEX = "nm";
    private static final String STORE_URL_NAME_INDEX = "strrl" + NM_INDEX + "s";
    private static final String OWNER_URL_NAME_INDEX = "wnrrl" + NM_INDEX;
    private static final String CATEGORY_URL_NAME_INDEX = "ctgry";
    private static final String EXTENDED_DISMAX_QUERY_PARSER = "edismax";
    private static final String DEF_TYPE = "defType";
    private static final String TEXT_INDEX = "text";
    private static final String TEXT_ENGLISH_INDEX = TEXT_INDEX + "_english";
    private static final String TEXT_ENGLISH_SPLITTING_INDEX = TEXT_ENGLISH_INDEX + "_splitting";
    private static final String TEXT_ENGLISH_SPLITTING_TIGHT_INDEX = TEXT_ENGLISH_SPLITTING_INDEX + "_tight";

    private static int itemCoreSolrServerStatus = -1;

    private final SolrServer itemCoreSolrServer;
    private static final long TEN_MINUTES = 600000l;
    private volatile Thread solrCheckingThread = null;

    public SearchServiceImpl(SolrServer itemCoreSolrServer) {
        this.itemCoreSolrServer = itemCoreSolrServer;
    }

    public void init() {
        Runnable r1 = new Runnable() {
            public void run() {
                Thread thisThread = Thread.currentThread();

                try {
                    while (solrCheckingThread == thisThread) {
                        try {
                            SolrPingResponse response = itemCoreSolrServer.ping();
                            itemCoreSolrServerStatus = response.getStatus();
                        } catch (SolrServerException e) {
                            if (log.isDebugEnabled()) {
                                log.debug(e.getMessage(), e);
                            }
                            itemCoreSolrServerStatus = -1;
                        } catch (IOException e) {
                            if (log.isDebugEnabled()) {
                                log.debug(e.getMessage(), e);
                            }
                            itemCoreSolrServerStatus = -1;
                        } finally {
                            /*
                            For testing only
                            if (itemCoreSolrServerStatus == 0) {
                                System.out.println("Item Core Solr Server [itemCoreSolrServer] is: ONLINE");
                            } else {
                                System.out.println("Item Core Solr Server [itemCoreSolrServer] is: OFFLINE");
                            }
                            */

                            solrCheckingThread.sleep(TEN_MINUTES);
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Solr checker shut down successfully");
                }
            }
        };

        solrCheckingThread = new Thread(r1);
        solrCheckingThread.start();
    }

    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("Shutting down solr checking thread...");
        }
        solrCheckingThread.interrupt();
    }

    @Override
    public boolean isItemCoreSolrServerAvailable() {
        return itemCoreSolrServerStatus == 0;
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void deleteItemByUrlName(String urlName) throws SearchException {
        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {

            if (urlName != null) {
                StringBuilder sb = new StringBuilder(ITEM_URL_NAME_INDEX)
                        .append(":(")
                        .append(urlName)
                        .append(")");

                deleteItemsByQuery(sb.toString());
            }
        }

    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void deleteItemsByUrlName(List<String> urlNames) throws SearchException {
        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {

            if (urlNames != null) {
                StringBuilder sb = new StringBuilder(ITEM_URL_NAME_INDEX).append(":(");

                for (int i = 0; i < urlNames.size(); i++) {
                    String urlName = urlNames.get(0);

                    sb.append(urlName);

                    if (i + 1 < urlNames.size()) {
                        sb.append(" OR ");
                    }
                }

                sb.append(")");

                deleteItemsByQuery(sb.toString());
            }
        }

    }

    @Override
    public SearchResult searchForItems(ItemSearchQuery query) throws SearchException {
        return searchForItems(query, true);
    }

    @Override
    public SearchResult searchForItems(ItemSearchQuery query, boolean dismax) throws SearchException {
        SearchResult result = null;
        int index = 0;
        int maxResults = 12;

        // we only ping the search server during startup
        // if the search server goes down after the application has started
        // this condition will still remain true and it wil instead catch an exception
        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {

            if (query == null) {
                throw new SearchException("query cannot be null");
            }
            if (StringUtils.isBlank(query.getTerms()) && StringUtils.isBlank(query.getCat())) {
                throw new SearchException("terms and cat conditions cannot both be null");
            }
            if (query.getPage() != null && query.getSize() != null) {
                index = query.getPage() * query.getSize();
                maxResults = query.getSize();

                result = new SearchResult(query.getPage(), query.getSize());
            } else {
                result = new SearchResult(index, maxResults);
            }

            try {

                // first we set up our keyword utility with the user input
                KeywordTerms kt;
                if (StringUtils.isNotBlank(query.getTerms())) {
                    kt = new KeywordTerms(query.getTerms());
                } else {
                    kt = new KeywordTerms();
                }

                if (StringUtils.isNotBlank(query.getCat())) {
                    List<String> categories = new ArrayList<String>(1);
                    categories.add(query.getCat());
                    kt.addSearchCondition(CATEGORY_URL_NAME_INDEX, categories, SearchConditionType.AND, false);
                }

                result.setKeywordTerms(kt);

                // here we configure the solrj query request
                SolrQuery sq;

                if (dismax) {
                    sq = new SolrQuery(kt.getQuery());
                    sq.set(DEF_TYPE, EXTENDED_DISMAX_QUERY_PARSER);
                } else {
                    sq = new SolrQuery(kt.getQuery());
                }

                sq.setFacet(true);
                sq.setFacetMinCount(1);
                sq.addFacetField(CATEGORY_URL_NAME_INDEX);
                sq.setStart(index);
                sq.setRows(maxResults);
                sq.setParam("qf", NM_INDEX + "^10", TEXT_INDEX, TEXT_ENGLISH_INDEX, TEXT_ENGLISH_SPLITTING_INDEX + "^5", TEXT_ENGLISH_SPLITTING_TIGHT_INDEX + "^10");
                sq.setParam("pf", NM_INDEX + "^100", TEXT_INDEX + "^10", TEXT_ENGLISH_INDEX + "^5", TEXT_ENGLISH_SPLITTING_INDEX + "^10", TEXT_ENGLISH_SPLITTING_TIGHT_INDEX + "^20");
                sq.setGetFieldStatistics(true);
                sq.setIncludeScore(true);

                // only for testing!
//                sq.setShowDebugInfo(true);

                if (log.isDebugEnabled()) {
                    log.debug("Querying solr: " + sq.getQuery());
                }

                // here's the response back from Solr
                QueryResponse response = itemCoreSolrServer.query(sq, SolrRequest.METHOD.POST);
                result.setQueryResponse(response);

            } catch (SolrServerException e) {
                throw new SearchException(e.getMessage(), e);
            }
        }

        return result;
    }

    @Override
    public GroupedSearchResult searchForItemsInStore(ItemsInStoreSearchQuery query) throws SearchException {
        return (GroupedSearchResult) doCommonSearchForItemsInStore(query);
    }

    @Override
    public SearchResult searchForCategoryItemsInStore(ItemsInStoreSearchQuery query) throws SearchException {
        return (SearchResult) doCommonSearchForItemsInStore(query);
    }

    private AbstractSearchResult doCommonSearchForItemsInStore(ItemsInStoreSearchQuery query) throws SearchException {
        AbstractSearchResult result = null;
        Integer index = 0;
        Integer maxResults = 12;
        int page;

        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {

            if (query == null) {
                throw new SearchException("ItemsInStoreSearchQuery cannot be null");
            }
            if (!StringUtils.isNotBlank(query.getUrlName())) {
                throw new SearchException("query url name cannot be null");
            }
            if (query.getPage() != null && query.getSize() != null) {
                index = query.getPage() * query.getSize();
                maxResults = query.getSize();
            }

            try {

                KeywordTerms kt = new KeywordTerms();
                List<String> stores = new ArrayList<String>();
                stores.add(query.getUrlName());
                kt.addSearchCondition(STORE_URL_NAME_INDEX, stores, SearchConditionType.AND, false);

                if (StringUtils.isNotBlank(query.getCat())) {
                    result = new SearchResult(query.getPage(), query.getSize());

                    List<String> categories = new ArrayList<String>(1);
                    categories.add(query.getCat());
                    kt.addSearchCondition(CATEGORY_URL_NAME_INDEX, categories, SearchConditionType.AND, false);
                } else {
                    result = new GroupedSearchResult(query.getPage(), query.getSize());
                }

                SolrQuery sq = new SolrQuery(kt.getQuery());

                // we always want to facet by category to get aggregate values
                sq.setFacet(true);
                sq.setFacetMinCount(1);
                sq.addFacetField(CATEGORY_URL_NAME_INDEX);

                // if the category is included we don't want to group,
                // we just want to retrieve a paginated view of the category
                if (StringUtils.isNotBlank(query.getCat())) {
                    sq.setStart(index);
                    sq.setRows(maxResults);
                } else {
                    // we always want to group by category
                    sq.setParam("group", true);
                    sq.setParam("group.field", CATEGORY_URL_NAME_INDEX);
                    sq.setParam("group.offset", index.toString());
                    sq.setParam("group.limit", maxResults.toString());

                    // the number of groups to return
                    sq.setParam("rows", "20");
                }

                sq.setGetFieldStatistics(true);
                sq.setIncludeScore(true);

                if (log.isDebugEnabled()) {
                    log.debug("Querying solr: " + sq.getQuery());
                }

                // here's the response back from Solr
                QueryResponse response = itemCoreSolrServer.query(sq, SolrRequest.METHOD.POST);
                result.setQueryResponse(response);

            } catch (SolrServerException e) {
                throw new SearchException(e.getMessage(), e);
            }
        }

        return result;
    }

    @Override
    public GroupedSearchResult searchForItemsForOwner(ItemsForOwnerSearchQuery query) throws SearchException {
        return (GroupedSearchResult) doCommonSearchForItemsForOwner(query);
    }

    @Override
    public SearchResult searchForCategoryItemsForOwner(ItemsForOwnerSearchQuery query) throws SearchException {
        return (SearchResult) doCommonSearchForItemsForOwner(query);
    }

    /**
     * This solr search query will most likely return several results as it is grouping on a facet field(s)
     *
     * @param query query
     * @return Name value aggregates
     * @throws SearchException
     */
    @Override
    public List<NameValueAggregate> searchForFacetAggregates(FacetSearchQuery query) throws SearchException {
        List<NameValueAggregate> result = null;

        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {

            if (query == null) {
                throw new SearchException("FacetSearchQuery cannot be null");
            }
            if ((query.getFacetFields() == null || query.getFacetFields().isEmpty()) &&
                    (query.getFacetQueries() == null || query.getFacetQueries().isEmpty())) {
                throw new SearchException("Facet fields and queries cannot be null");
            }

            try {

                SolrQuery sq = new SolrQuery(query.getQueryString());

                // we always want to facet by category to get aggregate values
                sq.setFacet(true);
                sq.setFacetMinCount(1);
                sq.setRows(0);

                // add facet fields
                if (query.getFacetFields() != null && !query.getFacetFields().isEmpty()) {
                    for (String facet : query.getFacetFields()) {
                        sq.addFacetField(facet);
                    }
                }

                // add facet queries
                if (query.getFacetQueries() != null && !query.getFacetQueries().isEmpty()) {
                    for (String facet : query.getFacetQueries()) {
                        sq.addFacetQuery(facet);
                    }
                }

                if (log.isDebugEnabled()) {
                    log.debug("Querying solr: " + sq.getQuery());
                }

                // here's the response back from Solr
                QueryResponse qr = itemCoreSolrServer.query(sq, SolrRequest.METHOD.POST);

                if (qr != null) {
                    result = new ArrayList<NameValueAggregate>();

                    // these are values from the facet field
                    if (query.getFacetFields() != null && !query.getFacetFields().isEmpty()) {
                        for (String facet : query.getFacetFields()) {
                            FacetField ff = qr.getFacetField(facet);

                            if (ff != null && ff.getValueCount() > 0) {
                                // here are the solr results
                                for (FacetField.Count count : ff.getValues()) {
                                    result.add(new NameValueAggregate(count.getName(), count.getCount()));
                                }
                            }
                        }
                    }

                    // these are values from the facet query
                    if (query.getFacetQueries() != null && !query.getFacetQueries().isEmpty()) {
                        for (String facet : query.getFacetQueries()) {
                            Integer value = qr.getFacetQuery().get(facet);
                            if (!StringUtils.isEmpty(facet) ) {
                               result.add(new NameValueAggregate(facet, value==null?0:value.longValue()));
                            }
                        }
                    }
                }
            } catch (SolrServerException e) {
                throw new SearchException(e.getMessage(), e);
            }
        }

        return result;
    }

    private AbstractSearchResult doCommonSearchForItemsForOwner(ItemsForOwnerSearchQuery query) throws SearchException {
        AbstractSearchResult result = null;
        Integer index = 0;
        Integer maxResults = 12;

        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {

            if (query == null) {
                throw new SearchException("ItemsInStoreSearchQuery cannot be null");
            }
            if (!StringUtils.isNotBlank(query.getUrlName())) {
                throw new SearchException("query url name cannot be null");
            }
            if (query.getPage() != null && query.getSize() != null) {
                index = query.getPage() * query.getSize();
                maxResults = query.getSize();
            }

            try {

                KeywordTerms kt = new KeywordTerms();
                List<String> owner = new ArrayList<String>(1);
                owner.add(query.getUrlName());
                kt.addSearchCondition(OWNER_URL_NAME_INDEX, owner, SearchConditionType.AND, false);

                if (StringUtils.isNotBlank(query.getCat())) {
                    result = new SearchResult(query.getPage(), query.getSize());

                    List<String> categories = new ArrayList<String>(1);
                    categories.add(query.getCat());
                    kt.addSearchCondition(CATEGORY_URL_NAME_INDEX, categories, SearchConditionType.AND, false);
                } else {
                    result = new GroupedSearchResult(query.getPage(), query.getSize());
                }

                SolrQuery sq = new SolrQuery(kt.getQuery());

                // we always want to group by category
                sq.setFacet(true);
                sq.setFacetMinCount(1);
                sq.addFacetField(CATEGORY_URL_NAME_INDEX);

                // if the category is included we don't want to group,
                // we just want to retrieve a paginated view of the category
                if (StringUtils.isNotBlank(query.getCat())) {
                    sq.setStart(index);
                    sq.setRows(maxResults);
                } else {
                    // we always want to group by category
                    sq.setParam("group", true);
                    sq.setParam("group.field", CATEGORY_URL_NAME_INDEX);
                    sq.setParam("group.offset", index.toString());
                    sq.setParam("group.limit", maxResults.toString());

                    // the number of groups to return
                    sq.setParam("rows", "20");
                }

                sq.setGetFieldStatistics(true);
                sq.setIncludeScore(true);

                if (log.isDebugEnabled()) {
                    log.debug("Querying solr: " + sq.getQuery());
                }

                // here's the response back from Solr
                QueryResponse response = itemCoreSolrServer.query(sq, SolrRequest.METHOD.POST);
                result.setQueryResponse(response);

            } catch (SolrServerException e) {
                throw new SearchException(e.getMessage(), e);
            }
        }

        return result;
    }

    /**
     * Adds an item to the Solr itemCoreSolrServer
     *
     * @param item item
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void indexItem(Item item) throws SearchException {
        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {
            try {
                itemCoreSolrServer.addBean(new ItemSolrDocument(item), 1);
            } catch (SolrServerException e) {
                throw new SearchException(e.getMessage(), e);
            } catch (IOException e) {
                throw new SearchException(e.getMessage(), e);
            }
        }
    }

    /**
     * Adds a list of items to the Solr itemCoreSolrServer
     *
     * @param items items
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void indexItems(List<Item> items) throws SearchException {
        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {
            try {
                List<ItemSolrDocument> list = new ArrayList<ItemSolrDocument>(items.size());

                for (Item item : items) {
                    list.add(new ItemSolrDocument(item));
                }

                itemCoreSolrServer.addBeans(list, 1);
            } catch (SolrServerException e) {
                throw new SearchException(e.getMessage(), e);
            } catch (IOException e) {
                throw new SearchException(e.getMessage(), e);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void deleteItemsById(List<String> ids) throws SearchException {
        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {

            try {
                itemCoreSolrServer.deleteById(ids);
                itemCoreSolrServer.commit();
            } catch (SolrServerException e) {
                log.error(e.getMessage(), e);
                throw new SearchException(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SearchException(e.getMessage(), e);
            }
        }

    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void deleteItemsByQuery(String query) throws SearchException {
        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {

            try {
                itemCoreSolrServer.deleteByQuery(query);
                itemCoreSolrServer.commit();
            } catch (SolrServerException e) {
                log.error(e.getMessage(), e);
                throw new SearchException(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new SearchException(e.getMessage(), e);
            }
        }
    }

    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public void deleteAll() throws SearchException {
        if (!isItemCoreSolrServerAvailable()) {
            if (log.isErrorEnabled()) {
                log.error(SOLR_SERVER_ERROR);
            }
        } else {
            try {
                itemCoreSolrServer.deleteByQuery("*:*");
                itemCoreSolrServer.commit();
            } catch (SolrServerException e) {
                throw new SearchException(e.getMessage(), e);
            } catch (IOException e) {
                throw new SearchException(e.getMessage(), e);
            }
        }
    }
}
