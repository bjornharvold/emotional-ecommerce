package com.lela.domain.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/14/12
 * Time: 7:37 AM
 * To change this template use File | Settings | File Templates.
 */
@Document
public class AffiliateSale extends AbstractDocument implements Serializable {
    /**
     * Affiliate url name
     */
    @Indexed(unique = true)
    private String rlnm;

    /**
     * User Code
     */
    private String srcd;

    /**
     * Total Price
     */
    private Double ttlPrce;

    /**
     * Commission
     */
    private Double cmmssn;

    /**
     * Item UPC
     */
    private String upc;

    /**
     * SKU
     */
    private String sku;

    /**
     *  Redirect ID
     */
    private String rdrctd;

    /**
     * Sub ID (aka SID)
     */
    private String sbd;

    /**
     * Product Code
     **/
    private String prdctcd;

    /**
     * Order ID
     **/
    private String ordrd;

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getSrcd() {
        return srcd;
    }

    public void setSrcd(String srcd) {
        this.srcd = srcd;
    }

    public Double getTtlPrce() {
        return ttlPrce;
    }

    public void setTtlPrce(Double ttlPrce) {
        this.ttlPrce = ttlPrce;
    }

    public Double getCmmssn() {
        return cmmssn;
    }

    public void setCmmssn(Double cmmssn) {
        this.cmmssn = cmmssn;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getRdrctd() {
        return rdrctd;
    }

    public void setRdrctd(String rdrctd) {
        this.rdrctd = rdrctd;
    }

    public String getSbd() {
        return sbd;
    }

    public void setSbd(String sbd) {
        this.sbd = sbd;
    }

    public String getPrdctcd() {
        return prdctcd;
    }

    public void setPrdctcd(String prdctcd) {
        this.prdctcd = prdctcd;
    }

    public String getOrdrd() {
        return ordrd;
    }

    public void setOrdrd(String ordrd) {
        this.ordrd = ordrd;
    }
}
