package com.lela.commons.service;

import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 5/29/12
 * Time: 8:14 PM
 * Responsibility:
 */
public interface ValidatorService {
    boolean isPayloadValid(String profile, Map<String, List<String>> map);
}
