package com.lela.web.web.validator;

import com.lela.domain.dto.AffiliateAccountAndImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.lela.commons.web.validator.ImageFileUploadValidator;

@Component
public class AffiliateAccountAndImageValidator extends ImageFileUploadValidator implements Validator {

	@Autowired
	private AffiliateAccountValidator affiliateAccountValidator;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AffiliateAccountAndImage.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
        AffiliateAccountAndImage affiliateAccountAndImage = (AffiliateAccountAndImage) object;
        affiliateAccountValidator.validate(affiliateAccountAndImage.getAffiliate(), errors);
        super.validate(affiliateAccountAndImage.getBannerImageFile() , errors);
	}

}
