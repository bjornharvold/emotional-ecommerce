/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.data.web.validator;

import com.lela.commons.service.AffiliateService;
import com.lela.domain.document.AffiliateAccount;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AffiliateAccountValidator implements Validator {
    private final AffiliateService affiliateService;

    @Autowired
    public AffiliateAccountValidator(AffiliateService affiliateService) {
        this.affiliateService = affiliateService;
    }

    public boolean supports(Class<?> clazz) {
        return AffiliateAccount.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        AffiliateAccount account = (AffiliateAccount) target;

        if (!errors.hasErrors()) {
            // check for rlnm availability
            AffiliateAccount tmp = affiliateService.findAffiliateAccountByUrlName(account.getRlnm());

            if (tmp != null) {
                if (StringUtils.equals(tmp.getRlnm(), account.getRlnm()) && !tmp.getId().equals(account.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.affiliateaccount.url.name", null, "Url name is already taken");
                }
            }
        }
    }
}