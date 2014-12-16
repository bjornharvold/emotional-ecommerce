/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import com.lela.domain.document.AbstractStore;
import com.lela.domain.document.Category;
import com.lela.domain.document.ListCard;
import com.lela.domain.document.ListCardProfile;
import com.lela.domain.document.Owner;
import com.lela.domain.dto.AbstractJSONPayload;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/2/12
 * Time: 9:08 PM
 * Responsibility:
 */
public class UserList extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 4522648457404840688L;

    private String nm;
    private String cd;
    private Page<ListCard> page;
    private List<ListCard> cards;
    private List<Category> categories;
    private List<Owner> owners = null;
    private List<AbstractStore> stores = null;
    private UserListQuery query;
    private ListCardProfile prfl;

    public UserList(UserListQuery query) {
        this.query = query;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public Page<ListCard> getPage() {
        return page;
    }

    public void setPage(Page<ListCard> page) {
        this.page = page;
    }

    public List<ListCard> getCards() {
        return cards;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public List<AbstractStore> getStores() {
        return stores;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public UserListQuery getQuery() {
        return query;
    }

    public ListCardProfile getPrfl() {
        return prfl;
    }

    public void setPrfl(ListCardProfile prfl) {
        this.prfl = prfl;
    }

    private boolean containsOwner(String ownerUrlName) {
        boolean result = false;
        
        if (owners != null && !owners.isEmpty()) {
            for (Owner owner : owners) {
                if (StringUtils.equals(owner.getRlnm(), ownerUrlName)) {
                    result = true;
                    break;
                }
            }
        }
        
        return result;
    }

    private boolean containsStore(String storeUrlName) {
        boolean result = false;
        
        if (stores != null && !stores.isEmpty()) {
            for (AbstractStore store : stores) {
                if (StringUtils.equals(store.getRlnm(), storeUrlName)) {
                    result = true;
                    break;
                }
            }
        }
        
        return result;
    }

    public void addCards(List<ListCard> cards) {
        if (this.cards == null) {
            this.cards = new ArrayList<ListCard>();
        }

        if (cards != null && !cards.isEmpty()) {
            this.cards.addAll(cards);
        }
    }

    public void addCard(ListCard card) {
        if (this.cards == null) {
            this.cards = new ArrayList<ListCard>();
        }

        if (card != null) {
            this.cards.add(card);
        }
    }

    public void addOwner(Owner owner) {

        if (this.owners == null) {
            this.owners = new ArrayList<Owner>();
        }

        if (owner != null && !containsOwner(owner.getRlnm())) {
            this.owners.add(owner);
        }
    }

    public void addStore(AbstractStore store) {

        if (this.stores == null) {
            this.stores = new ArrayList<AbstractStore>();
        }

        if (store != null && !containsStore(store.getRlnm())) {
            this.stores.add(store);
        }
    }

    public void addCategories(List<Category> categories) {

        if (this.categories == null) {
            this.categories = new ArrayList<Category>();
        }

        if (categories != null && !categories.isEmpty()) {
            List<Category> toRemove = new ArrayList<Category>();

            for (Category category : this.categories) {
                for (Category newCategory : categories) {
                    if (StringUtils.equals(category.getRlnm(), newCategory.getRlnm())) {
                        toRemove.add(category);
                    }
                }
            }

            if (!toRemove.isEmpty()) {
                this.categories.removeAll(toRemove);
            }

            this.categories.addAll(categories);
        }
    }
}
