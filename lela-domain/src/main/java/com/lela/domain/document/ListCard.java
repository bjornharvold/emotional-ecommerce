/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.list.ListCardType;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Bjorn Harvold
 * Date: 9/4/12
 * Time: 12:05 PM
 * Responsibility:
 */
public class ListCard implements Serializable {
    private final static long serialVersionUID = -3996043206003160462L;

    /** Order */
    private Integer rdr;

    /** Item url name */
    private String rlnm;

    /** Notes */
    private List<Note> nts;

    /** Reviews */
    private List<Review> rvws;

    /** Pictures */
    private List<Picture> pctrs;

    /** Comments */
    private List<Comment> cmmnts;

    /** Date the list item was added */
    private Date dt;

    /** List type */
    private ListCardType tp;

    /** I own the contents of this card. Note: Only used for items */
    private Boolean wn = false;

    /** Alerts. Only being used by item as well */
    private Alert lrt;

    /** Title to be populated  */
    private String nm;

    /** Url of external site when it's an external card */
    private String rl;

    @Transient
    private RelevantItem relevantItem;

    @Transient
    private Item item;

    @Transient
    private Owner owner;

    @Transient
    private Store store;

    @Transient
    private Branch branch;

    @Transient
    private ListCardProfile prfl;

    public ListCard() {
    }

    public ListCard(ListCardType tp, String rlnm, Integer rdr) {
        this.rlnm = rlnm;
        this.rdr = rdr;
        this.tp = tp;
    }

    public ListCard(ListCardType tp, String rlnm, Integer rdr, Boolean own) {
        this.rlnm = rlnm;
        this.rdr = rdr;
        this.tp = tp;
        this.wn = own;
    }

    public Integer getRdr() {
        return rdr;
    }

    public void setRdr(Integer rdr) {
        this.rdr = rdr;
    }

    public String getRlnm() {
        return rlnm;
    }

    public void setRlnm(String rlnm) {
        this.rlnm = rlnm;
    }

    public List<Note> getNts() {
        return nts;
    }

    public void setNts(List<Note> nts) {
        this.nts = nts;
    }

    public List<Review> getRvws() {
        return rvws;
    }

    public void setRvws(List<Review> rvws) {
        this.rvws = rvws;
    }

    public List<Picture> getPctrs() {
        return pctrs;
    }

    public void setPctrs(List<Picture> pctrs) {
        this.pctrs = pctrs;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public ListCardType getTp() {
        return tp;
    }

    public void setTp(ListCardType tp) {
        this.tp = tp;
    }

    public RelevantItem getRelevantItem() {
        return relevantItem;
    }

    public Item getItem() {
        return item;
    }

    public Boolean getWn() {
        return wn;
    }

    public void setWn(Boolean wn) {
        this.wn = wn;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Alert getLrt() {
        return lrt;
    }

    public void setLrt(Alert lrt) {
        this.lrt = lrt;
    }

    public List<Comment> getCmmnts() {
        return cmmnts;
    }

    public void setCmmnts(List<Comment> cmmnts) {
        this.cmmnts = cmmnts;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public void setRelevantItem(RelevantItem relevantItem) {
        this.relevantItem = relevantItem;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Note getNote(String noteCode) {
        if (this.nts != null && !this.nts.isEmpty()) {
            for (Note note : this.nts) {
                if (StringUtils.equals(note.getCd(), noteCode)) {
                    return note;
                }
            }
        }
        
        return null;
    }

    public Review getReview(String reviewCode) {
        if (this.rvws != null && !this.rvws.isEmpty()) {
            for (Review review : this.rvws) {
                if (StringUtils.equals(review.getCd(), reviewCode)) {
                    return review;
                }
            }
        }
        
        return null;
    }

    public Picture getPicture(String pictureCode) {
        if (this.pctrs != null && !this.pctrs.isEmpty()) {
            for (Picture picture : this.pctrs) {
                if (StringUtils.equals(picture.getCd(), pictureCode)) {
                    return picture;
                }
            }
        }
        
        return null;
    }

    public Comment getComment(String commentCode) {
        if (this.cmmnts != null && !this.cmmnts.isEmpty()) {
            for (Comment comment : this.cmmnts) {
                if (StringUtils.equals(comment.getCd(), commentCode)) {
                    return comment;
                }
            }
        }
        
        return null;
    }

    public ListCardProfile getPrfl() {
        return prfl;
    }

    public void setPrfl(ListCardProfile prfl) {
        this.prfl = prfl;
    }

    public String getRl() {
        return rl;
    }

    public void setRl(String rl) {
        this.rl = rl;
    }
}
