package com.lela.domain.document;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/20/12
 * Time: 9:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductImage {
    /** Field description */
    private static final long serialVersionUID = -3014705963156919994L;

    //~--- fields -------------------------------------------------------------

    /** Hex color value */
    private String hx;

    /** Preferred */
    private String nm;

    /** Sizes */
    private Map<String, String> sz;

    /** Preferred */
    private Boolean prfrrd;

    //~--- get methods --------------------------------------------------------



    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getNm() {
        return nm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Map<String, String> getSz() {
        return sz;
    }

    //~--- set methods --------------------------------------------------------



    /**
     * Method description
     *
     *
     * @param nm nm
     */
    public void setNm(String nm) {
        this.nm = nm;
    }

    /**
     * Method description
     *
     *
     * @param sz sz
     */
    public void setSz(Map<String, String> sz) {
        this.sz = sz;
    }

    public Boolean getPrfrrd() {
        return prfrrd;
    }

    public void setPrfrrd(Boolean prfrrd) {
        this.prfrrd = prfrrd;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getHx() {
        return hx;
    }

    /**
     * Method description
     *
     *
     * @param hx hex
     */
    public void setHx(String hx) {
        this.hx = hx;
    }
}
