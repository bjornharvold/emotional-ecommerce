/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.service.ProductGridService;
import com.lela.domain.document.ProductGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates that a blog submission is correct
 */
@Component
public class ProductGridValidator implements Validator {

    private final ProductGridService productGridService;

    @Autowired
    public ProductGridValidator(ProductGridService productGridService) {
        this.productGridService = productGridService;
    }

    public boolean supports(Class<?> clazz) {
        return ProductGrid.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ProductGrid entry = (ProductGrid) target;

        if (!errors.hasErrors()) {

            // make sure the url name is unique
            ProductGrid tmp = productGridService.findProductGridByUrlName(entry.getRlnm());

            if (tmp != null) {
                // don't check for this until all other errors have been taken care of
                if (!errors.hasErrors()) {
                    if ((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId())) {
                        errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                    }
                }
            }
        }
    }
}