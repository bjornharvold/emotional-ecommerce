/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.store;

import com.lela.domain.dto.Letter;
import com.lela.util.utilities.string.StringUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 5/10/12
 * Time: 5:05 PM
 * Responsibility:
 */
public class LocalStoresSearchResult implements Serializable {
    private static final long serialVersionUID = 940931567990144886L;

    private List<LocalStoreAggregate> stores;
    private List<Letter> alphabet;
    private String searchedForZipcode;
    private Float longitude;
    private Float latitude;

    public void setStores(List<LocalStoreAggregate> stores) {
        this.stores = stores;

        // create alphabet based on stores
        if (stores != null && !stores.isEmpty()) {
            // finally we can create the alphabet
            List<Letter> result = null;

            if (!stores.isEmpty()) {
                result = new ArrayList<Letter>();

                Map<String, String> letterMap = new HashMap<String, String>();
                for (LocalStoreAggregate store : stores) {
                    letterMap.put(store.getRlnm().substring(0, 1).toLowerCase(), store.getRlnm().substring(0, 1).toLowerCase());
                }

                if (!letterMap.isEmpty()) {
                    for (String letter : StringUtility.getAlphabet()) {
                        if (letterMap.containsKey(letter)) {
                            result.add(new Letter(letter, true));
                        } else {
                            result.add(new Letter(letter, false));
                        }
                    }
                }
            }

            this.alphabet = result;
        } else {
            this.alphabet = null;
        }
    }

    public List<LocalStoreAggregate> getStores() {
        return stores;
    }

    public String getSearchedForZipcode() {
        return searchedForZipcode;
    }

    public void setSearchedForZipcode(String searchedForZipcode) {
        this.searchedForZipcode = searchedForZipcode;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public List<Letter> getAlphabet() {
        return alphabet;
    }
}
