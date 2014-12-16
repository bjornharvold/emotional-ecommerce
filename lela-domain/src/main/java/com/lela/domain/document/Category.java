/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.enums.PublishStatus;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/15/11
 * Time: 2:13 PM
 * Responsibility:
 */
@Document
public class Category extends AbstractDocument implements Serializable {

    /** Field description */
    private static final long serialVersionUID = 3048088136802634405L;

    //~--- fields -------------------------------------------------------------

    /** Catalog key */
    private String ctlgky;

    /** Name */
    private String nm;

    /** Order */
    private Integer rdr;

    /** URL name */
    @Indexed
    private String rlnm;

    /** SEO URL name */
    private String srlnm;

    /** Publish Status */
    private PublishStatus stts;

    /** Member of these category group url names */
    private List<String> grps;

    /** Member of these Departments (calculated by Navbar save) */
    private List<String> dprtmnts;

    /** Member of these Navbars (calculated by Navbar save) */
    private List<String> nvbrs;

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getCtlgky() {
        return ctlgky;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    @NotNull
    public String getNm() {
        return nm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Integer getRdr() {
        return rdr;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getRlnm() {
        return rlnm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public PublishStatus getStts() {
        return stts;
    }

    public String getSrlnm() {
        return srlnm;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param ctlgky ctlgky
     */
    public void setCtlgky(String ctlgky) {
        this.ctlgky = ctlgky;
    }

    /**
     * Method description
     *
     *
     * @param nm name
     */
    public void setNm(String nm) {
        this.nm = nm;
    }

    /**
     * Method description
     *
     *
     * @param rdr rdr
     */
    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    /**
     * Method description
     *
     *
     * @param rlnm rlnm
     */
    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    /**
     * Method description
     *
     *
     * @param stts stts
     */
    public void setStts(PublishStatus stts) {
        this.stts = stts;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public List<String> getGrps() {
        return grps;
    }

    public void setGrps(List<String> grps) {
        this.grps = grps;
    }

    public List<String> getDprtmnts() {
        return dprtmnts;
    }

    public void setDprtmnts(List<String> dprtmnts) {
        this.dprtmnts = dprtmnts;
    }

    public List<String> getNvbrs() {
        return nvbrs;
    }

    public void setNvbrs(List<String> nvbrs) {
        this.nvbrs = nvbrs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (rlnm != null ? !rlnm.equals(category.rlnm) : category.rlnm != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return rlnm != null ? rlnm.hashCode() : 0;
    }
}
