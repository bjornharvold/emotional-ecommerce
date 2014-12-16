/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.domain.document;

//~--- non-JDK imports --------------------------------------------------------

import com.lela.domain.enums.StoreType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Created by Bjorn Harvold
 * Date: 9/5/11
 * Time: 4:34 PM
 * Responsibility:
 */
@Document
public class Store extends AbstractStore implements Serializable {

    /** Field description */
    private static final long serialVersionUID = -3757932108100032152L;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     */
    public Store() {}

    public Store(String mrchntd) {
        super(mrchntd);
    }

    /**
     * Constructs ...
     *
     *
     * @param tp tp
     * @param nm nm
     * @param rlnm rlnm
     */
    public Store(String nm, String rlnm, StoreType ... tp) {
        super(nm, rlnm, tp);
    }

}
