package com.lela.domain.enums;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/1/12
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public enum AffiliateNetworkType {
    NO_NETWORK(0),
    SHARE_A_SALE(1),
    COMMISSION_JUNCTION(2),
    LINK_SHARE(4),
    GOOGLE_AFFILIATE(5),
    LINK_CONNECTOR(7),
    PEPPERJAM(8),
    AMAZON(13);

    private int id;

    private AffiliateNetworkType(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}
