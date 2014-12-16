/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.lela.commons.repository.CampaignRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.CampaignService;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.Campaign;
import com.lela.domain.enums.CacheType;
import com.lela.util.utilities.number.NumberUtils;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 4:44 PM
 * Responsibility:
 */
@Service("campaignService")
public class CampaignServiceImpl extends AbstractCacheableService implements CampaignService {

	private static final Logger log = LoggerFactory.getLogger(CampaignServiceImpl.class);
	
	private final static String CAMPAIGN_FILE_BUCKET = "lela-affiliate-images";
    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignServiceImpl(CacheService cacheService, 
    		CampaignRepository campaignRepository) {
        super(cacheService);
        this.campaignRepository = campaignRepository;
    }

    @Override
    public List<Campaign> findActiveCampaigns() {
        List<Campaign> result = new ArrayList<Campaign>();
        for (Campaign campaign : campaignRepository.findAll()) {
            if (campaign != null && campaign.isActiveAndCurrent()) {
                result.add(campaign);
            }
        }

        // No valid or active campaign
        return result;
    }

    /**
     * @param urlName urlName
     * @return
     */
    @Override
    public Campaign findActiveCampaignByUrlName(String urlName) {
        Campaign campaign = findCampaignByUrlName(urlName);
        if (campaign != null && campaign.isActiveAndCurrent()) {
            return campaign;
        }

        // No valid or active campaign
        return null;
    }

    /**
     * @param campaign campaign
     *
     * @return Return value
     */
    @Override
    public Campaign saveCampaign(Campaign campaign) {
        Campaign result = campaignRepository.save(campaign);

        // Remove from cache
        removeFromCache(CacheType.CAMPAIGN, campaign.getRlnm());

        return result;
    }

    /**
     * @param list list
     *
     * @return Return value
     */
    @Override
    public List<Campaign> saveCampaigns(List<Campaign> list) {
        List<Campaign> result = (List<Campaign>) campaignRepository.save(list);

        // Remove from cache
        if (list != null && !list.isEmpty()) {
            List<String> cacheKeys = new ArrayList<String>(list.size());

            for (Campaign item : list) {
                cacheKeys.add(item.getRlnm());
            }

            removeFromCache(CacheType.CAMPAIGN, cacheKeys);
        }

        return result;
    }

    @Override
    public Campaign findCampaignByUrlName(String urlName) {
        Campaign result = null;
        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.CAMPAIGN_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof Campaign) {
            result = (Campaign) wrapper.get();
        } else {
            result = campaignRepository.findByUrlName(urlName);

            if (result != null) {
                putInCache(ApplicationConstants.CAMPAIGN_CACHE, urlName, result);
            }
        }

        return result;
    }

    @Override
    public Page<Campaign> findCampaigns(Integer page, Integer size) {
        return campaignRepository.findAll(new PageRequest(page, size));
    }

    @Override
    public Campaign removeCampaign(String rlnm) {
        Campaign campaign = campaignRepository.findByUrlName(rlnm);

        if (campaign != null) {
        	//Remove campaign
            campaignRepository.delete(campaign);

            // Remove from cache
            removeFromCache(CacheType.CAMPAIGN, campaign.getRlnm());
        }

        return campaign;
    }

    @Override
    public Long findCampaignCount() {
        return campaignRepository.count();
    }

    /**
     * TODO an improvement to this would be to only return those fields that will be used
     * @param chunk chunk
     * @return
     */
    @Override
    public List<Campaign> paginateThroughAllCampaigns(Integer chunk) {
        List<Campaign> result = null;
        Long count;
        Integer iterations;
        count = findCampaignCount();

        if (count != null && count > 0) {
            result = new ArrayList<Campaign>(count.intValue());
            iterations = NumberUtils.calculateIterations(count, chunk.longValue());

            // load up items in a paginated fashion from mongo
            for (int i = 0; i < iterations; i++) {
                result.addAll(findCampaigns(i, chunk.intValue()).getContent());
            }
        }

        return result;
    }
    //http://lela-affiliate-images/ss-Photo on 8-4-12 at 5.50 PM.jpg
    private String trimFileName(String fullFileName){
    	if (fullFileName.indexOf(CAMPAIGN_FILE_BUCKET) != -1){
    		StringUtils.replace(fullFileName, CAMPAIGN_FILE_BUCKET, "");
    		StringUtils.replace(fullFileName, "/", "");
    	}
    	return fullFileName;
    }
}
