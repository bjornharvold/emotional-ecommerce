/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.owner;

import com.lela.domain.dto.Letter;
import com.lela.domain.dto.store.StoreAggregate;
import com.lela.util.utilities.string.StringUtility;
import org.apache.commons.lang3.StringUtils;

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
public class OwnersSearchResult implements Serializable {
    private static final long serialVersionUID = 7632630126918819522L;

    private List<OwnerAggregate> owners;
    private List<Letter> alphabet;

    public OwnersSearchResult(List<OwnerAggregate> owners) {
        this.owners = owners;

        createAlphabet(owners);
    }

    public OwnersSearchResult(OwnersSearchResult ssr, String filterOnLetter) {
        List<OwnerAggregate> ownerAggregates = null;

        if (ssr.getOwners() != null && !ssr.getOwners().isEmpty()) {
            for (OwnerAggregate sa : ssr.getOwners()) {
                if (sa.getOwner() != null && StringUtils.startsWithIgnoreCase(sa.getOwner().getNm(), filterOnLetter)) {
                    if (ownerAggregates == null) {
                        ownerAggregates = new ArrayList<OwnerAggregate>();
                    }

                    ownerAggregates.add(sa);
                }
            }

        }

        this.owners = ownerAggregates;

        createAlphabet(ssr.getOwners());
    }

    public List<OwnerAggregate> getOwners() {
        return owners;
    }

    public void setOwners(List<OwnerAggregate> owners) {
        this.owners = owners;
    }

    public List<Letter> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<Letter> alphabet) {
        this.alphabet = alphabet;
    }

    private void createAlphabet(List<OwnerAggregate> owners) {
        String[] alphabet = StringUtility.getAlphabet();
        List<Letter> result = new ArrayList<Letter>(alphabet.length);
        Map<String, String> letterMap = null;

        // create alphabet based on stores
        if (owners != null && !owners.isEmpty()) {

            if (!owners.isEmpty()) {

                letterMap = new HashMap<String, String>();

                for (OwnerAggregate owner : owners) {
                    letterMap.put(owner.getRlnm().substring(0, 1).toLowerCase(), owner.getRlnm().substring(0, 1).toLowerCase());
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
