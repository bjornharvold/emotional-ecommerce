/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.TaskType;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Bjorn Harvold
 * Date: 8/18/12
 * Time: 9:32 PM
 * Responsibility:
 */
@Document
public class Task extends AbstractDocument implements Serializable {
    private static final long serialVersionUID = -6457163806785558978L;

    /** Task type */
    @NotNull
    private TaskType tp;

    /** Total steps */
    @NotNull
    private Integer stps;

    /** Current step */
    @NotNull
    private Integer cstp;

    /** Recipient */
    private String rcpnt;

    /** Message */
    private String msg;

    /** Estimate time in long format */
    private Long tm;

    public TaskType getTp() {
        return tp;
    }

    public void setTp(TaskType tp) {
        this.tp = tp;
    }

    public Integer getStps() {
        return stps;
    }

    public void setStps(Integer stps) {
        this.stps = stps;
    }

    public Integer getCstp() {
        return cstp;
    }

    public void setCstp(Integer cstp) {
        this.cstp = cstp;
    }

    public String getRcpnt() {
        return rcpnt;
    }

    public void setRcpnt(String rcpnt) {
        this.rcpnt = rcpnt;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTm() {
        return tm;
    }

    public void setTm(Long tm) {
        this.tm = tm;
    }
}
