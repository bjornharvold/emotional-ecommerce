/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.commons.web.ResourceNotFoundException;
import com.lela.domain.ApplicationConstants;
import com.lela.commons.comparator.AvailableInStoreByPriceAndNameComparator;
import com.lela.commons.remote.MerchantClient;
import com.lela.commons.remote.impl.AmazonMerchantClient;
import com.lela.commons.service.BranchService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.MerchantService;
import com.lela.commons.service.OfferService;
import com.lela.commons.service.PostalCodeService;
import com.lela.commons.service.StoreService;
import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.document.AvailableInStore;
import com.lela.domain.document.Branch;
import com.lela.domain.document.Deal;
import com.lela.domain.document.Item;
import com.lela.domain.document.Offer;
import com.lela.domain.document.PostalCode;
import com.lela.domain.document.Redirect;
import com.lela.domain.document.Store;
import com.lela.domain.document.User;
import com.lela.domain.dto.BranchSearchResult;
import com.lela.domain.dto.BranchSearchResults;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.enums.MerchantType;
import com.lela.domain.enums.RedirectType;
import com.lela.util.utilities.number.NumberUtils;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.NoSuchMessageException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 9/13/11
 * Time: 8:21 PM
 * Responsibility: All functionality related to buying an item from an affiliate site
 */
public class MerchantServiceImpl implements MerchantService {
    private final static Logger log = LoggerFactory.getLogger(MerchantServiceImpl.class);

    private final Map<String, MerchantClient> merchantClients;
    private final PostalCodeService postalCodeService;
    private final StoreService storeService;
    private final BranchService branchService;
    private final LookupService lookupService;
    private final LookupService lookupServiceIpv6;
    private final ItemService itemService;
    private final OfferService offerService;

    @Value("${merchant.cache.timeout.interval.in.seconds:604800}")
    private Long cacheTimeoutInterval;

    public MerchantServiceImpl(Map<String, MerchantClient> merchantClients,
                               PostalCodeService postalCodeService,
                               StoreService storeService,
                               BranchService branchService,
                               LookupService lookupService,
                               LookupService lookupServiceIpv6, ItemService itemService, OfferService offerService) {
        this.merchantClients = merchantClients;
        this.offerService = offerService;
        this.postalCodeService = postalCodeService;
        this.storeService = storeService;
        this.branchService = branchService;
        this.lookupService = lookupService;
        this.lookupServiceIpv6 = lookupServiceIpv6;
        this.itemService = itemService;
    }

    @Override
    public Redirect constructRedirect(String itemUrlName, String merchantId, ObjectId userId) {
        // Create a redirect request
        // Explicitly give this redirect an ID that will be used as part of the redirect url construction
        Redirect redirect = new Redirect();
        redirect.setId(new ObjectId());
        redirect.setRdrctdt(new Date());
        redirect.setTp(RedirectType.BUY_IT);

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("mrchntd", merchantId);
        attributes.put("rlnm", itemUrlName);
        redirect.setAttrs(attributes);

        // Defer the actual URL creation to the merchant client implementation
        Item item = itemService.findItemByUrlName(itemUrlName);
        String url = null;
        for (AvailableInStore store : item.getStrs()) {
            if (store.getMrchntd().equals(merchantId)) {
                // Get the purchase URL
                url = (String)store.getTms().get(0).getSubAttributes().get("PurchaseUrl");

                // Determine the merchant source... if the source is undefined, default
                // to the old db schema logic of Amazon vs Popshops
                String source = store.getMrchntsrc();
                if (source == null) {
                    if (merchantId.equals(AmazonMerchantClient.MERCHANT_ID)) {
                        source = MerchantType.AMZN.toString();
                    } else {
                        source = MerchantType.POPSHOPS.toString();
                    }
                }

                // Lookup MerchantClient by the merchant source (e.g. - Amazon, Google Affiliate Network, Commision Junction)
                // If there is a client implementation, use it
                // otherwise pass the redirect URL through unchanged
                MerchantClient client = merchantClients.get(source);
                if (client != null) {
                    url = client.constructTrackableUrl(url, userId, redirect, item);
                }

                break;
            }
        }

        // Set the url on the Redirect
        redirect.setRl(url);

        return redirect;
    }

    @Override
    public Redirect constructAddToCartRedirect(AffiliateAccount domainAffiliate, String itemUrlName, String merchantId, ObjectId userId) {
        if (domainAffiliate == null) {
            throw new ResourceNotFoundException("No domain affiliate found for Add to Cart");
        }

        if (!domainAffiliate.getShwcrt()) {
            throw new ResourceNotFoundException("Add to cart not available for subdomain");
        }

        // Create a redirect request
        // Explicitly give this redirect an ID that will be used as part of the redirect url construction
        Redirect redirect = new Redirect();
        redirect.setId(new ObjectId());
        redirect.setRdrctdt(new Date());
        redirect.setTp(RedirectType.ADD_TO_CART);

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("mrchntd", merchantId);
        attributes.put("rlnm", itemUrlName);
        redirect.setAttrs(attributes);

        // Defer the actual URL creation to the merchant client implementation
        Item item = itemService.findItemByUrlName(itemUrlName);
        String url = null;
        for (AvailableInStore store : item.getStrs()) {
            if (store.getMrchntd().equals(merchantId)) {
                // Get the purchase URL
                url = (String)store.getTms().get(0).getSubAttributes().get("PurchaseUrl");
                break;
            }
        }

        // Set the url on the Redirect
        redirect.setRl(url);

        return redirect;
    }

