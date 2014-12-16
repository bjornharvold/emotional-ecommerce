/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;



import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 12/7/11
 * Time: 1:11 AM
 * Responsibility: Serves to persist static web content
 */
@Document
public class StaticContent extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = -3365276138286514156L;

    @Indexed( unique = true )
    private String rlnm;

    /** SEO friendly url name */
    private String srlnm;

    private String bdy;

    private String nm;
    
    /**
     * Is this static content a velocity template?
     */
    private boolean vlctytmplt;

	/**
     * Last Image url. This URL is only used everytime an image is uploaded.
     * It has no relevance later on because the static content may have several images, however this url
     * only represents URL of the last image uploaded
     */
    private String lmgrl;
    
    public StaticContent() {
    }

    public StaticContent(StaticContent sc, String velocifiedString) {
        setId(sc.getId());
        setCdt(sc.getCdt());
        setLdt(sc.getLdt());
        this.rlnm = sc.getRlnm();
        this.srlnm = sc.getSrlnm();
        this.bdy = velocifiedString;
        this.nm = sc.getNm();
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getBdy() {
        return bdy;
    }

    public void setBdy(String bdy) {
        this.bdy = bdy;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public String getNameType() {
        return nm + ", " + rlnm;
    }

	public boolean isVlctytmplt() {
		return vlctytmplt;
	}

	public void setVlctytmplt(boolean vlctytmplt) {
		this.vlctytmplt = vlctytmplt;
	}

	public String getLmgrl() {
		return lmgrl;
	}

	public void setLmgrl(String lmgrl) {
		this.lmgrl = lmgrl;
	}
}
