package com.lela.data.enums;

import com.lela.data.domain.entity.AttributeTypeSource;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/28/12
 * Time: 7:03 AM
 * To change this template use File | Settings | File Templates.
 */
public enum AttributeTypeSources {

    LELA_SPREADSHEET(1l), CNET(2l), ETALIZE(3l), MANUAL(4l);

    private Long id;

    private AttributeTypeSources(Long id)
    {
        this.id = id;
    }

    public AttributeTypeSource getAttributeTypeSource()
    {
       return AttributeTypeSource.findAttributeTypeSource(this.id);
    }

    public Long getId()
    {
        return this.id;
    }
}