    @Override
    public List<AvailableInStore> findOnlineStores(AbstractItem item) {

        List<AvailableInStore> result = null;

        if (item != null) {
            result = item.getStrs();
            if (result != null && !result.isEmpty()) {
                Collections.sort(result, new AvailableInStoreByPriceAndNameComparator());
            }
        }

        return result;
    }

    /**
     * Search precedence:
     * <p/>
     * 1. Lat/Lon/Radius if available
     * 2. Zipcode -> Lat/Long/Radius if available
     * 3. IP -> Geo if available
     * 4. If no search criteria, return empty list
     * <p/>
     * Rules
     * - Any merchant in online tab with local stores will have local stores displayed
     * - Any merchant without an affiliate relationship will be hidden
     * - Any local only merchant will have local stores displayed (until merchant dashboard implemented)
     * - Any merchant/store in the Lela list will be sorted to top
     * - Remaining stores should sort by proximity descending
     *
     * @param item  Item to be found in local stores
     * @param query Location query defining criteria for finding local stores
     * @return
     */
    @Override
    public BranchSearchResults findLocalStoresForItem(AbstractItem item,
                                                      LocationQuery query,
                                                      List<AvailableInStore> onlineStores) {

        BranchSearchResults result = new BranchSearchResults();
        List<BranchSearchResult> results = new ArrayList<BranchSearchResult>();

        // Get the search values from the query
        // Default will be to search by latitude and longitude provided
        Float longitude = query.getLongitude();
        Float latitude = query.getLatitude();
        Float radius = query.getRadius();

        log.debug("findLocalStoresForItem with query - lon: " + longitude + ", lat: " + latitude + ", radius: " + radius);

        if (radius == null) {
            radius = ApplicationConstants.RADIUS_STEPS[0];
        }

        // Search by Zip or IP -> Zip if lat/long not provided
        if (longitude == null || latitude == null) {

            // Search by Zip
            // If both lat and long were not provided
            if (StringUtils.isNotBlank(query.getZipcode())) {
                log.debug("findLocalStoresForItem search location for Zipcode: " + query.getZipcode());
                PostalCode postalCode = postalCodeService.findPostalCodeByCode(query.getZipcode());

                if (postalCode != null) {
                    longitude = postalCode.getLongitude();
                    latitude = postalCode.getLatitude();
                    result.setSearchedForZipcode(query.getZipcode());

                    log.debug("findLocalStoresForItem substitute zipcode params - lon: " + longitude + ", lat: " + latitude + ", radius: " + radius);
                }
            }
        }

        // Search by IP -> Geo mapping
        // If no zip code was found
        if (longitude == null || latitude == null) {

            // TODO IP -> ZIP Mapping
            if (StringUtils.isNotBlank(query.getIpAddress())) {
                log.debug("findLocalStoresForItem search location for IP: " + query.getIpAddress());

                Location location = null;
                if (query.getIpAddress().contains(":")) {
                    log.debug("IPv6 location query");
                    location = lookupServiceIpv6.getLocationV6(query.getIpAddress());
                } else {
                    log.debug("IPv4 location query");
                    location = lookupService.getLocation(query.getIpAddress());
                }

                if (location != null) {
                    longitude = location.longitude;
                    latitude = location.latitude;
                    result.setSearchedForZipcode(location.postalCode);
                    log.debug("findLocalStoresForItem substitute Geo IP params - lon: " + longitude + ", lat: " + latitude + ", radius: " + radius);
                } else {
                    log.warn("Could not determine latitude and longitude for local stores search from query - lon: " +
                            query.getLongitude() + ", lat: " + query.getLatitude() + ", radius: " + query.getRadius() +
                            ", zip: " + query.getZipcode());
                }
            }
        }

        // Run the actual Search by Lat/Long
        if (longitude != null && latitude != null) {

            result.setLongitude(longitude);
            result.setLatitude(latitude);

            // If there are no online search results, nothing should be shown
            if (onlineStores == null || onlineStores.isEmpty()) {
                result.setResults(results);
                return result;
            }

            // Filter the results by the online search results if any
            HashMap<String, Double> merchantIdToListPrice = new HashMap<String, Double>();
            if (!onlineStores.isEmpty()) {
                for (AvailableInStore store : onlineStores) {
                    Object listPrice = null;
                    if (store.getTms() != null && !store.getTms().isEmpty() && store.getTms().get(0).getSubAttributes() != null) {
                        listPrice = store.getTms().get(0).getSubAttributes().get("ListPrice");
                    }

                    merchantIdToListPrice.put(store.getMrchntd(), NumberUtils.safeDouble(listPrice));
                }
            }

            // Perform the search
            results = geoSearch(longitude, latitude, radius, merchantIdToListPrice.keySet());

            // If this is a logged in user, check to see if they have favorite stores
            // if so, those will sort to the top
            if (results != null && !results.isEmpty()) {

                // Store local lookup cache
                Map<String, Store> stores = new HashMap<String, Store>();

                // Geo results are already sorted by distance so just pull
                // favorites to the front of the list in sorted order
                // and pull stores with offers to the front after favorites
                ArrayList<BranchSearchResult> hasOffers = new ArrayList<BranchSearchResult>();
                ArrayList<BranchSearchResult> remaining = new ArrayList<BranchSearchResult>();
                for (BranchSearchResult searchResult : results) {

                    // Populate the store for the branch
                    Branch branch = searchResult.getBranch();
                    searchResult.setStore(lookupStore(stores, branch));

                    // Check for an offer for the search result
                    List<Offer> offers = offerService.findValidOffersByBranchUrlName(branch.getRlnm());
                    searchResult.setOffers(offers);

                    // Split into separate lists based on offers first then all other stores
                    if (offers != null && !offers.isEmpty()) {
                        hasOffers.add(searchResult);
                    } else {
                        remaining.add(searchResult);
                    }
                }

                hasOffers.addAll(remaining);

                results = hasOffers;
            }

            // Add the list price
            if (!merchantIdToListPrice.isEmpty()) {
                for (BranchSearchResult searchResult : results) {
                    searchResult.setListPrice(merchantIdToListPrice.get(searchResult.getBranch().getMrchntd()));
                }
            }
        }

        result.setResults(results);
        return result;
    }

