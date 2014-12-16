package com.lela.domain.document;

import com.lela.domain.enums.AffiliateApiType;
import com.lela.domain.enums.AffiliateNetworkType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/1/12
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Document
public class CategoryInitiatorQuery extends AbstractDocument {

    /**
     * Registered USER ID
     */
    @Indexed
    private ObjectId srid;

    private String nm;

    /* Description */
    private String dscrptn;

    /* Keywords */
    private String[] kywrds = new String[3];

    /* Vendor Specific Queries */
    private Map<AffiliateApiType, CategoryInitiatorAffiliateQuery> qrys;

    public ObjectId getSrid() {
        return srid;
    }

    public void setSrid(ObjectId srid) {
        this.srid = srid;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getDscrptn() {
        return dscrptn;
    }

    public void setDscrptn(String dscrptn) {
        this.dscrptn = dscrptn;
    }

    public String[] getKywrds() {
        return kywrds;
    }

    public void setKywrds(String[] kywrds) {
        this.kywrds = kywrds;
    }

    public Map<AffiliateApiType, CategoryInitiatorAffiliateQuery> getQrys() {
        return qrys;
    }

    public void setQrys(Map<AffiliateApiType, CategoryInitiatorAffiliateQuery> qrys) {
        this.qrys = qrys;
    }
}
