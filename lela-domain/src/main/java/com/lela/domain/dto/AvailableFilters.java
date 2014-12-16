package com.lela.domain.dto;

import com.lela.domain.document.FunctionalFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 1/20/12
 * Time: 9:36 AM
 */
public class AvailableFilters {
    
    private Map<String, AvailableFilterOptions> filters = new HashMap<String, AvailableFilterOptions>();

    public void addTrackedFilter(FunctionalFilter filter) {
        filters.put(filter.getKy(), new AvailableFilterOptions(filter.getKy(), filter.getTp()));
    }
    
    public void addAvailableOption(FunctionalFilter filter, String optionKey) {
        filters.get(filter.getKy()).addAvailable(optionKey);
    }
    
    public List<AvailableFilterOptions> getFilters() {
        return new ArrayList<AvailableFilterOptions>(filters.values());
    }
}
