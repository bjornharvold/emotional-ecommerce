/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- JDK imports ------------------------------------------------------------

import com.lela.domain.document.UserSupplement;
import com.lela.domain.enums.MetricType;

import java.io.Serializable;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/5/11
 * Time: 2:36 PM
 * Responsibility:
 */
public class Favorite extends AbstractJSONPayload implements Serializable {

    /**
     * Field description
     */
    private static final long serialVersionUID = 916903601879989506L;

    //~--- fields -------------------------------------------------------------

    /**
     * Beer
     */
    private String br;

    /**
     * Car maker
     */
    private String crmkr;

    /**
     * Fashion brand
     */
    private String fshnbrnd;

    /**
     * Grocery store
     */
    private String grcrstr;

    /**
     * Online store
     */
    private String nlnstr;

    /**
     * Shampoo
     */
    private String shmp;

    public Favorite() {
    }

    public Favorite(UserSupplement us) {

        if (us.getMtrcs() != null) {
            if (us.getMtrcs().containsKey(MetricType.CAR_MAKER)) {
                this.crmkr = us.getMtrcs().get(MetricType.CAR_MAKER);
            }
            if (us.getMtrcs().containsKey(MetricType.SHAMPOO)) {
                this.shmp = us.getMtrcs().get(MetricType.SHAMPOO);
            }
            if (us.getMtrcs().containsKey(MetricType.FASHION_BRAND)) {
                this.fshnbrnd = us.getMtrcs().get(MetricType.FASHION_BRAND);
            }
            if (us.getMtrcs().containsKey(MetricType.BEER)) {
                this.br = us.getMtrcs().get(MetricType.BEER);
            }
            if (us.getMtrcs().containsKey(MetricType.ONLINE_STORE)) {
                this.nlnstr = us.getMtrcs().get(MetricType.ONLINE_STORE);
            }
            if (us.getMtrcs().containsKey(MetricType.GROCERY_STORE)) {
                this.grcrstr = us.getMtrcs().get(MetricType.GROCERY_STORE);
            }
        }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     * @return Return value
     */
    public String getBr() {
        return br;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getCrmkr() {
        return crmkr;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getFshnbrnd() {
        return fshnbrnd;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getGrcrstr() {
        return grcrstr;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getNlnstr() {
        return nlnstr;
    }

    /**
     * Method description
     *
     * @return Return value
     */
    public String getShmp() {
        return shmp;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     * @param br br
     */
    public void setBr(String br) {
        this.br = br;
    }

    /**
     * Method description
     *
     * @param crmkr crmkr
     */
    public void setCrmkr(String crmkr) {
        this.crmkr = crmkr;
    }

    /**
     * Method description
     *
     * @param fshnbrnd fshnbrnd
     */
    public void setFshnbrnd(String fshnbrnd) {
        this.fshnbrnd = fshnbrnd;
    }

    /**
     * Method description
     *
     * @param grcrstr grcrstr
     */
    public void setGrcrstr(String grcrstr) {
        this.grcrstr = grcrstr;
    }

    /**
     * Method description
     *
     * @param nlnstr nlnstr
     */
    public void setNlnstr(String nlnstr) {
        this.nlnstr = nlnstr;
    }

    /**
     * Method description
     *
     * @param shmp shmp
     */
    public void setShmp(String shmp) {
        this.shmp = shmp;
    }
}
