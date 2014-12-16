package com.lela.domain.document;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/1/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventField implements Serializable {
    private static final long serialVersionUID = 1565747545637850651L;

    /** Field Name */
    private String fldNm;

    /** True if field is required */
    private Boolean rqrd = false;

    /** Flag for DTO to indicate element needs to be deleted */
    @Transient
    private Boolean delete = false;

    public String getFldNm() {
        return fldNm;
    }

    public void setFldNm(String fldNm) {
        this.fldNm = fldNm;
    }

    public Boolean getRqrd() {
        return rqrd;
    }

    public void setRqrd(Boolean rqrd) {
        this.rqrd = rqrd;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }
}
