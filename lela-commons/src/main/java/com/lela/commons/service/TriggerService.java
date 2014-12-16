package com.lela.commons.service;

import org.springframework.data.domain.Page;

import com.lela.domain.document.CronTrigger;

public interface TriggerService {
	Page<CronTrigger> findTriggers(Integer page, Integer size);
    CronTrigger findTriggerByUrlName(String urlName);
    CronTrigger saveTrigger(CronTrigger trigger);
    CronTrigger removeTrigger(String rlnm);
}
