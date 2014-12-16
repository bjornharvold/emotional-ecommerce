package com.lela.commons.repository;

import com.lela.domain.document.Store;

import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 5/10/12
 * Time: 10:19 PM
 * Responsibility:
 */
public interface StoreRepositoryCustom {
    List<Store> findStoreUrlNamesByMerchantIds(List<String> merchantIds);
    List<Store> findStores(List<String> fields);
}