    public Boolean hasStoreDataExpired(Item item) {
        Boolean hasExpired = false;
        Date now = new Date();
        Long cacheTimeoutIntervalInMs = cacheTimeoutInterval * 1000;
        for (AvailableInStore store : item.getStrs()) {
            if (store.getLdt().getTime() + cacheTimeoutIntervalInMs < now.getTime()) {
                hasExpired = true;
                break;
            }
        }

        return hasExpired;
    }

    /**
     * Lookup store in local cache on merchant id, or retrieve from referenceDataService
     */
    private Store lookupStore(Map<String, Store> stores, Branch branch) {
        Store store = stores.get(branch.getMrchntd());
        if (store == null) {
            store = storeService.findStoreByMerchantId(branch.getMrchntd());
            stores.put(branch.getMrchntd(), store);
        }
        return store;
    }

    /**
     * Method to attempt to back out distances if no results are returned
     */
    private List<BranchSearchResult> geoSearch(Float longitude, Float latitude, Float radius, Set<String> merchantIds) {

        List<BranchSearchResult> results = new ArrayList<BranchSearchResult>();

        results = branchService.findNearest(longitude, latitude, radius, merchantIds);
        if (results == null || results.isEmpty()) {

            // Determine if any of the default radius steps are greater than the given radius
            for (Float nextRadius : ApplicationConstants.RADIUS_STEPS) {
                if (nextRadius > radius) {
                    log.debug("No results within radius, step out to radius: " + nextRadius);
                    return geoSearch(longitude, latitude, nextRadius, merchantIds);
                }
            }
        }

        return results;
    }

    private void orderDeals(Item item, List<AvailableInStore> stores, User user) {
        if ((stores != null) && (stores.size() > 0)) {

            List<Deal> dealsWithCategory = new ArrayList<Deal>();
            //List<Deal> dealsWithGroupCategory = new ArrayList<Deal>();
            List<Deal> dealsWithShipping = new ArrayList<Deal>();
            List<Deal> dealsWithOther = new ArrayList<Deal>();

            String categoryName = null;
            try {
                categoryName = item.getCtgry().getNm();
            } catch (NoSuchMessageException e) {
                log.error(e.getMessage());
            }

            for (AvailableInStore store : stores) {
                dealsWithCategory.clear();
                //dealsWithGroupCategory.clear();
                dealsWithShipping.clear();
                dealsWithOther.clear();
                if ((store.getDls() != null) && (store.getDls().size() > 0)) {
                    for (Deal deal : store.getDls()) {
                        if (deal.getNm() != null) {
                            if (deal.getNm().matches("(?i).*" + categoryName  + ".*")) {
                                dealsWithCategory.add(deal);
                            //} else if (deal.getNm().matches("(?i).*.*")) {
                                //dealsWithGroupCategory.add(deal);
                            } else if (deal.getNm().matches("(?i).*free shipping.*")) {
                                dealsWithShipping.add(deal);
                            } else {
                                dealsWithOther.add(deal);
                            }
                        }
                    }

                    store.getDls().clear();
                    store.getDls().addAll(dealsWithCategory);
                    store.getDls().addAll(dealsWithShipping);
                    store.getDls().addAll(dealsWithOther);
                }
            }
        }
    }
}
