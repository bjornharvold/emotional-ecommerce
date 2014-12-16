/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 10/15/11
 * Time: 7:52 AM
 * Responsibility:
 */
public class FunctionalFilterPreset implements Serializable {
    private static final long serialVersionUID = -5493453469873146701L;

    /** Category url name */
    private String rlnm;

    /** Filter key */
    private String ky;

    /** Preset options */
    private List<FunctionalFilterPresetOption> ptns;

    public FunctionalFilterPreset() {
    }

    public FunctionalFilterPreset(String rlnm, String ky, List<FunctionalFilterPresetOption> ptns) {
        this.rlnm = rlnm;
        this.ky = ky;
        this.ptns = ptns;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public List<FunctionalFilterPresetOption> getPtns() {
        return ptns;
    }

    public void setPtns(List<FunctionalFilterPresetOption> ptns) {
        this.ptns = ptns;
    }

    public FunctionalFilterPresetOption getOption(String key) {
        if ((ptns != null) && !ptns.isEmpty()) {
            for (FunctionalFilterPresetOption option : ptns) {
                if (StringUtils.equals(option.getKy(), key)) {
                    return option;
                }
            }
        }

        return null;
    }
}
