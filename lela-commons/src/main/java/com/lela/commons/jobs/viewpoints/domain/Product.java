package com.lela.commons.jobs.viewpoints.domain;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

	private String href;
	private String category;
	private String reviews;
	private String name;
	private String category_name;
	private String average_rating;
	private AttributeRating[] attribute_ratings;
	private CommunityUrl community_urls;
	private String active_reviews_count;
	
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getReviews() {
		return reviews;
	}
	public void setReviews(String reviews) {
		this.reviews = reviews;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getAverage_rating() {
		return average_rating;
	}
	public void setAverage_rating(String average_rating) {
		this.average_rating = average_rating;
	}
	public AttributeRating[] getAttribute_ratings() {
		return attribute_ratings;
	}
	public void setAttribute_ratings(AttributeRating[] attribute_ratings) {
		this.attribute_ratings = attribute_ratings;
	}
	public CommunityUrl getCommunity_urls() {
		return community_urls;
	}
	public void setCommunity_urls(CommunityUrl community_urls) {
		this.community_urls = community_urls;
	}
	public String getActive_reviews_count() {
		return active_reviews_count;
	}
	public void setActive_reviews_count(String active_reviews_count) {
		this.active_reviews_count = active_reviews_count;
	}
}
