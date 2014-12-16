/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import com.lela.domain.ApplicationConstants;
import com.lela.domain.dto.AbstractJSONPayload;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 9/8/11
 * Time: 11:48 AM
 * Responsibility:
 */
public final class ListEntry extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = -6044824746690311206L;

    private String boardCode = ApplicationConstants.DEFAULT_BOARD_NAME;

    /** If the boardCode is empty, the boardName should be filled in */
    private String boardName;

    /** Item url name */
    private String rlnm;

    /** User user */
    private String userCode;

    /** I own this */
    private Boolean own;

    public ListEntry() {
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public ListEntry(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Boolean getOwn() {
        return own;
    }

    public void setOwn(Boolean own) {
        this.own = own;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
