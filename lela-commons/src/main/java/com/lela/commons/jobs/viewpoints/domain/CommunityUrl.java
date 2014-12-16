package com.lela.commons.jobs.viewpoints.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityUrl {
	private String review;
	private String write_review;
	private String product_page; 

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getWrite_review() {
		return write_review;
	}

	public void setWrite_review(String write_review) {
		this.write_review = write_review;
	}

	public String getProduct_page() {
		return product_page;
	}

	public void setProduct_page(String product_page) {
		this.product_page = product_page;
	}
	
}
