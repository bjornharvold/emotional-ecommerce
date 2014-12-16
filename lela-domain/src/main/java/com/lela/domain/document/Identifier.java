package com.lela.domain.document;

import com.lela.domain.enums.IdentifierType;

public class Identifier extends AbstractDocument {

	/**
	 * Type
	 */
	private IdentifierType tp;
	
	/**
	 * value
	 */
	private String vl;

	public IdentifierType getTp() {
		return tp;
	}

	public void setTp(IdentifierType tp) {
		this.tp = tp;
	}

	public String getVl() {
		return vl;
	}

	public void setVl(String vl) {
		this.vl = vl;
	}
}
