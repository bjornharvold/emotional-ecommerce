package com.lela.commons.service;

import com.lela.domain.document.RestartableProcessRecord;

import java.util.Set;

/**
 * User: Chris Tallent
 * Date: 9/24/12
 * Time: 3:03 PM
 */
public interface RestartableProcessService {
    Set<String> findAllKeys(String process);
    RestartableProcessRecord trackKey(String process, String key);
}
