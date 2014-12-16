package com.lela.commons.service;

import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 10/19/12
 * Time: 2:27 PM
 * Responsibility:
 */
public interface VelocityService {
    String mergeTemplateIntoString(String templateLocation, Map model);
}
