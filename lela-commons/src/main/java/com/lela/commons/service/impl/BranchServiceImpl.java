/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import com.lela.domain.ApplicationConstants;
import com.lela.commons.repository.BranchRepository;
import com.lela.commons.service.BranchService;
import com.lela.commons.service.PostalCodeService;
import com.lela.commons.service.StoreService;
import com.lela.domain.document.Branch;
import com.lela.domain.document.PostalCode;
import com.lela.domain.document.Store;
import com.lela.domain.dto.BranchSearchResult;
import com.lela.domain.dto.LocationQuery;
import com.lela.domain.dto.store.BranchDistance;
import com.lela.domain.dto.store.LocalStoreAggregate;
import com.lela.domain.dto.store.LocalStoresSearchResult;
import com.lela.domain.dto.store.StoreAggregate;
import com.lela.domain.dto.store.StoreAggregateQuery;
import com.lela.domain.dto.store.StoresSearchResult;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Bjorn Harvold
 * Date: 5/9/12
 * Time: 6:45 PM
 * Responsibility:
 */
@Service("branchService")
public class BranchServiceImpl implements BranchService {
    private final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    /**
     * Field description
     */
    private final BranchRepository branchRepository;

    private final PostalCodeService postalCodeService;

    private final StoreService storeService;

    private final LookupService lookupService;

