/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.enums.StoreType;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/14/11
 * Time: 1:57 PM
 * Responsibility:
 */
public abstract class AbstractStore extends AbstractDocument {

    /**
     * Affiliate Url Name
     */
    private String ffltrlnm;

    /**
     * Product Categories
     */
    private List<LocalCategory> lclctgrs;

    /**
     * Logo url
     */
    private String lgrl;

    /**
     * Image quality
     */
    private String mgqlty;

    /**
     * Merchant ID
     */
    private String mrchntd;

    /**
     * Name
     */
    private String nm;

    /**
     * SocialNetwork ID
     */
    private String ntwrkd;

    /**
     * Merchant Client source (e.g. - AMZN, GAN, etc...)
     */
    private String mrchntsrc;

    /**
     * URL
     */
    private String rl;

    /**
     * URL name
     */
    private String rlnm;

    /** SEO URL name */
    private String srlnm;

    /**
     * Store type
     */
    private List<String> tp;

    /**
     * Merchant Approved
     */
    private Boolean pprvd;

    /** Privacy policy */
    private String plcy;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     */
    public AbstractStore() {}

    /**
     * Constructs ...
     *
     *
     * @param store store
     */
    public AbstractStore(AbstractStore store) {
        this.lgrl    = store.getLgrl();
        this.mgqlty  = store.getMgqlty();
        this.mrchntd = store.getMrchntd();
        this.nm      = store.getNm();
        this.ntwrkd  = store.getNtwrkd();
        this.mrchntsrc = store.getMrchntsrc();
        this.rl      = store.getRl();
        this.rlnm    = store.getRlnm();
        this.tp      = store.getTp();
        this.pprvd   = store.getPprvd();
        this.plcy = store.getPlcy();
    }

    /**
     * Constructs ...
     *
     *
     * @param mrchntd mrchntd
     */
    public AbstractStore(String mrchntd) {
        this.mrchntd = mrchntd;
    }

    /**
     * Constructs ...
     *
     * @param tp   tp
     * @param nm   nm
     * @param rlnm rlnm
     */
    public AbstractStore(String nm, String rlnm, StoreType... tp) {
        if (tp != null) {
            if (this.tp == null) {
                this.tp = new ArrayList<String>();
            }

            for (StoreType type : tp) {
                this.tp.add(type.name());
            }
        }

        this.nm   = nm;
        this.rlnm = rlnm;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     * @return Return value
     */
    public String getLgrl() {
        return lgrl;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getMgqlty() {
        return mgqlty;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getMrchntd() {
        return mrchntd;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getNm() {
        return nm;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getNtwrkd() {
        return ntwrkd;
    }

    public String getMrchntsrc() {
        return mrchntsrc;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getRl() {
        return rl;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getRlnm() {
        return rlnm;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public List<String> getTp() {
        return tp;
    }

    public Boolean getPprvd() {
        return pprvd;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getFfltrlnm() {
        return ffltrlnm;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public List<LocalCategory> getLclctgrs() {
        return lclctgrs;
    }

    public String getSrlnm() {
        return srlnm;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     * @param lgrl lgrl
     */
    public void setLgrl(String lgrl) {
        this.lgrl = lgrl;
    }

    /**
     * Method description
     *
     * @param mgqlty mgqlty
     */
    public void setMgqlty(String mgqlty) {
        this.mgqlty = mgqlty;
    }

    /**
     * Method description
     *
     * @param mrchntd mrchntd
     */
    public void setMrchntd(String mrchntd) {
        this.mrchntd = mrchntd;
    }

    /**
     * Method description
     *
     * @param nm nm
     */
    public void setNm(String nm) {
        this.nm = nm;
    }

    /**
     * Method description
     *
     * @param ntwrkd ntwrkd
     */
    public void setNtwrkd(String ntwrkd) {
        this.ntwrkd = ntwrkd;
    }

    public void setMrchntsrc(String mrchntsrc) {
        this.mrchntsrc = mrchntsrc;
    }

    /**
     * Method description
     *
     * @param rl rl
     */
    public void setRl(String rl) {
        this.rl = rl;
    }

    /**
     * Method description
     *
     * @param rlnm rlnm
     */
    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    /**
     * Method description
     *
     * @param tp tp
     */
    public void setTp(List<String> tp) {
        this.tp = tp;
    }

    public void setPprvd(Boolean pprvd) {
        this.pprvd = pprvd;
    }

    /**
     * Method description
     *
     * @param ffltrlnm ffltrlnm
     */
    public void setFfltrlnm(String ffltrlnm) {
        this.ffltrlnm = ffltrlnm;
    }

    /**
     * Method description
     *
     * @param lclctgrs lclctgrs
     */
    public void setLclctgrs(List<LocalCategory> lclctgrs) {
        this.lclctgrs = lclctgrs;
    }

    public String getPlcy() {
        return plcy;
    }

    public void setPlcy(String plcy) {
        this.plcy = plcy;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }
}
