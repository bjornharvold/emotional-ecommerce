/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.dto.blog;

import com.lela.domain.document.Blog;
import com.lela.domain.document.BlogItem;
import com.lela.domain.document.Category;
import com.lela.domain.document.Item;
import com.lela.domain.enums.PublishStatus;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 3/24/12
 * Time: 12:18 PM
 * Responsibility:
 */
public class BlogEntry implements Serializable {
    private static final long serialVersionUID = -213628756132253431L;
    private ObjectId id;

    /** Created date */
    private Date cdt;

    /** Last update */
    private Date ldt;

    /** Publish date */
    private Date dt;

    /** Name */
    @NotNull
    @NotEmpty
    private String nm;

    /** Url name */
    @NotNull
    @NotEmpty
    private String rlnm;

    /** Content */
    private String cntn;

    /** Header */
    private String hdr;

    /** Category */
    private Category ctgry;

    /** Items */
    private List<BlogItem> tms;

    /** Image url */
    private String mgrl;

    /** Meta keyword */
    private String mtkywrd;

    /** Meta description */
    private String mtdscrptn;

    /** Status */
    @NotNull
    private PublishStatus stts;

    private MultipartFile multipartFile;

    public BlogEntry(Blog blog) {
        setId(blog.getId());
        setCdt(blog.getCdt());
        setLdt(blog.getLdt());
        this.cntn = blog.getCntn();
        this.ctgry = blog.getCtgry();
        this.hdr = blog.getHdr();
        this.mgrl = blog.getMgrl();
        this.nm = blog.getNm();
        this.rlnm = blog.getRlnm();
        this.stts = blog.getStts();
        this.tms = blog.getTms();
        this.mtdscrptn = blog.getMtdscrptn();
        this.mtkywrd = blog.getMtkywrd();
        this.dt = blog.getDt();
    }

    public BlogEntry() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Date getCdt() {
        return cdt;
    }

    public void setCdt(Date cdt) {
        this.cdt = cdt;
    }

    public Date getLdt() {
        return ldt;
    }

    public void setLdt(Date ldt) {
        this.ldt = ldt;
    }

    public List<BlogItem> getTms() {
        return tms;
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

    public void setTms(List<BlogItem> tms) {
        this.tms = tms;
    }

    public PublishStatus getStts() {
        return stts;
    }

    public void setStts(PublishStatus stts) {
        this.stts = stts;
    }

    public Category getCtgry() {
        return ctgry;
    }

    public void setCtgry(Category ctgry) {
        this.ctgry = ctgry;
    }

    public String getCntn() {
        return cntn;
    }

    public void setCntn(String cntn) {
        this.cntn = cntn;
    }

    public String getHdr() {
        return hdr;
    }

    public void setHdr(String hdr) {
        this.hdr = hdr;
    }

    public String getMgrl() {
        return mgrl;
    }

    public void setMgrl(String mgrl) {
        this.mgrl = mgrl;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getMtkywrd() {
        return mtkywrd;
    }

    public void setMtkywrd(String mtkywrd) {
        this.mtkywrd = mtkywrd;
    }

    public String getMtdscrptn() {
        return mtdscrptn;
    }

    public void setMtdscrptn(String mtdscrptn) {
        this.mtdscrptn = mtdscrptn;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
}
