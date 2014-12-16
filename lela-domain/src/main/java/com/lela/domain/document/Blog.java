/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.Syndicatable;
import com.lela.domain.dto.blog.BlogEntry;
import com.lela.domain.enums.PublishStatus;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 1/21/12
 * Time: 12:57 AM
 * Responsibility:
 */
@Document
public class Blog extends AbstractDocument implements Serializable, Syndicatable {
    private static final long serialVersionUID = 3257969046812666880L;

    /** Name */
    @NotNull
    @NotEmpty
    private String nm;

    /** Url name */
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

    /** Publish date */
    private Date dt;

    /** Status */
    @NotNull
    private PublishStatus stts;

    @Transient
    private Map<String, RelevantItem> relevantItems;

    @Transient
    private Map<String, ItemDetails> itemsDetails;
    
    /**
     * Map of items associated to this blog keyed off the item url
     */
    @Transient
    private Map<String, Item> itemMap = new HashMap<String, Item>();

    public Blog() {
    }

    public Blog(BlogEntry entry) {
        setId(entry.getId());
        setCdt(entry.getCdt());
        setLdt(entry.getLdt());
        this.cntn = entry.getCntn();
        this.ctgry = entry.getCtgry();
        this.hdr = entry.getHdr();
        this.mgrl = entry.getMgrl();
        this.nm = entry.getNm();
        this.rlnm = entry.getRlnm();
        this.stts = entry.getStts();
        this.tms = entry.getTms();
        this.mtdscrptn = entry.getMtdscrptn();
        this.mtkywrd = entry.getMtkywrd();
        this.dt = entry.getDt();
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

    public Map<String, RelevantItem> getRelevantItems() {
        return relevantItems;
    }

    public void setRelevantItems(Map<String, RelevantItem> relevantItems) {
        this.relevantItems = relevantItems;
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

    public List<ItemDetails> getItemsDetails() {
        List<ItemDetails> result = null;
        
        if (this.tms != null && !this.tms.isEmpty()) {
            result = new ArrayList<ItemDetails>(tms.size());
            
            for (BlogItem blogItem : this.tms) {
                if (relevantItems != null && relevantItems.containsKey(blogItem.getRlnm())) {
                    result.add(new ItemDetails(relevantItems.get(blogItem.getRlnm())));
                } else {
                	if (itemMap.containsKey(blogItem.getRlnm())){
                		result.add(new ItemDetails(itemMap.get(blogItem.getRlnm())));
                	}
                }
            }
        }
        
        return result;
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

	public Map<String, Item> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Item> itemMap) {
		this.itemMap = itemMap;
	}

	@Override
	public String getIdentifier() {
		return this.getRlnm();
	}
	
	@Override
	public String getLink() {
		return this.getRlnm();
	}

	@Override
	public String getTitle() {
		return this.getNm();
	}

	@Override
	public String getContent() {
		return this.getCntn();
	}

	@Override
	public Date getPublishDate() {
		Date d = null;
	
		if (this.getDt() != null){
			d = this.getDt();
		} else if (this.getCdt() != null){
			d = this.getCdt();
		} 
		return d;
	}
}
