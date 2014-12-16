package com.lela.domain.dto;

import java.io.Serializable;

import com.lela.domain.document.AffiliateAccount;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO that wraps a campaign document and a multipart file.
 * 
 * This has been created because of the inability of Documents to handle 
 * multipart files.
 * 
 * @author pankaj
 *
 */
public class AffiliateAccountAndImage implements Serializable {
	private static final long serialVersionUID = -3826676775884976895L;
	
	private AffiliateAccount affiliate;

    private MultipartFile bannerImageFile;
    
    private boolean deleteImage;

	public AffiliateAccountAndImage(){
		if (affiliate == null){
			this.affiliate = new AffiliateAccount();
		}
	}
	
	public AffiliateAccountAndImage(AffiliateAccount affiliate){
		this.affiliate = affiliate;
	}

	public AffiliateAccount getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(AffiliateAccount campaign) {
		this.affiliate = campaign;
	}

	public MultipartFile getBannerImageFile() {
		return bannerImageFile;
	}

	public void setBannerImageFile(MultipartFile bannerImageFile) {
		this.bannerImageFile = bannerImageFile;
	}

	public boolean isDeleteImage() {
		return deleteImage;
	}

	public void setDeleteImage(boolean deleteImage) {
		this.deleteImage = deleteImage;
	}

	
}
