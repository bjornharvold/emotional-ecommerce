/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.ApplicationType;
import com.lela.domain.enums.PublishStatus;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 11/11/11
 * Time: 10:05 PM
 * Responsibility: Persists all external applications allowed to connect with the api endpoints
 */
@Document
public class Application extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = 6997796126637087985L;

    /** Application url name */
    @NotNull
    @NotEmpty
    private String rlnm;

    /** Application status */
    @NotNull
    private Boolean pblshd;

    /** Application name */
    @NotNull
    @NotEmpty
    private String nm;

    /** Application description */
    private String dsc;

    /** Tool url name. e.g. quiz url name */
    @NotNull
    @NotEmpty
    private String trlnm;

    /** Application type */
    @NotNull
    private ApplicationType tp;

    public String getTrlnm() {
        return trlnm;
    }

    public void setTrlnm(String trlnm) {
        this.trlnm = trlnm;
    }

    public Boolean getPblshd() {
        return pblshd;
    }

    public void setPblshd(Boolean pblshd) {
        this.pblshd = pblshd;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public ApplicationType getTp() {
        return tp;
    }

    public void setTp(ApplicationType tp) {
        this.tp = tp;
    }

    public String getNameType() {
        return nm + " - " + rlnm + " - " + tp;
    }
}
