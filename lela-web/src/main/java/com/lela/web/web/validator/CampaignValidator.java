/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.lela.commons.service.CampaignService;
import com.lela.domain.document.Campaign;

@Component
public class CampaignValidator implements Validator {
    private final CampaignService campaignService;

    @Autowired
    public CampaignValidator(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    public boolean supports(Class<?> clazz) {
        return Campaign.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Campaign campaign = (Campaign) target;

        if (!errors.hasErrors()) {
            // check for rlnm availability
            Campaign tmp = campaignService.findCampaignByUrlName(campaign.getRlnm());

            if (tmp != null) {
                if (StringUtils.equals(tmp.getRlnm(), campaign.getRlnm()) && !tmp.getId().equals(campaign.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.campaign.url.name");
                }
            }
            int total = 0;
            if (!StringUtils.isEmpty(campaign.getVwnm())  ) {
            	total += 1;
            }
            if (!StringUtils.isEmpty(campaign.getRdrctrl())  ) {
            	total += 1;
            }
            if (!StringUtils.isEmpty(campaign.getSttccntnt())  ) {
            	total += 1;
            }
            
            if (total >= 2) {
            	errors.rejectValue("rdrctrl", "campaign.destination.error");
            }            
        }
    }
}