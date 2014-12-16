/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.commons.comparator;

import com.lela.domain.document.Category;
import com.lela.domain.dto.search.CategoryCount;
import org.springframework.context.MessageSource;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Bjorn Harvold
 * Date: 10/10/11
 * Time: 9:56 PM
 * Responsibility:
 */
public class CategoryCountNameComparator implements Comparator<CategoryCount> {
    private final MessageSource messageSource;
    private final Locale locale;
    
    public CategoryCountNameComparator(MessageSource messageSource, Locale locale) {
        this.messageSource = messageSource;
        this.locale = locale;
    }

    /**
     * Method description
     *
     * @param c1 c1
     * @param c2 c2
     * @return Return value
     */
    @Override
    public int compare(CategoryCount c1, CategoryCount c2) {
        String localizedStringC1 = messageSource.getMessage("nav.cat." + c1.getCategory().getRlnm(), null, locale);
        String localizedStringC2 = messageSource.getMessage("nav.cat." + c2.getCategory().getRlnm(), null, locale);

        return localizedStringC1.compareTo(localizedStringC2);
    }
}
