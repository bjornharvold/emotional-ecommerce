package com.lela.domain.dto;

import com.lela.domain.enums.FunctionalFilterType;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Chris Tallent
 * Date: 1/20/12
 * Time: 9:35 AM
 */
public class AvailableFilterOptions {

    private String key;
    private FunctionalFilterType type;
    private Set<String> available = new HashSet<String>();

    public AvailableFilterOptions() {
        
    }
    
    public AvailableFilterOptions(String key, FunctionalFilterType type) {
        this.key = key;
        this.type = type;
    }
    
    public void addAvailable(String optionKey) {
        available.add(optionKey);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FunctionalFilterType getType() {
        return type;
    }

    public void setType(FunctionalFilterType type) {
        this.type = type;
    }

    public Set<String> getAvailable() {
        return available;
    }

    public void setAvailable(Set<String> available) {
        this.available = available;
    }
}
