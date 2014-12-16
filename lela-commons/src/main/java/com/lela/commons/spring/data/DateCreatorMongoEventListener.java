/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.spring.data;

import com.lela.domain.document.AbstractDocument;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 10/28/11
 * Time: 12:38 PM
 * Responsibility:
 */
public class DateCreatorMongoEventListener extends AbstractMongoEventListener<AbstractDocument> {
    @Override
    public void onBeforeConvert(AbstractDocument document) {
        Date d = new Date();
        // set created date if necessary
        if (document.getCdt() == null) {
            document.setCdt(d);
        }

        // update last updated date
        document.setLdt(d);
    }
}
