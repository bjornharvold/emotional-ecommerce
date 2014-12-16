package com.lela.commons.jobs.viewpoints.domain;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {

	private String href;
	private String username;
	private Date published_at;
	private String sound_bite;
	private int rating;
	private String content;
	private Date created_at;
	private Date updated_at;
	private String status;
	private CommunityUrl community_urls;
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getPublished_at() {
		return published_at;
	}
	public void setPublished_at(Date published_at) {
		this.published_at = published_at;
	}
	public String getSound_bite() {
		return sound_bite;
	}
	public void setSound_bite(String sound_bite) {
		this.sound_bite = sound_bite;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public CommunityUrl getCommunity_urls() {
		return community_urls;
	}
	public void setCommunity_urls(CommunityUrl community_urls) {
		this.community_urls = community_urls;
	}
	

	
	
}
