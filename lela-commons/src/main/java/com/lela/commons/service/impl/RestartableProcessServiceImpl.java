/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
package com.lela.commons.service.impl;

import com.lela.commons.repository.RestartableProcessRecordRepository;
import com.lela.commons.service.RestartableProcessService;
import com.lela.domain.document.RestartableProcessRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: Chris Tallent
 * Date: 9/24/12
 * Time: 3:03 PM
 */
@Service("restartableProcessService")
public class RestartableProcessServiceImpl implements RestartableProcessService {

    private final RestartableProcessRecordRepository restartableProcessRecordRepository;

    @Autowired
    public RestartableProcessServiceImpl(RestartableProcessRecordRepository restartableProcessRecordRepository) {
        this.restartableProcessRecordRepository = restartableProcessRecordRepository;
    }

    @Override
    public Set<String> findAllKeys(String process) {
        HashSet<String> result = new HashSet<String>();

        List<RestartableProcessRecord> records = restartableProcessRecordRepository.findAllWithKey(process);
        if (records != null && !records.isEmpty()) {
            for (RestartableProcessRecord record : records) {
                result.add(record.getKy());
            }
        }

        return result;
    }

    @Override
    public RestartableProcessRecord trackKey(String process, String key) {
        RestartableProcessRecord record = new RestartableProcessRecord(process, key);
        return restartableProcessRecordRepository.save(record);
    }
}
