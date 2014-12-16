/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.list;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.lela.domain.ApplicationConstants;
import com.lela.domain.dto.AbstractJSONPayload;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 10/5/12
 * Time: 10:41 AM
 * Responsibility: Used as a form binding object when creating a list card that does not originate from Lela content
 */
public class ExternalListCard extends AbstractJSONPayload implements Serializable {
    private static final long serialVersionUID = 2034062871377515499L;

    private String userCode;

    private String boardCode = ApplicationConstants.DEFAULT_BOARD_NAME;

    private String imageUrl;

    private String width;

    @NotNull
    @NotEmpty
    private String name;

    private String note;

    private MultipartFile multipartFile;

    private String externalUrl;

    private Map<String, String> externalImages;

    private Boolean fb;

    private final static EthernetAddress nic = EthernetAddress.fromInterface();

    private final static TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(nic);

    /**
     * Checksum that we can use to uniquely identify object
     */
    private String cd = uuidGenerator.generate().toString();

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public void setBoardCode(String boardCode) {
        this.boardCode = boardCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Map<String, String> getExternalImages() {
        return externalImages;
    }

    public void setExternalImages(Map<String, String> externalImages) {
        this.externalImages = externalImages;
    }

    public Boolean getFb() {
        return fb;
    }

    public void setFb(Boolean fb) {
        this.fb = fb;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }
}
