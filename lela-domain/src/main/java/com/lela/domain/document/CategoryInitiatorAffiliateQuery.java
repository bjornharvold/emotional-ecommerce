package com.lela.domain.document;

import com.lela.domain.enums.AffiliateApiType;
import com.lela.domain.enums.AffiliateNetworkType;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/1/12
 * Time: 3:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryInitiatorAffiliateQuery {

    /* Affiliate */
    AffiliateApiType afflteApi;

    /* Affiliate */
    AffiliateNetworkType afflteNtwrk;

    CategoryInitiatorSubquery[] qrys = new CategoryInitiatorSubquery[0];

    Boolean nbld = Boolean.TRUE;

    public AffiliateApiType getAfflteApi() {
        return afflteApi;
    }

    public void setAfflteApi(AffiliateApiType afflteApi) {
        this.afflteApi = afflteApi;
    }

    public AffiliateNetworkType getAfflteNtwrk() {
        return afflteNtwrk;
    }

    public void setAfflteNtwrk(AffiliateNetworkType afflteNtwrk) {
        this.afflteNtwrk = afflteNtwrk;
    }

    public Boolean getNbld() {
        return nbld;
    }

    public void setNbld(Boolean nbld) {
        this.nbld = nbld;
    }

    public CategoryInitiatorSubquery[] getQrys() {
        return qrys;
    }

    public void setQrys(CategoryInitiatorSubquery[] qrys) {
        this.qrys = qrys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryInitiatorAffiliateQuery that = (CategoryInitiatorAffiliateQuery) o;

        if (afflteApi != that.afflteApi) return false;
        if (afflteNtwrk != that.afflteNtwrk) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = afflteApi != null ? afflteApi.hashCode() : 0;
        result = 31 * result + (afflteNtwrk != null ? afflteNtwrk.hashCode() : 0);
        return result;
    }
}
