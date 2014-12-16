package com.lela.data.enums;

import com.lela.data.domain.entity.IdentifierType;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/28/12
 * Time: 7:01 AM
 * To change this template use File | Settings | File Templates.
 */
public enum IdentifierTypes {

    ASIN(1l), CNET(2l), MRF_PART_NO(3l), POPSHOPS(4l), UPC(5l), ETALIZE(6l);

    private Long id;

    private IdentifierTypes(Long id)
    {
        this.id = id;
    }

    public IdentifierType getIdentifierType()
    {
        return IdentifierType.findIdentifierType(this.id);
    }

    public Long getId()
    {
        return this.id;
    }
}
