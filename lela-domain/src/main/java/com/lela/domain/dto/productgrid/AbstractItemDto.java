/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.productgrid;

import org.apache.commons.lang.StringUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/26/12
 * Time: 10:44 AM
 * Responsibility:
 */
public class AbstractItemDto implements Serializable {
    private static final long serialVersionUID = -7014074668002604592L;

    /** Attributes */
    private List<AttributeDto> attrs;

    /** Sub Attributes */
    private List<AttributeDto> sbttrs;

    /** Attribute Map */
    private Map<String, AttributeDto> attrmp;

    /** Color List */
    private List<ColorDto> clrs;

    /** Name */
    private String nm;

    /** URL name */
    private String rlnm;

    /** SEO URL name */
    private String srlnm;

    /** Owner */
    private OwnerDto wnr;

    private CategoryDto ctgry;

    public List<AttributeDto> getAttrs() {
        return attrs;
    }

    /**
     * We should filter this list when Dozer is mapping it from its full counterpart
     * @param attrs attrs
     */
    public void setAttrs(List<AttributeDto> attrs) {
        if (attrs != null && !attrs.isEmpty()) {
            for (AttributeDto attr : attrs) {
                if (StringUtils.equals(attr.getKy(), "LowestPrice")) {
                    addAttributeDto(attr);
                } else if (StringUtils.equals(attr.getKy(), "MerchantName")) {
                    addAttributeDto(attr);
                } else if (StringUtils.equals(attr.getKy(), "StoreCount")) {
                    addAttributeDto(attr);
                }
            }
        }
    }

    public List<AttributeDto> getSbttrs() {
        return sbttrs;
    }

    /**
     * We should filter this list when Dozer is mapping it from its full counterpart
     * @param sbttrs sbattrs
     */
    public void setSbttrs(List<AttributeDto> sbttrs) {
        if (sbttrs != null && !sbttrs.isEmpty()) {
            for (AttributeDto attr : sbttrs) {
                // Merchant ID *MUST* come from sbttrs where it will be "Amazon" and not the int ID
                if (StringUtils.equals(attr.getKy(), "MerchantId")) {
                    addAttributeDto(attr);
                } else if (StringUtils.equals(attr.getKy(), "Brand")) {
                    addAttributeDto(attr);
                }
            }
        }
    }

    private void addAttributeDto(AttributeDto attr) {
        if (this.attrmp == null) {
            this.attrmp = new HashMap<String, AttributeDto>();
        }

        this.attrmp.put(attr.getKy(), attr);
    }

    public Map<String, AttributeDto> getAttrmp() {
        return attrmp;
    }

    public void setAttrmp(Map<String, AttributeDto> attrmp) {
        this.attrmp = attrmp;
    }

    public List<ColorDto> getClrs() {
        return clrs;
    }

    public void setClrs(List<ColorDto> clrs) {
        this.clrs = clrs;
    }

    public CategoryDto getCtgry() {
        return ctgry;
    }

    public void setCtgry(CategoryDto ctgry) {
        this.ctgry = ctgry;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

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

    public OwnerDto getWnr() {
        return wnr;
    }

    public void setWnr(OwnerDto wnr) {
        this.wnr = wnr;
    }
}
