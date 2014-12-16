/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service;

import com.lela.domain.document.Campaign;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 6/4/12
 * Time: 4:44 PM
 * Responsibility:
 */
public interface CampaignService {
    Campaign findActiveCampaignByUrlName(String urlName);
    List<Campaign> saveCampaigns(List<Campaign> list);
    Campaign saveCampaign(Campaign campaign);
    Campaign findCampaignByUrlName(String urlName);
    Page<Campaign> findCampaigns(Integer page, Integer size);
    Campaign removeCampaign(String urlName);
    Long findCampaignCount();
    List<Campaign> paginateThroughAllCampaigns(Integer maxResults);
    List<Campaign> findActiveCampaigns();
}
