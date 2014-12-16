/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Chris Tallent
 * Date: 11/3/11
 * Time: 2:49 PM
 */
@Document
public class AffiliateAccount extends AbstractDocument implements Serializable {

    private static final long serialVersionUID = 4979935227022114696L;

    /**
     * Affiliate Account name
     */
    private String nm;

    /**
     * Affiliate url name
     */
    @Indexed(unique = true)
    private String rlnm;

    /**
     * Is the affiliate account active
     */
    private Boolean ctv = true;

    /**
     * The url that the banner will point to
     */
    private String bnrrl;

    /**
     * URL of the banner image file
     */
    private String bnrmgrl;

    /**
     * Alt text of the banner image
     */
    private String bnrlttxt;

    /** Domain for branding */
    private String dmn;

    /** (Optional) Navbar to be used for domain */
    private String dmnnvbr;

    /** Domain header iframe URL (Optional) */
    private String dmnhdrrl;

    /** Domain footer iframe URL (Optional) */
    private String dmnftrrl;

    /** Domain auto-redirect for Index / */
    private String dmnrdrct;

    /** Show lela lists on subdomain (Optional) */
    private String rgstrrl;

    private Boolean shwlsts = true;

    /** Show product compare on subdomain (Optional) */
    private Boolean shwcmpr = true;

    /** Show registration prompt popup on subdomain (Optional) */
    private Boolean shwrgprmpt = true;

    /** Show shopping cart on subdomain (Optional) */
    private Boolean shwcrt = false;

    /** Show item recommedations */
    private Boolean shwrcmmd = true;

    /** Send Lela Registration Confirmation Email */
    private Boolean sndrgcnf = true;

    /** Custom google analytics id */
    private String gglsnltcsd;

    /** Use Https for secured urls */
    private Boolean ssl;

    /**
     * Affiliate specific domain styles
     */
    private List<AffiliateAccountCssStyle> stls;

    /**
     * Affiliate subdomain allowed stores (Optional)
     */
    private List<AffiliateAccountStore> strs;

    /**
     * LogoUrl
     */
    private String lgrl;

    public boolean hasAssociatedBannerImage(){
        return !org.apache.commons.lang3.StringUtils.isEmpty(bnrmgrl);
    }

    /**
     * Application url names
     */
    private List<AffiliateAccountApplication> pps;

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public Boolean getCtv() {
        return ctv;
    }

    public void setCtv(Boolean ctv) {
        this.ctv = ctv;
    }

    public String getBnrrl() {
        return bnrrl;
    }

    public void setBnrrl(String bnrrl) {
        this.bnrrl = bnrrl;
    }

    public String getBnrmgrl() {
        return bnrmgrl;
    }

    public void setBnrmgrl(String bnrmgrl) {
        this.bnrmgrl = bnrmgrl;
    }

    public String getBnrlttxt() {
        return bnrlttxt;
    }

    public void setBnrlttxt(String bnrlttxt) {
        this.bnrlttxt = bnrlttxt;
    }

    public String getDmn() {
        return dmn;
    }

    public void setDmn(String dmn) {
        this.dmn = dmn;
    }

    public String getDmnnvbr() {
        return dmnnvbr;
    }

    public void setDmnnvbr(String dmnnvbr) {
        this.dmnnvbr = dmnnvbr;
    }

    public String getDmnhdrrl() {
        return dmnhdrrl;
    }

    public void setDmnhdrrl(String dmnhdrrl) {
        this.dmnhdrrl = dmnhdrrl;
    }

    public String getDmnftrrl() {
        return dmnftrrl;
    }

    public void setDmnftrrl(String dmnftrrl) {
        this.dmnftrrl = dmnftrrl;
    }

    public String getDmnrdrct() {
        return dmnrdrct;
    }

    public void setDmnrdrct(String dmnrdrct) {
        this.dmnrdrct = dmnrdrct;
    }

    public String getRgstrrl() {
        return rgstrrl;
    }

    public void setRgstrrl(String rgstrrl) {
        this.rgstrrl = rgstrrl;
    }

    public Boolean getShwlsts() {
        return shwlsts;
    }

    public void setShwlsts(Boolean shwlsts) {
        this.shwlsts = shwlsts;
    }

    public Boolean getShwcmpr() {
        return shwcmpr;
    }

    public void setShwcmpr(Boolean shwcmpr) {
        this.shwcmpr = shwcmpr;
    }

    public Boolean getShwrgprmpt() {
        return shwrgprmpt;
    }

    public void setShwrgprmpt(Boolean shwrgprmpt) {
        this.shwrgprmpt = shwrgprmpt;
    }

    public Boolean getShwcrt() {
        return shwcrt;
    }

    public void setShwcrt(Boolean shwcrt) {
        this.shwcrt = shwcrt;
    }

    public Boolean getShwrcmmd() {
        return shwrcmmd;
    }

    public void setShwrcmmd(Boolean shwrcmmd) {
        this.shwrcmmd = shwrcmmd;
    }

    public Boolean getSndrgcnf() {
        return sndrgcnf;
    }

