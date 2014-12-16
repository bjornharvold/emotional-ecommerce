package com.lela.domain.document;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductReviewDetail implements Serializable {
	
	private static final long serialVersionUID = 5426821432142963458L;

	/**
	 * url of this specific review (also the logical id)
	 */
	private String rlnm;

	/**
	 * publish date 
	 */
	private Date pbdt;
	
	/**
	 * user name
	 */
	private String srnm;
	
	/**
	 * sound bite
	 */
	private String sndbt;
	
	/**
	 * Rating
	 */
	
	private Integer rtng;
	
	/**
	 * Content
	 */
	private String cntnt;
	
	/**
	 * created date
	 */
	private Date crtdt;
	
	/**
	 * updated date
	 */
	private Date upddt;
	
	@Override
	public boolean equals(Object that){
		boolean boo = false;
		if (that instanceof ProductReviewDetail){
			if (StringUtils.equals(this.getRlnm(), ((ProductReviewDetail)that).getRlnm())){
				boo = true;
			}
		}
		return boo;
	}
	
	@Override
	public int hashCode(){
		return this.getRlnm().hashCode();
	}

	public String getRlnm() {
		return rlnm;
	}

	public void setRlnm(String rlnm) {
		this.rlnm = rlnm;
	}

	public Date getPbdt() {
		return pbdt;
	}

	public void setPbdt(Date pbdt) {
		this.pbdt = pbdt;
	}

	public String getSrnm() {
		return srnm;
	}

	public void setSrnm(String srnm) {
		this.srnm = srnm;
	}

	public String getSndbt() {
		return sndbt;
	}

	public void setSndbt(String sndbt) {
		this.sndbt = sndbt;
	}

	public Integer getRtng() {
		return rtng;
	}

	public void setRtng(Integer rtng) {
		this.rtng = rtng;
	}

	public String getCntnt() {
		return cntnt;
	}

	public void setCntnt(String cntnt) {
		this.cntnt = cntnt;
	}

	public Date getCrtdt() {
		return crtdt;
	}

	public void setCrtdt(Date crtdt) {
		this.crtdt = crtdt;
	}

	public Date getUpddt() {
		return upddt;
	}

	public void setUpddt(Date upddt) {
		this.upddt = upddt;
	}
}
