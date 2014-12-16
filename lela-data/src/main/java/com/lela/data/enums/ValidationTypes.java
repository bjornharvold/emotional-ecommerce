package com.lela.data.enums;

import com.lela.data.domain.entity.ValidationType;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/28/12
 * Time: 7:03 AM
 * To change this template use File | Settings | File Templates.
 */
public enum ValidationTypes {

    NONE(1l), DO_NOT_ADD(2l), DATE(3l), CHILD_TABLE(4l), DERIVED_ATTRIBUTE(5l), NORMALIZED_ATTRIBUTE(6l);

    private Long id;

    private ValidationTypes(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }

    public ValidationType getValidationType()
    {
        return ValidationType.findValidationType(this.id);
    }
}