    public void setSndrgcnf(Boolean sndrgcnf) {
        this.sndrgcnf = sndrgcnf;
    }

    public List<AffiliateAccountApplication> getPps() {
        return pps;
    }

    public void setPps(List<AffiliateAccountApplication> pps) {
        this.pps = pps;
    }

    public boolean hasApplication(String urlName) {

        if (this.pps != null && !this.pps.isEmpty()) {
            for (AffiliateAccountApplication application : this.pps) {
                if (StringUtils.equals(urlName, application.getRlnm())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean addApplication(AffiliateAccountApplication application) {

        if (this.pps == null) {
            this.pps = new ArrayList<AffiliateAccountApplication>();
        }

        AffiliateAccountApplication dupe = null;

        for (AffiliateAccountApplication aaa : this.pps) {
            if (StringUtils.equals(aaa.getRlnm(), application.getRlnm())) {
                dupe = aaa;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.pps.remove(dupe);
        }

        this.pps.add(application);

        return true;

    }

    public boolean removeAffiliateAccountApplication(String applicationUrlName) {
        AffiliateAccountApplication toRemove = null;

        if (this.pps != null && !this.pps.isEmpty()) {
            for (AffiliateAccountApplication qs : this.pps) {
                if (StringUtils.equals(qs.getRlnm(), applicationUrlName)) {
                    toRemove = qs;
                    break;
                }
            }
        }

        if (toRemove != null) {
            this.pps.remove(toRemove);
        }

        return toRemove != null;
    }

    public List<AffiliateAccountCssStyle> getStls() {
        return stls;
    }

    public void setStls(List<AffiliateAccountCssStyle> stls) {
        this.stls = stls;
    }

    public AffiliateAccountCssStyle findCssStyle(String urlName) {
        if (this.stls != null && !this.stls.isEmpty()) {
            for (AffiliateAccountCssStyle style : this.stls) {
                if (StringUtils.equals(urlName, style.getRlnm())) {
                    return style;
                }
            }
        }

        return null;
    }

    public boolean hasCssStyle(String urlName) {
        if (this.stls != null && !this.stls.isEmpty()) {
            for (AffiliateAccountCssStyle style : this.stls) {
                if (StringUtils.equals(urlName, style.getRlnm())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean addCssStyle(AffiliateAccountCssStyle style) {

        if (this.stls == null) {
            this.stls = new ArrayList<AffiliateAccountCssStyle>();
        }

        AffiliateAccountCssStyle dupe = null;

        for (AffiliateAccountCssStyle acs : this.stls) {
            if (StringUtils.equals(acs.getRlnm(), style.getRlnm())) {
                dupe = acs;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.stls.remove(dupe);
        }

        this.stls.add(style);

        return true;

    }

    public boolean removeCssStyle(String urlName) {
        AffiliateAccountCssStyle toRemove = null;

        if (this.stls != null && !this.stls.isEmpty()) {
            for (AffiliateAccountCssStyle aacs : this.stls) {
                if (StringUtils.equals(aacs.getRlnm(), urlName)) {
                    toRemove = aacs;
                    break;
                }
            }
        }

        if (toRemove != null) {
            this.stls.remove(toRemove);
        }

        return toRemove != null;
    }

    public boolean getShowStores() {
        return this.strs == null || this.strs.size() != 1;
    }

    public List<AffiliateAccountStore> getStrs() {
        return strs;
    }

    public void setStrs(List<AffiliateAccountStore> strs) {
        this.strs = strs;
    }

    public boolean hasStore(String urlName) {

        if (this.strs != null && !this.strs.isEmpty()) {
            for (AffiliateAccountStore store : this.strs) {
                if (StringUtils.equals(urlName, store.getRlnm())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean addStore(AffiliateAccountStore store) {
        if (this.strs == null) {
            this.strs = new ArrayList<AffiliateAccountStore>();
        }

        AffiliateAccountStore dupe = null;

        for (AffiliateAccountStore aas : this.strs) {
            if (StringUtils.equals(aas.getRlnm(), store.getRlnm())) {
                dupe = aas;
                break;
            }
        }

        // overwrite original
        if (dupe != null) {
            this.strs.remove(dupe);
        }

        this.strs.add(store);

        return true;

    }

    public boolean removeAffiliateAccountStore(String urlName) {
        AffiliateAccountStore toRemove = null;

        if (this.strs != null && !this.strs.isEmpty()) {
            for (AffiliateAccountStore aas : this.strs) {
                if (StringUtils.equals(aas.getRlnm(), urlName)) {
                    toRemove = aas;
                    break;
                }
            }
        }

        if (toRemove != null) {
            this.strs.remove(toRemove);
        }

        return toRemove != null;
    }

    public String getLgrl() {
		return lgrl;
	}

	public void setLgrl(String lgrl) {
		this.lgrl = lgrl;
	}

    public String getGglsnltcsd() {
        return gglsnltcsd;
    }

    public void setGglsnltcsd(String gglsnltcsd) {
        this.gglsnltcsd = gglsnltcsd;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }
}
