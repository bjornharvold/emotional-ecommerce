/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.press;

import com.lela.domain.document.PressImage;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * Created by Bjorn Harvold
 * Date: 6/9/12
 * Time: 9:11 PM
 * Responsibility: Need to save this entry instead of the PressImage because we will have multipart file attachments
 */
public class PressImageEntry extends PressImage {
    private String rlnm;
    private MultipartFile multipartFile;

    public PressImageEntry(String rlnm) {
        this.rlnm = rlnm;
    }

    public PressImageEntry(String rlnm, PressImage pi) {
        this.rlnm = rlnm;
        this.setDt(pi.getDt());
        this.setMgid(pi.getMgid());
        this.setRl(pi.getRl());
        this.setHdr(pi.getHdr());
        this.setMgrl(pi.getMgrl());
        this.setPblshdt(pi.getPblshdt());
        this.setPblshr(pi.getPblshr());
        this.setRdr(pi.getRdr());
        this.setTxt(pi.getTxt());
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }
}
