/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.enums.FunctionalFilterDomainType;
import com.lela.domain.enums.FunctionalFilterType;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 8/4/11
 * Time: 12:18 PM
 * Responsibility:
 */
@Document
public class FunctionalFilter extends AbstractDocument implements Serializable {

    /**
     * Field description
     */
    private static final long serialVersionUID = -3234703516586583432L;

    //~--- fields -------------------------------------------------------------

    /**
     * Category url name
     */
    private String rlnm;

    /**
     * Answers
     */
    private List<FunctionalFilterOption> ptns = new ArrayList<FunctionalFilterOption>();

    /**
     * Data key
     */
    private String dtky;

    /**
     * Unique key
     */
    private String ky;

    /**
     * Locale
     */
    private String lcl;

    /**
     * Order
     */
    private Integer rdr;

    /**
     * Type
     */
    @NotNull
    private FunctionalFilterType tp;

    @NotNull
    private FunctionalFilterDomainType dtp;

    public FunctionalFilter() {
    }

    public FunctionalFilter(FunctionalFilter ff) {
        super(ff);
        this.dtky = ff.getDtky();
        this.ky = ff.getKy();
        this.lcl = ff.getLcl();
        this.rdr = ff.getRdr();
        this.rlnm = ff.getRlnm();
        this.tp = ff.getTp();
        this.dtp = ff.getDtp();

        if (ff.getPtns() != null && !ff.getPtns().isEmpty()) {
            this.ptns = new ArrayList<FunctionalFilterOption>();

            for (FunctionalFilterOption ffo : ff.getPtns()) {
                this.ptns.add(new FunctionalFilterOption(ffo));
            }
        }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     * @param key key
     * @return Return value
     */
    public FunctionalFilterOption getFunctionalFilterOption(String key) {
        if ((ptns != null) && !ptns.isEmpty()) {
            for (FunctionalFilterOption nswr : ptns) {
                if (StringUtils.equals(nswr.getKy(), key)) {
                    return nswr;
                }
            }
        }

        return null;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getDtky() {
        return dtky;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getKy() {
        return ky;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getLcl() {
        return lcl;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public List<FunctionalFilterOption> getPtns() {
        return ptns;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public Integer getRdr() {
        return rdr;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public FunctionalFilterType getTp() {
        return tp;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     * @param dtky dtky
     */
    public void setDtky(String dtky) {
        this.dtky = dtky;
    }

    /**
     * Method description
     *
     * @param ky ky
     */
    public void setKy(String ky) {
        this.ky = ky;
    }

    /**
     * Method description
     *
     * @param lcl lcl
     */
    public void setLcl(String lcl) {
        this.lcl = lcl;
    }

    /**
     * Method description
     *
     * @param ptns nswrs
     */
    public void setPtns(List<FunctionalFilterOption> ptns) {
        this.ptns = ptns;
    }

    /**
     * Method description
     *
     * @param rdr rdr
     */
    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    /**
     * Method description
     *
     * @param tp tp
     */
    public void setTp(FunctionalFilterType tp) {
        this.tp = tp;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public FunctionalFilterDomainType getDtp() {
        return dtp;
    }

    public void setDtp(FunctionalFilterDomainType dtp) {
        this.dtp = dtp;
    }

    public boolean hasSelected() {
        boolean result = false;

        if (ptns != null && !ptns.isEmpty()) {
            for (FunctionalFilterOption ffo : ptns) {
                if (ffo.getSlctd() != null && ffo.getSlctd()) {
                    return true;
                }
            }
        }


        return result;
    }
}
