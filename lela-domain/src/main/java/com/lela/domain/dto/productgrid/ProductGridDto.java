/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.productgrid;

import com.lela.domain.document.AbstractItem;
import com.lela.domain.document.Item;
import com.lela.domain.document.ProductGridFilter;
import com.lela.domain.document.RelevantItem;
import com.lela.domain.dto.AbstractApiMetricsDto;
import com.lela.domain.dto.ItemPage;
import com.lela.domain.enums.FunctionalSortType;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 8/25/12
 * Time: 1:17 PM
 * Responsibility:
 */
public class ProductGridDto extends AbstractApiMetricsDto implements Serializable {
    private static final long serialVersionUID = -2587480495273705956L;

    /** Url name */
    private String rlnm;

    /** SEO friendly Url name */
    private String srlnm;

    /** Category url name */
    private String crlnm;

    /** Locale */
    private String lcl;

    /** Title */
    private String nm;

    /** Header */
    private String hdr;

    private List<RelevantItemDto> relevantItems;

    private List<ItemDto> items;

    private Long totalElements;

    private String url;

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public String getSrlnm() {
        return srlnm;
    }

    public void setSrlnm(String srlnm) {
        this.srlnm = srlnm;
    }

    public String getLcl() {
        return lcl;
    }

    public void setLcl(String lcl) {
        this.lcl = lcl;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getHdr() {
        return hdr;
    }

    public void setHdr(String hdr) {
        this.hdr = hdr;
    }

    public List<RelevantItemDto> getRelevantItems() {
        return relevantItems;
    }

    public void setRelevantItems(List<RelevantItemDto> relevantItems) {
        this.relevantItems = relevantItems;
    }

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCrlnm() {
        return crlnm;
    }

    public void setCrlnm(String crlnm) {
        this.crlnm = crlnm;
    }
}
