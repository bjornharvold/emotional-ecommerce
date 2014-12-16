/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.lela.commons.LelaException;
import com.lela.commons.repository.TriggerRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.TriggerService;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.document.CronTrigger;
import com.lela.domain.document.QCronTrigger;
import com.lela.domain.enums.CacheType;

@Service("triggerService")
public class TriggerServiceImpl extends AbstractCacheableService implements
		TriggerService {

	private TriggerRepository triggerRepository;
	
	@Autowired
	protected TriggerServiceImpl(CacheService cacheService, TriggerRepository triggerRepository) {
		super(cacheService);
		this.triggerRepository = triggerRepository;
	}

	@Override
	public Page<CronTrigger> findTriggers(Integer page, Integer size) {
		return triggerRepository.findAll(new PageRequest(page, size));
	}
	@Override
	public CronTrigger findTriggerByUrlName(String urlName) {
        CronTrigger result = null;

        Cache.ValueWrapper wrapper = retrieveFromCache(ApplicationConstants.TRIGGER_CACHE, urlName);

        if (wrapper != null && wrapper.get() != null && wrapper.get() instanceof CronTrigger) {
            result = (CronTrigger) wrapper.get();
        } else {
            result = triggerRepository.findOne(QCronTrigger.cronTrigger.rlnm.eq(urlName));

            if (result != null) {
                putInCache(ApplicationConstants.TRIGGER_CACHE, urlName, result);
            }
        }

        return result;		
	}

	@Override
	public CronTrigger saveTrigger(CronTrigger trigger) {
		CronTrigger t =  triggerRepository.save(trigger);
		removeFromCache(CacheType.JOB, t.getRlnm());
		return t;
	}

	@Override
	public CronTrigger removeTrigger(String rlnm) {
		CronTrigger t = this.findTriggerByUrlName(rlnm);
		if (t == null){
			throw new LelaException("No Trigger found with URL: " + rlnm);
		}
		triggerRepository.delete(t);
		removeFromCache(CacheType.JOB, t.getRlnm());
		return t;
	}

}
