/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.store;

import com.lela.domain.dto.Letter;
import com.lela.util.utilities.string.StringUtility;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 4/27/12
 * Time: 12:50 AM
 * Responsibility:
 */
public class StoresSearchResult implements Serializable {
    private static final long serialVersionUID = 7632630126918819522L;

    private List<StoreAggregate> stores;
    private List<Letter> alphabet;

    public StoresSearchResult(List<StoreAggregate> stores) {
        this.stores = stores;

        createAlphabet(stores);
    }

    /**
     * This constructor is being used when we want to filter stores on a letter
     *
     * @param ssr            ssr
     * @param filterOnLetter filterOnLetter
     */
    public StoresSearchResult(StoresSearchResult ssr, String filterOnLetter) {
        List<StoreAggregate> storeAggregates = null;

        if (ssr.getStores() != null && !ssr.getStores().isEmpty()) {
            for (StoreAggregate sa : ssr.getStores()) {
                if (sa.getStore() != null && StringUtils.startsWithIgnoreCase(sa.getStore().getNm(), filterOnLetter)) {
                    if (storeAggregates == null) {
                        storeAggregates = new ArrayList<StoreAggregate>();
                    }

                    storeAggregates.add(sa);
                }
            }

        }

        this.stores = storeAggregates;

        createAlphabet(ssr.getStores());
    }

    public List<StoreAggregate> getStores() {
        return stores;
    }

    public List<Letter> getAlphabet() {
        return alphabet;
    }

    private void createAlphabet(List<StoreAggregate> stores) {
        String[] alphabet = StringUtility.getAlphabet();
        List<Letter> result = new ArrayList<Letter>(alphabet.length);
        Map<String, String> letterMap = null;

        // create alphabet based on stores
        if (stores != null && !stores.isEmpty()) {

            if (!stores.isEmpty()) {

                letterMap = new HashMap<String, String>();

                for (StoreAggregate store : stores) {
                    letterMap.put(store.getRlnm().substring(0, 1).toLowerCase(), store.getRlnm().substring(0, 1).toLowerCase());
                }

                if (!letterMap.isEmpty()) {

                }
            }
        }

        for (String letter : StringUtility.getAlphabet()) {
            if (letterMap != null && !letterMap.isEmpty()) {
                if (letterMap.containsKey(letter)) {
                    result.add(new Letter(letter, true));
                } else {
                    result.add(new Letter(letter, false));
                }
            } else {
                result.add(new Letter(letter, false));
            }
        }

        this.alphabet = result;
    }
}
