/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.document.UserSupplement;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.social.connect.UserProfile;

import java.util.Locale;

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 7/21/11
 * Time: 11:51 AM
 * Responsibility:
 */
public class UserDto {

    /** Field description */
    private String fnm;

    /** Field description */
    private Locale lcl;

    /** Field description */
    private String lnm;

    /** Field description */
    @NotEmpty
    @Email
    private String ml;

    /** Field description */
    @NotEmpty
    private String psswrd;
    
    private Boolean optin = false;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public UserDto() {}

    /**
     * Constructs ...
     *
     *
     * @param us user
     */
    public UserDto(UserSupplement us) {
        this.ml  = us.getMl();
        this.fnm = us.getFnm();
        this.lnm = us.getLnm();
    }

    /**
     * Constructs ...
     *
     *
     * @param profile profile
     */
    public UserDto(UserProfile profile) {
        this.ml  = profile.getEmail();
        this.fnm = profile.getFirstName();
        this.lnm = profile.getLastName();
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getFnm() {
        return fnm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public Locale getLcl() {
        return lcl;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getLnm() {
        return lnm;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getMl() {
        return ml;
    }

    /**
     * Method description
     *
     *
     * @return Return value
     */
    public String getPsswrd() {
        return psswrd;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param fnm fnm
     */
    public void setFnm(String fnm) {
        this.fnm = fnm;
    }

    /**
     * Method description
     *
     *
     * @param lcl lcl
     */
    public void setLcl(Locale lcl) {
        this.lcl = lcl;
    }

    /**
     * Method description
     *
     *
     * @param lnm lnm
     */
    public void setLnm(String lnm) {
        this.lnm = lnm;
    }

    /**
     * Method description
     *
     *
     * @param ml ml
     */
    public void setMl(String ml) {
        this.ml = ml;
    }

    /**
     * Method description
     *
     *
     * @param psswrd psswrd
     */
    public void setPsswrd(String psswrd) {
        this.psswrd = psswrd;
    }

    public Boolean getOptin() {
        return optin;
    }

    public void setOptin(Boolean optin) {
        this.optin = optin;
    }
}
