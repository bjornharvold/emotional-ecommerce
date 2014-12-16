/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import com.lela.domain.ApplicationConstants;
import com.lela.domain.dto.AbstractJSONPayload;
import com.lela.domain.enums.list.ListCardType;

import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 9/6/12
 * Time: 11:15 AM
 * Responsibility:
 */
public class ListPosition extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = -8596727531172439983L;

    private String boardName = ApplicationConstants.DEFAULT_BOARD_NAME;
    private String userCode;
    private String urlName;
    private Integer order;

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
