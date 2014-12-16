package com.lela.commons.repository;

import java.util.List;

import com.lela.domain.document.AffiliateAccount;

public interface AffiliateAccountRepositoryCustom {
    List<AffiliateAccount> findAll(Integer page, Integer maxResults, List<String> fields);
    List<AffiliateAccount> findAll(List<String> fields);

    List<AffiliateAccount> findAll(boolean includeInactive, Integer page, Integer maxResults);
    long countBasedOnStatus(boolean includeInactive);

}