    private final LookupService lookupServiceIpv6;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository,
                             PostalCodeService postalCodeService,
                             StoreService storeService,
                             @Qualifier("geoipLookupServiceIpv4") LookupService lookupService,
                             @Qualifier("geoipLookupServiceIpv6") LookupService lookupServiceIpv6) {
        this.branchRepository = branchRepository;
        this.postalCodeService = postalCodeService;
        this.storeService = storeService;
        this.lookupService = lookupService;
        this.lookupServiceIpv6 = lookupServiceIpv6;
    }

    /**
     * Method description
     *
     * @param urlName urlName
     * @return Return value
     */
    @Override
    public Branch findBranchByUrlName(String urlName) {
        return branchRepository.findByUrlName(urlName);
    }

    /**
     * Method description
     *
     * @param affiliateUrlName affiliateUrlName
     * @return Return value
     */
    @Override
    public List<Branch> findBranchesByAffiliateUrlName(String affiliateUrlName) {
        return branchRepository.findByAffiliateUrlName(affiliateUrlName);
    }

    /**
     * Method description
     *
     * @param branchCode branchCode
     * @return Return value
     */
    @Override
    public Branch findBranchByLocalCode(String branchCode) {
        return branchRepository.findByLocalCode(branchCode);
    }

    public List<Branch> findBranchesByMerchantId(String merchantId) {
        return branchRepository.findByMerchantId(merchantId);
    }

    /**
     * Method description
     *
     * @param longitude     longitude
     * @param latitude      latitude
     * @param radiusInMiles radiusInMiles
     * @param merchantIds   merchantIds
     * @return Return value
     */
    @Override
    public List<BranchSearchResult> findNearest(Float longitude, Float latitude, Float radiusInMiles,
                                                Set<String> merchantIds) {
        return branchRepository.findNearest(longitude, latitude, radiusInMiles, merchantIds);
    }

    /**
     * Method description
     *
     * @param longitude     longitude
     * @param latitude      latitude
     * @param radiusInMiles radiusInMiles
     * @return Return value
     */
    @Override
    public List<BranchDistance> findNearest(Float longitude, Float latitude, Float radiusInMiles) {
        return branchRepository.findNearest(longitude, latitude, radiusInMiles);
    }

    /**
     * Method description
     *
     * @param rlnm branch
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public Branch removeBranch(String rlnm) {
        Branch branch = branchRepository.findByUrlName(rlnm);

        if (branch != null) {
            branchRepository.delete(branch);
        }

        return branch;
    }

    /**
     * Method description
     *
     * @param branch branch
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST', 'RIGHT_LOGIN_ONBOARD')")
    @Override
    public Branch saveBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    /**
     * Method description
     *
     * @param list list
     * @return Return value
     */
    @PreAuthorize("hasAnyRole('RIGHT_CONTENT_INGEST')")
    @Override
    public List<Branch> saveBranches(List<Branch> list) {
        return (List<Branch>) branchRepository.save(list);
    }

    @Override
    public LocalStoresSearchResult findLocalStoreAggregateDetails(LocationQuery query) {

        LocalStoresSearchResult result = new LocalStoresSearchResult();

        // Get the search values from the query
        // Default will be to search by latitude and longitude provided
        Float longitude = query.getLongitude();
        Float latitude = query.getLatitude();
        Float radius = query.getRadius();

        log.debug("findLocalStoreAggregateDetails with query - lon: " + longitude + ", lat: " + latitude + ", radius: " + radius);

        if (radius == null) {
            radius = ApplicationConstants.RADIUS_STEPS[0];
        }

        // Search by Zip or IP -> Zip if lat/long not provided
        if ((longitude == null || latitude == null) && StringUtils.isNotBlank(query.getZipcode())) {

            if (log.isDebugEnabled()) {
                log.debug("findLocalStoreAggregateDetails search location for Zipcode: " + query.getZipcode());
            }

            PostalCode postalCode = postalCodeService.findPostalCodeByCode(query.getZipcode());

            if (postalCode != null) {
                longitude = postalCode.getLongitude();
                latitude = postalCode.getLatitude();
                result.setSearchedForZipcode(query.getZipcode());

                log.debug("findLocalStoreAggregateDetails substitute zipcode params - lon: " + longitude + ", lat: " + latitude + ", radius: " + radius);
            }
        }

        // Search by IP -> Geo mapping
        // If no zip code was found
        if ((longitude == null || latitude == null) && StringUtils.isNotBlank(query.getIpAddress())) {

            if (log.isDebugEnabled()) {
                log.debug("findLocalStoreAggregateDetails search location for IP: " + query.getIpAddress());
            }

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

                if (log.isDebugEnabled()) {
                    log.debug("findLocalStoreAggregateDetails substitute Geo IP params - lon: " + longitude + ", lat: " + latitude + ", radius: " + radius);
                }
            } else {
                if (log.isWarnEnabled()) {
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

            // Perform search for branches
            List<BranchDistance> branchDistances = findNearest(longitude, latitude, radius);

            // process all branches and enrich with online store and their aggregates
            result.setStores(processBrandDistances(branchDistances));
        } else if (StringUtils.isNotBlank(query.getCity())) {
            List<Branch> branches = branchRepository.findByCity(query.getCity());
            List<BranchDistance> branchDistances = null;

            // need to enrich
            if (branches != null && !branches.isEmpty()) {
                branchDistances = new ArrayList<BranchDistance>(branches.size());

                for (Branch branch : branches) {
                    branchDistances.add(new BranchDistance(branch, 0d));
                }

                // process all branches and enrich with online store and their aggregates
                result.setStores(processBrandDistances(branchDistances));
            }
        }

        return result;
    }

    private List<LocalStoreAggregate> processBrandDistances(List<BranchDistance> branchDistances) {
        List<LocalStoreAggregate> result = null;

        // try to connect the branch with a store -
        if (branchDistances != null && !branchDistances.isEmpty()) {
            List<String> merchantIds = new ArrayList<String>();

            for (BranchDistance bd : branchDistances) {
                if (StringUtils.isNotBlank(bd.getBranch().getMrchntd())) {
                    merchantIds.add(bd.getBranch().getMrchntd());
                }
            }

            // grab some light weight objects
            List<Store> stores = storeService.findStoreUrlNamesByMerchantIds(merchantIds);

            // NOTICE: this method returns no branches if the branch doesn't have a
            // corresponding online store
            if (stores != null && !stores.isEmpty()) {
                List<String> storeUrlNames = new ArrayList<String>(stores.size());

                for (Store store : stores) {
                    storeUrlNames.add(store.getRlnm());
                }

                StoreAggregateQuery saq = new StoreAggregateQuery();
                saq.setFacetQueries(storeUrlNames);

                // we want to drill down into each store and get per category item count
                StoresSearchResult ssr = storeService.findStoreAggregateDetails(saq);

                // at this point we have the online stores and branches
                // time to match them up
                if (ssr != null && ssr.getStores() != null && !ssr.getStores().isEmpty()) {
                    result = new ArrayList<LocalStoreAggregate>();

                    for (StoreAggregate asa : ssr.getStores()) {

                        // let's find the branch that belongs with this online store
                        for (BranchDistance bd : branchDistances) {
                            if (asa.getStore() != null && bd.getBranch() != null && StringUtils.equals(asa.getStore().getMrchntd(), bd.getBranch().getMrchntd())) {
                                LocalStoreAggregate lsa = new LocalStoreAggregate(asa);
                                lsa.setBranchDistance(bd);

                                result.add(lsa);
                                break;
                            }
                        }


                    }
                }
            }
        }

        return result;
    }

}
