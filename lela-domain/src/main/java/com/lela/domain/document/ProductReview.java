package com.lela.domain.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ProductReview extends AbstractDocument implements Serializable {
	private static final long serialVersionUID = 7269546887941680974L;
	
	/**
	 * URL name of the item this review is associated to
	 */
	private String tmrlnm;
	
	/**
	 * URL to writing the review
	 */
	private String wrl;
	
	/**
	 * URL to product itself
	 */
	private String prl;
	
	/**
	 * Overall rating of the product
	 */
	private String rtg;
	
	/**
	 * Attribute rating map. Holds rating for various attributes of product.
	 * Safety - 3.4
	 * Comfort - 4.9 
	 * etc
	 */
	private Map<String, String> ttrbrtg = new HashMap<String, String>();

	/**
	 * List of product review details associated to this item url (tmrlnm)
	 */
	private List<ProductReviewDetail> rdlst = new ArrayList<ProductReviewDetail>(); 
	
	/**
	 * Active Reviews Count
	 */
	private String tvrvcnt;
	
	public String getAttributeRating(String attribute){
		return ttrbrtg.get(attribute);
	}
	
	public void setAttributeRating(String attribute, String rating){
		ttrbrtg.put(attribute, rating);
	}

	public ProductReviewDetail getReviewDetail(String reviewUrl){
		ProductReviewDetail prd = null;
		for (ProductReviewDetail current : rdlst) {
			if (current.getRlnm().equals(reviewUrl)){
				prd = current;
				break;
			}
		}
		return prd;
	}
	public void addProductReviewDetail(ProductReviewDetail prd){
		rdlst.add(prd);
	}
	
	public String getTmrlnm() {
		return tmrlnm;
	}

	public void setTmrlnm(String tmrlnm) {
		this.tmrlnm = tmrlnm;
	}

	public String getWrl() {
		return wrl;
	}

	public void setWrl(String wrl) {
		this.wrl = wrl;
	}

	public String getPrl() {
		return prl;
	}

	public void setPrl(String prl) {
		this.prl = prl;
	}

	public String getRtg() {
		return rtg;
	}

	public void setRtg(String rtg) {
		this.rtg = rtg;
	}

	public Map<String, String> getTtrbrtg() {
		return ttrbrtg;
	}

	public void setTtrbrtg(Map<String, String> ttrbrtg) {
		this.ttrbrtg = ttrbrtg;
	}

	public List<ProductReviewDetail> getRdlst() {
		return rdlst;
	}

	public void setRdlst(List<ProductReviewDetail> rdlst) {
		this.rdlst = rdlst;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prl == null) ? 0 : prl.hashCode());
		result = prime * result + ((tmrlnm == null) ? 0 : tmrlnm.hashCode());
		return result;
	}

	/**
	 * Is tmrlnm AND prl are the same then this is the same item's review
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductReview other = (ProductReview) obj;
		if (prl == null) {
			if (other.prl != null)
				return false;
		} else if (!prl.equals(other.prl))
			return false;
		if (tmrlnm == null) {
			if (other.tmrlnm != null)
				return false;
		} else if (!tmrlnm.equals(other.tmrlnm))
			return false;
		return true;
	}

	public String getTvrvcnt() {
		return tvrvcnt;
	}

	public void setTvrvcnt(String tvrvcnt) {
		this.tvrvcnt = tvrvcnt;
	}

}
