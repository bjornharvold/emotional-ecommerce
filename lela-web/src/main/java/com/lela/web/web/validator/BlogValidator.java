/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.web.web.validator;

import com.lela.commons.service.BlogService;
import com.lela.commons.service.ItemService;
import com.lela.commons.web.validator.ImageFileUploadValidator;
import com.lela.domain.document.Blog;
import com.lela.domain.document.BlogItem;
import com.lela.domain.document.Item;
import com.lela.domain.dto.blog.BlogEntry;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates that a blog submission is correct
 */
@Component
public class BlogValidator extends ImageFileUploadValidator implements Validator {

    private final BlogService blogService;
    private final ItemService itemService;

    @Autowired
    public BlogValidator(BlogService blogService, ItemService itemService) {
        this.blogService = blogService;
        this.itemService = itemService;
    }

    public boolean supports(Class<?> clazz) {
        return BlogEntry.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        BlogEntry entry = (BlogEntry) target;

        if (!errors.hasErrors()) {

            // make sure the url name is unique
            Blog tmp = blogService.findBlogByUrlName(entry.getRlnm());

            if (tmp != null) {
                if ((tmp.getId() != null && entry.getId() == null) || !tmp.getId().equals(entry.getId())) {
                    errors.rejectValue("rlnm", "error.duplicate.url.name", new String[]{entry.getRlnm()}, "Url name not unique: " + entry.getRlnm());
                }
            }

            // make sure all items are of the specified category
            if (entry.getTms() != null && !entry.getTms().isEmpty()) {
                for (int i = 0; i < entry.getTms().size(); i++) {
                    BlogItem blogItem = entry.getTms().get(i);

                    if (blogItem != null && !blogItem.isEmpty()) {
                    	Item item = itemService.findItemByUrlName(blogItem.getRlnm());
                        // error out if the item category doesn't match the selected category
                        if (!StringUtils.equals(entry.getCtgry().getRlnm(), item.getCtgry().getRlnm())) {
                            errors.rejectValue("tms[" + i + "]", "error.item.category.incorrect", new String[]{item.getRlnm(), entry.getCtgry().getRlnm()},
                                    "Item with urlName: " + item.getRlnm() + " doesn't match category: " + entry.getCtgry().getRlnm());
                        }
                    }
                }
            }

            // if there's a multipart file we need to validate it as well
            if (entry.getMultipartFile() != null) {
                super.validate(entry.getMultipartFile(), errors);
            }
        }
    }
}