package com.lela.domain.dto.staticcontent;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.lela.domain.document.StaticContent;

public class StaticContentEntry implements Serializable {
	private static final long serialVersionUID = -5956853677532601524L;


	private MultipartFile multipartFile;
	
    
    private StaticContent staticContent = new StaticContent();
    
    public StaticContentEntry() {}
    
    public StaticContentEntry(StaticContent staticContent){
    	this.staticContent = staticContent;
    }

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public StaticContent getStaticContent() {
		return staticContent;
	}

	public void setStaticContent(StaticContent staticContent) {
		this.staticContent = staticContent;
	}
    
}
