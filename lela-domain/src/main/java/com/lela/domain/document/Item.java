/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import com.lela.domain.Cacheable;

import java.io.Serializable;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 6/13/11
 * Time: 2:31 PM
 * Responsibility: A purchasable item is something a user can choose to purchase from an external vendor.
 * This can include a soccer ball, a university education etc
 */
@Document
public class Item extends AbstractItem implements Serializable, Cacheable {

    /** Field description */
    private static final long serialVersionUID = 6192799894992681546L;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public Item() {}

    /**
     * Constructs ...
     *
     *
     * @param item item
     */
    public Item(AbstractItem item) {
        super(item);
    }

    @Override
    public String toString() {
        return super.getRlnm();
    }
    
    /**
     * An item that has a non null rlnm is considered valid
     */
    public boolean validateCacheable(){
    	return !StringUtils.isEmpty(this.getRlnm());
    }
}
