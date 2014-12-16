/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lela.commons.service.DistributedCacheEvictionService;
import com.lela.domain.dto.CacheEviction;
import com.lela.domain.dto.CacheEvictions;
import com.lela.domain.enums.CacheType;
import com.polarrose.amazon.sqs.SqsMessage;
import com.polarrose.amazon.sqs.SqsQueue;
import com.polarrose.amazon.sqs.SqsService;

/**
 * Created by Bjorn Harvold
 * Date: 11/23/11
 * Time: 3:45 AM
 * Responsibility:
 */
@Service("distributedCacheEvictionService")
public class DistributedCacheEvictionServiceImpl implements DistributedCacheEvictionService {
    private static final Logger log = LoggerFactory.getLogger(DistributedCacheEvictionServiceImpl.class);
    private static final Logger MESSAGE_LOG = LoggerFactory.getLogger("SQS_SENT");

    private static String hostName = "UNKNOWN";
    static {
    	try {
    		hostName = java.net.InetAddress.getLocalHost().getHostName();
    	} catch (Exception e){
    		//Ignore;
    	}
    }
    
    /**
     * Amazon SQS message lenth is 64k
     */
    private static final int MAX_LIST_SIZE = 50;

    /**
     * Amazon SQS Service
     */
    private final SqsService sqsService;

    /**
     * Amazon SQS queue
     */
    private final SqsQueue queue;

    /**
     * Jackson mapper for JSON serialization
     */
    private final ObjectMapper objectMapper;

    @Autowired
    public DistributedCacheEvictionServiceImpl(
                                               SqsService sqsService,
                                               SqsQueue queue,
                                               @Qualifier("queueObjectMapper") ObjectMapper objectMapper) {
        this.sqsService = sqsService;
        this.queue = queue;
        this.objectMapper = objectMapper;
    }

    /**
     * Send the cache eviction to the SQS queue
     */
    @Override
    public void evict(CacheType cacheType, String key) {

        // Eviction code is wrapped in try/catch to be failsafe to calling code
        try {
            if (key != null) {

                log.debug("Evict " + cacheType + ", key: " + key);
                List<CacheEviction> list = new ArrayList<CacheEviction>(1);
                list.add(new CacheEviction(cacheType, key));

                evict(list);
            } else {
                log.debug("No " + cacheType + " to evict");
            }
        } catch (Exception e) {
            log.error("Could not send cache evictions to SQS", e);
        }
    }

    /**
     * Send the cache eviction to the SQS queue
     */
    @Override
    public void evictAll(CacheType cacheType) {

        // Eviction code is wrapped in try/catch to be failsafe to calling code
        try {
            log.debug("Evict all from: " + cacheType);
            List<CacheEviction> list = new ArrayList<CacheEviction>(1);
            list.add(new CacheEviction(cacheType));
            evict(list);

        } catch (Exception e) {
            log.error("Could not send cache evictions to SQS", e);
        }
    }

    /**
     * Send the cache eviction to the SQS queue
     */
    @Override
    public void evict(CacheType cacheType, List<String> keys) {

        // Eviction code is wrapped in try/catch to be failsafe to calling code
        try {
            if (keys != null && !keys.isEmpty()) {

                log.debug("Evict " + cacheType + " count: " + keys.size());

                int count = 0;
                List<CacheEviction> list = new ArrayList<CacheEviction>();
                for (String key : keys) {
                    list.add(new CacheEviction(cacheType, key));

                    // SQS message body size is limited... send as chunks
                    if (++count >= MAX_LIST_SIZE) {
                        evict(list);

                        list.clear();
                        count = 0;
                    }
                }

                // Make sure that any remaining items are evicted
                if (list.size() > 0) {
                    evict(list);
                }
            } else {
                log.debug("No " + cacheType + " to evict");
            }
        } catch (Exception e) {
            log.error("Could not send cache evictions to SQS", e);
        }
    }

    /**
     * Submit the list of evictions to the SQS queue
     */
    private void evict(List<CacheEviction> list) {

        log.debug("Send evictions count: " + list.size());

        try {
            CacheEvictions cacheEvictions = new CacheEvictions();
            cacheEvictions.setList(list);

            String json = objectMapper.writeValueAsString(cacheEvictions);
            log.debug("Sending json from " + hostName + ": " + json);

            log.debug("Invoke SQS sendMessage on host: " + hostName);
            SqsMessage message = sqsService.sendMessage(queue, json);
            log.debug("Sent message: " + message);
            MESSAGE_LOG.info(json);
        } catch (IOException e) {
            log.error("Could not serialize cache evictions", e);
        }
    }
}
