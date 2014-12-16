package com.lela.domain.document;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document
public class Offer extends AbstractDocument implements Serializable {

    private static final long serialVersionUID = 6405196921380627800L;

    public Offer() {
    }

    public Offer(String rlnm, String brnchrlnm, String vlndtrms,
                 Date strtdt, Date xprtndt, String dscrptn,
                 Boolean nprprsn, Integer frqncy) {
        this.rlnm = rlnm;
        this.brnchrlnm = brnchrlnm;
        this.vlndtrms = vlndtrms;
        this.strtdt = strtdt;
        this.xprtndt = xprtndt;
        this.dscrptn = dscrptn;
        this.nprprsn = nprprsn;
        this.frqncy = frqncy;
    }

    /** Url Name */
    @Indexed( unique = true )
    private String rlnm;

    /** Branch Url Name */
    private String brnchrlnm;

    /** Value and Terms */
    private String vlndtrms;

    /** Start Date */
    private Date strtdt;

    /** Expiration Date  */
    private Date xprtndt;

    /** Description  */
    private String dscrptn;

    /** One Per Person */
    private Boolean nprprsn;

    /** Frequency (wait between coupon creations) in days, only if one per person is false.  If 0 can create coupons with no wait */
    private Integer frqncy;

    /** Is Deleted */
    private Boolean sdltd;

    public String getVlndtrms() {
        return vlndtrms;
    }

    public void setVlndtrms(String vlndtrms) {
        this.vlndtrms = vlndtrms;
    }

    public Date getXprtndt() {
        return xprtndt;
    }

    public void setXprtndt(Date xprtndt) {
        this.xprtndt = xprtndt;
    }

    public String getDscrptn() {
        return dscrptn;
    }

    public void setDscrptn(String dscrptn) {
        this.dscrptn = dscrptn;
    }

    public Date getStrtdt() {
        return strtdt;
    }

    public void setStrtdt(Date strtdt) {
        this.strtdt = strtdt;
    }

    public Boolean getNprprsn() {
        return nprprsn;
    }

    public void setNprprsn(Boolean nprprsn) {
        this.nprprsn = nprprsn;
    }

    public Boolean getSdltd() {
        return sdltd;
    }

    public void setSdltd(Boolean sdltd) {
        this.sdltd = sdltd;
    }

    public String getBrnchrlnm() {
        return brnchrlnm;
    }

    public void setBrnchrlnm(String brnchrlnm) {
        this.brnchrlnm = brnchrlnm;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public Integer getFrqncy() {
        return frqncy;
    }

    public void setFrqncy(Integer frqncy) {
        this.frqncy = frqncy;
    }
}
