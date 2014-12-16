package com.lela.commons.repository;

import com.lela.domain.document.Offer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/31/12
 * Time: 12:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OfferRepositoryCustom {
    List<Offer> findValidByBranchUrlName(String branchUrlName);
}
