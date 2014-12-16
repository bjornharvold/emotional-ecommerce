package com.lela.domain.document;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class BlogItem implements Serializable{

	private static final long serialVersionUID = 3477515593069637778L;
	/**
	 * Item url name
	 */
	private String rlnm;

	public BlogItem(String rlnm){
		this.rlnm = rlnm;
	}
	
	public boolean isEmpty(){
		return StringUtils.isEmpty(rlnm);
	}
	
	public String getRlnm() {
		return rlnm;
	}

	public void setRlnm(String rlnm) {
		this.rlnm = rlnm;
	}
	
    @Override
    public String toString() {
        return this.getRlnm();
    }
}
